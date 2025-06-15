package com.api.api.dto.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la solicitud de creación o actualización de una sección.")
public class SeccionRequestDTO {

    @NotBlank(message = "El nombre de la sección es obligatorio")
    @Schema(description = "Nombre único de la sección.", example = "Autenticación")
    private String nombre;

    @Schema(description = "Descripción de la sección.", example = "Sección dedicada a la gestión de la autenticación de usuarios.")
    private String descripcion;
}
