package com.api.api.service;

import com.api.api.repository.SesionesRepository;
import com.api.api.model.Sesiones;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class SesionesService {

    private final SesionesRepository sesionesRepository;

    public SesionesService(SesionesRepository sesionesRepository) {
        this.sesionesRepository = sesionesRepository;
    }

    // Buscar todas las sesiones con paginación y búsqueda opcional
    public Page<Sesiones> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return sesionesRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return sesionesRepository.findAll(pageable);
    }

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

    // Actualizar una sesión existente
    public Optional<Sesiones> update(UUID id, Sesiones sesionActualizada) {
        return sesionesRepository.findById(id).map(sesion -> {
            sesion.setToken(sesionActualizada.getToken());
            sesion.setUsuario(sesionActualizada.getUsuario());
            sesion.setIpOrigen(sesionActualizada.getIpOrigen());
            sesion.setEmailUsuario(sesionActualizada.getEmailUsuario());
            sesion.setInformacionDispositivo(sesionActualizada.getInformacionDispositivo());
            sesion.setFechaExpiracion(sesionActualizada.getFechaExpiracion());
            sesion.setEstado(sesionActualizada.getEstado());
            return sesionesRepository.save(sesion);
        });
    }

    // Eliminar definitivamente una sesión por su ID
    public void deleteById(UUID id) {
        sesionesRepository.deleteById(id);
    }

    // Eliminar lógicamente una sesión por su ID
    public Optional<Sesiones> softDelete(UUID id) {
        return sesionesRepository.findById(id).map(sesion -> {
            sesion.softDelete();
            return sesionesRepository.save(sesion);
        });
    }

    // Filtrar sesiones por estado con paginación
    public Page<Sesiones> findByEstado(String estado, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return sesionesRepository.findByEstado(estado, pageable);
    }
}