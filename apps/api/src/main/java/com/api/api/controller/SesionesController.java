package com.api.api.controller;

import com.api.api.dto.ResponseDTO.SesionResponseDTO;
import com.api.api.dto.ResponseDTO.ErrorResponseDTO;
import com.api.api.exception.ResourceNotFoundException;
import com.api.api.model.Sesiones;
import com.api.api.service.SesionesService;
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
import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/sesiones")
@Tag(name = "Sesiones", description = "Endpoints para la gestión y consulta de sesiones de usuario.")
@SecurityRequirement(name = "bearerAuth")
public class SesionesController {

    private final SesionesService sesionesService;
    private final ModelMapper modelMapper;

    public SesionesController(SesionesService sesionesService,
                              UsuariosService usuariosService,
                              ModelMapper modelMapper) {
        this.sesionesService = sesionesService;
        this.modelMapper = modelMapper;

        // Configuración de ModelMapper para mapear datos del usuario relacionado al DTO de respuesta
        modelMapper.createTypeMap(Sesiones.class, SesionResponseDTO.class)
            .addMapping(src -> src.getUsuario().getId(), SesionResponseDTO::setUsuarioId)
            .addMapping(src -> src.getUsuario().getNombres(), SesionResponseDTO::setUsuarioNombres)
            .addMapping(src -> src.getUsuario().getApellidos(), SesionResponseDTO::setUsuarioApellidos);
    }

    /**
     * Obtiene todas las sesiones con paginación y búsqueda opcional.
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param searchTerm Término de búsqueda opcional.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de SesionResponseDTO.
     */
    @Operation(summary = "Obtener todas las sesiones",
               description = "Recupera una lista paginada de todas las sesiones de usuario, con opción de búsqueda.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sesiones recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Page<SesionResponseDTO>> getAllSesiones(
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Término de búsqueda para filtrar sesiones (IP, email, dispositivo, estado).", example = "activa") @RequestParam(required = false) String searchTerm,
            HttpServletRequest request) {
        Page<Sesiones> sesionesPage = sesionesService.findAll(page, limit, searchTerm);
        Page<SesionResponseDTO> responsePage = sesionesPage.map(sesion -> modelMapper.map(sesion, SesionResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene una sesión por su ID.
     * @param id ID de la sesión.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con SesionResponseDTO si se encuentra.
     */
    @Operation(summary = "Obtener sesión por ID",
               description = "Recupera una sesión específica utilizando su ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sesión recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SesionResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Sesión no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SesionResponseDTO> getSesionById(
            @Parameter(description = "ID de la sesión a recuperar.", example = "b1c2d3e4-f5a6-7890-1234-567890abcdef") @PathVariable UUID id,
            HttpServletRequest request) {
        Sesiones sesion = sesionesService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada con ID: " + id));
        return ResponseEntity.ok(modelMapper.map(sesion, SesionResponseDTO.class));
    }

    /**
     * Actualiza el estado de una sesión por su ID.
     * Útil para cerrar sesiones de forma manual o marcar como expiradas.
     * @param id ID de la sesión a actualizar.
     * @param newStatus Nuevo estado para la sesión (ej. "cerrada", "expirada").
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con la SesionResponseDTO actualizada.
     */
    @Operation(summary = "Actualizar estado de una sesión",
               description = "Actualiza el estado de una sesión (ej. 'cerrada', 'expirada') y registra la fecha de fin.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de sesión actualizado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SesionResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Estado proporcionado inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Sesión no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<SesionResponseDTO> updateSesionStatus(
            @Parameter(description = "ID de la sesión a actualizar.", example = "b1c2d3e4-f5a6-7890-1234-567890abcdef") @PathVariable UUID id,
            @Parameter(description = "Nuevo estado para la sesión (ej. 'cerrada', 'expirada').", example = "cerrada") @RequestParam String newStatus,
            HttpServletRequest request) {
        
        // Validar que el newStatus sea uno de los permitidos
        if (!newStatus.equalsIgnoreCase("cerrada") && !newStatus.equalsIgnoreCase("expirada") && !newStatus.equalsIgnoreCase("activa")) {
            throw new IllegalArgumentException("El estado debe ser 'activa', 'cerrada' o 'expirada'.");
        }

        Optional<Sesiones> updatedSesion = sesionesService.updateStatus(id, newStatus, OffsetDateTime.now());
        return ResponseEntity.ok(updatedSesion.map(sesion -> modelMapper.map(sesion, SesionResponseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada con ID: " + id)));
    }

    /**
     * Elimina lógicamente una sesión por su ID.
     * @param id ID de la sesión a eliminar lógicamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Eliminar lógicamente una sesión",
               description = "Marca una sesión como eliminada lógicamente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sesión eliminada lógicamente exitosamente."),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Sesión no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Void> softDeleteSesion(
            @Parameter(description = "ID de la sesión a eliminar lógicamente.", example = "b1c2d3e4-f5a6-7890-1234-567890abcdef") @PathVariable UUID id,
            HttpServletRequest request) {
        if (!sesionesService.softDelete(id).isPresent()) {
            throw new ResourceNotFoundException("Sesión no encontrada con ID: " + id);
        }
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Obtiene sesiones filtradas por su estado con paginación y búsqueda opcional.
     * @param estado Estado por el cual filtrar (ej. "activa", "cerrada", "expirada").
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de SesionResponseDTO.
     */
    @Operation(summary = "Filtrar sesiones por estado",
               description = "Recupera una lista paginada de sesiones filtradas por su estado (activa, cerrada, expirada).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sesiones filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Parámetro 'estado' inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<SesionResponseDTO>> getSesionesByEstado(
            @Parameter(description = "Estado de la sesión a filtrar (activa, cerrada, expirada).", example = "activa") @PathVariable String estado,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        
        // Validación de estado: asegurar que solo se permitan estados válidos
        if (!estado.equalsIgnoreCase("activa") && !estado.equalsIgnoreCase("cerrada") && !estado.equalsIgnoreCase("expirada")) {
            throw new IllegalArgumentException("El estado debe ser 'activa', 'cerrada' o 'expirada'.");
        }

        Page<Sesiones> sesionesPage = sesionesService.findByEstado(estado, page, limit);
        Page<SesionResponseDTO> responsePage = sesionesPage.map(sesion -> modelMapper.map(sesion, SesionResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }
}
