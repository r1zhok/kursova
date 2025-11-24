package org.r1zhok.app.kursova_backend.controller;

import org.r1zhok.app.kursova_backend.dto.SupplierCreateDTO;
import org.r1zhok.app.kursova_backend.dto.SupplierDto;
import org.r1zhok.app.kursova_backend.dto.SupplierUpdateDTO;
import org.r1zhok.app.kursova_backend.entity.Supplier;
import org.r1zhok.app.kursova_backend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * Створення нового постачальника
     * POST /api/suppliers
     * Використовується в UI для кнопки "Додати Нового Постачальника"
     */
    @PostMapping
    public ResponseEntity<SupplierDto> createSupplier(@RequestBody SupplierCreateDTO dto) {
        var newSupplier = SupplierDto.from(supplierService.createSupplier(dto));
        return new ResponseEntity<>(newSupplier, HttpStatus.CREATED);
    }

    /**
     * Отримання всіх постачальників
     * GET /api/suppliers
     * Використовується в UI для завантаження списку
     */
    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
        var suppliers = supplierService.getAllSuppliers().stream().map(SupplierDto::from).toList();
        return ResponseEntity.ok(suppliers);
    }

    /**
     * Отримання постачальника по ID
     * GET /api/suppliers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getSupplierById(@PathVariable Long id) {
        var supplier = SupplierDto.from(supplierService.getSupplierById(id));
        return ResponseEntity.ok(supplier);
    }

    /**
     * Оновлення існуючого постачальника
     * PUT /api/suppliers/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> updateSupplier(
            @PathVariable Long id,
            @RequestBody SupplierUpdateDTO dto) {
        var updatedSupplier = SupplierDto.from(supplierService.updateSupplier(id, dto));
        return ResponseEntity.ok(updatedSupplier);
    }

    /**
     * Видалення постачальника
     * DELETE /api/suppliers/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
