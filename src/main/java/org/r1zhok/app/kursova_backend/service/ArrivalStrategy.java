package org.r1zhok.app.kursova_backend.service;

import org.r1zhok.app.kursova_backend.entity.Product;
import org.springframework.stereotype.Component;

@Component("arrivalStrategy")
public class ArrivalStrategy implements MovementStrategy {

    @Override
    public void execute(Product product, Integer quantity) {
        product.updateQuantity(quantity);
        System.out.println("✅ Надходження: +" + quantity + " од. товару '" + product.getName() + "'");
    }

    @Override
    public String getMovementTypeName() {
        return "ARRIVAL";
    }
}
