package com.api.api.service;

import com.api.api.repository.AplicacionesRepository;
import com.api.api.dto.SimpleDTO.AplicacionSimpleDTO;
import com.api.api.model.Aplicaciones;

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
    
    // Buscar todas las aplicaciones sin paginación ni búsqueda, solo id y nombre
    public Iterable<AplicacionSimpleDTO> findAllSelect() {
        return aplicacionesRepository.findAllSelect();
    }

    // Buscar todas las aplicaciones con paginación y búsqueda opcional
    public Page<Aplicaciones> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return aplicacionesRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return aplicacionesRepository.findAll(pageable);
    }

    // Buscar una aplicación por su ID
    public Optional<Aplicaciones> findById(UUID id) {
        return aplicacionesRepository.findById(id);
    }

    // Crear una nueva aplicación
    public Aplicaciones create(Aplicaciones aplicacion) {
        return aplicacionesRepository.save(aplicacion);
    }

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

    // Eliminar definitivamente una aplicación por su ID
    public void deleteById(UUID id) {
        aplicacionesRepository.deleteById(id);
    }

    // Eliminar lógicamente una aplicación por su ID
    public Optional<Aplicaciones> softDelete(UUID id) {
        return aplicacionesRepository.findById(id).map(aplicacion -> {
            aplicacion.softDelete();
            return aplicacionesRepository.save(aplicacion);
        });
    }

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
