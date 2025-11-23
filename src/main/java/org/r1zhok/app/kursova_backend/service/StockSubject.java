package org.r1zhok.app.kursova_backend.service;

import org.r1zhok.app.kursova_backend.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockSubject {

    private List<StockObserver> observers = new ArrayList<>();

    @Autowired
    public void setObservers(List<StockObserver> observers) {
        this.observers = observers;
    }

    public void notifyObservers(Product product, int oldQuantity, int newQuantity) {
        for (StockObserver observer : observers) {
            observer.onStockChanged(product, oldQuantity, newQuantity);
        }
    }
}
