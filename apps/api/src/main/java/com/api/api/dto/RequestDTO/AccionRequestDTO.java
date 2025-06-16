package com.api.api.dto.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la solicitud de creación o actualización de una acción.")
public class AccionRequestDTO {

    @NotBlank(message = "El nombre de la acción es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    @Schema(description = "Nombre único de la acción dentro de una aplicación.", example = "CREATE_USER")
    private String nombre;

    @Schema(description = "Descripción de la acción.", example = "Permite la creación de nuevos usuarios en el sistema.")
    private String descripcion;

    @NotNull(message = "El ID de la aplicación es obligatorio")
    @Schema(description = "ID de la aplicación a la que pertenece esta acción.", example = "40eebc99-9c0b-4ef8-bb6d-6bb9bd380a10")
    private UUID aplicacionId;

    @NotNull(message = "El ID de la sección es obligatorio")
    @Schema(description = "ID de la sección a la que pertenece esta acción.", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID seccionId;
}
