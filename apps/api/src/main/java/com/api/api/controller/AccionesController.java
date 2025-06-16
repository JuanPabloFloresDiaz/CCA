package com.api.api.controller;

import com.api.api.dto.RequestDTO.AccionRequestDTO;
import com.api.api.dto.ResponseDTO.AccionResponseDTO;
import com.api.api.dto.ResponseDTO.ErrorResponseDTO;
import com.api.api.dto.SimpleDTO.AccionSimpleDTO;
import com.api.api.exception.ResourceNotFoundException;
import com.api.api.model.Acciones;
import com.api.api.model.Aplicaciones;
import com.api.api.model.Secciones;
import com.api.api.service.AccionesService;
import com.api.api.service.AplicacionesService;
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

import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/acciones")
@Tag(name = "Acciones", description = "Endpoints para la gestión de acciones de seguridad.")
@SecurityRequirement(name = "bearerAuth")
public class AccionesController {

    private final AccionesService accionesService;
    private final AplicacionesService aplicacionesService;
    private final SeccionesService seccionesService;
    private final ModelMapper modelMapper;

    public AccionesController(AccionesService accionesService,
            AplicacionesService aplicacionesService,
            SeccionesService seccionesService,
            ModelMapper modelMapper) {
        this.accionesService = accionesService;
        this.aplicacionesService = aplicacionesService;
        this.seccionesService = seccionesService;
        this.modelMapper = modelMapper;

        modelMapper.createTypeMap(Acciones.class, AccionResponseDTO.class)
                .addMapping(src -> src.getAplicacion().getId(), AccionResponseDTO::setAplicacionId)
                .addMapping(src -> src.getAplicacion().getNombre(), AccionResponseDTO::setNombreAplicacion)
                .addMapping(src -> src.getSeccion().getId(), AccionResponseDTO::setSeccionId)
                .addMapping(src -> src.getSeccion().getNombre(), AccionResponseDTO::setNombreSeccion);
    }

