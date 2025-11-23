package org.r1zhok.app.kursova_backend.repository;

import org.r1zhok.app.kursova_backend.entity.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorRepository extends JpaRepository<Contractor, Long> {
}
