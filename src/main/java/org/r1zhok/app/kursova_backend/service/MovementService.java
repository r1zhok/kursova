package org.r1zhok.app.kursova_backend.service;

import jakarta.transaction.Transactional;
import org.r1zhok.app.kursova_backend.entity.*;
import org.r1zhok.app.kursova_backend.exception.InsufficientStockException;
import org.r1zhok.app.kursova_backend.repository.ProductMovementRepository;
import org.r1zhok.app.kursova_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MovementService {

    @Autowired
    private MovementFactory movementFactory;

    @Autowired
    private ProductMovementRepository movementRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockSubject stockSubject;

    /**
     * Створення надходження
     */
    public Arrival createArrival(Long productId, Integer quantity, Long userId, String description) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        int oldQuantity = product.getQuantity();

        // Factory Pattern
        Arrival arrival = movementFactory.createArrival(productId, quantity, userId, description);

        // Strategy Pattern - виконання
        arrival.execute();

        Arrival saved = movementRepository.save(arrival);
        productRepository.save(product);

        // Observer Pattern
        stockSubject.notifyObservers(product, oldQuantity, product.getQuantity());

        return saved;
    }

    /**
     * Створення відвантаження
     */
    public Shipment createShipment(Long productId, Integer quantity, Long userId, String description) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        // Перевірка наявності
        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException(
                    String.format("Недостатньо товару '%s'. Доступно: %d, потрібно: %d",
                            product.getName(), product.getQuantity(), quantity)
            );
        }

        int oldQuantity = product.getQuantity();

        // Factory Pattern
        Shipment shipment = movementFactory.createShipment(productId, quantity, userId, description);

        // Strategy Pattern - виконання
        shipment.execute();

        Shipment saved = movementRepository.save(shipment);
        productRepository.save(product);

        // Observer Pattern
        stockSubject.notifyObservers(product, oldQuantity, product.getQuantity());

        return saved;
    }

    /**
     * Створення списання
     */
    public WriteOff createWriteOff(Long productId, Integer quantity, Long userId, String reason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        // Перевірка наявності
        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException(
                    String.format("Недостатньо товару для списання '%s'. Доступно: %d",
                            product.getName(), product.getQuantity())
            );
        }

        int oldQuantity = product.getQuantity();

        // Factory Pattern
        WriteOff writeOff = movementFactory.createWriteOff(productId, quantity, userId, reason);

        // Strategy Pattern - виконання
        writeOff.execute();

        WriteOff saved = movementRepository.save(writeOff);
        productRepository.save(product);

        // Observer Pattern
        stockSubject.notifyObservers(product, oldQuantity, product.getQuantity());

        return saved;
    }

    /**
     * Отримання рухів по товару
     */
    public List<ProductMovement> getMovementsByProduct(Long productId) {
        return movementRepository.findByProduct_Id(productId);
    }

    /**
     * Отримання рухів за період
     */
    public List<ProductMovement> getMovementsByDateRange(LocalDateTime start, LocalDateTime end) {
        return movementRepository.findByDateBetween(start, end);
    }
}
