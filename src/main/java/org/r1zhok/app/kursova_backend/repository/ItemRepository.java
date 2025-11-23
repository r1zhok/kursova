package org.r1zhok.app.kursova_backend.repository;

import org.r1zhok.app.kursova_backend.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}
