package com.api.api.controller;

import com.api.api.dto.RequestDTO.AplicacionRequestDTO;
import com.api.api.dto.ResponseDTO.AplicacionResponseDTO;
import com.api.api.dto.ResponseDTO.ErrorResponseDTO;
import com.api.api.dto.SimpleDTO.AplicacionSimpleDTO;
import com.api.api.exception.ResourceNotFoundException;
import com.api.api.model.Aplicaciones;
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

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/aplicaciones")
@Tag(name = "Aplicaciones", description = "Endpoints para la gestión de aplicaciones.")
@SecurityRequirement(name = "bearerAuth")
public class AplicacionesController {

    private final AplicacionesService aplicacionesService;
    private final ModelMapper modelMapper;

    public AplicacionesController(AplicacionesService aplicacionesService, ModelMapper modelMapper) {
        this.aplicacionesService = aplicacionesService;
        this.modelMapper = modelMapper;
    }

    /**
     * Obtiene todas las aplicaciones con paginación y búsqueda opcional.
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param searchTerm Término de búsqueda opcional.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de AplicacionResponseDTO o ErrorResponseDTO.
     */
    @Operation(summary = "Obtener todas las aplicaciones",
               description = "Recupera una lista paginada de todas las aplicaciones, con opción de búsqueda.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de aplicaciones recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllAplicaciones(
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Término de búsqueda para filtrar aplicaciones (nombre, descripción, URL, llave).", example = "control") @RequestParam(required = false) String searchTerm,
            HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        try {
            Page<Aplicaciones> aplicacionesPage = aplicacionesService.findAll(page, limit, searchTerm);
            Page<AplicacionResponseDTO> responsePage = aplicacionesPage.map(app -> modelMapper.map(app, AplicacionResponseDTO.class));
            return ResponseEntity.ok(responsePage);
        } catch (Exception e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "Ocurrió un error al obtener las aplicaciones.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            System.err.println("Error en /api/aplicaciones: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtiene una aplicación por su ID.
     * @param id ID de la aplicación.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con AplicacionResponseDTO si se encuentra o ErrorResponseDTO.
     */
    @Operation(summary = "Obtener aplicación por ID",
               description = "Recupera una aplicación específica utilizando su ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aplicación recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AplicacionResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Aplicación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getAplicacionById(
            @Parameter(description = "ID de la aplicación a recuperar.", example = "40eebc99-9c0b-4ef8-bb6d-6bb9bd380a10") @PathVariable UUID id,
            HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        try {
            Aplicaciones aplicacion = aplicacionesService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Aplicación no encontrada con ID: " + id));
            return ResponseEntity.ok(modelMapper.map(aplicacion, AplicacionResponseDTO.class));
        } catch (ResourceNotFoundException e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "Ocurrió un error al obtener la aplicación.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            System.err.println("Error en /api/aplicaciones/{id}: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Crea una nueva aplicación.
     * @param aplicacionRequestDTO DTO con los datos de la nueva aplicación.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con la AplicacionResponseDTO creada o ErrorResponseDTO.
     */
    @Operation(summary = "Crear nueva aplicación",
               description = "Crea una nueva aplicación en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Aplicación creada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AplicacionResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "Conflicto: la llave identificadora ya existe.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<?> createAplicacion(@Valid @RequestBody AplicacionRequestDTO aplicacionRequestDTO,
                                              HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        try {
            // Verifica si la llave identificadora ya existe antes de intentar guardar
            if (aplicacionesService.findByLlaveIdentificadora(aplicacionRequestDTO.getLlaveIdentificadora()).isPresent()) {
                throw new IllegalArgumentException("La llave identificadora '" + aplicacionRequestDTO.getLlaveIdentificadora() + "' ya está en uso.");
            }
            Aplicaciones aplicacionToCreate = modelMapper.map(aplicacionRequestDTO, Aplicaciones.class);
            Aplicaciones createdAplicacion = aplicacionesService.create(aplicacionToCreate);
            return new ResponseEntity<>(modelMapper.map(createdAplicacion, AplicacionResponseDTO.class), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            String errorMessage = "Ocurrió un error al crear la aplicación.";
            
            if (e instanceof org.springframework.web.bind.MethodArgumentNotValidException) {
                status = HttpStatus.BAD_REQUEST;
                errorMessage = "Datos de entrada inválidos. Por favor, revise los campos.";
            } else if (e.getMessage() != null && e.getMessage().contains("ConstraintViolationException")) {
                status = HttpStatus.BAD_REQUEST;
                errorMessage = "Violación de restricción de base de datos o datos inválidos.";
            }

            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                errorMessage,
                status.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            System.err.println("Error en /api/aplicaciones (create): " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    /**
     * Actualiza una aplicación existente por su ID.
     * @param id ID de la aplicación a actualizar.
     * @param aplicacionRequestDTO DTO con los datos actualizados de la aplicación.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con la AplicacionResponseDTO actualizada o ErrorResponseDTO.
     */
    @Operation(summary = "Actualizar aplicación existente",
               description = "Actualiza los datos de una aplicación existente utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aplicación actualizada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AplicacionResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Aplicación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "Conflicto: la llave identificadora ya existe y pertenece a otra aplicación.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAplicacion(
            @Parameter(description = "ID de la aplicación a actualizar.", example = "40eebc99-9c0b-4ef8-bb6d-6bb9bd380a10") @PathVariable UUID id,
            @Valid @RequestBody AplicacionRequestDTO aplicacionRequestDTO,
            HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        try {
            // Verificar si la nueva llave identificadora ya existe y no pertenece a la aplicación actual
            Optional<Aplicaciones> existingAppWithKey = aplicacionesService.findByLlaveIdentificadora(aplicacionRequestDTO.getLlaveIdentificadora());
            if (existingAppWithKey.isPresent() && !existingAppWithKey.get().getId().equals(id)) {
                throw new IllegalArgumentException("La llave identificadora '" + aplicacionRequestDTO.getLlaveIdentificadora() + "' ya está en uso por otra aplicación.");
            }

            Aplicaciones aplicacionToUpdate = modelMapper.map(aplicacionRequestDTO, Aplicaciones.class);
            Optional<Aplicaciones> updatedAplicacion = aplicacionesService.update(id, aplicacionToUpdate);
            return updatedAplicacion.map(app -> ResponseEntity.ok(modelMapper.map(app, AplicacionResponseDTO.class)))
                    .orElseThrow(() -> new ResourceNotFoundException("Aplicación no encontrada con ID: " + id));
        } catch (ResourceNotFoundException e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (IllegalArgumentException e) {
             ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            String errorMessage = "Ocurrió un error al actualizar la aplicación.";
            
            if (e instanceof org.springframework.web.bind.MethodArgumentNotValidException) {
                status = HttpStatus.BAD_REQUEST;
                errorMessage = "Datos de entrada inválidos. Por favor, revise los campos.";
            } else if (e.getMessage() != null && e.getMessage().contains("ConstraintViolationException")) {
                status = HttpStatus.BAD_REQUEST;
                errorMessage = "Violación de restricción de base de datos o datos inválidos.";
            }

            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                errorMessage,
                status.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            System.err.println("Error en /api/aplicaciones/{id} (update): " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    /**
     * Elimina lógicamente una aplicación por su ID.
     * @param id ID de la aplicación a eliminar lógicamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido o ErrorResponseDTO.
     */
    @Operation(summary = "Eliminar lógicamente una aplicación",
               description = "Marca una aplicación como eliminada lógicamente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Aplicación eliminada lógicamente exitosamente."),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Aplicación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteAplicacion(
            @Parameter(description = "ID de la aplicación a eliminar lógicamente.", example = "40eebc99-9c0b-4ef8-bb6d-6bb9bd380a10") @PathVariable UUID id,
            HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        try {
            if (!aplicacionesService.softDelete(id).isPresent()) {
                throw new ResourceNotFoundException("Aplicación no encontrada con ID: " + id);
            }
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "Ocurrió un error al eliminar lógicamente la aplicación.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            System.err.println("Error en /api/aplicaciones/soft-delete/{id}: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Elimina definitivamente una aplicación por su ID.
     * Usar con precaución.
     * @param id ID de la aplicación a eliminar definitivamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido o ErrorResponseDTO.
     */
    @Operation(summary = "Eliminar definitivamente una aplicación (Precaución)",
               description = "Elimina de forma permanente una aplicación por su ID. Esta operación es irreversible.",
               deprecated = true)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Aplicación eliminada definitivamente exitosamente."),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Aplicación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAplicacion(
            @Parameter(description = "ID de la aplicación a eliminar definitivamente.", example = "40eebc99-9c0b-4ef8-bb6d-6bb9bd380a10") @PathVariable UUID id,
            HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        try {
            if (aplicacionesService.findById(id).isEmpty()) {
                throw new ResourceNotFoundException("Aplicación no encontrada con ID: " + id);
            }
            aplicacionesService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "Ocurrió un error al eliminar definitivamente la aplicación.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            System.err.println("Error en /api/aplicaciones/{id} (delete): " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Obtiene todas las aplicaciones en un formato simple (solo ID y nombre).
     * Útil para selectores o listas desplegables en el frontend.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una lista de AplicacionSimpleDTO o ErrorResponseDTO.
     */
    @Operation(summary = "Obtener todas las aplicaciones en formato simple",
               description = "Recupera una lista de todas las aplicaciones con solo su ID y nombre. Ideal para listas desplegables.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de aplicaciones simples recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AplicacionSimpleDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/select")
    public ResponseEntity<?> getAllAplicacionesForSelect(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        try {
            return ResponseEntity.ok(aplicacionesService.findAllSelect());
        } catch (Exception e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "Ocurrió un error al obtener las aplicaciones para seleccionar.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            System.err.println("Error en /api/aplicaciones/select: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Obtiene aplicaciones por su estado con paginación y búsqueda opcional.
     * @param estado Estado por el cual filtrar (ej. "activo", "inactivo").
     * @param page Número de página (por defecto 1).
     * @param limit Cantidad de elementos por página (por defecto 10).
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de AplicacionResponseDTO o ErrorResponseDTO.
     */
    @Operation(summary = "Filtrar aplicaciones por estado",
               description = "Recupera una lista paginada de aplicaciones filtradas por su estado (activo/inactivo).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de aplicaciones filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "400", description = "Parámetro 'estado' inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> getAplicacionesByEstado(
            @Parameter(description = "Estado de la aplicación a filtrar (activo o inactivo).", example = "activo") @PathVariable String estado,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        try {
            // Opcional: validación del estado en el controlador o dejar que el servicio lo maneje
            if (!estado.equalsIgnoreCase("activo") && !estado.equalsIgnoreCase("inactivo")) {
                throw new IllegalArgumentException("El estado debe ser 'activo' o 'inactivo'.");
            }
            Page<Aplicaciones> aplicacionesPage = aplicacionesService.findByEstado(estado, page, limit);
            Page<AplicacionResponseDTO> responsePage = aplicacionesPage.map(app -> modelMapper.map(app, AplicacionResponseDTO.class));
            return ResponseEntity.ok(responsePage);
        } catch (IllegalArgumentException e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "Ocurrió un error al filtrar aplicaciones por estado.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getClass().getSimpleName(),
                OffsetDateTime.now(),
                requestPath
            );
            System.err.println("Error en /api/aplicaciones/estado/{estado}: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
