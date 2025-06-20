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
@Schema(description = "DTO para la respuesta que representa una relación Usuario-TipoUsuario.")
public class UsuariosTipoUsuarioResponseDTO {
    @Schema(description = "ID único de la relación Usuario-TipoUsuario.", example = "1a2b3c4d-5e6f-7890-abcd-ef1234567890")
    private UUID id;

    @Schema(description = "ID del usuario relacionado.", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef")
    private UUID usuarioId;

    @Schema(description = "Nombres del usuario relacionado.", example = "Carlos Alberto")
    private String usuarioNombres;

    @Schema(description = "Apellidos del usuario relacionado.", example = "González Pérez")
    private String usuarioApellidos;

    @Schema(description = "Email del usuario relacionado.", example = "carlos.gonzalez@example.com")
    private String usuarioEmail;

    @Schema(description = "ID del tipo de usuario relacionado.", example = "5a6b7c8d-e9f0-1234-5678-90abcdef1234")
    private UUID tipoUsuarioId;

    @Schema(description = "Nombre del tipo de usuario relacionado.", example = "Administrador")
    private String tipoUsuarioNombre;

    @Schema(description = "Descripción del tipo de usuario relacionado.", example = "Tiene acceso completo al sistema.")
    private String tipoUsuarioDescripcion;

    @Schema(description = "Fecha y hora de creación del registro.", example = "2024-06-15T10:30:00Z")
    private OffsetDateTime createdAt;
    
    @Schema(description = "Fecha y hora de la última actualización del registro.", example = "2024-06-15T11:00:00Z")
    private OffsetDateTime updatedAt;
    
    @Schema(description = "Fecha y hora de eliminación lógica del registro (nulo si no está eliminado).", example = "null")
    private OffsetDateTime deletedAt;
}
