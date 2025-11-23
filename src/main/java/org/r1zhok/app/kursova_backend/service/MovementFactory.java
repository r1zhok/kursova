package org.r1zhok.app.kursova_backend.service;

import org.r1zhok.app.kursova_backend.entity.*;
import org.r1zhok.app.kursova_backend.repository.ProductRepository;
import org.r1zhok.app.kursova_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovementFactory {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Arrival createArrival(Long productId, Integer quantity, Long userId, String description) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Користувач не знайдено"));

        Arrival arrival = new Arrival();
        arrival.setProduct(product);
        arrival.setQuantity(quantity);
        arrival.setUser(user);
        arrival.setDescription(description);

        return arrival;
    }

    public Shipment createShipment(Long productId, Integer quantity, Long userId, String description) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Користувач не знайдено"));

        Shipment shipment = new Shipment();
        shipment.setProduct(product);
        shipment.setQuantity(quantity);
        shipment.setUser(user);
        shipment.setDescription(description);

        return shipment;
    }

    public WriteOff createWriteOff(Long productId, Integer quantity, Long userId, String reason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Користувач не знайдено"));

        WriteOff writeOff = new WriteOff();
        writeOff.setProduct(product);
        writeOff.setQuantity(quantity);
        writeOff.setUser(user);
        writeOff.setReason(reason);
        writeOff.setDescription("Списання: " + reason);

        return writeOff;
    }
}
