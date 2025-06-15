package com.api.api.service;

import com.api.api.repository.AccionesRepository;
import com.api.api.dto.SimpleDTO.AccionSimpleDTO;
import com.api.api.model.Acciones;
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
public class AccionesService {

    private final AccionesRepository accionesRepository;

    public AccionesService(AccionesRepository accionesRepository) {
        this.accionesRepository = accionesRepository;
    }

    // Auditar la acción de búsqueda de todas las acciones
    @AuditableAction(actionName = "Búsqueda de Acciones", message = "Se intentó buscar todas las acciones.", auditResult = AuditResultType.BOTH)
    // Buscar todas las acciones sin paginación ni búsqueda, solo id y nombre
    public Iterable<AccionSimpleDTO> findAllSelect() {
        return accionesRepository.findAllSelect();
    }

    // Auditar la acción de búsqueda de todas las acciones con paginación y búsqueda
    // opcional
    @AuditableAction(actionName = "Búsqueda de Acciones con Paginación", message = "Se intentó buscar todas las acciones con paginación y búsqueda opcional.", auditResult = AuditResultType.BOTH)
    // Buscar todas las acciones con paginación y búsqueda opcional
    public Page<Acciones> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return accionesRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return accionesRepository.findAll(pageable);
    }

    // Auditar la acción de búsqueda de una acción por su ID
    @AuditableAction(actionName = "Búsqueda de Acción por ID", message = "Se intentó buscar una acción por su ID.", auditResult = AuditResultType.BOTH)
    // Buscar una acción por su ID
    public Optional<Acciones> findById(UUID id) {
        return accionesRepository.findById(id);
    }

    // Auditar la acción de creación de una acción
    @AuditableAction(actionName = "Creación de Acción", message = "Se intentó crear una nueva acción.")
    // Crear una nueva acción
    public Acciones create(Acciones accion) {
        return accionesRepository.save(accion);
    }

    // Auditar la acción de creación de múltiples acciones
    @AuditableAction(actionName = "Creación de Múltiples Acciones", message = "Se intentó crear múltiples acciones.")
    // Crear múltiples acciones
    public List<Acciones> createAll(List<Acciones> acciones) {
        return accionesRepository.saveAll(acciones);
    }

    // Auditar la acción de actualización de una acción
    @AuditableAction(actionName = "Actualización de Acción", message = "Se intentó actualizar una acción existente.")
    // Actualizar una acción existente
    public Optional<Acciones> update(UUID id, Acciones accionActualizada) {
        return accionesRepository.findById(id).map(accion -> {
            accion.setNombre(accionActualizada.getNombre());
            accion.setDescripcion(accionActualizada.getDescripcion());
            accion.setAplicacion(accionActualizada.getAplicacion());
            accion.setSeccion(accionActualizada.getSeccion());
            return accionesRepository.save(accion);
        });
    }

    // Auditar la acción de eliminación de una acción por su ID
    @AuditableAction(actionName = "Eliminación de Acción por ID", message = "Se intentó eliminar una acción por su ID.")
    // Eliminar definitivamente una acción por su ID
    public void deleteById(UUID id) {
        accionesRepository.deleteById(id);
    }

    // Auditar la acción de eliminación lógica de una acción por su ID
    @AuditableAction(actionName = "Eliminación Lógica de Acción", message = "Se intentó eliminar lógicamente una acción por su ID.")
    // Eliminar lógicamente una acción por su ID
    public Optional<Acciones> softDelete(UUID id) {
        return accionesRepository.findById(id).map(accion -> {
            accion.softDelete();
            return accionesRepository.save(accion);
        });
    }

    // Auditar la acción de búsqueda de acciones por aplicación con paginación
    @AuditableAction(actionName = "Búsqueda de Acciones por Aplicación", message = "Se intentó buscar acciones por aplicación con paginación.", auditResult = AuditResultType.BOTH)
    // Buscar acciones por aplicación con paginación
    public Page<Acciones> findByAplicacionId(UUID aplicacionId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return accionesRepository.findByAplicacionId(aplicacionId, pageable);
    }

    // Auditar la acción de búsqueda de acciones por sección con paginación
    @AuditableAction(actionName = "Búsqueda de Acciones por Sección", message = "Se intentó buscar acciones por sección con paginación.", auditResult = AuditResultType.BOTH)
    // Buscar acciones por sección con paginación
    public Page<Acciones> findBySeccionId(UUID seccionId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return accionesRepository.findBySeccionId(seccionId, pageable);
    }

    // Buscar una acción por nombre y aplicación
    public Optional<Acciones> findByNombreAndAplicacionId(String nombre, UUID aplicacionId) {
        return Optional.ofNullable(accionesRepository.findByNombreAndAplicacionId(nombre, aplicacionId));
    }
}