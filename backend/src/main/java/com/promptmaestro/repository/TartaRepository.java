package com.promptmaestro.repository;

import com.promptmaestro.entity.Tarta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TartaRepository extends JpaRepository<Tarta, Long> {
    List<Tarta> findByActivoTrue();
    List<Tarta> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
    List<Tarta> findByTamanoAndActivoTrue(String tamano);
    List<Tarta> findByFormaAndActivoTrue(String forma);
    List<Tarta> findByDisponibleAndActivoTrue(Boolean disponible);
}
