package org.r1zhok.app.kursova_backend.controller;

import org.r1zhok.app.kursova_backend.dto.MovementDTO;
import org.r1zhok.app.kursova_backend.dto.MovementResponseDTO;
import org.r1zhok.app.kursova_backend.entity.Arrival;
import org.r1zhok.app.kursova_backend.entity.ProductMovement;
import org.r1zhok.app.kursova_backend.entity.Shipment;
import org.r1zhok.app.kursova_backend.entity.WriteOff;
import org.r1zhok.app.kursova_backend.repository.ProductMovementRepository;
import org.r1zhok.app.kursova_backend.repository.ProductRepository;
import org.r1zhok.app.kursova_backend.service.MovementFactory;
import org.r1zhok.app.kursova_backend.service.StockSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movements")
@CrossOrigin(origins = "*")
public class MovementController {

    @Autowired
    private MovementFactory movementFactory;

    @Autowired
    private ProductMovementRepository movementRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockSubject stockSubject;

    /**
     * Створення надходження товару
     * POST /api/movements/arrival
     */
    @PostMapping("/arrival")
    public ResponseEntity<MovementResponseDTO> createArrival(@RequestBody MovementDTO dto) {
        var product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        int oldQuantity = product.getQuantity();

        Arrival arrival = movementFactory.createArrival(
                dto.getProductId(),
                dto.getQuantity(),
                dto.getUserId(),
                dto.getDescription()
        );

        arrival.execute();
        Arrival saved = movementRepository.save(arrival);
        productRepository.save(product);

        stockSubject.notifyObservers(product, oldQuantity, product.getQuantity());
        MovementResponseDTO responseDto = MovementResponseDTO.from(saved, product.getName());

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Створення відвантаження товару
     * POST /api/movements/shipment
     */
    @PostMapping("/shipment")
    public ResponseEntity<MovementResponseDTO> createShipment(@RequestBody MovementDTO dto) {
        var product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        int oldQuantity = product.getQuantity();

        Shipment shipment = movementFactory.createShipment(
                dto.getProductId(),
                dto.getQuantity(),
                dto.getUserId(),
                dto.getDescription()
        );

        shipment.execute();
        Shipment saved = movementRepository.save(shipment);
        productRepository.save(product);

        stockSubject.notifyObservers(product, oldQuantity, product.getQuantity());
        MovementResponseDTO responseDto = MovementResponseDTO.from(saved, product.getName());

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Створення списання товару
     * POST /api/movements/writeoff
     */
    @PostMapping("/writeoff")
    public ResponseEntity<MovementResponseDTO> createWriteOff(@RequestBody MovementDTO dto) {
        var product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        int oldQuantity = product.getQuantity();

        WriteOff writeOff = movementFactory.createWriteOff(
                dto.getProductId(),
                dto.getQuantity(),
                dto.getUserId(),
                dto.getReason()
        );

        writeOff.execute();
        WriteOff saved = movementRepository.save(writeOff);
        productRepository.save(product);

        stockSubject.notifyObservers(product, oldQuantity, product.getQuantity());
        MovementResponseDTO responseDto = MovementResponseDTO.from(saved, product.getName());

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Отримання всіх рухів товару
     * GET /api/movements/product/{productId}
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<MovementResponseDTO>> getMovementsByProduct(@PathVariable Long productId) {
        List<ProductMovement> movements = movementRepository.findByProduct_Id(productId);
        List<MovementResponseDTO> dtos = movements.stream()
                .map(movement -> {
                    String productName = movement.getProduct() != null ? movement.getProduct().getName() : "N/A";
                    return MovementResponseDTO.from(movement, productName);
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }

    /**
     * Отримання рухів за період
     * GET /api/movements?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59
     */
    @GetMapping
    public ResponseEntity<List<MovementResponseDTO>> getMovementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<ProductMovement> movements = movementRepository.findByDateBetween(start, end);
        List<MovementResponseDTO> dtos = movements.stream()
                .map(movement -> {
                    String productName = movement.getProduct() != null ? movement.getProduct().getName() : "N/A";
                    return MovementResponseDTO.from(movement, productName);
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
