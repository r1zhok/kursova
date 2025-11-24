package org.r1zhok.app.kursova_backend.service;

import jakarta.transaction.Transactional;
import org.r1zhok.app.kursova_backend.dto.SupplierCreateDTO;
import org.r1zhok.app.kursova_backend.dto.SupplierUpdateDTO;
import org.r1zhok.app.kursova_backend.entity.Supplier;
import org.r1zhok.app.kursova_backend.exception.ResourceNotFoundException;
import org.r1zhok.app.kursova_backend.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    /**
     * Створення нового постачальника.
     */
    @Transactional
    public Supplier createSupplier(SupplierCreateDTO dto) {
        Supplier supplier = new Supplier();
        supplier.setName(dto.getName());
        supplier.setPhone(dto.getPhone());
        supplier.setEmail(dto.getEmail());
        supplier.setAddress(dto.getAddress());

        return supplierRepository.save(supplier);
    }

    /**
     * Отримання всіх постачальників.
     */
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    /**
     * Отримання постачальника за ID.
     */
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Постачальника з ID " + id + " не знайдено"));
    }

    /**
     * Оновлення існуючого постачальника.
     */
    @Transactional
    public Supplier updateSupplier(Long id, SupplierUpdateDTO dto) {
        Supplier supplier = getSupplierById(id);

        if (dto.getName() != null) {
            supplier.setName(dto.getName());
        }
        if (dto.getPhone() != null) {
            supplier.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            supplier.setEmail(dto.getEmail());
        }
        if (dto.getAddress() != null) {
            supplier.setAddress(dto.getAddress());
        }

        return supplierRepository.save(supplier);
    }

    /**
     * Видалення постачальника.
     * Оскільки в сутності Supplier є `CascadeType.ALL` на `products`,
     * видалення постачальника може призвести до видалення пов'язаних продуктів,
     * якщо ви не хочете цього, змініть `CascadeType` або додайте додаткову логіку.
     */
    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = getSupplierById(id);
        supplierRepository.delete(supplier);
    }
}
