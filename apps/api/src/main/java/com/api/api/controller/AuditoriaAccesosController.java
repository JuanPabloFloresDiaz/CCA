package com.api.api.controller;

import com.api.api.dto.ResponseDTO.AuditoriaAccesoResponseDTO;
import com.api.api.dto.ResponseDTO.ErrorResponseDTO;
import com.api.api.exception.ResourceNotFoundException;
import com.api.api.model.AuditoriaAccesos;
import com.api.api.service.AuditoriaAccesosService;
import com.api.api.service.UsuariosService;
import com.api.api.service.AplicacionesService;
import com.api.api.service.AccionesService;

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

@RestController
@RequestMapping("/api/auditoria-accesos")
@Tag(name = "Auditoría de Accesos", description = "Endpoints para la consulta y gestión de registros de auditoría.")
@SecurityRequirement(name = "bearerAuth") 
public class AuditoriaAccesosController {

    private final AuditoriaAccesosService auditoriaAccesosService;
    private final AplicacionesService aplicacionesService;
    private final AccionesService accionesService;
    private final ModelMapper modelMapper;

    public AuditoriaAccesosController(AuditoriaAccesosService auditoriaAccesosService,
                                      UsuariosService usuariosService,
                                      AplicacionesService aplicacionesService,
                                      AccionesService accionesService,
                                      ModelMapper modelMapper) {
        this.auditoriaAccesosService = auditoriaAccesosService;
        this.aplicacionesService = aplicacionesService;
        this.accionesService = accionesService;
        this.modelMapper = modelMapper;

        // Configuración de ModelMapper para mapear datos de las entidades relacionadas al DTO de respuesta
        modelMapper.createTypeMap(AuditoriaAccesos.class, AuditoriaAccesoResponseDTO.class)
            .addMapping(src -> src.getUsuario() != null ? src.getUsuario().getId() : null, AuditoriaAccesoResponseDTO::setUsuarioId)
            .addMapping(src -> src.getUsuario() != null ? src.getUsuario().getNombres() : null, AuditoriaAccesoResponseDTO::setUsuarioNombres)
            .addMapping(src -> src.getUsuario() != null ? src.getUsuario().getApellidos() : null, AuditoriaAccesoResponseDTO::setUsuarioApellidos)
            .addMapping(src -> src.getAplicacion().getId(), AuditoriaAccesoResponseDTO::setAplicacionId)
            .addMapping(src -> src.getAplicacion().getNombre(), AuditoriaAccesoResponseDTO::setAplicacionNombre)
            .addMapping(src -> src.getAccion().getId(), AuditoriaAccesoResponseDTO::setAccionId)
            .addMapping(src -> src.getAccion().getNombre(), AuditoriaAccesoResponseDTO::setAccionNombre)
            .addMapping(src -> src.getAccion().getDescripcion(), AuditoriaAccesoResponseDTO::setAccionDescripcion);
    }

