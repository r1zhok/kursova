package org.r1zhok.app.kursova_backend.service;

import jakarta.transaction.Transactional;
import org.r1zhok.app.kursova_backend.entity.Notification;
import org.r1zhok.app.kursova_backend.entity.Product;
import org.r1zhok.app.kursova_backend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Створення сповіщення про низький запас
     */
    public Notification createLowStockNotification(Product product) {
        String message = String.format(
                "⚠️ УВАГА! Товар '%s' (артикул: %s) досяг мінімального запасу. " +
                        "Поточна кількість: %d, мінімальна: %d. Терміново необхідне поповнення!",
                product.getName(),
                product.getArticle(),
                product.getQuantity(),
                product.getMinQuantity()
        );

        Notification notification = new Notification(product, message);
        Notification saved = notificationRepository.save(notification);

        System.out.println("📧 " + message);

        return saved;
    }

    /**
     * Отримання непрочитаних сповіщень
     */
    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalse();
    }

    /**
     * Позначити як прочитане
     */
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Сповіщення не знайдено"));

        notification.markAsRead();
        notificationRepository.save(notification);
    }

    /**
     * Отримання всіх сповіщень
     */
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    /**
     * Видалення старих прочитаних сповіщень
     */
    public void cleanupOldNotifications() {
        // Можна додати логіку видалення сповіщень старших за N днів
        System.out.println("🧹 Очистка старих сповіщень...");
    }
}
