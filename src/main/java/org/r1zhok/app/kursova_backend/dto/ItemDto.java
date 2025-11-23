package org.r1zhok.app.kursova_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDto {

    private String title;
    private String article;
    private Integer price;
    private Integer quantity;
    private String producer;
}
