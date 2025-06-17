package com.api.api.dto.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.api.api.validation.StrongPassword; 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la solicitud de CREACIÓN de un nuevo usuario.")
public class UsuarioCreateRequestDTO {

    @NotBlank(message = "El campo 'nombres' es obligatorio")
    @Size(max = 100, message = "Los nombres no pueden exceder los 100 caracteres")
    @Schema(description = "Nombres del usuario.", example = "Carlos Alberto")
    private String nombres;

    @NotBlank(message = "El campo 'apellidos' es obligatorio")
    @Size(max = 100, message = "Los apellidos no pueden exceder los 100 caracteres")
    @Schema(description = "Apellidos del usuario.", example = "González Pérez")
    private String apellidos;

    @NotBlank(message = "El campo 'email' es obligatorio")
    @Email(message = "El email debe ser una dirección válida")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    @Schema(description = "Correo electrónico único del usuario.", example = "carlos.gonzalez@example.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria para la creación")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @StrongPassword 
    @Schema(description = "Contraseña inicial del usuario. Debe cumplir con los requisitos de seguridad.", example = "PasswordSeguro123!")
    private String contrasena; 

    @NotBlank(message = "El estado del usuario es obligatorio")
    @Schema(description = "Estado inicial del usuario (ej. 'activo', 'inactivo').", example = "activo")
    private String estado;

    @Schema(description = "¿Está activo el doble factor de autenticación para este usuario?", example = "false")
    private boolean dosFactorActivo = false;
}
