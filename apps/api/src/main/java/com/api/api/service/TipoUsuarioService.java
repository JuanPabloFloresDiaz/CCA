package com.api.api.service;

import com.api.api.repository.TipoUsuarioRepository;
import com.api.api.dto.SimpleDTO.TipoUsuarioSimpleDTO;
import com.api.api.model.TipoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.api.api.audit.AuditableAction;
import com.api.api.audit.AuditableAction.AuditResultType;
import com.api.api.audit.AuditActions;

import java.util.Optional;
import java.util.UUID;

@Service
public class TipoUsuarioService {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    // Auditar la acción de búsqueda de todos los tipos de usuario
    @AuditableAction(actionName = AuditActions.BUSQUEDA_TIPOS_USUARIOS_SIMPLE, message = "Se intentó buscar todos los tipos de usuario.", auditResult = AuditResultType.BOTH)
    // Buscar todos los tipos de usuario sin paginación ni búsqueda, solo id y nombre
    public Iterable<TipoUsuarioSimpleDTO> findAllSelect() {
        return tipoUsuarioRepository.findAllSelect();
    }

    // Auditar la acción de búsqueda de todos los tipos de usuario con paginación y
    // búsqueda opcional
    @AuditableAction(actionName = AuditActions.BUSQUEDA_TIPOS_USUARIOS, message = "Se intentó buscar todos los tipos de usuario con paginación y búsqueda opcional.", auditResult = AuditResultType.BOTH)
    // Buscar todos los tipos de usuario con paginación y búsqueda opcional
    public Page<TipoUsuario> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return tipoUsuarioRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return tipoUsuarioRepository.findAll(pageable);
    }

    // Auditar la acción de búsqueda de un tipo de usuario por su ID
    @AuditableAction(actionName = AuditActions.BUSQUEDA_TIPOS_USUARIOS, message = "Se intentó buscar un tipo de usuario por su ID.", auditResult = AuditResultType.BOTH)
    // Buscar un tipo de usuario por su ID
    public Optional<TipoUsuario> findById(UUID id) {
        return tipoUsuarioRepository.findById(id);
    }

    // Auditar la acción de creación de un tipo de usuario
    @AuditableAction(actionName = AuditActions.CREACION_TIPO_USUARIO, message = "Se intentó crear un nuevo tipo de usuario.")
    // Crear un nuevo tipo de usuario
    public TipoUsuario create(TipoUsuario tipoUsuario) {
        return tipoUsuarioRepository.save(tipoUsuario);
    }

    // Auditar la acción de creación de múltiples tipos de usuario
    @AuditableAction(actionName = AuditActions.CREACION_TIPO_USUARIO, message = "Se intentó crear múltiples tipos de usuario.")
    // Crear múltiples tipos de usuario
    public Iterable<TipoUsuario> createAll(Iterable<TipoUsuario> tiposUsuario) {
        return tipoUsuarioRepository.saveAll(tiposUsuario);
    }

    // Auditar la acción de actualización de un tipo de usuario
    @AuditableAction(actionName = AuditActions.ACTUALIZACION_TIPO_USUARIO, message = "Se intentó actualizar un tipo de usuario existente.", auditResult = AuditResultType.BOTH)
    // Actualizar un tipo de usuario existente
    public Optional<TipoUsuario> update(UUID id, TipoUsuario tipoUsuarioActualizado) {
        return tipoUsuarioRepository.findById(id).map(tipoUsuario -> {
            tipoUsuario.setNombre(tipoUsuarioActualizado.getNombre());
            tipoUsuario.setDescripcion(tipoUsuarioActualizado.getDescripcion());
            tipoUsuario.setAplicacion(tipoUsuarioActualizado.getAplicacion());
            tipoUsuario.setEstado(tipoUsuarioActualizado.getEstado());
            return tipoUsuarioRepository.save(tipoUsuario);
        });
    }

    // Auditar la acción de eliminación de un tipo de usuario por su ID
    @AuditableAction(actionName = AuditActions.ELIMINACION_DEFINITIVA_TIPO_USUARIO, message = "Se intentó eliminar un tipo de usuario por su ID.", auditResult = AuditResultType.BOTH)
    // Eliminar definitivamente un tipo de usuario por su ID
    public void deleteById(UUID id) {
        tipoUsuarioRepository.deleteById(id);
    }

    // Auditar la acción de eliminación lógica de un tipo de usuario por su ID
    @AuditableAction(actionName = AuditActions.ELIMINACION_LOGICA_TIPO_USUARIO, message = "Se intentó eliminar lógicamente un tipo de usuario por su ID.", auditResult = AuditResultType.BOTH)
    // Eliminar lógicamente un tipo de usuario por su ID
    public Optional<TipoUsuario> softDelete(UUID id) {
        return tipoUsuarioRepository.findById(id).map(tipoUsuario -> {
            tipoUsuario.softDelete();
            return tipoUsuarioRepository.save(tipoUsuario);
        });
    }

    // Auditar la acción de actualización del estado de un tipo de usuario
    @AuditableAction(actionName = AuditActions.FILTRADO_TIPOS_USUARIOS_POR_ESTADO, message = "Se intentó actualizar el estado de un tipo de usuario.", auditResult = AuditResultType.BOTH)
    // Filtrar tipos de usuario por estado con paginación
    public Page<TipoUsuario> findByEstado(String estado, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return tipoUsuarioRepository.findByEstado(estado, pageable);
    }

    // Auditar la acción de filtrado de tipos de usuario por aplicación con paginación
    @AuditableAction(actionName = AuditActions.FILTRADO_TIPOS_USUARIOS_POR_APLICACION, message = "Se intentó filtrar tipos de usuario por aplicación con paginación.", auditResult = AuditResultType.BOTH)
    // Filtrar tipos de usuario por aplicación con paginación
    public Page<TipoUsuario> findByAplicacionId(UUID aplicacionId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return tipoUsuarioRepository.findByAplicacionId(aplicacionId, pageable);
    }

    // Filtrar tipos de usuario por nombre y aplicación
    public Optional<TipoUsuario> findByNombreAndAplicacionId(String nombre, UUID aplicacionId) {
        return tipoUsuarioRepository.findByNombreAndAplicacionId(nombre, aplicacionId).stream().findFirst();
    }
}