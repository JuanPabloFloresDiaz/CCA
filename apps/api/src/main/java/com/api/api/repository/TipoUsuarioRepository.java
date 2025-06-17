package com.api.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.api.dto.SimpleDTO.TipoUsuarioSimpleDTO;
import com.api.api.model.TipoUsuario;

import java.util.List;
import java.util.UUID;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, UUID> {

    @Query("SELECT t FROM TipoUsuario t WHERE " +
            "LOWER(t.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.descripcion) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(t.aplicacion.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            " OR LOWER(t.estado) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<TipoUsuario> searchAllFields(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT t FROM TipoUsuario t WHERE t.estado = :estado")
    Page<TipoUsuario> findByEstado(@Param("estado") String estado, Pageable pageable);

    @Query("SELECT t FROM TipoUsuario t WHERE t.aplicacion.id = :aplicacionId")
    Page<TipoUsuario> findByAplicacionId(@Param("aplicacionId") UUID aplicacionId, Pageable pageable);

    @Query("SELECT t FROM TipoUsuario t WHERE t.aplicacion.id = :aplicacionId")
    List<TipoUsuario> findAllByAplicacionId(@Param("aplicacionId") UUID aplicacionId);

    @Query("SELECT t.id AS id, t.nombre AS nombre FROM TipoUsuario t")
    Iterable<TipoUsuarioSimpleDTO> findAllSelect();

    @Query("SELECT t FROM TipoUsuario t WHERE LOWER(t.nombre) = LOWER(:nombre) AND t.aplicacion.id = :aplicacionId")
    List<TipoUsuario> findByNombreAndAplicacionId(@Param("nombre") String nombre, @Param("aplicacionId") UUID aplicacionId);
}
