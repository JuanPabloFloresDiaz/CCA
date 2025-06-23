package com.api.api.service;

import com.api.api.repository.UsuariosRepository;
import com.api.api.dto.SimpleDTO.UsuarioSimpleDTO;
import com.api.api.model.Usuarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.api.api.audit.AuditableAction;
import com.api.api.audit.AuditableAction.AuditResultType;
import com.api.api.audit.AuditActions;

import java.util.Optional;
import java.util.UUID;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuariosService(UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Auditar la acción de búsqueda de todos los usuarios
    @AuditableAction(actionName = AuditActions.BUSQUEDA_USUARIOS_SIMPLE, message = "Se intentó buscar todos los usuarios.", auditResult = AuditResultType.BOTH)
    // Buscar todos los usuarios sin paginación ni búsqueda, solo id, nombres,
    // apellidos y email
    public Iterable<UsuarioSimpleDTO> findAllSelect() {
        return usuariosRepository.findAllSelect();
    }

    // Auditar la acción de búsqueda de todos los usuarios con paginación y búsqueda
    // opcional
    @AuditableAction(actionName = AuditActions.BUSQUEDA_USUARIOS, message = "Se intentó buscar todos los usuarios con paginación y búsqueda opcional.", auditResult = AuditResultType.BOTH)
    // Buscar todos los usuarios con paginación y búsqueda opcional
    public Page<Usuarios> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return usuariosRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return usuariosRepository.findAll(pageable);
    }

    // Auditar la acción de búsqueda de un usuario por su ID
    @AuditableAction(actionName = AuditActions.BUSQUEDA_USUARIOS, message = "Se intentó buscar un usuario por su ID.", auditResult = AuditResultType.BOTH)
    // Buscar un usuario por su ID
    public Optional<Usuarios> findById(UUID id) {
        return usuariosRepository.findById(id);
    }

    // Auditar la acción de creación de un nuevo usuario
    @AuditableAction(actionName = AuditActions.CREACION_USUARIO, message = "Se intentó crear un nuevo usuario.", auditResult = AuditResultType.BOTH)
    // Crear un nuevo usuario
    public Usuarios create(Usuarios usuario) {
        // Encriptar la contraseña antes de guardar
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        // Establecer valores predeterminados para campos adicionales
        usuario.setFechaUltimoCambioContrasena(OffsetDateTime.now()); 
        usuario.setRequiereCambioContrasena(false); 
        usuario.setIntentosFallidosSesion(0);
        return usuariosRepository.save(usuario);
    }

    // Auditar la creación de múltiples usuarios
    @AuditableAction(actionName = AuditActions.CREACION_USUARIO, message = "Se intentó crear múltiples usuarios.")
    // Crear múltiples usuarios
    public List<Usuarios> createAll(List<Usuarios> usuarios) {
        for (Usuarios usuario : usuarios) {
            if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
                usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
            usuario.setFechaUltimoCambioContrasena(OffsetDateTime.now());
            usuario.setRequiereCambioContrasena(false);
            usuario.setIntentosFallidosSesion(0);
        }
        return usuariosRepository.saveAll(usuarios);
    }

    // Auditar la acción de actualización de un usuario existente
    @AuditableAction(actionName = AuditActions.ACTUALIZACION_USUARIO, message = "Se intentó actualizar un usuario.", auditResult = AuditResultType.BOTH)
    // Actualizar un usuario existente
    public Optional<Usuarios> update(UUID id, Usuarios usuarioActualizado) {
        return usuariosRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNombres(usuarioActualizado.getNombres());
            usuarioExistente.setApellidos(usuarioActualizado.getApellidos());
            usuarioExistente.setEmail(usuarioActualizado.getEmail());
            usuarioExistente.setEstado(usuarioActualizado.getEstado());
            usuarioExistente.setDosFactorActivo(usuarioActualizado.isDosFactorActivo());
            usuarioExistente.setIntentosFallidosSesion(usuarioActualizado.getIntentosFallidosSesion());
            usuarioExistente.setFechaUltimoIntentoFallido(usuarioActualizado.getFechaUltimoIntentoFallido());
            usuarioExistente.setFechaBloqueoSesion(usuarioActualizado.getFechaBloqueoSesion());
            usuarioExistente.setRequiereCambioContrasena(usuarioActualizado.isRequiereCambioContrasena());
            return usuariosRepository.save(usuarioExistente);
        });
    }

    // Auditar la acción de eliminación definitiva de un usuario
    @AuditableAction(actionName = AuditActions.ELIMINACION_DEFINITIVA_USUARIO, message = "Se intentó eliminar definitivamente un usuario.")
    // Eliminar definitivamente un usuario por su ID
    public void deleteById(UUID id) {
        usuariosRepository.deleteById(id);
    }

    // Auditar la acción de actualización de eliminación lógica de un usuario
    @AuditableAction(actionName = AuditActions.ELIMINACION_LOGICA_USUARIO, message = "Se intentó eliminar lógicamente un usuario.")
    // Eliminar lógicamente un usuario por su ID
    public Optional<Usuarios> softDelete(UUID id) {
        return usuariosRepository.findById(id).map(usuario -> {
            usuario.softDelete();
            return usuariosRepository.save(usuario);
        });
    }

    // Auditar filtrado de usuarios por estado
    @AuditableAction(actionName = AuditActions.FILTRADO_USUARIOS_POR_ESTADO, message = "Se intentó filtrar usuarios por estado.", auditResult = AuditResultType.BOTH)
    // Filtrar usuarios por estado con paginación
    public Page<Usuarios> findByEstado(String estado, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosRepository.findByEstado(estado, pageable);
    }

    // Auditar filtrado de usuarios que tienen 2FA activo
    @AuditableAction(actionName = AuditActions.FILTRADO_USUARIOS_POR_2FA, message = "Se intentó filtrar usuarios por si tienen 2FA activo.", auditResult = AuditResultType.BOTH)
    // Filtrar usuarios por si tienen 2FA activo
    public Page<Usuarios> findByDosFactorActivo(Boolean dosFactorActivo, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosRepository.findByDosFactorActivo(dosFactorActivo, pageable);
    }

    // Auditar filtrado de usuarios que requieren cambio de contraseña
    @AuditableAction(actionName = AuditActions.FILTRADO_USUARIOS_POR_CAMBIO_CONTRASENA_REQUERIDO, message = "Se intentó filtrar usuarios que requieren cambio de contraseña.", auditResult = AuditResultType.BOTH)
    // Filtrar usuarios que requieren cambio de contraseña
    public Page<Usuarios> findByRequiereCambioContrasena(boolean requiereCambioContrasena, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosRepository.findByRequiereCambioContrasena(requiereCambioContrasena, pageable);
    }

    // Auditar la acción de bloqueo de sesión de un usuario
    @AuditableAction(actionName = AuditActions.CONSULTA_BLOQUEO_SESION_USUARIO, message = "Se intentó bloquear la sesión de un usuario.")
    // Verificar si la sesión del usuario está bloqueada
    public boolean isSessionBlocked(UUID id) {
        return usuariosRepository.isSessionBlocked(id);
    }
}