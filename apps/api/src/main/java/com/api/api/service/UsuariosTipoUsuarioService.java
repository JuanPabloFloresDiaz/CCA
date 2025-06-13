package com.api.api.service;

import com.api.api.repository.UsuariosTipoUsuarioRepository;
import com.api.api.model.UsuariosTipoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class UsuariosTipoUsuarioService {

    private final UsuariosTipoUsuarioRepository usuariosTipoUsuarioRepository;

    public UsuariosTipoUsuarioService(UsuariosTipoUsuarioRepository usuariosTipoUsuarioRepository) {
        this.usuariosTipoUsuarioRepository = usuariosTipoUsuarioRepository;
    }

    // Buscar todos con paginación y búsqueda opcional
    public Page<UsuariosTipoUsuario> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return usuariosTipoUsuarioRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return usuariosTipoUsuarioRepository.findAll(pageable);
    }

    // Buscar por ID
    public Optional<UsuariosTipoUsuario> findById(UUID id) {
        return usuariosTipoUsuarioRepository.findById(id);
    }

    // Crear uno nuevo
    public UsuariosTipoUsuario create(UsuariosTipoUsuario usuarioTipoUsuario) {
        return usuariosTipoUsuarioRepository.save(usuarioTipoUsuario);
    }

    // Crear múltiples
    public List<UsuariosTipoUsuario> createAll(List<UsuariosTipoUsuario> lista) {
        return usuariosTipoUsuarioRepository.saveAll(lista);
    }

    // Actualizar existente
    public Optional<UsuariosTipoUsuario> update(UUID id, UsuariosTipoUsuario actualizado) {
        return usuariosTipoUsuarioRepository.findById(id).map(usuTipoUsu -> {
            usuTipoUsu.setUsuario(actualizado.getUsuario());
            usuTipoUsu.setTipoUsuario(actualizado.getTipoUsuario());
            return usuariosTipoUsuarioRepository.save(usuTipoUsu);
        });
    }

    // Eliminar definitivamente por ID
    public void deleteById(UUID id) {
        usuariosTipoUsuarioRepository.deleteById(id);
    }

    // Eliminar lógicamente por ID
    public Optional<UsuariosTipoUsuario> softDelete(UUID id) {
        return usuariosTipoUsuarioRepository.findById(id).map(usuTipoUsu -> {
            usuTipoUsu.softDelete();
            return usuariosTipoUsuarioRepository.save(usuTipoUsu);
        });
    }

    // Filtrar por usuario
    public Page<UsuariosTipoUsuario> findByUsuarioId(UUID usuarioId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosTipoUsuarioRepository.findByUsuarioId(usuarioId, pageable);
    }

    // Filtrar por tipo de usuario
    public Page<UsuariosTipoUsuario> findByTipoUsuarioId(UUID tipoUsuarioId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosTipoUsuarioRepository.findByTipoUsuarioId(tipoUsuarioId, pageable);
    }
}