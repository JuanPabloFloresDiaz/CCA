package com.api.api.controller;

import com.api.api.dto.RequestDTO.PermisosTipoUsuarioRequestDTO;
import com.api.api.dto.ResponseDTO.PermisosTipoUsuarioResponseDTO;
import com.api.api.dto.ResponseDTO.ErrorResponseDTO;
import com.api.api.exception.ResourceNotFoundException;
import com.api.api.model.PermisosTipoUsuario;
import com.api.api.model.TipoUsuario;
import com.api.api.model.Acciones;
import com.api.api.service.PermisosTipoUsuarioService;
import com.api.api.service.TipoUsuarioService;
import com.api.api.service.AccionesService;
import com.api.api.service.AplicacionesService;

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
@RequestMapping("/api/permisos-tipo-usuario")
@Tag(name = "Permisos por Tipo de Usuario", description = "Endpoints para la gestión de permisos asociados a tipos de usuario.")
@SecurityRequirement(name = "bearerAuth")
public class PermisosTipoUsuarioController {

    private final PermisosTipoUsuarioService permisosTipoUsuarioService;
    private final TipoUsuarioService tipoUsuarioService;
    private final AccionesService accionesService;
    private final AplicacionesService aplicacionesService;
    private final ModelMapper modelMapper;

    public PermisosTipoUsuarioController(PermisosTipoUsuarioService permisosTipoUsuarioService,
            TipoUsuarioService tipoUsuarioService,
            AccionesService accionesService,
            AplicacionesService aplicacionesService,
            ModelMapper modelMapper) {
        this.permisosTipoUsuarioService = permisosTipoUsuarioService;
        this.tipoUsuarioService = tipoUsuarioService;
        this.accionesService = accionesService;
        this.aplicacionesService = aplicacionesService;
        this.modelMapper = modelMapper;

        // Configuración de ModelMapper para mapear datos de las entidades relacionadas
        // al DTO de respuesta
        modelMapper.createTypeMap(PermisosTipoUsuario.class, PermisosTipoUsuarioResponseDTO.class)
                .addMapping(src -> src.getAccion().getId(), PermisosTipoUsuarioResponseDTO::setAccionId)
                .addMapping(src -> src.getAccion().getNombre(), PermisosTipoUsuarioResponseDTO::setAccionNombre)
                .addMapping(src -> src.getAccion().getDescripcion(),
                        PermisosTipoUsuarioResponseDTO::setAccionDescripcion)
                .addMapping(src -> src.getTipoUsuario().getId(), PermisosTipoUsuarioResponseDTO::setTipoUsuarioId)
                .addMapping(src -> src.getTipoUsuario().getNombre(),
                        PermisosTipoUsuarioResponseDTO::setTipoUsuarioNombre)
                .addMapping(src -> src.getTipoUsuario().getDescripcion(),
                        PermisosTipoUsuarioResponseDTO::setTipoUsuarioDescripcion);
    }

