package com.api.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.api.api.model.PermisosTipoUsuario;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface PermisosTipoUsuarioRepository extends JpaRepository<PermisosTipoUsuario, UUID> {

    @Query("SELECT p FROM PermisosTipoUsuario p WHERE " +
            "LOWER(p.tipoUsuario.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.aplicacion.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.accion.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<PermisosTipoUsuario> searchAllFields(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT p FROM PermisosTipoUsuario p WHERE p.tipoUsuario.id = :tipoUsuarioId")
    Page<PermisosTipoUsuario> findByTipoUsuarioId(@Param("tipoUsuarioId") UUID tipoUsuarioId, Pageable pageable);

    @Query("SELECT p FROM PermisosTipoUsuario p WHERE p.aplicacion.id = :aplicacionId")
    Page<PermisosTipoUsuario> findByAplicacionId(@Param("aplicacionId") UUID aplicacionId, Pageable pageable);

    @Query("SELECT p FROM PermisosTipoUsuario p WHERE p.tipoUsuario.id IN :tipoUsuarioIds")
    List<PermisosTipoUsuario> findByTipoUsuarioIdIn(Collection<UUID> tipoUsuarioIds);
}
