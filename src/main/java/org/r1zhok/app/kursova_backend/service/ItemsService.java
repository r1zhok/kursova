package org.r1zhok.app.kursova_backend.service;


import lombok.RequiredArgsConstructor;
import org.r1zhok.app.kursova_backend.dto.ItemDto;
import org.r1zhok.app.kursova_backend.dto.NewItemDto;
import org.r1zhok.app.kursova_backend.mapper.ItemsMapper;
import org.r1zhok.app.kursova_backend.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemRepository itemRepository;

    public ItemDto registerItem(NewItemDto itemDto) {
        return ItemsMapper.toDto(itemRepository.save(ItemsMapper.toEntity(itemDto)));
    }

    public List<ItemDto> getAllItems() {
        return itemRepository.findAll().stream().map(ItemsMapper::toDto).toList();
    }
}
