package org.r1zhok.app.kursova_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDTO {
    private String name;
    private String article;
    private BigDecimal price;
    private Integer minQuantity;
    private Long supplierId;
    private Integer initialQuantity;
    private Integer userId;
}
