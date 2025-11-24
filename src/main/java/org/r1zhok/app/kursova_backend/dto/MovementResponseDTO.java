package org.r1zhok.app.kursova_backend.dto;

import lombok.Data;
import org.r1zhok.app.kursova_backend.entity.ProductMovement;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MovementResponseDTO implements Serializable {
    private Long id;
    private LocalDateTime date;
    private String type;
    private Long productId;
    private String productName;
    private Integer quantity;
    private String description;

    public static MovementResponseDTO from(ProductMovement movement, String productName) {
        MovementResponseDTO dto = new MovementResponseDTO();
        dto.setId(movement.getId());
        dto.setDate(movement.getDate());
        dto.setType(movement.getClass().getSimpleName());
        dto.setProductId(movement.getProduct().getId());
        dto.setProductName(productName);
        dto.setQuantity(movement.getQuantity());

        if (movement.getDescription() != null) {
            dto.setDescription(movement.getDescription());
        } else {
            dto.setDescription("");
        }

        return dto;
    }
}
