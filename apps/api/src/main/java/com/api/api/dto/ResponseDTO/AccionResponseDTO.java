package com.api.api.dto.ResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la respuesta que representa una acción.")
public class AccionResponseDTO {
    @Schema(description = "ID único de la acción.", example = "a2b3c4d5-e6f7-8901-2345-67890abcdef1")
    private UUID id;
    
    @Schema(description = "Nombre de la acción.", example = "CREATE_USER")
    private String nombre;
    
    @Schema(description = "Descripción de la acción.", example = "Permite la creación de nuevos usuarios en el sistema.")
    private String descripcion;
    
    @Schema(description = "ID de la aplicación a la que pertenece esta acción.", example = "40eebc99-9c0b-4ef8-bb6d-6bb9bd380a10")
    private UUID aplicacionId;
    
    @Schema(description = "Nombre de la aplicación a la que pertenece esta acción.", example = "CCA_AUTH_SERVICE")
    private String nombreAplicacion;

    @Schema(description = "ID de la sección a la que pertenece esta acción.", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID seccionId;
    
    @Schema(description = "Nombre de la sección a la que pertenece esta acción.", example = "Usuarios")
    private String nombreSeccion;

    @Schema(description = "Fecha y hora de creación del registro.", example = "2024-06-15T10:30:00Z")
    private OffsetDateTime createdAt;
    
    @Schema(description = "Fecha y hora de la última actualización del registro.", example = "2024-06-15T11:00:00Z")
    private OffsetDateTime updatedAt;
    
    @Schema(description = "Fecha y hora de eliminación lógica del registro (nulo si no está eliminado).", example = "null")
    private OffsetDateTime deletedAt;
}
