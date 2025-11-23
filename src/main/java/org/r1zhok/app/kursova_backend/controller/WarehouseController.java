package org.r1zhok.app.kursova_backend.controller;

import lombok.RequiredArgsConstructor;
import org.r1zhok.app.kursova_backend.dto.ItemDto;
import org.r1zhok.app.kursova_backend.dto.NewItemDto;
import org.r1zhok.app.kursova_backend.service.ItemsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/warehouse")
public class WarehouseController {

    private final ItemsService service;

    @PostMapping
    public ResponseEntity<ItemDto> registerNewItem(NewItemDto newItem) {
        return ResponseEntity.ok(service.registerItem(newItem));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems() {
        return ResponseEntity.ok(service.getAllItems());
    }
}
