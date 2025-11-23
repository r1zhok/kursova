package org.r1zhok.app.kursova_backend.service;

import jakarta.transaction.Transactional;
import org.r1zhok.app.kursova_backend.entity.*;
import org.r1zhok.app.kursova_backend.repository.NotificationRepository;
import org.r1zhok.app.kursova_backend.repository.ProductMovementRepository;
import org.r1zhok.app.kursova_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WarehouseFacade {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MovementFactory movementFactory;

    @Autowired
    private ProductMovementRepository movementRepository;

    @Autowired
    private StockSubject stockSubject;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Комплексна операція: створення товару з початковим надходженням
     */
    public Product registerProductWithInitialStock(
            Product product,
            Integer initialQuantity,
            Long userId) {

        Product savedProduct = productRepository.save(product);

        if (initialQuantity > 0) {
            Arrival arrival = movementFactory.createArrival(
                    savedProduct.getId(),
                    initialQuantity,
                    userId,
                    "Початкове надходження"
            );
            arrival.execute();
            movementRepository.save(arrival);
            productRepository.save(savedProduct);
        }

        return savedProduct;
    }

    /**
     * Комплексна операція: відвантаження з автоматичним створенням накладної
     */
    public Invoice processShipment(
            Long contractorId,
            List<ShipmentItem> items,
            Long userId) {

        Invoice invoice = invoiceService.createInvoice(contractorId, InvoiceType.SHIPMENT, userId);

        for (ShipmentItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

            int oldQuantity = product.getQuantity();

            Shipment shipment = movementFactory.createShipment(
                    item.getProductId(),
                    item.getQuantity(),
                    userId,
                    "Відвантаження за накладною " + invoice.getNumber()
            );

            shipment.setInvoice(invoice);
            shipment.execute();
            movementRepository.save(shipment);

            stockSubject.notifyObservers(product, oldQuantity, product.getQuantity());
        }

        invoice.calculateTotal();

        return invoice;
    }

    /**
     * Отримання повного звіту по складу
     */
    public WarehouseReport getFullWarehouseReport() {
        List<Product> allProducts = productRepository.findAll();
        List<Product> lowStockProducts = productRepository.findLowStockProducts();
        List<Notification> unreadNotifications = notificationRepository.findByIsReadFalse();

        WarehouseReport report = new WarehouseReport();
        report.setTotalProducts(allProducts.size());
        report.setLowStockCount(lowStockProducts.size());
        report.setUnreadNotificationsCount(unreadNotifications.size());
        report.setProducts(allProducts);
        report.setLowStockProducts(lowStockProducts);

        return report;
    }

    public static class ShipmentItem {
        private Long productId;
        private Integer quantity;

        public ShipmentItem() {}

        public ShipmentItem(Long productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public static class WarehouseReport {
        private Integer totalProducts;
        private Integer lowStockCount;
        private Integer unreadNotificationsCount;
        private List<Product> products;
        private List<Product> lowStockProducts;

        // Getters and Setters
        public Integer getTotalProducts() { return totalProducts; }
        public void setTotalProducts(Integer totalProducts) { this.totalProducts = totalProducts; }

        public Integer getLowStockCount() { return lowStockCount; }
        public void setLowStockCount(Integer lowStockCount) { this.lowStockCount = lowStockCount; }

        public Integer getUnreadNotificationsCount() { return unreadNotificationsCount; }
        public void setUnreadNotificationsCount(Integer count) { this.unreadNotificationsCount = count; }

        public List<Product> getProducts() { return products; }
        public void setProducts(List<Product> products) { this.products = products; }

        public List<Product> getLowStockProducts() { return lowStockProducts; }
        public void setLowStockProducts(List<Product> products) { this.lowStockProducts = products; }
    }
}
