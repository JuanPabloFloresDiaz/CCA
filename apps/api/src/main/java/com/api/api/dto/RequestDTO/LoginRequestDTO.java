package com.api.api.dto.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data // Lombok para getters y setters
@Schema(description = "DTO para la solicitud de inicio de sesión de un usuario.")
public class LoginRequestDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Schema(description = "Dirección de correo electrónico del usuario.", example = "usuario@ejemplo.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario.", example = "MiContrasenaSegura123!")
    private String contrasena;
}