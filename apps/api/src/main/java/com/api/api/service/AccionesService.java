package com.api.api.service;

import com.api.api.repository.AccionesRepository;
import com.api.api.dto.SimpleDTO.AccionSimpleDTO;
import com.api.api.model.Acciones;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class AccionesService {

    private final AccionesRepository accionesRepository;

    public AccionesService(AccionesRepository accionesRepository) {
        this.accionesRepository = accionesRepository;
    }

    // Buscar todas las acciones sin paginación ni búsqueda, solo id y nombre
    public Iterable<AccionSimpleDTO> findAllSelect() {
        return accionesRepository.findAllSelect();
    }

    // Buscar todas las acciones con paginación y búsqueda opcional
    public Page<Acciones> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return accionesRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return accionesRepository.findAll(pageable);
    }

    // Buscar una acción por su ID
    public Optional<Acciones> findById(UUID id) {
        return accionesRepository.findById(id);
    }

    // Crear una nueva acción
    public Acciones create(Acciones accion) {
        return accionesRepository.save(accion);
    }

    // Crear múltiples acciones
    public List<Acciones> createAll(List<Acciones> acciones) {
        return accionesRepository.saveAll(acciones);
    }

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

    // Eliminar definitivamente una acción por su ID
    public void deleteById(UUID id) {
        accionesRepository.deleteById(id);
    }

    // Eliminar lógicamente una acción por su ID
    public Optional<Acciones> softDelete(UUID id) {
        return accionesRepository.findById(id).map(accion -> {
            accion.softDelete();
            return accionesRepository.save(accion);
        });
    }

    // Buscar acciones por aplicación con paginación
    public Page<Acciones> findByAplicacionId(UUID aplicacionId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return accionesRepository.findByAplicacionId(aplicacionId, pageable);
    }

    // Buscar acciones por sección con paginación
    public Page<Acciones> findBySeccionId(UUID seccionId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return accionesRepository.findBySeccionId(seccionId, pageable);
    }
}