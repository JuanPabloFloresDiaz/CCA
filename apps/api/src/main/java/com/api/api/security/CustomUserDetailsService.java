package com.api.api.security;

import com.api.api.model.Usuarios;
import com.api.api.model.TipoUsuario;
import com.api.api.model.PermisosTipoUsuario;
import com.api.api.model.Acciones;
import com.api.api.model.UsuariosTipoUsuario;
import com.api.api.repository.UsuariosRepository;
import com.api.api.repository.UsuariosTipoUsuarioRepository;
import com.api.api.service.PermisosTipoUsuarioService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuariosRepository usuariosRepository;
    private final UsuariosTipoUsuarioRepository usuariosTipoUsuarioRepository;
    private final PermisosTipoUsuarioService permisosTipoUsuarioService;

    public CustomUserDetailsService(UsuariosRepository usuariosRepository,
            UsuariosTipoUsuarioRepository usuariosTipoUsuarioRepository,
            PermisosTipoUsuarioService permisosTipoUsuarioService) {
        this.usuariosRepository = usuariosRepository;
        this.usuariosTipoUsuarioRepository = usuariosTipoUsuarioRepository;
        this.permisosTipoUsuarioService = permisosTipoUsuarioService;
    }

    /**
     * Carga los detalles del usuario por su nombre de usuario (en este caso,
     * email).
     * Este método es crucial para Spring Security, ya que proporciona la
     * información
     * del usuario (credenciales y autoridades/roles/permisos) para el proceso de
     * autenticación y autorización.
     *
     * @param email El email del usuario.
     * @return Un objeto UserDetails que contiene la información del usuario.
     * @throws UsernameNotFoundException Si el usuario no es encontrado.
     */
    @Override
    @Transactional // Asegura que las relaciones perezosas se carguen (e.g., usuariosTipoUsuarios)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos por email
        Usuarios usuario = usuariosRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Colección para almacenar todas las autoridades (roles y permisos/acciones)
        // del usuario
        Set<GrantedAuthority> authorities = new HashSet<>();

        // 1. Añadir roles basados en los Tipos de Usuario
        // 'UsuariosTipoUsuario' es la tabla intermedia que relaciona Usuarios con
        // TipoUsuario.
        // Un Usuario puede tener múltiples TipoUsuario.
        List<UsuariosTipoUsuario> usuarioTipoUsuarios = usuariosTipoUsuarioRepository
                .findAllByUsuarioId(usuario.getId());

        // Recopilar los IDs de los tipos de usuario para la siguiente consulta de
        // permisos
        Set<UUID> tipoUsuarioIds = new HashSet<>();

        if (usuarioTipoUsuarios != null && !usuarioTipoUsuarios.isEmpty()) {
            for (UsuariosTipoUsuario utu : usuarioTipoUsuarios) {
                TipoUsuario tipoUsuario = utu.getTipoUsuario();
                if (tipoUsuario != null) {
                    // Añadir el rol del TipoUsuario como una autoridad
                    // Aquí se asume que el nombre del tipo de usuario es único y se usa como   
                    // autoridad.
                    // Por convención, los roles se prefijan con "ROLE_".
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.getNombre().toUpperCase()));
                    tipoUsuarioIds.add(tipoUsuario.getId()); // Recopilar IDs de tipos de usuario
                }
            }
        }

        // 2. Añadir permisos (acciones) específicos basados en los Tipos de Usuario
        // Si el usuario tiene tipos de usuario, busca los permisos asociados a esos
        // tipos de usuario.
        if (!tipoUsuarioIds.isEmpty()) {
            List<PermisosTipoUsuario> permisos = permisosTipoUsuarioService.findByTipoUsuarioIdIn(tipoUsuarioIds);

            if (permisos != null && !permisos.isEmpty()) {
                for (PermisosTipoUsuario permiso : permisos) {
                    Acciones accion = permiso.getAccion();
                    if (accion != null) {
                        // Añadir la acción como una autoridad
                        // Aquí se asume que el nombre de la acción es único y se usa como
                        // autoridad.
                        authorities.add(new SimpleGrantedAuthority(accion.getNombre().toUpperCase()));
                    }
                }
            }
        }

        // Caso especial: Si por alguna razón el usuario no tiene roles asignados, puedes añadir un rol
        // por defecto. Aunque en un sistema de control de acceso, se esperaría que siempre tuviera
        // al menos uno.
        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));
        }

        // Retorna un objeto CustomUserDetails con la información del usuario
        // CustomUserDetails extiende User y añade el ID del usuario.
        return new CustomUserDetails(
                usuario.getId(), // ID del usuario
                usuario.getEmail(), // Nombre de usuario (email en este caso)
                usuario.getContrasena(), // Contraseña encriptada
                authorities // Roles y permisos/acciones del usuario
        );
    }
}
