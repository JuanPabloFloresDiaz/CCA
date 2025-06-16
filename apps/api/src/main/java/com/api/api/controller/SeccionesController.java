package com.api.api.controller;

import com.api.api.dto.RequestDTO.SeccionRequestDTO;
import com.api.api.dto.ResponseDTO.SeccionResponseDTO;
import com.api.api.dto.ResponseDTO.ErrorResponseDTO;
import com.api.api.dto.SimpleDTO.SeccionSimpleDTO;
import com.api.api.exception.ResourceNotFoundException;
import com.api.api.model.Secciones;
import com.api.api.service.SeccionesService;

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
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/secciones")
@Tag(name = "Secciones", description = "Endpoints para la gestión de secciones de aplicaciones.")
@SecurityRequirement(name = "bearerAuth")
public class SeccionesController {

        private final SeccionesService seccionesService;
        private final ModelMapper modelMapper;

        public SeccionesController(SeccionesService seccionesService, ModelMapper modelMapper) {
                this.seccionesService = seccionesService;
                this.modelMapper = modelMapper;
        }

        /**
         * Obtiene todas las secciones con paginación y búsqueda opcional.
         * 
         * @param page       Número de página (por defecto 1).
         * @param limit      Cantidad de elementos por página (por defecto 10).
         * @param searchTerm Término de búsqueda opcional.
         * @return ResponseEntity con una página de SeccionResponseDTO.
         */
        @Operation(summary = "Obtener todas las secciones", description = "Recupera una lista paginada de todas las secciones, con opción de búsqueda.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de secciones recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
                        @ApiResponse(responseCode = "401", description = "No autenticado."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @GetMapping
        public ResponseEntity<?> getAllSecciones(
                        @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
                        @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
                        @Parameter(description = "Término de búsqueda para filtrar secciones (nombre o descripción).", example = "autenticacion") @RequestParam(required = false) String searchTerm,
                        HttpServletRequest request) {
                String requestPath = request.getRequestURI();
                try {
                        Page<Secciones> seccionesPage = seccionesService.findAll(page, limit, searchTerm);
                        Page<SeccionResponseDTO> responsePage = seccionesPage
                                        .map(seccion -> modelMapper.map(seccion, SeccionResponseDTO.class));
                        return ResponseEntity.ok(responsePage);
                } catch (Exception e) {
                        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                        "Ocurrió un error al obtener las secciones.",
                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                        e.getClass().getSimpleName(),
                                        OffsetDateTime.now(),
                                        requestPath);
                        System.err.println("Error en /api/secciones: " + e.getMessage());
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                }
        }

        /**
         * Obtiene una sección por su ID.
         * 
         * @param id ID de la sección.
         * @return ResponseEntity con SeccionResponseDTO si se encuentra.
         */
        @Operation(summary = "Obtener sección por ID", description = "Recupera una sección específica utilizando su ID único.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Sección recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SeccionResponseDTO.class))),
                        @ApiResponse(responseCode = "401", description = "No autenticado."),
                        @ApiResponse(responseCode = "404", description = "Sección no encontrada."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @GetMapping("/{id}")
        public ResponseEntity<?> getSeccionById(
                        @Parameter(description = "ID de la sección a recuperar.", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID id,
                        HttpServletRequest request) {
                String requestPath = request.getRequestURI();
                try {
                        Secciones seccion = seccionesService.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Sección no encontrada con ID: " + id));
                        return ResponseEntity.ok(modelMapper.map(seccion, SeccionResponseDTO.class));
                } catch (ResourceNotFoundException e) {
                        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                        e.getMessage(),
                                        HttpStatus.NOT_FOUND.value(),
                                        e.getClass().getSimpleName(),
                                        OffsetDateTime.now(),
                                        requestPath);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                } catch (Exception e) {
                        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                        "Ocurrió un error al obtener la sección.",
                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                        e.getClass().getSimpleName(),
                                        OffsetDateTime.now(),
                                        requestPath);
                        System.err.println("Error en /api/secciones/{id}: " + e.getMessage());
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                }
        }

        /**
         * Crea una nueva sección.
         * 
         * @param seccionRequestDTO DTO con los datos de la nueva sección.
         * @return ResponseEntity con la SeccionResponseDTO creada.
         */
        @Operation(summary = "Crear nueva sección", description = "Crea una nueva sección en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Sección creada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SeccionResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
                        @ApiResponse(responseCode = "401", description = "No autenticado."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @PostMapping
        public ResponseEntity<?> createSeccion(@Valid @RequestBody SeccionRequestDTO seccionRequestDTO,
                        HttpServletRequest request) {
                String requestPath = request.getRequestURI();
                try {
                        Secciones seccionToCreate = modelMapper.map(seccionRequestDTO, Secciones.class);
                        Secciones createdSeccion = seccionesService.create(seccionToCreate);
                        return new ResponseEntity<>(modelMapper.map(createdSeccion, SeccionResponseDTO.class),
                                        HttpStatus.CREATED);
                } catch (Exception e) {
                        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
                        String errorMessage = "Ocurrió un error al crear la sección.";

                        if (e instanceof org.springframework.web.bind.MethodArgumentNotValidException) {
                                status = HttpStatus.BAD_REQUEST;
                                errorMessage = "Datos de entrada inválidos. Por favor, revise los campos.";
                        } else if (e.getMessage() != null && e.getMessage().contains("ConstraintViolationException")) {
                                status = HttpStatus.CONFLICT;
                                errorMessage = "Ya existe una sección con el nombre proporcionado o datos inválidos.";
                        }

                        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                        errorMessage,
                                        status.value(),
                                        e.getClass().getSimpleName(),
                                        OffsetDateTime.now(),
                                        requestPath);
                        System.err.println("Error en /api/secciones (create): " + e.getMessage());
                        e.printStackTrace();
                        return ResponseEntity.status(status).body(errorResponse);
                }
        }

        /**
         * Actualiza una sección existente por su ID.
         * 
         * @param id                ID de la sección a actualizar.
         * @param seccionRequestDTO DTO con los datos actualizados de la sección.
         * @return ResponseEntity con la SeccionResponseDTO actualizada.
         */
        @Operation(summary = "Actualizar sección existente", description = "Actualiza los datos de una sección existente utilizando su ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Sección actualizada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SeccionResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
                        @ApiResponse(responseCode = "401", description = "No autenticado."),
                        @ApiResponse(responseCode = "404", description = "Sección no encontrada."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @PutMapping("/{id}")
        public ResponseEntity<?> updateSeccion(
                        @Parameter(description = "ID de la sección a actualizar.", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID id,
                        @Valid @RequestBody SeccionRequestDTO seccionRequestDTO,
                        HttpServletRequest request) {
                String requestPath = request.getRequestURI();
                try {
                        Secciones seccionToUpdate = modelMapper.map(seccionRequestDTO, Secciones.class);
                        Optional<Secciones> updatedSeccion = seccionesService.update(id, seccionToUpdate);
                        return updatedSeccion
                                        .map(seccion -> ResponseEntity
                                                        .ok(modelMapper.map(seccion, SeccionResponseDTO.class)))
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Sección no encontrada con ID: " + id));
                } catch (ResourceNotFoundException e) {
                        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                        e.getMessage(),
                                        HttpStatus.NOT_FOUND.value(),
                                        e.getClass().getSimpleName(),
                                        OffsetDateTime.now(),
                                        requestPath);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                } catch (Exception e) {
                        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
                        String errorMessage = "Ocurrió un error al actualizar la sección.";

                        if (e instanceof org.springframework.web.bind.MethodArgumentNotValidException) {
                                status = HttpStatus.BAD_REQUEST;
                                errorMessage = "Datos de entrada inválidos. Por favor, revise los campos.";
                        } else if (e.getMessage() != null && e.getMessage().contains("ConstraintViolationException")) {
                                status = HttpStatus.CONFLICT;
                                errorMessage = "Conflicto de datos: el nombre de la sección podría ya existir o hay datos inválidos.";
                        }

                        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                        errorMessage,
                                        status.value(),
                                        e.getClass().getSimpleName(),
                                        OffsetDateTime.now(),
                                        requestPath);
                        System.err.println("Error en /api/secciones/{id} (update): " + e.getMessage());
                        e.printStackTrace();
                        return ResponseEntity.status(status).body(errorResponse);
                }
        }

        /**
         * Elimina lógicamente una sección por su ID.
         * 
         * @param id ID de la sección a eliminar lógicamente.
         * @return ResponseEntity sin contenido.
         */
        @Operation(summary = "Eliminar lógicamente una sección", description = "Marca una sección como eliminada lógicamente por su ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Sección eliminada lógicamente exitosamente."),
                        @ApiResponse(responseCode = "401", description = "No autenticado."),
                        @ApiResponse(responseCode = "404", description = "Sección no encontrada."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @DeleteMapping("/soft-delete/{id}")
        public ResponseEntity<?> softDeleteSeccion(
                        @Parameter(description = "ID de la sección a eliminar lógicamente.", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID id,
                        HttpServletRequest request) {
                String requestPath = request.getRequestURI();
                try {
                        if (!seccionesService.softDelete(id).isPresent()) {
                                throw new ResourceNotFoundException("Sección no encontrada con ID: " + id);
                        }
                        return ResponseEntity.noContent().build();
                } catch (ResourceNotFoundException e) {
                        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                        e.getMessage(),
                                        HttpStatus.NOT_FOUND.value(),
                                        e.getClass().getSimpleName(),
                                        OffsetDateTime.now(),
                                        requestPath);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                } catch (Exception e) {
                        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                        "Ocurrió un error al eliminar lógicamente la sección.",
                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                        e.getClass().getSimpleName(),
                                        OffsetDateTime.now(),
                                        requestPath);
                        System.err.println("Error en /api/secciones/soft-delete/{id}: " + e.getMessage());
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                }
        }

        /**
         * Elimina definitivamente una sección por su ID.
         * Usar con precaución.
         * 
         * @param id ID de la sección a eliminar definitivamente.
         * @return ResponseEntity sin contenido.
         */
        @Operation(summary = "Eliminar definitivamente una sección (Precaución)", description = "Elimina de forma permanente una sección por su ID. Esta operación es irreversible.", deprecated = true) // delete
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Sección eliminada definitivamente exitosamente."),
                        @ApiResponse(responseCode = "401", description = "No autenticado."),
                        @ApiResponse(responseCode = "404", description = "Sección no encontrada."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteSeccion(
                        @Parameter(description = "ID de la sección a eliminar definitivamente.", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID id,
                        HttpServletRequest request) {
                String requestPath = request.getRequestURI();
                try {
                        // Primero verificar si existe para lanzar 404 si no
                        if (seccionesService.findById(id).isEmpty()) {
                                throw new ResourceNotFoundException("Sección no encontrada con ID: " + id);
                        }
                        seccionesService.deleteById(id);
                        return ResponseEntity.noContent().build();
                } catch (ResourceNotFoundException e) {
                        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                        e.getMessage(),
                                        HttpStatus.NOT_FOUND.value(),
                                        e.getClass().getSimpleName(),
                                        OffsetDateTime.now(),
                                        requestPath);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                } catch (Exception e) {
                        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                        "Ocurrió un error al eliminar definitivamente la sección.",
                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                        e.getClass().getSimpleName(),
                                        OffsetDateTime.now(),
                                        requestPath);
                        System.err.println("Error en /api/secciones/{id} (delete): " + e.getMessage());
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                }
        }

        /**
         * Obtiene todas las secciones en un formato simple (solo ID y nombre).
         * Útil para selectores o listas desplegables en el frontend.
         * 
         * @return ResponseEntity con una lista de SeccionSimpleDTO.
         */
        @Operation(summary = "Obtener todas las secciones en formato simple", description = "Recupera una lista de todas las secciones con solo su ID y nombre. Ideal para listas desplegables.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de secciones simples recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SeccionSimpleDTO.class))),
                        @ApiResponse(responseCode = "401", description = "No autenticado."),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        })
        @GetMapping("/select")
        public ResponseEntity<?> getAllSeccionesForSelect(HttpServletRequest request) {
                String requestPath = request.getRequestURI();
                try {
                        return ResponseEntity.ok(seccionesService.findAllSelect());
                } catch (Exception e) {
                        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                        "Ocurrió un error al obtener las secciones para seleccionar.",
                                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                        e.getClass().getSimpleName(),
                                        OffsetDateTime.now(),
                                        requestPath);
                        System.err.println("Error en /api/secciones/select: " + e.getMessage());
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                }
        }
}
