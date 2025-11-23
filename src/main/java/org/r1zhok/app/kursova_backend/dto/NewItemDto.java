package org.r1zhok.app.kursova_backend.dto;

import lombok.Data;

@Data
public class NewItemDto {

    private String title;
    private String article;
    private Integer price;
    private Integer quantity;
    private String producer;
}
