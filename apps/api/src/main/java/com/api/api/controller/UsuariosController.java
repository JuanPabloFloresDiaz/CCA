package com.api.api.controller;

import com.api.api.dto.RequestDTO.UsuarioCreateRequestDTO;
import com.api.api.dto.RequestDTO.UsuarioUpdateRequestDTO;
import com.api.api.dto.ResponseDTO.UsuarioResponseDTO;
import com.api.api.dto.ResponseDTO.ErrorResponseDTO; 
import com.api.api.dto.SimpleDTO.UsuarioSimpleDTO; 
import com.api.api.exception.ResourceNotFoundException; 
import com.api.api.model.Usuarios; 
import com.api.api.service.UsuariosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Endpoints para la gestión de usuarios.")
@SecurityRequirement(name = "bearerAuth") 
public class UsuariosController {

    private final UsuariosService usuariosService;
    private final ModelMapper modelMapper;

    public UsuariosController(UsuariosService usuariosService, ModelMapper modelMapper) {
        this.usuariosService = usuariosService;
        this.modelMapper = modelMapper;
    }

    /**
     * Obtiene todos los usuarios con paginación y búsqueda opcional.
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param searchTerm Término de búsqueda opcional.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de UsuarioResponseDTO.
     */
    @Operation(summary = "Obtener todos los usuarios",
               description = "Recupera una lista paginada de todos los usuarios, con opción de búsqueda.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> getAllUsuarios(
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Término de búsqueda para filtrar usuarios (nombres, apellidos, email, estado).", example = "juan") @RequestParam(required = false) String searchTerm,
            HttpServletRequest request) {
        Page<Usuarios> usuariosPage = usuariosService.findAll(page, limit, searchTerm);
        Page<UsuarioResponseDTO> responsePage = usuariosPage.map(usuario -> modelMapper.map(usuario, UsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene un usuario por su ID.
     * @param id ID del usuario.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con UsuarioResponseDTO si se encuentra.
     */
    @Operation(summary = "Obtener usuario por ID",
               description = "Recupera un usuario específico utilizando su ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario recuperado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(
            @Parameter(description = "ID del usuario a recuperar.", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef") @PathVariable UUID id,
            HttpServletRequest request) {
        Usuarios usuario = usuariosService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return ResponseEntity.ok(modelMapper.map(usuario, UsuarioResponseDTO.class));
    }

    /**
     * Crea un nuevo usuario.
     * @param createRequest DTO con los datos del nuevo usuario, incluyendo la contraseña.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con la UsuarioResponseDTO creada.
     */
    @Operation(summary = "Crear nuevo usuario",
               description = "Crea un nuevo usuario en el sistema. La contraseña se encriptará automáticamente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. email ya existe, contraseña débil).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "Conflicto: el email ya está en uso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> createUsuario(@Valid @RequestBody UsuarioCreateRequestDTO createRequest,
                                           HttpServletRequest request) {
        Usuarios usuarioToCreate = modelMapper.map(createRequest, Usuarios.class);
        Usuarios createdUsuario = usuariosService.create(usuarioToCreate);
        return new ResponseEntity<>(modelMapper.map(createdUsuario, UsuarioResponseDTO.class), HttpStatus.CREATED);
    }

    /**
     * Actualiza un usuario existente por su ID (excluyendo la contraseña).
     * @param id ID del usuario a actualizar.
     * @param updateRequest DTO con los datos actualizados del usuario (sin contraseña).
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con la UsuarioResponseDTO actualizada.
     */
    @Operation(summary = "Actualizar usuario existente (sin contraseña)",
               description = "Actualiza los datos de un usuario existente utilizando su ID. La contraseña debe cambiarse por separado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. email ya existe en otro usuario).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "Conflicto: el email proporcionado ya está en uso por otro usuario.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateUsuario(
            @Parameter(description = "ID del usuario a actualizar.", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef") @PathVariable UUID id,
            @Valid @RequestBody UsuarioUpdateRequestDTO updateRequest,
            HttpServletRequest request) {
        Usuarios usuarioToUpdate = modelMapper.map(updateRequest, Usuarios.class);
        
        Optional<Usuarios> updatedUsuario = usuariosService.update(id, usuarioToUpdate);
        return ResponseEntity.ok(updatedUsuario.map(usuario -> modelMapper.map(usuario, UsuarioResponseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id)));
    }

    /**
     * Elimina lógicamente un usuario por su ID.
     * @param id ID del usuario a eliminar lógicamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Eliminar lógicamente un usuario",
               description = "Marca un usuario como eliminado lógicamente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado lógicamente exitosamente."),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Void> softDeleteUsuario(
            @Parameter(description = "ID del usuario a eliminar lógicamente.", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef") @PathVariable UUID id,
            HttpServletRequest request) {
        if (!usuariosService.softDelete(id).isPresent()) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina definitivamente un usuario por su ID.
     * Usar con precaución.
     * @param id ID del usuario a eliminar definitivamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Eliminar definitivamente un usuario (Precaución)",
               description = "Elimina de forma permanente un usuario por su ID. Esta operación es irreversible.",
               deprecated = true)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado definitivamente exitosamente."),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(
            @Parameter(description = "ID del usuario a eliminar definitivamente.", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef") @PathVariable UUID id,
            HttpServletRequest request) {
        if (usuariosService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuariosService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Obtiene todos los usuarios en un formato simple (solo ID, nombres, apellidos y email).
     * Útil para selectores o listas desplegables en el frontend.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una lista de UsuarioSimpleDTO.
     */
    @Operation(summary = "Obtener todos los usuarios en formato simple",
               description = "Recupera una lista de todos los usuarios con solo su ID, nombres, apellidos y email. Ideal para listas desplegables.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios simples recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioSimpleDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/select")
    public ResponseEntity<Iterable<UsuarioSimpleDTO>> getAllUsuariosForSelect(HttpServletRequest request) {
        return ResponseEntity.ok(usuariosService.findAllSelect());
    }

    /**
     * Obtiene usuarios por su estado con paginación y búsqueda opcional.
     * @param estado Estado por el cual filtrar (ej. "activo", "inactivo").
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de UsuarioResponseDTO.
     */
    @Operation(summary = "Filtrar usuarios por estado",
               description = "Recupera una lista paginada de usuarios filtrados por su estado (activo, inactivo, bloqueado).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Parámetro 'estado' inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<UsuarioResponseDTO>> getUsuariosByEstado(
            @Parameter(description = "Estado del usuario a filtrar (activo, inactivo, bloqueado).", example = "activo") @PathVariable String estado,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        Page<Usuarios> usuariosPage = usuariosService.findByEstado(estado, page, limit);
        Page<UsuarioResponseDTO> responsePage = usuariosPage.map(usuario -> modelMapper.map(usuario, UsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene usuarios por si tienen 2FA activo.
     * @param dosFactorActivo Booleano para filtrar por 2FA activo (true/false).
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de UsuarioResponseDTO.
     */
    @Operation(summary = "Filtrar usuarios por 2FA activo",
               description = "Recupera una lista paginada de usuarios filtrados por si tienen el doble factor de autenticación activo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/dos-factor-activo/{dosFactorActivo}")
    public ResponseEntity<Page<UsuarioResponseDTO>> getUsuariosByDosFactorActivo(
            @Parameter(description = "Booleano para filtrar usuarios con 2FA activo (true) o inactivo (false).", example = "true") @PathVariable Boolean dosFactorActivo,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        Page<Usuarios> usuariosPage = usuariosService.findByDosFactorActivo(dosFactorActivo, page, limit);
        Page<UsuarioResponseDTO> responsePage = usuariosPage.map(usuario -> modelMapper.map(usuario, UsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene usuarios que requieren cambio de contraseña.
     * @param requiereCambioContrasena Booleano para filtrar por si requieren cambio de contraseña (true/false).
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de UsuarioResponseDTO.
     */
    @Operation(summary = "Filtrar usuarios que requieren cambio de contraseña",
               description = "Recupera una lista paginada de usuarios que están marcados para requerir un cambio de contraseña.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/requiere-cambio-contrasena/{requiereCambioContrasena}")
    public ResponseEntity<Page<UsuarioResponseDTO>> getUsuariosByRequiereCambioContrasena(
            @Parameter(description = "Booleano para filtrar usuarios que requieren cambio de contraseña (true) o no (false).", example = "true") @PathVariable boolean requiereCambioContrasena,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        Page<Usuarios> usuariosPage = usuariosService.findByRequiereCambioContrasena(requiereCambioContrasena, page, limit);
        Page<UsuarioResponseDTO> responsePage = usuariosPage.map(usuario -> modelMapper.map(usuario, UsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Verifica si la sesión de un usuario está bloqueada.
     * @param id ID del usuario.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con un booleano indicando si la sesión está bloqueada.
     */
    @Operation(summary = "Verificar si la sesión del usuario está bloqueada",
               description = "Comprueba el estado de bloqueo de la sesión de un usuario por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de bloqueo recuperado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}/is-session-blocked")
    public ResponseEntity<Boolean> isSessionBlocked(
            @Parameter(description = "ID del usuario a verificar.", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef") @PathVariable UUID id,
            HttpServletRequest request) {
        usuariosService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        
        boolean isBlocked = usuariosService.isSessionBlocked(id);
        return ResponseEntity.ok(isBlocked);
    }
}
