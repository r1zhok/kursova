package org.r1zhok.app.kursova_backend.dto;

import java.util.List;

public class ShipmentRequestDTO {
    private Long contractorId;
    private Long userId;
    private List<ShipmentItemDTO> items;

    public static class ShipmentItemDTO {
        private Long productId;
        private Integer quantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public Long getContractorId() { return contractorId; }
    public void setContractorId(Long contractorId) { this.contractorId = contractorId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<ShipmentItemDTO> getItems() { return items; }
    public void setItems(List<ShipmentItemDTO> items) { this.items = items; }
}
