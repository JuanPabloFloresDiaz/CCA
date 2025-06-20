package com.api.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.api.model.UsuariosTipoUsuario;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsuariosTipoUsuarioRepository extends JpaRepository<UsuariosTipoUsuario, UUID> {

    @Query("SELECT u FROM UsuariosTipoUsuario u WHERE " +
           "LOWER(u.usuario.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.tipoUsuario.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<UsuariosTipoUsuario> searchAllFields(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT u FROM UsuariosTipoUsuario u WHERE u.usuario.id = :usuarioId")
    Page<UsuariosTipoUsuario> findByUsuarioId(@Param("usuarioId") UUID usuarioId, Pageable pageable);

    @Query("SELECT u FROM UsuariosTipoUsuario u WHERE u.tipoUsuario.id = :tipoUsuarioId")
    Page<UsuariosTipoUsuario> findByTipoUsuarioId(@Param("tipoUsuarioId") UUID tipoUsuarioId, Pageable pageable);

    @Query("SELECT u FROM UsuariosTipoUsuario u WHERE u.usuario.id = :usuarioId")
    List<UsuariosTipoUsuario> findAllByUsuarioId(@Param("usuarioId") UUID usuarioId);

    // Método para buscar usuarios tipo usuario por ID de tipo de usuario y ID de usuario
    @Query("SELECT u FROM UsuariosTipoUsuario u WHERE u.tipoUsuario.id = :tipoUsuarioId AND u.usuario.id = :usuarioId")
    List<UsuariosTipoUsuario> findByTipoUsuarioIdAndUsuarioId(@Param("tipoUsuarioId") UUID tipoUsuarioId, @Param("usuarioId") UUID usuarioId);
}
