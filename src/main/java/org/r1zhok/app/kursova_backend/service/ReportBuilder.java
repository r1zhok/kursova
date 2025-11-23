package org.r1zhok.app.kursova_backend.service;

import org.r1zhok.app.kursova_backend.entity.MovementType;
import org.r1zhok.app.kursova_backend.entity.Product;
import org.r1zhok.app.kursova_backend.entity.ProductMovement;
import org.r1zhok.app.kursova_backend.repository.ProductMovementRepository;
import org.r1zhok.app.kursova_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ReportBuilder {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMovementRepository movementRepository;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ReportType reportType;
    private List<Long> productIds;

    public enum ReportType {
        WAREHOUSE_STATUS,
        MOVEMENT_DYNAMICS,
        FINANCIAL_SUMMARY
    }

    public ReportBuilder forDateRange(LocalDateTime start, LocalDateTime end) {
        this.startDate = start;
        this.endDate = end;
        return this;
    }

    public ReportBuilder ofType(ReportType type) {
        this.reportType = type;
        return this;
    }

    public ReportBuilder forProducts(List<Long> productIds) {
        this.productIds = productIds;
        return this;
    }

    public Report build() {
        switch (reportType) {
            case WAREHOUSE_STATUS:
                return buildWarehouseStatusReport();
            case MOVEMENT_DYNAMICS:
                return buildMovementDynamicsReport();
            case FINANCIAL_SUMMARY:
                return buildFinancialSummaryReport();
            default:
                throw new IllegalStateException("Unknown report type");
        }
    }

    private Report buildWarehouseStatusReport() {
        List<Product> products = productIds != null && !productIds.isEmpty()
                ? productRepository.findAllById(productIds)
                : productRepository.findAll();

        WarehouseStatusReport report = new WarehouseStatusReport();
        report.setGeneratedAt(LocalDateTime.now());
        report.setProducts(products);
        report.setTotalProducts(products.size());

        BigDecimal totalValue = products.stream()
                .map(p -> p.getPrice().multiply(new BigDecimal(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.setTotalValue(totalValue);

        return report;
    }

    private Report buildMovementDynamicsReport() {
        List<ProductMovement> movements = movementRepository.findByDateBetween(startDate, endDate);

        MovementDynamicsReport report = new MovementDynamicsReport();
        report.setGeneratedAt(LocalDateTime.now());
        report.setStartDate(startDate);
        report.setEndDate(endDate);

        Map<MovementType, List<ProductMovement>> groupedByType = movements.stream()
                .collect(Collectors.groupingBy(ProductMovement::getMovementType));

        report.setArrivalCount(groupedByType.getOrDefault(MovementType.ARRIVAL, List.of()).size());
        report.setShipmentCount(groupedByType.getOrDefault(MovementType.SHIPMENT, List.of()).size());
        report.setWriteOffCount(groupedByType.getOrDefault(MovementType.WRITE_OFF, List.of()).size());
        report.setMovements(movements);

        return report;
    }

    private Report buildFinancialSummaryReport() {
        List<Product> products = productRepository.findAll();

        FinancialSummaryReport report = new FinancialSummaryReport();
        report.setGeneratedAt(LocalDateTime.now());

        BigDecimal totalValue = products.stream()
                .map(p -> p.getPrice().multiply(new BigDecimal(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        report.setTotalInventoryValue(totalValue);
        report.setTotalProducts(products.size());
        report.setTotalQuantity(products.stream().mapToInt(Product::getQuantity).sum());

        return report;
    }

    public abstract static class Report {
        private LocalDateTime generatedAt;

        public LocalDateTime getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    }

    public static class WarehouseStatusReport extends Report {
        private List<Product> products;
        private Integer totalProducts;
        private BigDecimal totalValue;

        public List<Product> getProducts() { return products; }
        public void setProducts(List<Product> products) { this.products = products; }
        public Integer getTotalProducts() { return totalProducts; }
        public void setTotalProducts(Integer totalProducts) { this.totalProducts = totalProducts; }
        public BigDecimal getTotalValue() { return totalValue; }
        public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }
    }

    public static class MovementDynamicsReport extends Report {
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer arrivalCount;
        private Integer shipmentCount;
        private Integer writeOffCount;
        private List<ProductMovement> movements;

        public LocalDateTime getStartDate() { return startDate; }
        public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
        public LocalDateTime getEndDate() { return endDate; }
        public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
        public Integer getArrivalCount() { return arrivalCount; }
        public void setArrivalCount(Integer arrivalCount) { this.arrivalCount = arrivalCount; }
        public Integer getShipmentCount() { return shipmentCount; }
        public void setShipmentCount(Integer shipmentCount) { this.shipmentCount = shipmentCount; }
        public Integer getWriteOffCount() { return writeOffCount; }
        public void setWriteOffCount(Integer writeOffCount) { this.writeOffCount = writeOffCount; }
        public List<ProductMovement> getMovements() { return movements; }
        public void setMovements(List<ProductMovement> movements) { this.movements = movements; }
    }

    public static class FinancialSummaryReport extends Report {
        private BigDecimal totalInventoryValue;
        private Integer totalProducts;
        private Integer totalQuantity;

        public BigDecimal getTotalInventoryValue() { return totalInventoryValue; }
        public void setTotalInventoryValue(BigDecimal value) { this.totalInventoryValue = value; }
        public Integer getTotalProducts() { return totalProducts; }
        public void setTotalProducts(Integer totalProducts) { this.totalProducts = totalProducts; }
        public Integer getTotalQuantity() { return totalQuantity; }
        public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    }
}
