package org.r1zhok.app.kursova_backend.repository;

import org.r1zhok.app.kursova_backend.entity.ProductMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductMovementRepository extends JpaRepository<ProductMovement, Long> {
    List<ProductMovement> findByProduct_Id(Long productId);
    List<ProductMovement> findByDateBetween(LocalDateTime start, LocalDateTime end);
}
