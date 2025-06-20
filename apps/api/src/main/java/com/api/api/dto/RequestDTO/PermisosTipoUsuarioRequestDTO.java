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
@Schema(description = "DTO para la solicitud de creación o actualización de un Permiso por Tipo de Usuario.")
public class PermisosTipoUsuarioRequestDTO {

    @NotNull(message = "El ID de la acción es obligatorio")
    @Schema(description = "ID de la acción a la que se concede permiso.", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID accionId;

    @NotNull(message = "El ID del tipo de usuario es obligatorio")
    @Schema(description = "ID del tipo de usuario al que se le asigna el permiso.", example = "5a6b7c8d-e9f0-1234-5678-90abcdef1234")
    private UUID tipoUsuarioId;
}
