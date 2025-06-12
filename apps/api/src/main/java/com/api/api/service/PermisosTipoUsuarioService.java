package com.api.api.service;

import com.api.api.repository.PermisosTipoUsuarioRepository;
import com.api.api.model.PermisosTipoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class PermisosTipoUsuarioService {

    private final PermisosTipoUsuarioRepository permisosTipoUsuarioRepository;

    public PermisosTipoUsuarioService(PermisosTipoUsuarioRepository permisosTipoUsuarioRepository) {
        this.permisosTipoUsuarioRepository = permisosTipoUsuarioRepository;
    }

    // Buscar todos los permisos con paginación y búsqueda opcional
    public Page<PermisosTipoUsuario> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return permisosTipoUsuarioRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return permisosTipoUsuarioRepository.findAll(pageable);
    }

    // Buscar un permiso por su ID
    public Optional<PermisosTipoUsuario> findById(UUID id) {
        return permisosTipoUsuarioRepository.findById(id);
    }

    // Crear un nuevo permiso
    public PermisosTipoUsuario create(PermisosTipoUsuario permiso) {
        return permisosTipoUsuarioRepository.save(permiso);
    }

    // Crear múltiples permisos
    public List<PermisosTipoUsuario> createAll(List<PermisosTipoUsuario> permisos) {
        return permisosTipoUsuarioRepository.saveAll(permisos);
    }

    // Actualizar un permiso existente
    public Optional<PermisosTipoUsuario> update(UUID id, PermisosTipoUsuario permisoActualizado) {
        return permisosTipoUsuarioRepository.findById(id).map(permiso -> {
            permiso.setAccion(permisoActualizado.getAccion());
            permiso.setTipoUsuario(permisoActualizado.getTipoUsuario());
            return permisosTipoUsuarioRepository.save(permiso);
        });
    }

    // Eliminar definitivamente un permiso por su ID
    public void deleteById(UUID id) {
        permisosTipoUsuarioRepository.deleteById(id);
    }

    // Eliminar lógicamente un permiso por su ID
    public Optional<PermisosTipoUsuario> softDelete(UUID id) {
        return permisosTipoUsuarioRepository.findById(id).map(permiso -> {
            permiso.softDelete();
            return permisosTipoUsuarioRepository.save(permiso);
        });
    }

    // Filtrar permisos por tipo de usuario con paginación
    public Page<PermisosTipoUsuario> findByTipoUsuarioId(UUID tipoUsuarioId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return permisosTipoUsuarioRepository.findByTipoUsuarioId(tipoUsuarioId, pageable);
    }

    // Filtrar permisos por aplicación con paginación
    public Page<PermisosTipoUsuario> findByAplicacionId(UUID aplicacionId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return permisosTipoUsuarioRepository.findByAplicacionId(aplicacionId, pageable);
    }
}