package com.api.api.dto.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.api.api.validation.StrongPassword;

/**
 * DTO para la solicitud de cambio de contraseña.
 * Contiene los campos necesarios para cambiar la contraseña del usuario.
 */
@Schema(description = "DTO para la solicitud de cambio de contraseña del usuario.")
public class PasswordChangeRequestDTO {
    @Schema(description = "Contraseña actual del usuario.", example = "MiContraseñaActual123")    
    @NotBlank(message = "La contraseña actual no puede estar vacía")
    private String currentPassword;

    @Schema(description = "Nueva contraseña del usuario.", example = "MiNuevaContraseñaSegura123")
    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    @StrongPassword
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
