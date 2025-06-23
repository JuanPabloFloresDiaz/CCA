package com.api.api.service;

import com.api.api.repository.SeccionesRepository;
import com.api.api.dto.SimpleDTO.SeccionSimpleDTO;
import com.api.api.model.Secciones;

import com.api.api.audit.AuditableAction;
import com.api.api.audit.AuditableAction.AuditResultType;
import com.api.api.audit.AuditActions;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

@Service
public class SeccionesService {

    // Inyección de dependencias
    private final SeccionesRepository seccionesRepository;

    public SeccionesService(SeccionesRepository seccionesRepository) {
        this.seccionesRepository = seccionesRepository;
    }

    // Auditar la acción de búsqueda de todas las secciones
    @AuditableAction(actionName = AuditActions.BUSQUEDA_SECCIONES_SIMPLE, message = "Se intentó buscar todas las secciones.", auditResult = AuditResultType.BOTH)
    // Buscar todas las secciones sin paginación ni búsqueda y trayendo solo el id y
    // el nombre para evitar problemas de rendimiento
    public Iterable<SeccionSimpleDTO> findAllSelect() {
        return seccionesRepository.findAllSelect();
    }

    // Auditar la acción de búsqueda de todas las secciones con paginación y
    // búsqueda
    // opcional
    @AuditableAction(actionName = AuditActions.BUSQUEDA_SECCIONES, message = "Se intentó buscar todas las secciones con paginación y búsqueda opcional.", auditResult = AuditResultType.BOTH)
    // Buscar todas las secciones con paginación y búsqueda opcional
    public Page<Secciones> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return seccionesRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return seccionesRepository.findAll(pageable);
    }

    // Auditar la acción de búsqueda de una sección por su ID
    @AuditableAction(actionName = AuditActions.BUSQUEDA_SECCIONES, message = "Se intentó buscar una sección por su ID.", auditResult = AuditResultType.BOTH)
    // Buscar una sección por su ID
    public Optional<Secciones> findById(UUID id) {
        return seccionesRepository.findById(id);
    }

    // Auditar la acción de creación de una sección
    @AuditableAction(actionName = AuditActions.CREACION_SECCION, message = "Se intentó crear una nueva sección.")
    // Crear una nueva sección
    public Secciones create(Secciones seccion) {
        return seccionesRepository.save(seccion);
    }

    // Auditar la acción de creación de múltiples secciones
    @AuditableAction(actionName = AuditActions.CREACION_SECCION, message = "Se intentó crear múltiples secciones.")
    // Crear múltiples secciones
    public Iterable<Secciones> createAll(Iterable<Secciones> secciones) {
        return seccionesRepository.saveAll(secciones);
    }

    // Auditar la acción de actualización de una sección
    @AuditableAction(actionName = AuditActions.ACTUALIZACION_SECCION, message = "Se intentó actualizar una sección.")
    // Actualizar una sección existente
    public Optional<Secciones> update(UUID id, Secciones seccionActualizada) {
        return seccionesRepository.findById(id).map(seccion -> {
            seccion.setNombre(seccionActualizada.getNombre());
            seccion.setDescripcion(seccionActualizada.getDescripcion());
            return seccionesRepository.save(seccion);
        });
    }

    // Auditar la acción de eliminación de una sección por su ID
    @AuditableAction(actionName = AuditActions.ELIMINACION_DEFINITIVA_SECCION, message = "Se intentó eliminar una sección por su ID.")
    // Eliminar definitivamente una sección por su ID
    public void deleteById(UUID id) {
        seccionesRepository.deleteById(id);
    }

    // Auditar la acción de eliminación lógica de una sección por su ID
    @AuditableAction(actionName = AuditActions.ELIMINACION_LOGICA_SECCION, message = "Se intentó eliminar lógicamente una sección por su ID.")
    // Eliminar lógicamente una sección por su ID
    public Optional<Secciones> softDelete(UUID id) {
        return seccionesRepository.findById(id).map(seccion -> {
            seccion.softDelete();
            return seccionesRepository.save(seccion);
        });
    }

}
