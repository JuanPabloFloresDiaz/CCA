package com.api.api.service;

import com.api.api.repository.SesionesRepository;
import com.api.api.model.Sesiones;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.api.audit.AuditableAction;
import com.api.api.audit.AuditableAction.AuditResultType;
import java.util.Optional;
import java.util.UUID;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class SesionesService {

    private final SesionesRepository sesionesRepository;

    public SesionesService(SesionesRepository sesionesRepository) {
        this.sesionesRepository = sesionesRepository;
    }

    // Auditar la acción de búsqueda de todas las sesiones
    @AuditableAction(actionName = "Búsqueda de Sesiones", message = "Se intentó buscar todas las sesiones.", auditResult = AuditResultType.BOTH)
    // Buscar todas las sesiones con paginación y búsqueda opcional
    public Page<Sesiones> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return sesionesRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return sesionesRepository.findAll(pageable);
    }

    // Auditar la acción de búsqueda de una sesión por su ID
    @AuditableAction(actionName = "Búsqueda de Sesión por ID", message = "Se intentó buscar una sesión por su ID.", auditResult = AuditResultType.BOTH)
    // Buscar una sesión por su ID
    public Optional<Sesiones> findById(UUID id) {
        return sesionesRepository.findById(id);
    }

    // Crear una nueva sesión
    public Sesiones create(Sesiones sesion) {
        return sesionesRepository.save(sesion);
    }

    // Crear múltiples sesiones
    public List<Sesiones> createAll(List<Sesiones> sesiones) {
        return sesionesRepository.saveAll(sesiones);
    }

    // Auditar la acción de actualización de una sesión
    @AuditableAction(actionName = "Actualización de Sesión", message = "Se intentó actualizar una sesión existente.", auditResult = AuditResultType.BOTH)
    // Actualizar una sesión existente
    public Optional<Sesiones> update(UUID id, Sesiones sesionActualizada) {
        return sesionesRepository.findById(id).map(sesion -> {
            sesion.setToken(sesionActualizada.getToken());
            sesion.setUsuario(sesionActualizada.getUsuario());
            sesion.setIpOrigen(sesionActualizada.getIpOrigen());
            sesion.setEmailUsuario(sesionActualizada.getEmailUsuario());
            sesion.setInformacionDispositivo(sesionActualizada.getInformacionDispositivo());
            sesion.setFechaExpiracion(sesionActualizada.getFechaExpiracion());
            sesion.setFechaInicio(sesionActualizada.getFechaInicio()); 
            sesion.setFechaFin(sesionActualizada.getFechaFin()); 
            sesion.setEstado(sesionActualizada.getEstado());
            return sesionesRepository.save(sesion);
        });
    }

    // Auditar la acción de actualización del estado de una sesión
    @AuditableAction(actionName = "Actualización de Estado de Sesión", message = "Se intentó actualizar el estado de una sesión.", auditResult = AuditResultType.BOTH)
    // Actualizar el estado de una sesión por su ID
    public Optional<Sesiones> updateStatus(UUID id, String newStatus, OffsetDateTime endDate) {
        return sesionesRepository.findById(id).map(sesion -> {
            sesion.setEstado(newStatus);
            sesion.setFechaFin(endDate);
            return sesionesRepository.save(sesion);
        });
    }

    // Auditar la acción de eliminación de una sesión por su ID
    @AuditableAction(actionName = "Eliminación de Sesión por ID", message = "Se intentó eliminar una sesión por su ID.", auditResult = AuditResultType.BOTH)
    // Eliminar definitivamente una sesión por su ID
    public void deleteById(UUID id) {
        sesionesRepository.deleteById(id);
    }

    // Auditar la acción de eliminación lógica de una sesión por su ID
    @AuditableAction(actionName = "Eliminación Lógica de Sesión por ID", message = "Se intentó eliminar lógicamente una sesión por su ID.", auditResult = AuditResultType.BOTH)
    // Eliminar lógicamente una sesión por su ID
    public Optional<Sesiones> softDelete(UUID id) {
        return sesionesRepository.findById(id).map(sesion -> {
            sesion.softDelete();
            return sesionesRepository.save(sesion);
        });
    }

    // Auditar la acción de búsqueda de sesiones por estado con paginación
    @AuditableAction(actionName = "Búsqueda de Sesiones por Estado", message = "Se intentó buscar sesiones por estado con paginación.", auditResult = AuditResultType.BOTH)
    // Filtrar sesiones por estado con paginación
    public Page<Sesiones> findByEstado(String estado, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return sesionesRepository.findByEstado(estado, pageable);
    }

    // Buscar una sesión por su token JWT
    public Optional<Sesiones> findByToken(String token) {
        return sesionesRepository.findByToken(token);
    }
}