package com.api.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.api.dto.SimpleDTO.AplicacionSimpleDTO;
import com.api.api.model.Aplicaciones;
import java.util.UUID;

@Repository
public interface AplicacionesRepository extends JpaRepository<Aplicaciones, UUID> {
    @Query("SELECT a FROM Aplicaciones a WHERE " +
           "LOWER(a.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.descripcion) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
           " OR LOWER(a.url) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.llaveIdentificadora) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Aplicaciones> searchAllFields(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT a FROM Aplicaciones a WHERE a.estado = :estado")
    Page<Aplicaciones> findByEstado(@Param("estado") Boolean estado, Pageable pageable);

    @Query("SELECT a.id AS id, a.nombre AS nombre FROM Aplicaciones a")
    Iterable<AplicacionSimpleDTO> findAllSelect();
}
