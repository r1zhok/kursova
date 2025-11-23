package org.r1zhok.app.kursova_backend.mapper;

import org.r1zhok.app.kursova_backend.dto.ItemDto;
import org.r1zhok.app.kursova_backend.dto.NewItemDto;
import org.r1zhok.app.kursova_backend.entity.ItemEntity;

public class ItemsMapper {

    public static ItemEntity toEntity(NewItemDto itemDto) {
        return ItemEntity.builder()
                .title(itemDto.getTitle())
                .price(itemDto.getPrice())
                .article(itemDto.getArticle())
                .quantity(itemDto.getQuantity())
                .producer(itemDto.getProducer())
                .build();
    }

    public static ItemDto toDto(ItemEntity itemEntity) {
        return ItemDto.builder()
                .title(itemEntity.getTitle())
                .price(itemEntity.getPrice())
                .article(itemEntity.getArticle())
                .quantity(itemEntity.getQuantity())
                .producer(itemEntity.getProducer())
                .build();
    }
}
