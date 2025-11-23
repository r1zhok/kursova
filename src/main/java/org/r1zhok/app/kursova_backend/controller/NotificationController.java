package org.r1zhok.app.kursova_backend.controller;

import org.r1zhok.app.kursova_backend.entity.Notification;
import org.r1zhok.app.kursova_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * Отримання непрочитаних сповіщень
     * GET /api/notifications/unread
     */
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications() {
        List<Notification> notifications = notificationService.getUnreadNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Отримання всіх сповіщень
     * GET /api/notifications
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Позначити сповіщення як прочитане
     * PUT /api/notifications/{id}/read
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