    /**
     * Obtiene todos los permisos de tipo de usuario con paginación y búsqueda
     * opcional.
     * 
     * @param page       Número de página (por defecto 1).
     * @param limit      Cantidad de elementos por página (por defecto 10).
     * @param searchTerm Término de búsqueda opcional.
     * @param request    HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de PermisosTipoUsuarioResponseDTO.
     */
    @Operation(summary = "Obtener todos los permisos por tipo de usuario", description = "Recupera una lista paginada de todos los permisos asignados a tipos de usuario, con opción de búsqueda.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de permisos recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Page<PermisosTipoUsuarioResponseDTO>> getAllPermisosTipoUsuario(
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Término de búsqueda para filtrar (nombre de tipo de usuario, aplicación, acción).", example = "admin") @RequestParam(required = false) String searchTerm,
            HttpServletRequest request) {
        Page<PermisosTipoUsuario> permisosPage = permisosTipoUsuarioService.findAll(page, limit, searchTerm);
        Page<PermisosTipoUsuarioResponseDTO> responsePage = permisosPage
                .map(permiso -> modelMapper.map(permiso, PermisosTipoUsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene un permiso de tipo de usuario por su ID.
     * GET /api/permisos-tipo-usuario/:id
     * 
     * @param id      ID del permiso.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con PermisosTipoUsuarioResponseDTO si se encuentra.
     */
    @Operation(summary = "Obtener permiso por tipo de usuario por ID", description = "Recupera un permiso específico asignado a un tipo de usuario utilizando su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso recuperado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermisosTipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PermisosTipoUsuarioResponseDTO> getPermisoTipoUsuarioById(
            @Parameter(description = "ID del permiso a recuperar.", example = "0f1a2b3c-4d5e-6f7a-8b9c-0d1e2f3a4b5c") @PathVariable UUID id,
            HttpServletRequest request) {
        PermisosTipoUsuario permiso = permisosTipoUsuarioService.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Permiso de tipo de usuario no encontrado con ID: " + id));
        return ResponseEntity.ok(modelMapper.map(permiso, PermisosTipoUsuarioResponseDTO.class));
    }

    /**
     * Crea un nuevo permiso de tipo de usuario.
     * 
     * @param requestDTO DTO con los IDs de la acción y el tipo de usuario.
     * @param request    HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con el PermisosTipoUsuarioResponseDTO creado.
     */
    @Operation(summary = "Crear nuevo permiso por tipo de usuario", description = "Asigna una acción a un tipo de usuario. Asegura que la acción y el tipo de usuario existan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Permiso creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermisosTipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. acción o tipo de usuario no existe, permiso duplicado).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto: El permiso ya existe para esta acción y tipo de usuario.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<PermisosTipoUsuarioResponseDTO> createPermisosTipoUsuario(
            @Valid @RequestBody PermisosTipoUsuarioRequestDTO requestDTO,
            HttpServletRequest request) {
        // Validar que la acción y el tipo de usuario existan
        Acciones accion = accionesService.findById(requestDTO.getAccionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Acción no encontrada con ID: " + requestDTO.getAccionId()));

        TipoUsuario tipoUsuario = tipoUsuarioService.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tipo de usuario no encontrado con ID: " + requestDTO.getTipoUsuarioId()));

        PermisosTipoUsuario permisoToCreate = modelMapper.map(requestDTO, PermisosTipoUsuario.class);
        permisoToCreate.setAccion(accion);
        permisoToCreate.setTipoUsuario(tipoUsuario);

        PermisosTipoUsuario createdPermiso = permisosTipoUsuarioService.create(permisoToCreate);
        return new ResponseEntity<>(modelMapper.map(createdPermiso, PermisosTipoUsuarioResponseDTO.class),
                HttpStatus.CREATED);
    }

    /**
     * Actualiza un permiso de tipo de usuario existente por su ID.
     * 
     * @param id         ID del permiso a actualizar.
     * @param requestDTO DTO con los IDs actualizados de la acción y el tipo de
     *                   usuario.
     * @param request    HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con el PermisosTipoUsuarioResponseDTO actualizado.
     */
    @Operation(summary = "Actualizar permiso por tipo de usuario existente", description = "Actualiza una asignación de permiso a un tipo de usuario utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso actualizado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermisosTipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. acción o tipo de usuario no existe, permiso duplicado).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto: El permiso ya existe con los nuevos IDs de acción y tipo de usuario.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PermisosTipoUsuarioResponseDTO> updatePermisosTipoUsuario(
            @Parameter(description = "ID del permiso a actualizar.", example = "0f1a2b3c-4d5e-6f7a-8b9c-0d1e2f3a4b5c") @PathVariable UUID id,
            @Valid @RequestBody PermisosTipoUsuarioRequestDTO requestDTO,
            HttpServletRequest request) {
        // Validar que la acción y el tipo de usuario existan para los nuevos IDs
        Acciones accion = accionesService.findById(requestDTO.getAccionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Acción no encontrada con ID: " + requestDTO.getAccionId()));

        TipoUsuario tipoUsuario = tipoUsuarioService.findById(requestDTO.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tipo de usuario no encontrado con ID: " + requestDTO.getTipoUsuarioId()));

        PermisosTipoUsuario permisoToUpdate = modelMapper.map(requestDTO, PermisosTipoUsuario.class);
        permisoToUpdate.setAccion(accion);
        permisoToUpdate.setTipoUsuario(tipoUsuario);

        Optional<PermisosTipoUsuario> updatedPermiso = permisosTipoUsuarioService.update(id, permisoToUpdate);
        return ResponseEntity.ok(updatedPermiso.map(perm -> modelMapper.map(perm, PermisosTipoUsuarioResponseDTO.class))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Permiso de tipo de usuario no encontrado con ID: " + id)));
    }

    /**
     * Elimina lógicamente un permiso de tipo de usuario por su ID.
     * 
     * @param id      ID del permiso a eliminar lógicamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Eliminar lógicamente un permiso por tipo de usuario", description = "Marca un permiso asignado a un tipo de usuario como eliminado lógicamente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permiso eliminado lógicamente exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Void> softDeletePermisoTipoUsuario(
            @Parameter(description = "ID del permiso a eliminar lógicamente.", example = "0f1a2b3c-4d5e-6f7a-8b9c-0d1e2f3a4b5c") @PathVariable UUID id,
            HttpServletRequest request) {
        if (!permisosTipoUsuarioService.softDelete(id).isPresent()) {
            throw new ResourceNotFoundException("Permiso de tipo de usuario no encontrado con ID: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina definitivamente un permiso de tipo de usuario por su ID.
     * Usar con precaución.
     * 
     * @param id      ID del permiso a eliminar definitivamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Eliminar definitivamente un permiso por tipo de usuario (Precaución)", description = "Elimina de forma permanente un permiso asignado a un tipo de usuario por su ID. Esta operación es irreversible.", deprecated = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permiso eliminado definitivamente exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermisoTipoUsuario(
            @Parameter(description = "ID del permiso a eliminar definitivamente.", example = "0f1a2b3c-4d5e-6f7a-8b9c-0d1e2f3a4b5c") @PathVariable UUID id,
            HttpServletRequest request) {
        if (permisosTipoUsuarioService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Permiso de tipo de usuario no encontrado con ID: " + id);
        }
        permisosTipoUsuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene permisos de tipo de usuario filtrados por el ID de un tipo de
     * usuario.
     * 
     * @param tipoUsuarioId ID del tipo de usuario para filtrar los permisos.
     * @param page          Número de página (por defecto 1).
     * @param limit         Cantidad de elementos por página (por defecto 10).
     * @param request       HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de PermisosTipoUsuarioResponseDTO.
     */
    @Operation(summary = "Filtrar permisos por ID de tipo de usuario", description = "Recupera una lista paginada de permisos asociados a un tipo de usuario específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de permisos filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "ID de tipo de usuario inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/by-tipo-usuario/{tipoUsuarioId}")
    public ResponseEntity<Page<PermisosTipoUsuarioResponseDTO>> getPermisosTipoUsuarioByTipoUsuarioId(
            @Parameter(description = "ID del tipo de usuario para filtrar los permisos.", example = "5a6b7c8d-e9f0-1234-5678-90abcdef1234") @PathVariable UUID tipoUsuarioId,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        // Validar que el tipo de usuario exista
        tipoUsuarioService.findById(tipoUsuarioId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Tipo de usuario no encontrado con ID: " + tipoUsuarioId));

        Page<PermisosTipoUsuario> permisosPage = permisosTipoUsuarioService.findByTipoUsuarioId(tipoUsuarioId, page,
                limit);
        Page<PermisosTipoUsuarioResponseDTO> responsePage = permisosPage
                .map(permiso -> modelMapper.map(permiso, PermisosTipoUsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene permisos de tipo de usuario filtrados por el ID de una aplicación.
     * 
     * @param aplicacionId ID de la aplicación para filtrar los permisos.
     * @param page         Número de página (por defecto 1).
     * @param limit        Cantidad de elementos por página (por defecto 10).
     * @param request      HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de PermisosTipoUsuarioResponseDTO.
     */
    @Operation(summary = "Filtrar permisos por ID de aplicación", description = "Recupera una lista paginada de permisos asociados a una aplicación específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de permisos filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "ID de aplicación inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Aplicación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/by-aplicacion/{aplicacionId}")
    public ResponseEntity<Page<PermisosTipoUsuarioResponseDTO>> getPermisosTipoUsuarioByAplicacionId(
            @Parameter(description = "ID de la aplicación para filtrar los permisos.", example = "f1e2d3c4-b5a6-7890-1234-567890fedcba") @PathVariable UUID aplicacionId,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        // Validar que la aplicación exista
        aplicacionesService.findById(aplicacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Aplicación no encontrada con ID: " + aplicacionId));

        Page<PermisosTipoUsuario> permisosPage = permisosTipoUsuarioService.findByAplicacionId(aplicacionId, page,
                limit);
        Page<PermisosTipoUsuarioResponseDTO> responsePage = permisosPage
                .map(permiso -> modelMapper.map(permiso, PermisosTipoUsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }
}
