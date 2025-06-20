package com.api.api.controller;

import com.api.api.dto.RequestDTO.UsuariosTipoUsuarioRequestDTO;
import com.api.api.dto.ResponseDTO.UsuariosTipoUsuarioResponseDTO;
import com.api.api.dto.ResponseDTO.ErrorResponseDTO;
import com.api.api.dto.ResponseDTO.SeccionPermisosDTO;
import com.api.api.exception.ResourceNotFoundException;
import com.api.api.model.UsuariosTipoUsuario;
import com.api.api.model.Usuarios;
import com.api.api.model.TipoUsuario;
import com.api.api.service.UsuariosTipoUsuarioService;
import com.api.api.service.UsuariosService;
import com.api.api.service.TipoUsuarioService;

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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios-tipos-usuario")
@Tag(name = "Usuarios Tipos de Usuario", description = "Endpoints para la gestión de la asignación de tipos de usuario a usuarios.")
@SecurityRequirement(name = "bearerAuth")
public class UsuariosTipoUsuarioController {

    private final UsuariosTipoUsuarioService usuariosTipoUsuarioService;
    private final UsuariosService usuariosService;
    private final TipoUsuarioService tipoUsuarioService;
    private final ModelMapper modelMapper;

    public UsuariosTipoUsuarioController(UsuariosTipoUsuarioService usuariosTipoUsuarioService,
            UsuariosService usuariosService,
            TipoUsuarioService tipoUsuarioService,
            ModelMapper modelMapper) {
        this.usuariosTipoUsuarioService = usuariosTipoUsuarioService;
        this.usuariosService = usuariosService;
        this.tipoUsuarioService = tipoUsuarioService;
        this.modelMapper = modelMapper;

        // Configuración de ModelMapper para mapear datos de las entidades relacionadas
        // al DTO de respuesta
        modelMapper.createTypeMap(UsuariosTipoUsuario.class, UsuariosTipoUsuarioResponseDTO.class)
                .addMapping(src -> src.getUsuario().getId(), UsuariosTipoUsuarioResponseDTO::setUsuarioId)
                .addMapping(src -> src.getUsuario().getNombres(), UsuariosTipoUsuarioResponseDTO::setUsuarioNombres)
                .addMapping(src -> src.getUsuario().getApellidos(), UsuariosTipoUsuarioResponseDTO::setUsuarioApellidos)
                .addMapping(src -> src.getUsuario().getEmail(), UsuariosTipoUsuarioResponseDTO::setUsuarioEmail)
                .addMapping(src -> src.getTipoUsuario().getId(), UsuariosTipoUsuarioResponseDTO::setTipoUsuarioId)
                .addMapping(src -> src.getTipoUsuario().getNombre(),
                        UsuariosTipoUsuarioResponseDTO::setTipoUsuarioNombre)
                .addMapping(src -> src.getTipoUsuario().getDescripcion(),
                        UsuariosTipoUsuarioResponseDTO::setTipoUsuarioDescripcion);
    }

