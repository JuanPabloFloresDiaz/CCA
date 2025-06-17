package com.api.api.controller;

import com.api.api.dto.RequestDTO.TipoUsuarioRequestDTO;
import com.api.api.dto.ResponseDTO.TipoUsuarioResponseDTO;
import com.api.api.dto.ResponseDTO.ErrorResponseDTO;
import com.api.api.dto.SimpleDTO.TipoUsuarioSimpleDTO;
import com.api.api.exception.ResourceNotFoundException;
import com.api.api.model.TipoUsuario;
import com.api.api.model.Aplicaciones;
import com.api.api.service.TipoUsuarioService;
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
@RequestMapping("/api/tipos-usuario")
@Tag(name = "Tipos de Usuario", description = "Endpoints para la gestión de tipos de usuario.")
@SecurityRequirement(name = "bearerAuth")
public class TipoUsuarioController {

    private final TipoUsuarioService tipoUsuarioService;
    private final AplicacionesService aplicacionesService;
    private final ModelMapper modelMapper;

    public TipoUsuarioController(TipoUsuarioService tipoUsuarioService,
                                 AplicacionesService aplicacionesService,
                                 ModelMapper modelMapper) {
        this.tipoUsuarioService = tipoUsuarioService;
        this.aplicacionesService = aplicacionesService;
        this.modelMapper = modelMapper;

        // Configuración de ModelMapper para mapear nombre de la aplicación al DTO de respuesta
        modelMapper.createTypeMap(TipoUsuario.class, TipoUsuarioResponseDTO.class)
            .addMapping(src -> src.getAplicacion().getId(), TipoUsuarioResponseDTO::setAplicacionId)
            .addMapping(src -> src.getAplicacion().getNombre(), TipoUsuarioResponseDTO::setNombreAplicacion);
    }

