package com.api.api.dto.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la solicitud de creación o actualización de una aplicación.")
public class AplicacionRequestDTO {

    @NotBlank(message = "El nombre de la aplicación es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Schema(description = "Nombre de la aplicación.", example = "Centro de Control de Acceso")
    private String nombre;

    @Schema(description = "Descripción detallada de la aplicación.", example = "Sistema centralizado de autenticación, autorización y auditoría.")
    private String descripcion;

    @NotBlank(message = "La URL de la aplicación es obligatoria")
    @Pattern(regexp = "^(http|https)://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}(/.*)?$", message = "La URL debe ser válida (ej. http://ejemplo.com o https://ejemplo.com/path)")
    @Size(max = 255, message = "La URL no puede exceder los 255 caracteres")
    @Schema(description = "URL base de la aplicación (frontend o API).", example = "http://localhost:3000")
    private String url;

    @NotBlank(message = "La llave identificadora es obligatoria")
    @Size(max = 100, message = "La llave identificadora no puede exceder los 100 caracteres")
    @Schema(description = "Llave única para identificar la aplicación en el sistema (usada por el backend del CCA).", example = "CCA_AUTH_SERVICE")
    private String llaveIdentificadora;
}
