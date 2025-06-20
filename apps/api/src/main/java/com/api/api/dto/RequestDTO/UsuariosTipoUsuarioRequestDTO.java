package com.api.api.dto.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la solicitud de creación o actualización de una relación Usuario-TipoUsuario.")
public class UsuariosTipoUsuarioRequestDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    @Schema(description = "ID del usuario al que se le asigna el tipo de usuario.", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef")
    private UUID usuarioId;

    @NotNull(message = "El ID de tipo de usuario es obligatorio")
    @Schema(description = "ID del tipo de usuario que se asigna al usuario.", example = "5a6b7c8d-e9f0-1234-5678-90abcdef1234")
    private UUID tipoUsuarioId;
}
