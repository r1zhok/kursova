package org.r1zhok.app.kursova_backend.service;

import org.r1zhok.app.kursova_backend.entity.Product;
import org.springframework.stereotype.Component;

@Component("shipmentStrategy")
public class ShipmentStrategy implements MovementStrategy {

    @Override
    public void execute(Product product, Integer quantity) {
        if (product.getQuantity() < quantity) {
            throw new IllegalStateException(
                    "Недостатньо товару на складі! Доступно: " + product.getQuantity() +
                            ", потрібно: " + quantity
            );
        }
        product.updateQuantity(-quantity);
        System.out.println("📦 Відвантаження: -" + quantity + " од. товару '" + product.getName() + "'");
    }

    @Override
    public String getMovementTypeName() {
        return "SHIPMENT";
    }
}
