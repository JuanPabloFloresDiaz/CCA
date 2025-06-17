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
@Schema(description = "DTO para la solicitud de creación o actualización de un tipo de usuario.")
public class TipoUsuarioRequestDTO {

    @NotBlank(message = "El nombre del tipo de usuario es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Schema(description = "Nombre único del tipo de usuario.", example = "Administrador")
    private String nombre;

    @Schema(description = "Descripción del tipo de usuario.", example = "Tiene acceso completo a la configuración del sistema.")
    private String descripcion;

    @NotNull(message = "El ID de la aplicación es obligatorio")
    @Schema(description = "ID de la aplicación a la que pertenece este tipo de usuario.", example = "40eebc99-9c0b-4ef8-bb6d-6bb9bd380a10")
    private UUID aplicacionId;

    @NotBlank(message = "El estado del tipo de usuario es obligatorio")
    @Schema(description = "Estado del tipo de usuario (ej. 'activo', 'inactivo').", example = "activo")
    private String estado;
}
