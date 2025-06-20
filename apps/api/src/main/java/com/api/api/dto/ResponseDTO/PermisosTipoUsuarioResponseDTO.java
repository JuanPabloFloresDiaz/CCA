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
@Schema(description = "DTO para la respuesta que representa un Permiso por Tipo de Usuario.")
public class PermisosTipoUsuarioResponseDTO {
    @Schema(description = "ID único del permiso de tipo de usuario.", example = "0f1a2b3c-4d5e-6f7a-8b9c-0d1e2f3a4b5c")
    private UUID id;

    @Schema(description = "ID de la acción asociada al permiso.", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID accionId;

    @Schema(description = "Nombre de la acción asociada al permiso.", example = "CREAR_USUARIO")
    private String accionNombre;

    @Schema(description = "Descripción de la acción asociada al permiso.", example = "Permite la creación de nuevos usuarios en el sistema.")
    private String accionDescripcion;

    @Schema(description = "ID del tipo de usuario asociado al permiso.", example = "5a6b7c8d-e9f0-1234-5678-90abcdef1234")
    private UUID tipoUsuarioId;

    @Schema(description = "Nombre del tipo de usuario asociado al permiso.", example = "ADMINISTRADOR")
    private String tipoUsuarioNombre;

    @Schema(description = "Descripción del tipo de usuario asociado al permiso.", example = "Acceso completo a la administración del sistema.")
    private String tipoUsuarioDescripcion;

    @Schema(description = "Fecha y hora de creación del registro.", example = "2024-06-15T10:30:00Z")
    private OffsetDateTime createdAt;
    
    @Schema(description = "Fecha y hora de la última actualización del registro.", example = "2024-06-15T11:00:00Z")
    private OffsetDateTime updatedAt;
    
    @Schema(description = "Fecha y hora de eliminación lógica del registro (nulo si no está eliminado).", example = "null")
    private OffsetDateTime deletedAt;
}
