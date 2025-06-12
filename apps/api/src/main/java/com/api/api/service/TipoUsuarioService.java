package com.api.api.service;

import com.api.api.repository.TipoUsuarioRepository;
import com.api.api.dto.SimpleDTO.TipoUsuarioSimpleDTO;
import com.api.api.model.TipoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TipoUsuarioService {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    // Buscar todos los tipos de usuario sin paginación ni búsqueda, solo id y nombre
    public Iterable<TipoUsuarioSimpleDTO> findAllSelect() {
        return tipoUsuarioRepository.findAllSelect();
    }

    // Buscar todos los tipos de usuario con paginación y búsqueda opcional
    public Page<TipoUsuario> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return tipoUsuarioRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return tipoUsuarioRepository.findAll(pageable);
    }

    // Buscar un tipo de usuario por su ID
    public Optional<TipoUsuario> findById(UUID id) {
        return tipoUsuarioRepository.findById(id);
    }

    // Crear un nuevo tipo de usuario
    public TipoUsuario create(TipoUsuario tipoUsuario) {
        return tipoUsuarioRepository.save(tipoUsuario);
    }

    // Crear múltiples tipos de usuario
    public Iterable<TipoUsuario> createAll(Iterable<TipoUsuario> tiposUsuario) {
        return tipoUsuarioRepository.saveAll(tiposUsuario);
    }

    // Actualizar un tipo de usuario existente
    public Optional<TipoUsuario> update(UUID id, TipoUsuario tipoUsuarioActualizado) {
        return tipoUsuarioRepository.findById(id).map(tipoUsuario -> {
            tipoUsuario.setNombre(tipoUsuarioActualizado.getNombre());
            tipoUsuario.setDescripcion(tipoUsuarioActualizado.getDescripcion());
            tipoUsuario.setAplicacion(tipoUsuarioActualizado.getAplicacion());
            tipoUsuario.setEstado(tipoUsuarioActualizado.getEstado());
            return tipoUsuarioRepository.save(tipoUsuario);
        });
    }

    // Eliminar definitivamente un tipo de usuario por su ID
    public void deleteById(UUID id) {
        tipoUsuarioRepository.deleteById(id);
    }

    // Eliminar lógicamente un tipo de usuario por su ID
    public Optional<TipoUsuario> softDelete(UUID id) {
        return tipoUsuarioRepository.findById(id).map(tipoUsuario -> {
            tipoUsuario.softDelete();
            return tipoUsuarioRepository.save(tipoUsuario);
        });
    }

    // Filtrar tipos de usuario por estado con paginación
    public Page<TipoUsuario> findByEstado(String estado, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return tipoUsuarioRepository.findByEstado(estado, pageable);
    }

    // Filtrar tipos de usuario por aplicación con paginación
    public Page<TipoUsuario> findByAplicacionId(UUID aplicacionId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return tipoUsuarioRepository.findByAplicacionId(aplicacionId, pageable);
    }
}