    /**
     * Obtiene todas las relaciones Usuario-TipoUsuario con paginación y búsqueda
     * opcional.
     * 
     * @param page       Número de página (por defecto 1).
     * @param limit      Cantidad de elementos por página (por defecto 10).
     * @param searchTerm Término de búsqueda opcional.
     * @param request    HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de UsuariosTipoUsuarioResponseDTO.
     */
    @Operation(summary = "Obtener todas las relaciones Usuario-TipoUsuario", description = "Recupera una lista paginada de todas las asignaciones de tipos de usuario a usuarios, con opción de búsqueda.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de relaciones recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Page<UsuariosTipoUsuarioResponseDTO>> getAllUsuariosTipoUsuario(
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Término de búsqueda para filtrar (nombre de usuario o tipo de usuario).", example = "admin") @RequestParam(required = false) String searchTerm,
            HttpServletRequest request) {
        Page<UsuariosTipoUsuario> usuariosTipoUsuarioPage = usuariosTipoUsuarioService.findAll(page, limit, searchTerm);
        Page<UsuariosTipoUsuarioResponseDTO> responsePage = usuariosTipoUsuarioPage
                .map(rel -> modelMapper.map(rel, UsuariosTipoUsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene una relación Usuario-TipoUsuario por su ID.
     * 
     * @param id      ID de la relación.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con UsuariosTipoUsuarioResponseDTO si se encuentra.
     */
    @Operation(summary = "Obtener relación Usuario-TipoUsuario por ID", description = "Recupera una asignación específica de tipo de usuario a usuario utilizando su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuariosTipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Relación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuariosTipoUsuarioResponseDTO> getUsuariosTipoUsuarioById(
            @Parameter(description = "ID de la relación Usuario-TipoUsuario a recuperar.", example = "1a2b3c4d-5e6f-7890-abcd-ef1234567890") @PathVariable UUID id,
            HttpServletRequest request) {
        UsuariosTipoUsuario relacion = usuariosTipoUsuarioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Relación Usuario-TipoUsuario no encontrada con ID: " + id));
        return ResponseEntity.ok(modelMapper.map(relacion, UsuariosTipoUsuarioResponseDTO.class));
    }

    /**
     * Crea una nueva relación Usuario-TipoUsuario.
     * 
     * @param requestDTO DTO con los IDs del usuario y el tipo de usuario.
     * @param request    HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con la UsuariosTipoUsuarioResponseDTO creada.
     */
    @Operation(summary = "Crear nueva relación Usuario-TipoUsuario", description = "Asigna un tipo de usuario a un usuario. Asegura que el usuario y el tipo de usuario existan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Relación creada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuariosTipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. usuario o tipo de usuario no existe, relación duplicada).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto: La relación Usuario-TipoUsuario ya existe.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<UsuariosTipoUsuarioResponseDTO> createUsuariosTipoUsuario(
            @Valid @RequestBody UsuariosTipoUsuarioRequestDTO requestDTO,
            HttpServletRequest request) {
        // Validar que el usuario y el tipo de usuario existan
        Usuarios usuario = usuariosService.findById(requestDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + requestDTO.getUsuarioId()));

        TipoUsuario tipoUsuario = tipoUsuarioService.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tipo de usuario no encontrado con ID: " + requestDTO.getTipoUsuarioId()));

        // Mapear a la entidad y establecer las relaciones
        UsuariosTipoUsuario relacionToCreate = modelMapper.map(requestDTO, UsuariosTipoUsuario.class);
        relacionToCreate.setUsuario(usuario);
        relacionToCreate.setTipoUsuario(tipoUsuario);
        // Verificar si la relación ya existe
        List<UsuariosTipoUsuario> relacionesExistentes = usuariosTipoUsuarioService.findByTipoUsuarioIdAndUsuarioId(
                requestDTO.getTipoUsuarioId(), requestDTO.getUsuarioId());
        if (!relacionesExistentes.isEmpty()) {
            throw new IllegalArgumentException("La relación entre el usuario y el tipo de usuario ya existe.");
        }

        UsuariosTipoUsuario createdRelacion = usuariosTipoUsuarioService.create(relacionToCreate);
        return new ResponseEntity<>(modelMapper.map(createdRelacion, UsuariosTipoUsuarioResponseDTO.class),
                HttpStatus.CREATED);
    }

    /**
     * Actualiza una relación Usuario-TipoUsuario existente por su ID.
     * Nota: La actualización de esta relación típicamente implicaría cambiar
     * el TipoUsuario asignado a un Usuario, o bien eliminar la relación existente
     * y crear una nueva. Este método asume que se puede actualizar el usuario y/o
     * tipoUsuario asociado.
     * 
     * @param id         ID de la relación a actualizar.
     * @param requestDTO DTO con los IDs actualizados del usuario y el tipo de
     *                   usuario.
     * @param request    HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con la UsuariosTipoUsuarioResponseDTO actualizada.
     */
    @Operation(summary = "Actualizar relación Usuario-TipoUsuario existente", description = "Actualiza una asignación existente de tipo de usuario a usuario utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación actualizada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuariosTipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. usuario o tipo de usuario no existe, relación duplicada).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Relación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto: La relación Usuario-TipoUsuario ya existe con los nuevos IDs.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuariosTipoUsuarioResponseDTO> updateUsuariosTipoUsuario(
            @Parameter(description = "ID de la relación Usuario-TipoUsuario a actualizar.", example = "1a2b3c4d-5e6f-7890-abcd-ef1234567890") @PathVariable UUID id,
            @Valid @RequestBody UsuariosTipoUsuarioRequestDTO requestDTO,
            HttpServletRequest request) {
        // Validar que el usuario y el tipo de usuario existan para los nuevos IDs
        Usuarios usuario = usuariosService.findById(requestDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + requestDTO.getUsuarioId()));

        TipoUsuario tipoUsuario = tipoUsuarioService.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tipo de usuario no encontrado con ID: " + requestDTO.getTipoUsuarioId()));

        // Mapear a la entidad y establecer las nuevas relaciones
        UsuariosTipoUsuario relacionToUpdate = modelMapper.map(requestDTO, UsuariosTipoUsuario.class);
        relacionToUpdate.setUsuario(usuario);
        relacionToUpdate.setTipoUsuario(tipoUsuario);

        Optional<UsuariosTipoUsuario> updatedRelacion = usuariosTipoUsuarioService.update(id, relacionToUpdate);
        return ResponseEntity.ok(updatedRelacion.map(rel -> modelMapper.map(rel, UsuariosTipoUsuarioResponseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Relación Usuario-TipoUsuario no encontrada con ID: " + id)));
    }

    /**
     * Elimina lógicamente una relación Usuario-TipoUsuario por su ID.
     * 
     * @param id      ID de la relación a eliminar lógicamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Eliminar lógicamente una relación Usuario-TipoUsuario", description = "Marca una asignación de tipo de usuario a usuario como eliminada lógicamente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Relación eliminada lógicamente exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Relación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Void> softDeleteUsuariosTipoUsuario(
            @Parameter(description = "ID de la relación a eliminar lógicamente.", example = "1a2b3c4d-5e6f-7890-abcd-ef1234567890") @PathVariable UUID id,
            HttpServletRequest request) {
        if (!usuariosTipoUsuarioService.softDelete(id).isPresent()) {
            throw new ResourceNotFoundException("Relación Usuario-TipoUsuario no encontrada con ID: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina definitivamente una relación Usuario-TipoUsuario por su ID.
     * Usar con precaución.
     * 
     * @param id      ID de la relación a eliminar definitivamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Eliminar definitivamente una relación Usuario-TipoUsuario (Precaución)", description = "Elimina de forma permanente una asignación de tipo de usuario a usuario por su ID. Esta operación es irreversible.", deprecated = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Relación eliminada definitivamente exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Relación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuariosTipoUsuario(
            @Parameter(description = "ID de la relación a eliminar definitivamente.", example = "1a2b3c4d-5e6f-7890-abcd-ef1234567890") @PathVariable UUID id,
            HttpServletRequest request) {
        if (usuariosTipoUsuarioService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Relación Usuario-TipoUsuario no encontrada con ID: " + id);
        }
        usuariosTipoUsuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene relaciones Usuario-TipoUsuario filtradas por el ID de un usuario.
     * 
     * @param usuarioId ID del usuario para filtrar las relaciones.
     * @param page      Número de página (por defecto 1).
     * @param limit     Cantidad de elementos por página (por defecto 10).
     * @param request   HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de UsuariosTipoUsuarioResponseDTO.
     */
    @Operation(summary = "Filtrar relaciones por ID de usuario", description = "Recupera una lista paginada de asignaciones de tipos de usuario para un usuario específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de relaciones filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "ID de usuario inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/by-usuario/{usuarioId}")
    public ResponseEntity<Page<UsuariosTipoUsuarioResponseDTO>> getUsuariosTipoUsuarioByUsuarioId(
            @Parameter(description = "ID del usuario para filtrar las relaciones.", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef") @PathVariable UUID usuarioId,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        // Verificar que el usuario exista
        usuariosService.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        Page<UsuariosTipoUsuario> usuariosTipoUsuarioPage = usuariosTipoUsuarioService.findByUsuarioId(usuarioId, page,
                limit);
        Page<UsuariosTipoUsuarioResponseDTO> responsePage = usuariosTipoUsuarioPage
                .map(rel -> modelMapper.map(rel, UsuariosTipoUsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene relaciones Usuario-TipoUsuario filtradas por el ID de un tipo de
     * usuario.
     * 
     * @param tipoUsuarioId ID del tipo de usuario para filtrar las relaciones.
     * @param page          Número de página (por defecto 1).
     * @param limit         Cantidad de elementos por página (por defecto 10).
     * @param request       HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de UsuariosTipoUsuarioResponseDTO.
     */
    @Operation(summary = "Filtrar relaciones por ID de tipo de usuario", description = "Recupera una lista paginada de asignaciones de tipos de usuario por un tipo de usuario específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de relaciones filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "ID de tipo de usuario inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/by-tipo-usuario/{tipoUsuarioId}")
    public ResponseEntity<Page<UsuariosTipoUsuarioResponseDTO>> getUsuariosTipoUsuarioByTipoUsuarioId(
            @Parameter(description = "ID del tipo de usuario para filtrar las relaciones.", example = "5a6b7c8d-e9f0-1234-5678-90abcdef1234") @PathVariable UUID tipoUsuarioId,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        // Verificar que el tipo de usuario exista
        tipoUsuarioService.findById(tipoUsuarioId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Tipo de usuario no encontrado con ID: " + tipoUsuarioId));

        Page<UsuariosTipoUsuario> usuariosTipoUsuarioPage = usuariosTipoUsuarioService
                .findByTipoUsuarioId(tipoUsuarioId, page, limit);
        Page<UsuariosTipoUsuarioResponseDTO> responsePage = usuariosTipoUsuarioPage
                .map(rel -> modelMapper.map(rel, UsuariosTipoUsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene los permisos agrupados por sección para un usuario específico y una
     * aplicación dada.
     * Esto es útil para construir menús dinámicos o interfaces de usuario basadas
     * en los permisos del usuario.
     * 
     * @param userId                El UUID del usuario.
     * @param applicationIdentifier La llave identificadora de la aplicación (ej.
     *                              "mi_aplicacion_web").
     * @param request               HttpServletRequest para obtener la ruta de la
     *                              solicitud.
     * @return ResponseEntity con una lista de SeccionPermisosDTO.
     */
    @Operation(summary = "Obtener permisos agrupados por sección para un usuario y aplicación", description = "Recupera los permisos de un usuario, organizados por sección, para una aplicación específica. Ideal para construir menús de navegación dinámicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permisos recuperados y agrupados exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SeccionPermisosDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario o Aplicación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{userId}/permissions-by-section/{applicationIdentifier}")
    public ResponseEntity<List<SeccionPermisosDTO>> getPermissionsForUserAndApplicationGroupedBySection(
            @Parameter(description = "ID del usuario para el cual obtener los permisos.", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef") @PathVariable UUID userId,
            @Parameter(description = "Llave identificadora de la aplicación.", example = "CCA_AUTH_SERVICE") @PathVariable String applicationIdentifier,
            HttpServletRequest request) {
        List<SeccionPermisosDTO> permissions = usuariosTipoUsuarioService
                .getPermissionsForUserAndApplicationGroupedBySection(userId, applicationIdentifier);
        return ResponseEntity.ok(permissions);
    }
}
