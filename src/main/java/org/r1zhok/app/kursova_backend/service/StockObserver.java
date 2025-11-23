package org.r1zhok.app.kursova_backend.service;

import org.r1zhok.app.kursova_backend.entity.Product;

public interface StockObserver {
    void onStockChanged(Product product, int oldQuantity, int newQuantity);
}
