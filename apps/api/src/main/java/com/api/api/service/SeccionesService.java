package com.api.api.service;

import com.api.api.repository.SeccionesRepository;
import com.api.api.dto.SeccionSimpleDTO;
import com.api.api.model.Secciones;

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

    // Buscar todas las secciones sin paginación ni búsqueda y trayendo solo el id y
    // el nombre para evitar problemas de rendimiento
    public Iterable<SeccionSimpleDTO> findAllSelect() {
        return seccionesRepository.findAllSelect();
    }

    // Buscar todas las secciones con paginación y búsqueda opcional
    public Page<Secciones> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return seccionesRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return seccionesRepository.findAll(pageable);
    }

    // Buscar una sección por su ID
    public Optional<Secciones> findById(UUID id) {
        return seccionesRepository.findById(id);
    }

    // Crear una nueva sección
    public Secciones create(Secciones seccion) {
        return seccionesRepository.save(seccion);
    }

    // Actualizar una sección existente
    public Optional<Secciones> update(UUID id, Secciones seccionActualizada) {
        return seccionesRepository.findById(id).map(seccion -> {
            seccion.setNombre(seccionActualizada.getNombre());
            seccion.setDescripcion(seccionActualizada.getDescripcion());
            return seccionesRepository.save(seccion);
        });
    }

    // Eliminar definitivamente una sección por su ID
    public void deleteById(UUID id) {
        seccionesRepository.deleteById(id);
    }

    // Eliminar lógicamente una sección por su ID
    public Optional<Secciones> softDelete(UUID id) {
        return seccionesRepository.findById(id).map(seccion -> {
            seccion.softDelete();
            return seccionesRepository.save(seccion);
        });
    }

}