    /**
     * Obtiene todas las acciones con paginación y búsqueda opcional.
     * 
     * @param page       Número de página (por defecto 1).
     * @param limit      Cantidad de elementos por página (por defecto 10).
     * @param searchTerm Término de búsqueda opcional.
     * @param request    HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de AccionResponseDTO.
     */
    @Operation(summary = "Obtener todas las acciones", description = "Recupera una lista paginada de todas las acciones, con opción de búsqueda.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de acciones recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Page<AccionResponseDTO>> getAllAcciones(
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Término de búsqueda para filtrar acciones (nombre o descripción).", example = "crear") @RequestParam(required = false) String searchTerm,
            HttpServletRequest request) {
        Page<Acciones> accionesPage = accionesService.findAll(page, limit, searchTerm);
        Page<AccionResponseDTO> responsePage = accionesPage
                .map(accion -> modelMapper.map(accion, AccionResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene una acción por su ID.
     * 
     * @param id      ID de la acción.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con AccionResponseDTO si se encuentra.
     */
    @Operation(summary = "Obtener acción por ID", description = "Recupera una acción específica utilizando su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Acción recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccionResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Acción no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccionResponseDTO> getAccionById(
            @Parameter(description = "ID de la acción a recuperar.", example = "a2b3c4d5-e6f7-8901-2345-67890abcdef1") @PathVariable UUID id,
            HttpServletRequest request) {
        Acciones accion = accionesService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acción no encontrada con ID: " + id));
        return ResponseEntity.ok(modelMapper.map(accion, AccionResponseDTO.class));
    }

    /**
     * Crea una nueva acción.
     * 
     * @param accionRequestDTO DTO con los datos de la nueva acción.
     * @param request          HttpServletRequest para obtener la ruta de la
     *                         solicitud.
     * @return ResponseEntity con la AccionResponseDTO creada.
     */
    @Operation(summary = "Crear nueva acción", description = "Crea una nueva acción en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Acción creada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. ID de aplicación/sección no existe).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto: ya existe una acción con el mismo nombre para la misma aplicación.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<AccionResponseDTO> createAccion(@Valid @RequestBody AccionRequestDTO accionRequestDTO,
            HttpServletRequest request) {
        // Validar que la aplicación y la sección existan
        Aplicaciones aplicacion = aplicacionesService.findById(accionRequestDTO.getAplicacionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aplicación no encontrada con ID: " + accionRequestDTO.getAplicacionId()));

        Secciones seccion = seccionesService.findById(accionRequestDTO.getSeccionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sección no encontrada con ID: " + accionRequestDTO.getSeccionId()));

        // Verificar si ya existe una acción con el mismo nombre para esta aplicación
        if (accionesService
                .findByNombreAndAplicacionId(accionRequestDTO.getNombre(), accionRequestDTO.getAplicacionId())
                .isPresent()) {
            throw new IllegalArgumentException("Ya existe una acción con el nombre '" + accionRequestDTO.getNombre()
                    + "' para la aplicación con ID: " + accionRequestDTO.getAplicacionId());
        }

        Acciones accionToCreate = modelMapper.map(accionRequestDTO, Acciones.class);
        accionToCreate.setAplicacion(aplicacion);
        accionToCreate.setSeccion(seccion);

        Acciones createdAccion = accionesService.create(accionToCreate);
        return new ResponseEntity<>(modelMapper.map(createdAccion, AccionResponseDTO.class), HttpStatus.CREATED);
    }

    /**
     * Actualiza una acción existente por su ID.
     * 
     * @param id               ID de la acción a actualizar.
     * @param accionRequestDTO DTO con los datos actualizados de la acción.
     * @param request          HttpServletRequest para obtener la ruta de la
     *                         solicitud.
     * @return ResponseEntity con la AccionResponseDTO actualizada.
     */
    @Operation(summary = "Actualizar acción existente", description = "Actualiza los datos de una acción existente utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Acción actualizada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. ID de aplicación/sección no existe).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Acción no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto: ya existe otra acción con el mismo nombre para la misma aplicación.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccionResponseDTO> updateAccion(
            @Parameter(description = "ID de la acción a actualizar.", example = "a2b3c4d5-e6f7-8901-2345-67890abcdef1") @PathVariable UUID id,
            @Valid @RequestBody AccionRequestDTO accionRequestDTO,
            HttpServletRequest request) {
        // Validar que la aplicación y la sección existan
        Aplicaciones aplicacion = aplicacionesService.findById(accionRequestDTO.getAplicacionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aplicación no encontrada con ID: " + accionRequestDTO.getAplicacionId()));

        Secciones seccion = seccionesService.findById(accionRequestDTO.getSeccionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sección no encontrada con ID: " + accionRequestDTO.getSeccionId()));

        // Verificar si la acción con el nuevo nombre ya existe para esta aplicación y
        // no es la propia acción que se está actualizando
        Optional<Acciones> existingAccion = accionesService.findByNombreAndAplicacionId(accionRequestDTO.getNombre(),
                accionRequestDTO.getAplicacionId());
        if (existingAccion.isPresent() && !existingAccion.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe otra acción con el nombre '" + accionRequestDTO.getNombre()
                    + "' para la aplicación con ID: " + accionRequestDTO.getAplicacionId());
        }

        Acciones accionToUpdate = modelMapper.map(accionRequestDTO, Acciones.class);
        accionToUpdate.setAplicacion(aplicacion);
        accionToUpdate.setSeccion(seccion);

        Optional<Acciones> updatedAccion = accionesService.update(id, accionToUpdate);
        return ResponseEntity.ok(updatedAccion.map(accion -> modelMapper.map(accion, AccionResponseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Acción no encontrada con ID: " + id)));
    }

    /**
     * Elimina lógicamente una acción por su ID.
     * 
     * @param id      ID de la acción a eliminar lógicamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Eliminar lógicamente una acción", description = "Marca una acción como eliminada lógicamente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Acción eliminada lógicamente exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Acción no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Void> softDeleteAccion(
            @Parameter(description = "ID de la acción a eliminar lógicamente.", example = "a2b3c4d5-e6f7-8901-2345-67890abcdef1") @PathVariable UUID id,
            HttpServletRequest request) {
        if (!accionesService.softDelete(id).isPresent()) {
            throw new ResourceNotFoundException("Acción no encontrada con ID: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Elimina definitivamente una acción por su ID.
     * Usar con precaución.
     * 
     * @param id      ID de la acción a eliminar definitivamente.
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity sin contenido.
     */
    @Operation(summary = "Eliminar definitivamente una acción (Precaución)", description = "Elimina de forma permanente una acción por su ID. Esta operación es irreversible.", deprecated = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Acción eliminada definitivamente exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Acción no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccion(
            @Parameter(description = "ID de la acción a eliminar definitivamente.", example = "a2b3c4d5-e6f7-8901-2345-67890abcdef1") @PathVariable UUID id,
            HttpServletRequest request) {
        if (accionesService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Acción no encontrada con ID: " + id);
        }
        accionesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene todas las acciones en un formato simple (solo ID y nombre).
     * Útil para selectores o listas desplegables en el frontend.
     * 
     * @param request HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una lista de AccionSimpleDTO.
     */
    @Operation(summary = "Obtener todas las acciones en formato simple", description = "Recupera una lista de todas las acciones con solo su ID y nombre. Ideal para listas desplegables.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de acciones simples recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccionSimpleDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/select")
    public ResponseEntity<Iterable<AccionSimpleDTO>> getAllAccionesForSelect(HttpServletRequest request) {
        return ResponseEntity.ok(accionesService.findAllSelect());
    }

    /**
     * Obtiene acciones por ID de aplicación con paginación y búsqueda opcional.
     * 
     * @param aplicacionId ID de la aplicación por la cual filtrar las acciones.
     * @param page         Número de página (por defecto 1).
     * @param limit        Cantidad de elementos por página (por defecto 10).
     * @param request      HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de AccionResponseDTO.
     */
    @Operation(summary = "Filtrar acciones por aplicación", description = "Recupera una lista paginada de acciones filtradas por el ID de la aplicación a la que pertenecen.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de acciones filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "ID de aplicación inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Aplicación no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/by-aplicacion/{aplicacionId}")
    public ResponseEntity<Page<AccionResponseDTO>> getAccionesByAplicacionId(
            @Parameter(description = "ID de la aplicación para filtrar acciones.", example = "40eebc99-9c0b-4ef8-bb6d-6bb9bd380a10") @PathVariable UUID aplicacionId,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        // Verificar si la aplicación existe antes de buscar acciones
        aplicacionesService.findById(aplicacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Aplicación no encontrada con ID: " + aplicacionId));

        Page<Acciones> accionesPage = accionesService.findByAplicacionId(aplicacionId, page, limit);
        Page<AccionResponseDTO> responsePage = accionesPage
                .map(accion -> modelMapper.map(accion, AccionResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }

    /**
     * Obtiene acciones por ID de sección con paginación y búsqueda opcional.
     * 
     * @param seccionId ID de la sección por la cual filtrar las acciones.
     * @param page      Número de página (por defecto 1).
     * @param limit     Cantidad de elementos por página (por defecto 10).
     * @param request   HttpServletRequest para obtener la ruta de la solicitud.
     * @return ResponseEntity con una página de AccionResponseDTO.
     */
    @Operation(summary = "Filtrar acciones por sección", description = "Recupera una lista paginada de acciones filtradas por el ID de la sección a la que pertenecen.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de acciones filtrada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "ID de sección inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Sección no encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/by-seccion/{seccionId}")
    public ResponseEntity<Page<AccionResponseDTO>> getAccionesBySeccionId(
            @Parameter(description = "ID de la sección para filtrar acciones.", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID seccionId,
            @Parameter(description = "Número de página (inicia en 1).", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página.", example = "10") @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        // Verificar si la sección existe antes de buscar acciones
        seccionesService.findById(seccionId)
                .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada con ID: " + seccionId));

        Page<Acciones> accionesPage = accionesService.findBySeccionId(seccionId, page, limit);
        Page<AccionResponseDTO> responsePage = accionesPage
                .map(accion -> modelMapper.map(accion, AccionResponseDTO.class));
        return ResponseEntity.ok(responsePage);
    }
}
