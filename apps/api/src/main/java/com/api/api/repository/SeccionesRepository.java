package com.api.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.api.api.dto.SeccionSimpleDTO;
import com.api.api.model.Secciones;
import java.util.UUID;

@Repository
public interface SeccionesRepository extends JpaRepository<Secciones, UUID> {

    @Query("SELECT s FROM Secciones s WHERE " +
            "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.descripcion) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Secciones> searchAllFields(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT s.id AS id, s.nombre AS nombre FROM Secciones s")
    Iterable<SeccionSimpleDTO> findAllSelect();
}
