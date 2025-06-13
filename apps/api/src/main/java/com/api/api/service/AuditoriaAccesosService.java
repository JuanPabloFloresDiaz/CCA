package com.api.api.service;

import com.api.api.repository.AuditoriaAccesosRepository;
import com.api.api.model.AuditoriaAccesos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    // Buscar una auditoría por su ID
    public Optional<AuditoriaAccesos> findById(UUID id) {
        return auditoriaAccesosRepository.findById(id);
    }

    // Crear una nueva auditoría
    public AuditoriaAccesos create(AuditoriaAccesos auditoria) {
        return auditoriaAccesosRepository.save(auditoria);
    }

    // Crear múltiples auditorías
    public List<AuditoriaAccesos> createAll(List<AuditoriaAccesos> auditorias) {
        return auditoriaAccesosRepository.saveAll(auditorias);
    }

    // Actualizar una auditoría existente
    public Optional<AuditoriaAccesos> update(UUID id, AuditoriaAccesos auditoriaActualizada) {
        return auditoriaAccesosRepository.findById(id).map(auditoria -> {
            auditoria.setUsuario(auditoriaActualizada.getUsuario());
            auditoria.setAplicacion(auditoriaActualizada.getAplicacion());
            auditoria.setAccion(auditoriaActualizada.getAccion());
            auditoria.setFecha(auditoriaActualizada.getFecha());
            auditoria.setIpOrigen(auditoriaActualizada.getIpOrigen());
            auditoria.setEstado(auditoriaActualizada.getEstado());
            return auditoriaAccesosRepository.save(auditoria);
        });
    }

    // Eliminar definitivamente una auditoría por su ID
    public void deleteById(UUID id) {
        auditoriaAccesosRepository.deleteById(id);
    }

    // Eliminar lógicamente una auditoría por su ID
    public Optional<AuditoriaAccesos> softDelete(UUID id) {
        return auditoriaAccesosRepository.findById(id).map(auditoria -> {
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