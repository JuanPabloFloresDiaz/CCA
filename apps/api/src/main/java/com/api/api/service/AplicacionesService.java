package com.api.api.service;

import com.api.api.repository.AplicacionesRepository;
import com.api.api.dto.SimpleDTO.AplicacionSimpleDTO;
import com.api.api.model.Aplicaciones;
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
public class AplicacionesService {

    private final AplicacionesRepository aplicacionesRepository;

    public AplicacionesService(AplicacionesRepository aplicacionesRepository) {
        this.aplicacionesRepository = aplicacionesRepository;
    }
    
    // Auditar la acción de búsqueda de todas las aplicaciones
    @AuditableAction(actionName = AuditActions.BUSQUEDA_APLICACIONES_SIMPLE, message = "Se intentó buscar todas las aplicaciones.", auditResult = AuditResultType.BOTH)
    // Buscar todas las aplicaciones sin paginación ni búsqueda, solo id y nombre
    public Iterable<AplicacionSimpleDTO> findAllSelect() {
        return aplicacionesRepository.findAllSelect();
    }

    // Auditar la acción de búsqueda de todas las aplicaciones con paginación y búsqueda opcional
    @AuditableAction(actionName = AuditActions.BUSQUEDA_APLICACIONES, message = "Se intentó buscar todas las aplicaciones con paginación y búsqueda opcional.", auditResult = AuditResultType.BOTH)
    // Buscar todas las aplicaciones con paginación y búsqueda opcional
    public Page<Aplicaciones> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return aplicacionesRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return aplicacionesRepository.findAll(pageable);
    }

    // Auditar la acción de búsqueda de una aplicación por su ID
    @AuditableAction(actionName = AuditActions.BUSQUEDA_APLICACIONES, message = "Se intentó buscar una aplicación por su ID.", auditResult = AuditResultType.BOTH)
    // Buscar una aplicación por su ID
    public Optional<Aplicaciones> findById(UUID id) {
        return aplicacionesRepository.findById(id);
    }

    // Auditar la acción de creación de una aplicación
    @AuditableAction(actionName = AuditActions.CREACION_APLICACION, message = "Se intentó crear una nueva aplicación.")
    // Crear una nueva aplicación
    public Aplicaciones create(Aplicaciones aplicacion) {
        return aplicacionesRepository.save(aplicacion);
    }

    // Auditar la acción de creación de múltiples aplicaciones
    @AuditableAction(actionName = AuditActions.CREACION_APLICACION, message = "Se intentó crear múltiples aplicaciones.")
    // Actualizar una aplicación existente
    public Optional<Aplicaciones> update(UUID id, Aplicaciones aplicacionActualizada) {
        return aplicacionesRepository.findById(id).map(aplicacion -> {
            aplicacion.setNombre(aplicacionActualizada.getNombre());
            aplicacion.setDescripcion(aplicacionActualizada.getDescripcion());
            aplicacion.setUrl(aplicacionActualizada.getUrl());
            aplicacion.setLlaveIdentificadora(aplicacionActualizada.getLlaveIdentificadora());
            return aplicacionesRepository.save(aplicacion);
        });
    }
    // Auditar la acción de eliminación de una aplicación por su ID
    @AuditableAction(actionName = AuditActions.ELIMINACION_DEFINITIVA_APLICACION, message = "Se intentó eliminar una aplicación por su ID.")
    // Eliminar definitivamente una aplicación por su ID
    public void deleteById(UUID id) {
        aplicacionesRepository.deleteById(id);
    }

    // Auditar la acción de eliminación lógica de una aplicación por su ID
    @AuditableAction(actionName = AuditActions.ELIMINACION_LOGICA_APLICACION, message = "Se intentó eliminar lógicamente una aplicación por su ID.")
    // Eliminar lógicamente una aplicación por su ID
    public Optional<Aplicaciones> softDelete(UUID id) {
        return aplicacionesRepository.findById(id).map(aplicacion -> {
            aplicacion.softDelete();
            return aplicacionesRepository.save(aplicacion);
        });
    }

    // Auditar la acción de busqueda de aplicaciones por estado con paginación
    @AuditableAction(actionName = AuditActions.BUSQUEDA_APLICACIONES, message = "Se intentó buscar aplicaciones por estado con paginación.", auditResult = AuditResultType.BOTH)
    // Filtrar aplicaciones por estado con paginación
    public Page<Aplicaciones> findByEstado(String estado, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return aplicacionesRepository.findByEstado(estado, pageable);
    }

    // Buscar una aplicación por su llave identificadora
    public Optional<Aplicaciones> findByLlaveIdentificadora(String llaveIdentificadora) {
        return Optional.ofNullable(aplicacionesRepository.findByLlaveIdentificadora(llaveIdentificadora));
    }
}
