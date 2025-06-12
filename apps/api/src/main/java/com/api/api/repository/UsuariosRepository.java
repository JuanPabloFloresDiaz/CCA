package com.api.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.api.model.Usuarios;
import java.util.UUID;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, UUID> {

    @Query("SELECT u FROM Usuarios u WHERE " +
            "LOWER(u.nombres) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.estado) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Usuarios> searchAllFields(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT u FROM Usuarios u WHERE u.estado = :estado")
    Page<Usuarios> findByEstado(@Param("estado") Boolean estado, Pageable pageable);

    // Buscar usuarios por la propiedad dosFactorActivo
    @Query("SELECT u FROM Usuarios u WHERE u.dosFactorActivo = :dosFactorActivo")
    Page<Usuarios> findByDosFactorActivo(@Param("dosFactorActivo") Boolean dosFactorActivo, Pageable pageable);

    // Buscar usuarios por la propiedad requiereCambioContrasena
    @Query("SELECT u FROM Usuarios u WHERE u.requiereCambioContrasena = :requiereCambioContrasena")
    Page<Usuarios> findByRequiereCambioContrasena(@Param("requiereCambioContrasena") boolean requiereCambioContrasena, Pageable pageable);

    // Buscar si el usuario tiene la sesión bloqueada
    @Query("""
        SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
        FROM Usuarios u
        WHERE u.id = :id
          AND u.fechaBloqueoSesion IS NOT NULL
          AND u.fechaBloqueoSesion > CURRENT_TIMESTAMP
        """)
    boolean isSessionBlocked(@Param("id") UUID id);
}
