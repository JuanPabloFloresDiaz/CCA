package com.api.api.service;

import com.api.api.repository.AuditoriaAccesosRepository;
import com.api.api.model.AuditoriaAccesos;
import com.api.api.model.AuditoriaAccesosId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Page<AuditoriaAccesos> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return auditoriaAccesosRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return auditoriaAccesosRepository.findAll(pageable);
    }

    // Buscar una auditoría por su ID compuesto (UUID y Fecha)
    public Optional<AuditoriaAccesos> findById(UUID id, OffsetDateTime fecha) {
        AuditoriaAccesosId compositeId = new AuditoriaAccesosId(id, fecha);
        return auditoriaAccesosRepository.findById(compositeId);
    }

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

    // Filtrar auditorías por aplicación con paginación
    public Page<AuditoriaAccesos> findByAplicacionId(UUID aplicacionId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return auditoriaAccesosRepository.findByAplicacionId(aplicacionId, pageable);
    }

    // Filtrar auditorías por acción con paginación
    public Page<AuditoriaAccesos> findByAccionId(UUID accionId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return auditoriaAccesosRepository.findByAccionId(accionId, pageable);
    }
}
