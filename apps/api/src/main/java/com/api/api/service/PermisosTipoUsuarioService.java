package com.api.api.service;

import com.api.api.repository.PermisosTipoUsuarioRepository;
import com.api.api.model.PermisosTipoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.api.api.audit.AuditableAction;
import com.api.api.audit.AuditableAction.AuditResultType;

import java.util.Optional;
import java.util.UUID;
import java.util.Collection;
import java.util.List;

@Service
public class PermisosTipoUsuarioService {

    private final PermisosTipoUsuarioRepository permisosTipoUsuarioRepository;

    public PermisosTipoUsuarioService(PermisosTipoUsuarioRepository permisosTipoUsuarioRepository) {
        this.permisosTipoUsuarioRepository = permisosTipoUsuarioRepository;
    }

    // Auditar la acción de búsqueda de todos los permisos
    @AuditableAction(actionName = "Búsqueda de Permisos", message = "Se intentó buscar todos los permisos.", auditResult = AuditResultType.BOTH)
    // Buscar todos los permisos con paginación y búsqueda opcional
    public Page<PermisosTipoUsuario> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return permisosTipoUsuarioRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return permisosTipoUsuarioRepository.findAll(pageable);
    }


    // Auditar la acción de búsqueda de un permiso por su ID
    // Buscar un permiso por su ID
    public Optional<PermisosTipoUsuario> findById(UUID id) {
        return permisosTipoUsuarioRepository.findById(id);
    }

    // Auditar la acción de creación de un permiso
    @AuditableAction(actionName = "Creación de Permiso", message = "Se intentó crear un nuevo permiso.")
    // Crear un nuevo permiso
    public PermisosTipoUsuario create(PermisosTipoUsuario permiso) {
        return permisosTipoUsuarioRepository.save(permiso);
    }

    // Auditar la acción de creación de múltiples permisos
    @AuditableAction(actionName = "Creación de Múltiples Permisos", message = "Se intentó crear múltiples permisos.")
    // Crear múltiples permisos
    public List<PermisosTipoUsuario> createAll(List<PermisosTipoUsuario> permisos) {
        return permisosTipoUsuarioRepository.saveAll(permisos);
    }

    // Auditar la acción de actualización de un permiso
    @AuditableAction(actionName = "Actualización de Permiso", message = "Se intentó actualizar un permiso existente.")
    // Actualizar un permiso existente
    public Optional<PermisosTipoUsuario> update(UUID id, PermisosTipoUsuario permisoActualizado) {
        return permisosTipoUsuarioRepository.findById(id).map(permiso -> {
            permiso.setAccion(permisoActualizado.getAccion());
            permiso.setTipoUsuario(permisoActualizado.getTipoUsuario());
            return permisosTipoUsuarioRepository.save(permiso);
        });
    }

    // Auditar la acción de eliminación de un permiso por su ID
    @AuditableAction(actionName = "Eliminación de Permiso", message = "Se intentó eliminar un permiso por su ID.")
    // Eliminar definitivamente un permiso por su ID
    public void deleteById(UUID id) {
        permisosTipoUsuarioRepository.deleteById(id);
    }

    // Auditar la acción de eliminación lógica de un permiso por su ID

    @AuditableAction(actionName = "Eliminación Lógica de Permiso", message = "Se intentó eliminar lógicamente un permiso por su ID.")
    // Eliminar lógicamente un permiso por su ID
    public Optional<PermisosTipoUsuario> softDelete(UUID id) {
        return permisosTipoUsuarioRepository.findById(id).map(permiso -> {
            permiso.softDelete();
            return permisosTipoUsuarioRepository.save(permiso);
        });
    }

    // Auditar la acción de búsqueda de permisos por tipo de usuario
    @AuditableAction(actionName = "Búsqueda de Permisos por Tipo de Usuario", message = "Se intentó buscar permisos por tipo de usuario con paginación.", auditResult = AuditResultType.BOTH)
    // Filtrar permisos por tipo de usuario con paginación
    public Page<PermisosTipoUsuario> findByTipoUsuarioId(UUID tipoUsuarioId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return permisosTipoUsuarioRepository.findByTipoUsuarioId(tipoUsuarioId, pageable);
    }

    // Auditar la acción de búsqueda de permisos por aplicación
    @AuditableAction(actionName = "Búsqueda de Permisos por Aplicación", message = "Se intentó buscar permisos por aplicación con paginación.", auditResult = AuditResultType.BOTH)
    // Filtrar permisos por aplicación con paginación
    public Page<PermisosTipoUsuario> findByAplicacionId(UUID aplicacionId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return permisosTipoUsuarioRepository.findByAplicacionId(aplicacionId, pageable);
    }

    /**
     * Obtiene una lista de permisos de tipo de usuario para una colección de IDs de tipo de usuario.
     * No está auditado directamente para evitar bucles si es llamado por el aspecto de auditoría o por sí mismo.
     * @param tipoUsuarioIds Colección de UUIDs de tipos de usuario.
     * @return Lista de PermisosTipoUsuario.
     */
    public List<PermisosTipoUsuario> findByTipoUsuarioIdIn(Collection<UUID> tipoUsuarioIds) {
        return permisosTipoUsuarioRepository.findByTipoUsuarioIdIn(tipoUsuarioIds);
    }
}