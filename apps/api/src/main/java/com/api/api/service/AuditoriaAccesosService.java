package com.api.api.service;

import com.api.api.repository.AuditoriaAccesosRepository;
import com.api.api.model.AuditoriaAccesos;
import com.api.api.model.AuditoriaAccesosId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.api.api.audit.AuditableAction;
import com.api.api.audit.AuditableAction.AuditResultType;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class AuditoriaAccesosService {

    private final AuditoriaAccesosRepository auditoriaAccesosRepository;

    public AuditoriaAccesosService(AuditoriaAccesosRepository auditoriaAccesosRepository) {
        this.auditoriaAccesosRepository = auditoriaAccesosRepository;
    }

    // Buscar todas las auditorías con paginación y búsqueda opcional
    // Auditar la búsqueda de todas las auditorías (lectura de logs)
    @AuditableAction(actionName = "Consulta de Auditorías (Todas)", message = "Se consultaron todos los registros de auditoría.", auditResult = AuditResultType.SUCCESS)
    public Page<AuditoriaAccesos> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return auditoriaAccesosRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return auditoriaAccesosRepository.findAll(pageable);
    }

    // Buscar una auditoría por su ID compuesto (UUID y Fecha)
    // Auditar la búsqueda de una auditoría específica por su ID compuesto
    @AuditableAction(actionName = "Consulta de Auditoría por ID", message = "Se consultó un registro de auditoría específico.", auditResult = AuditResultType.SUCCESS)
    public Optional<AuditoriaAccesos> findById(UUID id, OffsetDateTime fecha) {
        AuditoriaAccesosId compositeId = new AuditoriaAccesosId(id, fecha);
        return auditoriaAccesosRepository.findById(compositeId);
    }

    // RECORDATORIO:
    // Los métodos CREATE, UPDATE, DELETE NO llevan @AuditableAction
    // para evitar bucles recursivos en el sistema de auditoría.
    // Crear una nueva auditoría
    public AuditoriaAccesos create(AuditoriaAccesos auditoria) {
        if (auditoria.getFecha() == null) {
            auditoria.setFecha(OffsetDateTime.now());
        }
        if (auditoria.getUuidId() == null) {
            auditoria.setUuidId(null);
        }
        return auditoriaAccesosRepository.save(auditoria);
    }

    // Crear múltiples auditorías
    public List<AuditoriaAccesos> createAll(List<AuditoriaAccesos> auditorias) {
        auditorias.forEach(auditoria -> {
            if (auditoria.getFecha() == null) {
                auditoria.setFecha(OffsetDateTime.now());
            }
            if (auditoria.getUuidId() == null) {
                auditoria.setUuidId(null);
            }
        });
        return auditoriaAccesosRepository.saveAll(auditorias);
    }

    // Actualizar una auditoría existente por su ID compuesto (UUID y Fecha)
    public Optional<AuditoriaAccesos> update(UUID id, OffsetDateTime fecha, AuditoriaAccesos auditoriaActualizada) {
        AuditoriaAccesosId compositeId = new AuditoriaAccesosId(id, fecha);
        return auditoriaAccesosRepository.findById(compositeId).map(auditoria -> {
            auditoria.setUsuario(auditoriaActualizada.getUsuario());
            auditoria.setAplicacion(auditoriaActualizada.getAplicacion());
            auditoria.setAccion(auditoriaActualizada.getAccion());
            auditoria.setIpOrigen(auditoriaActualizada.getIpOrigen());
            auditoria.setEstado(auditoriaActualizada.getEstado());
            auditoria.setEmailUsuario(auditoriaActualizada.getEmailUsuario());
            auditoria.setInformacionDispositivo(auditoriaActualizada.getInformacionDispositivo());
            auditoria.setMensaje(auditoriaActualizada.getMensaje());
            auditoria.setCreatedAt(auditoriaActualizada.getCreatedAt());
            auditoria.setUpdatedAt(OffsetDateTime.now());
            auditoria.setDeletedAt(auditoriaActualizada.getDeletedAt());

            return auditoriaAccesosRepository.save(auditoria);
        });
    }

    // Eliminar definitivamente una auditoría por su ID compuesto (UUID y Fecha)
    public void deleteById(UUID id, OffsetDateTime fecha) {
        AuditoriaAccesosId compositeId = new AuditoriaAccesosId(id, fecha);
        auditoriaAccesosRepository.deleteById(compositeId);
    }

    // Eliminar lógicamente una auditoría por su ID compuesto (UUID y Fecha)
    public Optional<AuditoriaAccesos> softDelete(UUID id, OffsetDateTime fecha) {
        AuditoriaAccesosId compositeId = new AuditoriaAccesosId(id, fecha);
        return auditoriaAccesosRepository.findById(compositeId).map(auditoria -> {
            auditoria.softDelete();
            return auditoriaAccesosRepository.save(auditoria);
        });
    }

    // Auditar el filtrado de auditorías por aplicación
    @AuditableAction(actionName = "Filtrado de Auditorías por Aplicación", message = "Se filtraron registros de auditoría por aplicación.", auditResult = AuditResultType.SUCCESS)
    // Filtrar auditorías por aplicación con paginación
    public Page<AuditoriaAccesos> findByAplicacionId(UUID aplicacionId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return auditoriaAccesosRepository.findByAplicacionId(aplicacionId, pageable);
    }

    // Auditar el filtrado de auditorías por acción
    @AuditableAction(actionName = "Filtrado de Auditorías por Acción", message = "Se filtraron registros de auditoría por acción.", auditResult = AuditResultType.SUCCESS)
    // Filtrar auditorías por acción con paginación
    public Page<AuditoriaAccesos> findByAccionId(UUID accionId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return auditoriaAccesosRepository.findByAccionId(accionId, pageable);
    }
}
