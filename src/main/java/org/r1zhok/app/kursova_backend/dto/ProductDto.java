package org.r1zhok.app.kursova_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.r1zhok.app.kursova_backend.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class ProductDto implements Serializable {

    private Long id;
    private String name;
    private String article;
    private BigDecimal price;
    private Integer quantity;
    private Integer minQuantity;

    @JsonIgnore
    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .article(product.getArticle())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .minQuantity(product.getMinQuantity())
                .build();
    }
}
