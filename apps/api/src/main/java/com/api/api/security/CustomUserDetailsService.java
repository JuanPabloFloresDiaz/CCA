package com.api.api.security;

import com.api.api.model.Usuarios;
import com.api.api.repository.UsuariosRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections; // Importación necesaria
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuariosRepository usuariosRepository;

    public CustomUserDetailsService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    /**
     * Carga los detalles del usuario por su nombre de usuario (en este caso,
     * email).
     * 
     * @param email El email del usuario.
     * @return Un objeto UserDetails que contiene la información del usuario.
     * @throws UsernameNotFoundException Si el usuario no es encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos por email
        Usuarios usuario = usuariosRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Construir el objeto UserDetails.
        // Si el modelo Usuarios tiene roles (ej. List<Role>), mapearlos a
        // GrantedAuthority.
        // En un sistema de control de accesos, los roles vendrán de
        // 'usuariosTipoUsuarios'.
        // Pendiente: Modificar esta sección para cargar los roles/autoridades reales
        // del usuario.

        // Pendiente: Asignar un rol 'USER' si no hay roles específicos mapeados aún.
        // Pendiente: Cargar desde usuario.getUsuariosTipoUsuarios() si ya tienes esa
        // lógica.
        // Por ahora, se crea una autoridad simple.
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        // Idealmente, se cargarian las autoridades reales del usuario desde la base de
        // datos,
        // por ejemplo, desde la tabla usuarios_tipo_usuario y permisos_tipo_usuario.
        /*
         * List<GrantedAuthority> authorities =
         * usuario.getUsuariosTipoUsuarios().stream()
         * .map(utu -> new SimpleGrantedAuthority("ROLE_" +
         * utu.getTipoUsuario().getNombre().toUpperCase()))
         * .collect(Collectors.toList());
         */

        // Retorna un objeto CustomUserDetails con la información del usuario
        // CustomUserDetails extiende User y añade el ID del usuario.
        return new CustomUserDetails(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getContrasena(),
                authorities // Aquí se pasarán los roles/permisos reales
        );
    }
}
