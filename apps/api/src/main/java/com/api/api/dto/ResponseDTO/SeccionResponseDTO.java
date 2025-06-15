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
@Schema(description = "DTO para la respuesta que representa una sección.")
public class SeccionResponseDTO {
    @Schema(description = "ID único de la sección.", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    
    @Schema(description = "Nombre de la sección.", example = "Autenticación")
    private String nombre;
    
    @Schema(description = "Descripción de la sección.", example = "Sección dedicada a la gestión de la autenticación de usuarios.")
    private String descripcion;
    
    @Schema(description = "Fecha y hora de creación del registro.", example = "2024-06-15T10:30:00Z")
    private OffsetDateTime createdAt;
    
    @Schema(description = "Fecha y hora de la última actualización del registro.", example = "2024-06-15T11:00:00Z")
    private OffsetDateTime updatedAt;
    
    @Schema(description = "Fecha y hora de eliminación lógica del registro (nulo si no está eliminado).", example = "null")
    private OffsetDateTime deletedAt;
}
