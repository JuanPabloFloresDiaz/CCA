package com.api.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.api.dto.SimpleDTO.AccionSimpleDTO;
import com.api.api.model.Acciones;
import java.util.UUID;

@Repository
public interface AccionesRepository extends JpaRepository<Acciones, UUID> {
    @Query("SELECT a FROM Acciones a WHERE " +
           "LOWER(a.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.descripcion) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Acciones> searchAllFields(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT a FROM Acciones a WHERE a.aplicacion.id = :aplicacionId")
    Page<Acciones> findByAplicacionId(@Param("aplicacionId") UUID aplicacionId, Pageable pageable);

    @Query("SELECT a FROM Acciones a WHERE a.seccion.id = :seccionId")
    Page<Acciones> findBySeccionId(@Param("seccionId") UUID seccionId, Pageable pageable);

    @Query("SELECT a.id AS id, a.nombre AS nombre FROM Acciones a")
    Iterable<AccionSimpleDTO> findAllSelect();
}
