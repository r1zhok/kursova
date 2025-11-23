package org.r1zhok.app.kursova_backend.service;

import org.r1zhok.app.kursova_backend.entity.Notification;
import org.r1zhok.app.kursova_backend.entity.Product;
import org.r1zhok.app.kursova_backend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LowStockObserver implements StockObserver {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void onStockChanged(Product product, int oldQuantity, int newQuantity) {
        if (newQuantity <= product.getMinQuantity() && oldQuantity > product.getMinQuantity()) {
            createNotification(product);
        }
    }

    private void createNotification(Product product) {
        String message = String.format(
                "⚠️ УВАГА! Товар '%s' (артикул: %s) досяг мінімального запасу. " +
                        "Поточна кількість: %d, мінімальна: %d. Рекомендується замовити!",
                product.getName(),
                product.getArticle(),
                product.getQuantity(),
                product.getMinQuantity()
        );

        Notification notification = new Notification(product, message);
        notificationRepository.save(notification);

        System.out.println("📧 Створено сповіщення: " + message);
    }
}
