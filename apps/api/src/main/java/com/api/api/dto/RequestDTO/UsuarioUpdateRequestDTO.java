package com.api.api.dto.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la solicitud de ACTUALIZACIÓN de un usuario (excluyendo la contraseña).")
public class UsuarioUpdateRequestDTO {

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

    @NotBlank(message = "El estado del usuario es obligatorio")
    @Schema(description = "Estado del usuario (ej. 'activo', 'inactivo', 'bloqueado').", example = "activo")
    private String estado;

    @Schema(description = "¿Está activo el doble factor de autenticación para este usuario? (Solo lectura en update)", example = "true")
    private boolean dosFactorActivo; 

    @Schema(description = "Secreto TOTP para el doble factor de autenticación (no se actualiza directamente, solo se expone para lectura).", example = "ABCDEF1234567890")
    private String dosFactorSecretoTotp; 

    @Schema(description = "Número de intentos fallidos de sesión.", example = "0")
    private int intentosFallidosSesion;

    @Schema(description = "Fecha y hora del último intento de sesión fallido.", example = "2024-06-15T10:00:00Z")
    private OffsetDateTime fechaUltimoIntentoFallido;

    @Schema(description = "Fecha y hora en que la sesión del usuario fue bloqueada.", example = "2024-06-15T10:15:00Z")
    private OffsetDateTime fechaBloqueoSesion;

    @Schema(description = "¿Requiere el usuario cambiar su contraseña en el próximo inicio de sesión?", example = "false")
    private boolean requiereCambioContrasena;
}
