package org.r1zhok.app.kursova_backend.service;

import org.r1zhok.app.kursova_backend.entity.Product;

public interface MovementStrategy {
    void execute(Product product, Integer quantity);
    String getMovementTypeName();
}
