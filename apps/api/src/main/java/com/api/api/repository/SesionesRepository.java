package com.api.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.api.api.model.Sesiones;
import java.util.UUID;

@Repository
public interface SesionesRepository extends JpaRepository<Sesiones, UUID> {

    @Query("SELECT s FROM Sesiones s WHERE " +
            "LOWER(s.usuario.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.ipOrigen) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.emailUsuario) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.informacionDispositivo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.estado) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Sesiones> searchAllFields(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT s FROM Sesiones s WHERE s.estado = :estado")
    Page<Sesiones> findByEstado(@Param("estado") Boolean estado, Pageable pageable);

}
