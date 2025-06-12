package com.api.api.service;

import com.api.api.repository.UsuariosRepository;
import com.api.api.dto.SimpleDTO.UsuarioSimpleDTO;
import com.api.api.model.Usuarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    // Buscar todos los usuarios sin paginación ni búsqueda, solo id, nombres, apellidos y email
    public Iterable<UsuarioSimpleDTO> findAllSelect() {
        return usuariosRepository.findAllSelect();
    }

    // Buscar todos los usuarios con paginación y búsqueda opcional
    public Page<Usuarios> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return usuariosRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return usuariosRepository.findAll(pageable);
    }

    // Buscar un usuario por su ID
    public Optional<Usuarios> findById(UUID id) {
        return usuariosRepository.findById(id);
    }

    // Crear un nuevo usuario
    public Usuarios create(Usuarios usuario) {
        return usuariosRepository.save(usuario);
    }

    // Crear múltiples usuarios
    public List<Usuarios> createAll(List<Usuarios> usuarios) {
        return usuariosRepository.saveAll(usuarios);
    }

    // Actualizar un usuario existente
    public Optional<Usuarios> update(UUID id, Usuarios usuarioActualizado) {
        return usuariosRepository.findById(id).map(usuario -> {
            usuario.setNombres(usuarioActualizado.getNombres());
            usuario.setApellidos(usuarioActualizado.getApellidos());
            usuario.setEmail(usuarioActualizado.getEmail());
            usuario.setEstado(usuarioActualizado.getEstado());
            usuario.setDosFactorActivo(usuarioActualizado.isDosFactorActivo());
            usuario.setDosFactorSecretoTotp(usuarioActualizado.getDosFactorSecretoTotp());
            usuario.setIntentosFallidosSesion(usuarioActualizado.getIntentosFallidosSesion());
            usuario.setFechaUltimoIntentoFallido(usuarioActualizado.getFechaUltimoIntentoFallido());
            usuario.setFechaBloqueoSesion(usuarioActualizado.getFechaBloqueoSesion());
            usuario.setRequiereCambioContrasena(usuarioActualizado.isRequiereCambioContrasena());
            return usuariosRepository.save(usuario);
        });
    }

    // Eliminar definitivamente un usuario por su ID
    public void deleteById(UUID id) {
        usuariosRepository.deleteById(id);
    }

    // Eliminar lógicamente un usuario por su ID
    public Optional<Usuarios> softDelete(UUID id) {
        return usuariosRepository.findById(id).map(usuario -> {
            usuario.softDelete();
            return usuariosRepository.save(usuario);
        });
    }

    // Filtrar usuarios por estado con paginación
    public Page<Usuarios> findByEstado(String estado, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosRepository.findByEstado(estado, pageable);
    }

    // Filtrar usuarios por si tienen 2FA activo
    public Page<Usuarios> findByDosFactorActivo(Boolean dosFactorActivo, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosRepository.findByDosFactorActivo(dosFactorActivo, pageable);
    }

    // Filtrar usuarios que requieren cambio de contraseña
    public Page<Usuarios> findByRequiereCambioContrasena(boolean requiereCambioContrasena, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosRepository.findByRequiereCambioContrasena(requiereCambioContrasena, pageable);
    }

    // Verificar si la sesión del usuario está bloqueada
    public boolean isSessionBlocked(UUID id) {
        return usuariosRepository.isSessionBlocked(id);
    }
}