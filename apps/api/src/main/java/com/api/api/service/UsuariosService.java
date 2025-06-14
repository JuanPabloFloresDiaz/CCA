package com.api.api.service;

import com.api.api.repository.UsuariosRepository;
import com.api.api.dto.SimpleDTO.UsuarioSimpleDTO;
import com.api.api.model.Usuarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.api.api.audit.AuditableAction;
import com.api.api.audit.AuditableAction.AuditResultType;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    // Auditar la acción de búsqueda de todos los usuarios
    @AuditableAction(actionName = "Búsqueda de Usuarios", message = "Se intentó buscar todos los usuarios.", auditResult = AuditResultType.BOTH)
    // Buscar todos los usuarios sin paginación ni búsqueda, solo id, nombres,
    // apellidos y email
    public Iterable<UsuarioSimpleDTO> findAllSelect() {
        return usuariosRepository.findAllSelect();
    }

    // Auditar la acción de búsqueda de todos los usuarios con paginación y búsqueda
    // opcional
    @AuditableAction(actionName = "Búsqueda de Usuarios con Paginación", message = "Se intentó buscar todos los usuarios con paginación y búsqueda opcional.", auditResult = AuditResultType.BOTH)
    // Buscar todos los usuarios con paginación y búsqueda opcional
    public Page<Usuarios> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return usuariosRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return usuariosRepository.findAll(pageable);
    }

    // Auditar la acción de búsqueda de un usuario por su ID
    @AuditableAction(actionName = "Búsqueda de Usuario por ID", message = "Se intentó buscar un usuario por su ID.", auditResult = AuditResultType.BOTH)
    // Buscar un usuario por su ID
    public Optional<Usuarios> findById(UUID id) {
        return usuariosRepository.findById(id);
    }

    // ¡Auditar la creación de un usuario!
    @AuditableAction(actionName = "Creación de Usuario", message = "Se intentó crear un nuevo usuario.")
    // Crear un nuevo usuario
    public Usuarios create(Usuarios usuario) {
        return usuariosRepository.save(usuario);
    }

    // Auditar la creación de múltiples usuarios
    @AuditableAction(actionName = "Creación de Múltiples Usuarios", message = "Se intentó crear múltiples usuarios.")
    // Crear múltiples usuarios
    public List<Usuarios> createAll(List<Usuarios> usuarios) {
        return usuariosRepository.saveAll(usuarios);
    }

    // Auditar la acción de actualización de un usuario
    @AuditableAction(actionName = "Actualización de Usuario", message = "Se intentó actualizar un usuario existente.")
    // Actualizar un usuario existente
    public Optional<Usuarios> update(UUID id, Usuarios usuarioActualizado) {
        return usuariosRepository.findById(id).map(usuario -> {
            usuario.setNombres(usuarioActualizado.getNombres());
            usuario.setApellidos(usuarioActualizado.getApellidos());
            usuario.setEmail(usuarioActualizado.getEmail());
            usuario.setEstado(usuarioActualizado.getEstado());
            usuario.setDosFactorActivo(usuarioActualizado.isDosFactorActivo());
            usuario.setDosFactorSecretoTotp(usuarioActualizado.getDosFactorSecretoTotp());
            usuario.setIntentosFallidosSesion(usuarioActualizado.getIntentosFallidosSesion());
            usuario.setFechaUltimoIntentoFallido(usuarioActualizado.getFechaUltimoIntentoFallido());
            usuario.setFechaBloqueoSesion(usuarioActualizado.getFechaBloqueoSesion());
            usuario.setRequiereCambioContrasena(usuarioActualizado.isRequiereCambioContrasena());
            return usuariosRepository.save(usuario);
        });
    }

    // Auditar la acción de eliminación definitiva de un usuario
    @AuditableAction(actionName = "Eliminación Definitiva de Usuario", message = "Se intentó eliminar definitivamente un usuario.")
    // Eliminar definitivamente un usuario por su ID
    public void deleteById(UUID id) {
        usuariosRepository.deleteById(id);
    }

    // Auditar la acción de actualización de eliminación lógica de un usuario
    @AuditableAction(actionName = "Actualización de Eliminación Lógica de Usuario", message = "Se intentó eliminar lógicamente un usuario.")
    // Eliminar lógicamente un usuario por su ID
    public Optional<Usuarios> softDelete(UUID id) {
        return usuariosRepository.findById(id).map(usuario -> {
            usuario.softDelete();
            return usuariosRepository.save(usuario);
        });
    }

    // Auditar filtrado de usuarios por estado
    @AuditableAction(actionName = "Filtrado de Usuarios por Estado", message = "Se intentó filtrar usuarios por estado.", auditResult = AuditResultType.BOTH)
    // Filtrar usuarios por estado con paginación
    public Page<Usuarios> findByEstado(String estado, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosRepository.findByEstado(estado, pageable);
    }

    // Auditar filtrado de usuarios que tienen 2FA activo
    @AuditableAction(actionName = "Filtrado de Usuarios por 2FA Activo", message = "Se intentó filtrar usuarios por si tienen 2FA activo.", auditResult = AuditResultType.BOTH)
    // Filtrar usuarios por si tienen 2FA activo
    public Page<Usuarios> findByDosFactorActivo(Boolean dosFactorActivo, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosRepository.findByDosFactorActivo(dosFactorActivo, pageable);
    }

    // Auditar filtrado de usuarios que requieren cambio de contraseña
    @AuditableAction(actionName = "Filtrado de Usuarios por Cambio de Contraseña Requerido", message = "Se intentó filtrar usuarios que requieren cambio de contraseña.", auditResult = AuditResultType.BOTH)
    // Filtrar usuarios que requieren cambio de contraseña
    public Page<Usuarios> findByRequiereCambioContrasena(boolean requiereCambioContrasena, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosRepository.findByRequiereCambioContrasena(requiereCambioContrasena, pageable);
    }

    // Auditar la acción de bloqueo de sesión de un usuario
    @AuditableAction(actionName = "Bloqueo de Sesión de Usuario", message = "Se intentó bloquear la sesión de un usuario.")
    // Verificar si la sesión del usuario está bloqueada
    public boolean isSessionBlocked(UUID id) {
        return usuariosRepository.isSessionBlocked(id);
    }
}