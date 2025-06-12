package com.api.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.api.api.model.AuditoriaAccesos;
import java.util.UUID;

@Repository
public interface AuditoriaAccesosRepository extends JpaRepository<AuditoriaAccesos, UUID> {
    
    @Query("SELECT a FROM AuditoriaAccesos a WHERE " +
           "LOWER(a.usuario) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.aplicacion.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.accion.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<AuditoriaAccesos> searchAllFields(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT a FROM AuditoriaAccesos a WHERE a.aplicacion.id = :aplicacionId")
    Page<AuditoriaAccesos> findByAplicacionId(@Param("aplicacionId") UUID aplicacionId, Pageable pageable);
    
    @Query("SELECT a FROM AuditoriaAccesos a WHERE a.accion.id = :accionId")
    Page<AuditoriaAccesos> findByAccionId(@Param("accionId") UUID accionId, Pageable pageable);

}