    /**
     * Obtiene todos los tipos de usuario con paginación y búsqueda opcional.
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param searchTerm Término de búsqueda opcional.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de TipoUsuarioResponseDTO.
     */
    @Operation(summary = "Obtener todos los tipos de usuario",
               description = "Recupera una lista paginada de todos los tipos de usuario, con opción de búsqueda.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos de usuario recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Page<TipoUsuarioResponseDTO>> getAllTiposUsuario(
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Término de búsqueda para filtrar tipos de usuario (nombre, descripción, aplicación, estado).", example = "admin") @RequestParam(required = false) String searchTerm,
            HttpServletRequest request) {
        Page<TipoUsuario> tiposUsuarioPage = tipoUsuarioService.findAll(page, limit, searchTerm);
        Page<TipoUsuarioResponseDTO> responsePage = tiposUsuarioPage.map(tipo -> modelMapper.map(tipo, TipoUsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene un tipo de usuario por su ID.
     * @param id ID del tipo de usuario.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con TipoUsuarioResponseDTO si se encuentra.
     */
    @Operation(summary = "Obtener tipo de usuario por ID",
               description = "Recupera un tipo de usuario específico utilizando su ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de usuario recuperado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoUsuarioResponseDTO> getTipoUsuarioById(
            @Parameter(description = "ID del tipo de usuario a recuperar.", example = "5a6b7c8d-e9f0-1234-5678-90abcdef1234") @PathVariable UUID id,
            HttpServletRequest request) {
        TipoUsuario tipoUsuario = tipoUsuarioService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuario no encontrado con ID: " + id));
        return ResponseEntity.ok(modelMapper.map(tipoUsuario, TipoUsuarioResponseDTO.class));
    }

    /**
     * Crea un nuevo tipo de usuario.
     * @param tipoUsuarioRequestDTO DTO con los datos del nuevo tipo de usuario.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con el TipoUsuarioResponseDTO creado.
     */
    @Operation(summary = "Crear nuevo tipo de usuario",
               description = "Crea un nuevo tipo de usuario en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tipo de usuario creado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. ID de aplicación no existe, nombre duplicado).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "Conflicto: ya existe un tipo de usuario con el mismo nombre para la misma aplicación.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<TipoUsuarioResponseDTO> createTipoUsuario(@Valid @RequestBody TipoUsuarioRequestDTO tipoUsuarioRequestDTO,
                                                    HttpServletRequest request) {
        Aplicaciones aplicacion = aplicacionesService.findById(tipoUsuarioRequestDTO.getAplicacionId())
            .orElseThrow(() -> new ResourceNotFoundException("Aplicación no encontrada con ID: " + tipoUsuarioRequestDTO.getAplicacionId()));
        
        // Verificar si ya existe un tipo de usuario con el mismo nombre para esta aplicación
        if (tipoUsuarioService.findByNombreAndAplicacionId(tipoUsuarioRequestDTO.getNombre(), tipoUsuarioRequestDTO.getAplicacionId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un tipo de usuario con el nombre '" + tipoUsuarioRequestDTO.getNombre() + "' para la aplicación con ID: " + tipoUsuarioRequestDTO.getAplicacionId());
        }

        TipoUsuario tipoUsuarioToCreate = modelMapper.map(tipoUsuarioRequestDTO, TipoUsuario.class);
        tipoUsuarioToCreate.setAplicacion(aplicacion);

        TipoUsuario createdTipoUsuario = tipoUsuarioService.create(tipoUsuarioToCreate);
        return new ResponseEntity<>(modelMapper.map(createdTipoUsuario, TipoUsuarioResponseDTO.class), HttpStatus.CREATED);
    }

    /**
     * Actualiza un tipo de usuario existente por su ID.
     * @param id ID del tipo de usuario a actualizar.
     * @param tipoUsuarioRequestDTO DTO con los datos actualizados del tipo de usuario.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con el TipoUsuarioResponseDTO actualizado.
     */
    @Operation(summary = "Actualizar tipo de usuario existente",
               description = "Actualiza los datos de un tipo de usuario existente utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de usuario actualizado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. ID de aplicación no existe, nombre duplicado).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "Conflicto: ya existe otro tipo de usuario con el mismo nombre para la misma aplicación.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoUsuarioResponseDTO> updateTipoUsuario(
            @Parameter(description = "ID del tipo de usuario a actualizar.", example = "5a6b7c8d-e9f0-1234-5678-90abcdef1234") @PathVariable UUID id,
            @Valid @RequestBody TipoUsuarioRequestDTO tipoUsuarioRequestDTO,
            HttpServletRequest request) {
        // Validar que la aplicación exista
        Aplicaciones aplicacion = aplicacionesService.findById(tipoUsuarioRequestDTO.getAplicacionId())
            .orElseThrow(() -> new ResourceNotFoundException("Aplicación no encontrada con ID: " + tipoUsuarioRequestDTO.getAplicacionId()));
        
        Optional<TipoUsuario> existingTipoUsuario = tipoUsuarioService.findByNombreAndAplicacionId(tipoUsuarioRequestDTO.getNombre(), tipoUsuarioRequestDTO.getAplicacionId());
        if (existingTipoUsuario.isPresent() && !existingTipoUsuario.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe otro tipo de usuario con el nombre '" + tipoUsuarioRequestDTO.getNombre() + "' para la aplicación con ID: " + tipoUsuarioRequestDTO.getAplicacionId());
        }

        TipoUsuario tipoUsuarioToUpdate = modelMapper.map(tipoUsuarioRequestDTO, TipoUsuario.class);
        tipoUsuarioToUpdate.setAplicacion(aplicacion); // Asignar la entidad completa

        Optional<TipoUsuario> updatedTipoUsuario = tipoUsuarioService.update(id, tipoUsuarioToUpdate);
        return ResponseEntity.ok(updatedTipoUsuario.map(tipo -> modelMapper.map(tipo, TipoUsuarioResponseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuario no encontrado con ID: " + id)));
    }

    /**
     * Elimina lógicamente un tipo de usuario por su ID.
     * @param id ID del tipo de usuario a eliminar lógicamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Eliminar lógicamente un tipo de usuario",
               description = "Marca un tipo de usuario como eliminado lógicamente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tipo de usuario eliminado lógicamente exitosamente."),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Void> softDeleteTipoUsuario(
            @Parameter(description = "ID del tipo de usuario a eliminar lógicamente.", example = "5a6b7c8d-e9f0-1234-5678-90abcdef1234") @PathVariable UUID id,
            HttpServletRequest request) {
        if (!tipoUsuarioService.softDelete(id).isPresent()) {
            throw new ResourceNotFoundException("Tipo de usuario no encontrado con ID: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina definitivamente un tipo de usuario por su ID.
     * Usar con precaución.
     * @param id ID del tipo de usuario a eliminar definitivamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Eliminar definitivamente un tipo de usuario (Precaución)",
               description = "Elimina de forma permanente un tipo de usuario por su ID. Esta operación es irreversible.",
               deprecated = true)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tipo de usuario eliminado definitivamente exitosamente."),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoUsuario(
            @Parameter(description = "ID del tipo de usuario a eliminar definitivamente.", example = "5a6b7c8d-e9f0-1234-5678-90abcdef1234") @PathVariable UUID id,
            HttpServletRequest request) {
        if (tipoUsuarioService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Tipo de usuario no encontrado con ID: " + id);
        }
        tipoUsuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Obtiene todos los tipos de usuario en un formato simple (solo ID y nombre).
     * Útil para selectores o listas desplegables en el frontend.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una lista de TipoUsuarioSimpleDTO.
     */
    @Operation(summary = "Obtener todos los tipos de usuario en formato simple",
               description = "Recupera una lista de todos los tipos de usuario con solo su ID y nombre. Ideal para listas desplegables.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos de usuario simples recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoUsuarioSimpleDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/select")
    public ResponseEntity<Iterable<TipoUsuarioSimpleDTO>> getAllTiposUsuarioForSelect(HttpServletRequest request) {
        return ResponseEntity.ok(tipoUsuarioService.findAllSelect());
    }

    /**
     * Obtiene tipos de usuario por su estado con paginación y búsqueda opcional.
     * @param estado Estado por el cual filtrar (ej. "activo", "inactivo").
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de TipoUsuarioResponseDTO.
     */
    @Operation(summary = "Filtrar tipos de usuario por estado",
               description = "Recupera una lista paginada de tipos de usuario filtrados por su estado (activo, inactivo).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos de usuario filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Parámetro 'estado' inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<TipoUsuarioResponseDTO>> getTiposUsuarioByEstado(
            @Parameter(description = "Estado del tipo de usuario a filtrar (activo, inactivo).", example = "activo") @PathVariable String estado,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        Page<TipoUsuario> tiposUsuarioPage = tipoUsuarioService.findByEstado(estado, page, limit);
        Page<TipoUsuarioResponseDTO> responsePage = tiposUsuarioPage.map(tipo -> modelMapper.map(tipo, TipoUsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene tipos de usuario por ID de aplicación con paginación y búsqueda opcional.
     * @param aplicacionId ID de la aplicación por la cual filtrar los tipos de usuario.
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de TipoUsuarioResponseDTO.
     */
    @Operation(summary = "Filtrar tipos de usuario por aplicación",
               description = "Recupera una lista paginada de tipos de usuario filtrados por el ID de la aplicación a la que pertenecen.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos de usuario filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "ID de aplicación inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Aplicación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/by-aplicacion/{aplicacionId}")
    public ResponseEntity<Page<TipoUsuarioResponseDTO>> getTiposUsuarioByAplicacionId(
            @Parameter(description = "ID de la aplicación para filtrar tipos de usuario.", example = "40eebc99-9c0b-4ef8-bb6d-6bb9bd380a10") @PathVariable UUID aplicacionId,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        aplicacionesService.findById(aplicacionId)
            .orElseThrow(() -> new ResourceNotFoundException("Aplicación no encontrada con ID: " + aplicacionId));

        Page<TipoUsuario> tiposUsuarioPage = tipoUsuarioService.findByAplicacionId(aplicacionId, page, limit);
        Page<TipoUsuarioResponseDTO> responsePage = tiposUsuarioPage.map(tipo -> modelMapper.map(tipo, TipoUsuarioResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }
}
