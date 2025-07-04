package com.api.api.service;

import com.api.api.repository.UsuariosTipoUsuarioRepository;
import com.api.api.dto.ResponseDTO.PermisoAccionDTO;
import com.api.api.dto.ResponseDTO.SeccionPermisosDTO;
import com.api.api.exception.ResourceNotFoundException;
import com.api.api.model.Acciones;
import com.api.api.model.Aplicaciones;
import com.api.api.model.PermisosTipoUsuario;
import com.api.api.model.TipoUsuario;
import com.api.api.model.UsuariosTipoUsuario;
import com.api.api.model.Secciones;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.api.api.audit.AuditableAction;
import com.api.api.audit.AuditableAction.AuditResultType;
import com.api.api.audit.AuditActions;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.LinkedHashMap; 
@Service
public class UsuariosTipoUsuarioService {

    private final UsuariosTipoUsuarioRepository usuariosTipoUsuarioRepository;
    private final AplicacionesService aplicacionesService;
    private final PermisosTipoUsuarioService permisosTipoUsuarioService;

    public UsuariosTipoUsuarioService(UsuariosTipoUsuarioRepository usuariosTipoUsuarioRepository,
            AplicacionesService aplicacionesService,
            PermisosTipoUsuarioService permisosTipoUsuarioService) {
        this.usuariosTipoUsuarioRepository = usuariosTipoUsuarioRepository;
        this.aplicacionesService = aplicacionesService;
        this.permisosTipoUsuarioService = permisosTipoUsuarioService;
    }