    /**
     * Obtiene todos los registros de auditoría con paginación y búsqueda opcional.
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param searchTerm Término de búsqueda opcional.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de AuditoriaAccesoResponseDTO.
     */
    @Operation(summary = "Obtener todos los registros de auditoría",
               description = "Recupera una lista paginada de todos los registros de auditoría, con opción de búsqueda por usuario, aplicación o acción.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de registros de auditoría recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Page<AuditoriaAccesoResponseDTO>> getAllAuditoriaAccesos(
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Término de búsqueda para filtrar registros (nombre de usuario, nombre de aplicación, nombre de acción).", example = "login") @RequestParam(required = false) String searchTerm,
            HttpServletRequest request) {
        Page<AuditoriaAccesos> auditoriaPage = auditoriaAccesosService.findAll(page, limit, searchTerm);
        Page<AuditoriaAccesoResponseDTO> responsePage = auditoriaPage.map(audit -> modelMapper.map(audit, AuditoriaAccesoResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene un registro de auditoría por su ID compuesto (UUID y Fecha).
     * @param uuidId UUID del registro.
     * @param fecha Fecha del registro en formato ISO 8601 (ej. 2024-06-15T14:30:00Z).
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con AuditoriaAccesoResponseDTO si se encuentra.
     */
    @Operation(summary = "Obtener registro de auditoría por ID y Fecha",
               description = "Recupera un registro de auditoría específico utilizando su ID compuesto (UUID y la fecha exacta).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro de auditoría recuperado exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuditoriaAccesoResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Registro de auditoría no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{uuidId}/fecha/{fecha}")
    public ResponseEntity<AuditoriaAccesoResponseDTO> getAuditoriaAccesoByIdAndFecha(
            @Parameter(description = "UUID del registro de auditoría.", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef") @PathVariable UUID uuidId,
            @Parameter(description = "Fecha y hora exacta del registro en formato ISO 8601.", example = "2024-06-15T14:30:00Z") @PathVariable OffsetDateTime fecha,
            HttpServletRequest request) {
        AuditoriaAccesos auditoria = auditoriaAccesosService.findById(uuidId, fecha)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de auditoría no encontrado con UUID: " + uuidId + " y Fecha: " + fecha));
        return ResponseEntity.ok(modelMapper.map(auditoria, AuditoriaAccesoResponseDTO.class));
    }

    /**
     * Obtiene registros de auditoría filtrados por el ID de una aplicación.
     * @param aplicacionId ID de la aplicación para filtrar los registros.
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de AuditoriaAccesoResponseDTO.
     */
    @Operation(summary = "Filtrar registros de auditoría por aplicación",
               description = "Recupera una lista paginada de registros de auditoría asociados a una aplicación específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de registros de auditoría filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "ID de aplicación inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Aplicación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/by-aplicacion/{aplicacionId}")
    public ResponseEntity<Page<AuditoriaAccesoResponseDTO>> getAuditoriaAccesosByAplicacionId(
            @Parameter(description = "ID de la aplicación para filtrar los registros de auditoría.", example = "f1e2d3c4-b5a6-7890-1234-567890fedcba") @PathVariable UUID aplicacionId,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        // Validar que la aplicación exista
        aplicacionesService.findById(aplicacionId)
            .orElseThrow(() -> new ResourceNotFoundException("Aplicación no encontrada con ID: " + aplicacionId));

        Page<AuditoriaAccesos> auditoriaPage = auditoriaAccesosService.findByAplicacionId(aplicacionId, page, limit);
        Page<AuditoriaAccesoResponseDTO> responsePage = auditoriaPage.map(audit -> modelMapper.map(audit, AuditoriaAccesoResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene registros de auditoría filtrados por el ID de una acción.
     * @param accionId ID de la acción para filtrar los registros.
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de AuditoriaAccesoResponseDTO.
     */
    @Operation(summary = "Filtrar registros de auditoría por acción",
               description = "Recupera una lista paginada de registros de auditoría asociados a una acción específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de registros de auditoría filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "ID de acción inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Acción no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/by-accion/{accionId}")
    public ResponseEntity<Page<AuditoriaAccesoResponseDTO>> getAuditoriaAccesosByAccionId(
            @Parameter(description = "ID de la acción para filtrar los registros de auditoría.", example = "09876543-21ab-cdef-1234-567890abcdef") @PathVariable UUID accionId,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        // Validar que la acción exista
        accionesService.findById(accionId)
            .orElseThrow(() -> new ResourceNotFoundException("Acción no encontrada con ID: " + accionId));

        Page<AuditoriaAccesos> auditoriaPage = auditoriaAccesosService.findByAccionId(accionId, page, limit);
        Page<AuditoriaAccesoResponseDTO> responsePage = auditoriaPage.map(audit -> modelMapper.map(audit, AuditoriaAccesoResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }
}
