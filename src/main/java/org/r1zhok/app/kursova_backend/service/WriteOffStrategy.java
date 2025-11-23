package org.r1zhok.app.kursova_backend.service;

import org.r1zhok.app.kursova_backend.entity.Product;
import org.springframework.stereotype.Component;

@Component("writeOffStrategy")
public class WriteOffStrategy implements MovementStrategy {

    @Override
    public void execute(Product product, Integer quantity) {
        if (product.getQuantity() < quantity) {
            throw new IllegalStateException(
                    "Недостатньо товару для списання! Доступно: " + product.getQuantity()
            );
        }
        product.updateQuantity(-quantity);
        System.out.println("🗑️ Списання: -" + quantity + " од. товару '" + product.getName() + "'");
    }

    @Override
    public String getMovementTypeName() {
        return "WRITE_OFF";
    }
}