    // Auditar la acción de búsqueda de todos los usuarios tipo usuario
    @AuditableAction(actionName = AuditActions.BUSQUEDA_TIPOS_USUARIOS, message = "Se intentó buscar todos los usuarios tipo usuario.", auditResult = AuditResultType.BOTH)
    // Buscar todos con paginación y búsqueda opcional
    public Page<UsuariosTipoUsuario> findAll(int page, int limit, String searchTerm) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return usuariosTipoUsuarioRepository.searchAllFields(searchTerm.toLowerCase(), pageable);
        }
        return usuariosTipoUsuarioRepository.findAll(pageable);
    }

    // Auditar la acción de búsqueda de un usuario tipo usuario por ID
    @AuditableAction(actionName = AuditActions.BUSQUEDA_TIPOS_USUARIOS, message = "Se intentó buscar un usuario tipo usuario por su ID.", auditResult = AuditResultType.BOTH)
    // Buscar por ID
    public Optional<UsuariosTipoUsuario> findById(UUID id) {
        return usuariosTipoUsuarioRepository.findById(id);
    }

    // Auditar la acción de creación de un usuario tipo usuario
    @AuditableAction(actionName = AuditActions.CREACION_TIPO_USUARIO, message = "Se intentó crear un nuevo usuario tipo usuario.")
    // Crear uno nuevo
    public UsuariosTipoUsuario create(UsuariosTipoUsuario usuarioTipoUsuario) {
        return usuariosTipoUsuarioRepository.save(usuarioTipoUsuario);
    }

    // Auditar la acción de creación de múltiples usuarios tipo usuario
    @AuditableAction(actionName = AuditActions.CREACION_TIPO_USUARIO, message = "Se intentó crear múltiples usuarios tipo usuario.")
    // Crear múltiples
    public List<UsuariosTipoUsuario> createAll(List<UsuariosTipoUsuario> lista) {
        return usuariosTipoUsuarioRepository.saveAll(lista);
    }

    // Auditar la acción de actualización de un usuario tipo usuario
    @AuditableAction(actionName = AuditActions.ACTUALIZACION_TIPO_USUARIO, message = "Se intentó actualizar un usuario tipo usuario existente.")
    // Actualizar existente
    public Optional<UsuariosTipoUsuario> update(UUID id, UsuariosTipoUsuario actualizado) {
        return usuariosTipoUsuarioRepository.findById(id).map(usuTipoUsu -> {
            usuTipoUsu.setUsuario(actualizado.getUsuario());
            usuTipoUsu.setTipoUsuario(actualizado.getTipoUsuario());
            return usuariosTipoUsuarioRepository.save(usuTipoUsu);
        });
    }

    // Auditar la acción de eliminación de un usuario tipo usuario por ID
    @AuditableAction(actionName = AuditActions.ELIMINACION_DEFINITIVA_TIPO_USUARIO, message = "Se intentó eliminar un usuario tipo usuario por su ID.")
    // Eliminar definitivamente por ID
    public void deleteById(UUID id) {
        usuariosTipoUsuarioRepository.deleteById(id);
    }

    // Auditar la acción de eliminación lógica de un usuario tipo usuario por ID
    @AuditableAction(actionName = AuditActions.ELIMINACION_LOGICA_TIPO_USUARIO, message = "Se intentó eliminar lógicamente un usuario tipo usuario por su ID.")
    // Eliminar lógicamente por ID
    public Optional<UsuariosTipoUsuario> softDelete(UUID id) {
        return usuariosTipoUsuarioRepository.findById(id).map(usuTipoUsu -> {
            usuTipoUsu.softDelete();
            return usuariosTipoUsuarioRepository.save(usuTipoUsu);
        });
    }

    // Auditar la acción de busqueda de usuarios tipo usuario por usuario ID
    @AuditableAction(actionName = AuditActions.BUSQUEDA_TIPOS_USUARIOS, message = "Se intentó buscar usuarios tipo usuario por usuario ID.", auditResult = AuditResultType.BOTH)
    // Filtrar por usuario
    public Page<UsuariosTipoUsuario> findByUsuarioId(UUID usuarioId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosTipoUsuarioRepository.findByUsuarioId(usuarioId, pageable);
    }

    // Auditar la acción de busqueda de usuarios tipo usuario por tipo de usuario IDq
    @AuditableAction(actionName = AuditActions.BUSQUEDA_TIPOS_USUARIOS, message = "Se intentó buscar usuarios tipo usuario por tipo de usuario ID.", auditResult = AuditResultType.BOTH)
    // Filtrar por tipo de usuario
    public Page<UsuariosTipoUsuario> findByTipoUsuarioId(UUID tipoUsuarioId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return usuariosTipoUsuarioRepository.findByTipoUsuarioId(tipoUsuarioId, pageable);
    }

    // Auditar la acción de obtener permisos agrupados por sección para un usuario en una aplicación específica
    @AuditableAction(actionName = AuditActions.CONSULTA_PERMISOS_USUARIO_APLICACION_SECCION, message = "Se intentó obtener permisos agrupados por sección para un usuario en una aplicación específica.", auditResult = AuditResultType.BOTH)
    /**
     * Obtiene una lista de SeccionPermisosDTO, donde cada SeccionPermisosDTO
     * contiene el nombre de la sección y las acciones a las que el usuario tiene
     * acceso dentro de esa sección para una aplicación específica.
     *
     * @param userId El UUID del usuario.
     * @param applicationIdentifier La llave identificadora de la aplicación.
     * @return Una lista de SeccionPermisosDTOs. Lista vacía si no hay permisos.
     * @throws ResourceNotFoundException si la aplicación no es encontrada.
     */
    public List<SeccionPermisosDTO> getPermissionsForUserAndApplicationGroupedBySection(UUID userId, String applicationIdentifier) {
        // 1. Encontrar la aplicación por su llave identificadora
        Aplicaciones targetApp = aplicacionesService.findByLlaveIdentificadora(applicationIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException("Aplicación no encontrada con identificador: " + applicationIdentifier));

        // 2. Obtener todos los UsuariosTipoUsuario para el userId dado
        List<UsuariosTipoUsuario> usuarioTipoUsuarios = usuariosTipoUsuarioRepository.findAllByUsuarioId(userId);

        // 3. Filtrar los Tipos de Usuario que pertenecen a la aplicación objetivo
        Set<UUID> tipoUsuarioIdsInApp = usuarioTipoUsuarios.stream()
                .map(UsuariosTipoUsuario::getTipoUsuario) 
                .filter(tu -> tu.getAplicacion().getId().equals(targetApp.getId()))
                .map(TipoUsuario::getId) 
                .collect(Collectors.toSet()); 

        if (tipoUsuarioIdsInApp.isEmpty()) {
            return List.of();
        }

        // 4. Obtener todos los PermisosTipoUsuario para los tipos de usuario encontrados en la aplicación
        List<PermisosTipoUsuario> permisos = permisosTipoUsuarioService.findByTipoUsuarioIdIn(tipoUsuarioIdsInApp);

        // 5. Agrupar las acciones por sección y mapear a los DTOs
        Map<Secciones, Set<Acciones>> accionesPorSeccion = new LinkedHashMap<>();

        for (PermisosTipoUsuario permiso : permisos) {
            Acciones accion = permiso.getAccion();
            if (accion != null && accion.getSeccion() != null) {
                if (accion.getAplicacion() != null && accion.getAplicacion().getId().equals(targetApp.getId())) {
                    accionesPorSeccion.computeIfAbsent(accion.getSeccion(), k -> new HashSet<>()).add(accion);
                }
            }
        }

        // 6. Mapear el mapa agrupado a List<SeccionPermisosDTO>
        return accionesPorSeccion.entrySet().stream()
                .map(entry -> {
                    Secciones seccion = entry.getKey();
                    List<PermisoAccionDTO> accionesDTO = entry.getValue().stream()
                            .map(accion -> new PermisoAccionDTO(accion.getNombre(), accion.getDescripcion()))
                            .sorted((a1, a2) -> a1.getNombreAccion().compareTo(a2.getNombreAccion()))
                            .collect(Collectors.toList());
                    return new SeccionPermisosDTO(seccion.getNombre(), seccion.getDescripcion(), accionesDTO);
                })
                .sorted((s1, s2) -> s1.getNombreSeccion().compareTo(s2.getNombreSeccion()))
                .collect(Collectors.toList());
    }

    // Método para buscar usuarios tipo usuario por ID de tipo de usuario y ID de usuario
    public List<UsuariosTipoUsuario> findByTipoUsuarioIdAndUsuarioId(UUID tipoUsuarioId, UUID usuarioId) {
        return usuariosTipoUsuarioRepository.findByTipoUsuarioIdAndUsuarioId(tipoUsuarioId, usuarioId);
    }
}