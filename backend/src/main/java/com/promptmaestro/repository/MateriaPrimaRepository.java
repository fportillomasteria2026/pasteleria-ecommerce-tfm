package com.promptmaestro.repository;

import com.promptmaestro.entity.MateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaPrimaRepository extends JpaRepository<MateriaPrima, Long> {
    List<MateriaPrima> findByNombreContainingIgnoreCase(String nombre);
}
