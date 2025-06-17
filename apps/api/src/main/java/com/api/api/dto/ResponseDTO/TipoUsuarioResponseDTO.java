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
@Schema(description = "DTO para la respuesta que representa un tipo de usuario.")
public class TipoUsuarioResponseDTO {
    @Schema(description = "ID único del tipo de usuario.", example = "5a6b7c8d-e9f0-1234-5678-90abcdef1234")
    private UUID id;
    
    @Schema(description = "Nombre del tipo de usuario.", example = "Administrador")
    private String nombre;
    
    @Schema(description = "Descripción del tipo de usuario.", example = "Tiene acceso completo a la configuración del sistema.")
    private String descripcion;
    
    @Schema(description = "ID de la aplicación a la que pertenece este tipo de usuario.", example = "40eebc99-9c0b-4ef8-bb6d-6bb9bd380a10")
    private UUID aplicacionId; 
    
    @Schema(description = "Nombre de la aplicación a la que pertenece este tipo de usuario.", example = "CCA Web")
    private String nombreAplicacion; 
    
    @Schema(description = "Estado del tipo de usuario (ej. 'activo', 'inactivo').", example = "activo")
    private String estado;

    @Schema(description = "Fecha y hora de creación del registro.", example = "2024-06-15T10:30:00Z")
    private OffsetDateTime createdAt;
    
    @Schema(description = "Fecha y hora de la última actualización del registro.", example = "2024-06-15T11:00:00Z")
    private OffsetDateTime updatedAt;
    
    @Schema(description = "Fecha y hora de eliminación lógica del registro (nulo si no está eliminado).", example = "null")
    private OffsetDateTime deletedAt;
}
