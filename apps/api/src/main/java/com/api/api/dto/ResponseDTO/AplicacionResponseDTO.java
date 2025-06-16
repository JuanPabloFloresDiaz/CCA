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
@Schema(description = "DTO para la respuesta que representa una aplicación.")
public class AplicacionResponseDTO {
    @Schema(description = "ID único de la aplicación.", example = "40eebc99-9c0b-4ef8-bb6d-6bb9bd380a10")
    private UUID id;
    
    @Schema(description = "Nombre de la aplicación.", example = "Centro de Control de Acceso")
    private String nombre;
    
    @Schema(description = "Descripción detallada de la aplicación.", example = "Sistema centralizado de autenticación, autorización y auditoría.")
    private String descripcion;
    
    @Schema(description = "URL base de la aplicación (frontend o API).", example = "http://localhost:3000")
    private String url;
    
    @Schema(description = "Llave única para identificar la aplicación en el sistema.", example = "CCA_AUTH_SERVICE")
    private String llaveIdentificadora;

    @Schema(description = "Estado de la aplicación (activo/inactivo).", example = "activo")
    private String estado;
    
    @Schema(description = "Fecha y hora de creación del registro.", example = "2024-06-15T10:30:00Z")
    private OffsetDateTime createdAt;
    
    @Schema(description = "Fecha y hora de la última actualización del registro.", example = "2024-06-15T11:00:00Z")
    private OffsetDateTime updatedAt;
    
    @Schema(description = "Fecha y hora de eliminación lógica del registro (nulo si no está eliminado).", example = "null")
    private OffsetDateTime deletedAt;
}
