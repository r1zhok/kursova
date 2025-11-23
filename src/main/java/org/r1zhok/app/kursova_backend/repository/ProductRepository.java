package org.r1zhok.app.kursova_backend.repository;

import org.r1zhok.app.kursova_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByArticle(String article);

    @Query("SELECT p FROM Product p WHERE p.quantity <= p.minQuantity")
    List<Product> findLowStockProducts();

    List<Product> findBySupplier_Id(Long supplierId);
}