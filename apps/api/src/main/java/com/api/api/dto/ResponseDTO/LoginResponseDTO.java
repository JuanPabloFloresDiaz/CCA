package com.api.api.dto.ResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; 
import lombok.Data; 
import lombok.NoArgsConstructor; 
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la respuesta del inicio de sesión, incluyendo el token JWT y datos del usuario.")
public class LoginResponseDTO {
    @Schema(description = "ID único del usuario autenticado.", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID id;
    @Schema(description = "Nombres del usuario autenticado.", example = "Juan")
    private String nombres;
    @Schema(description = "Apellidos del usuario autenticado.", example = "Pérez")
    private String apellidos;
    @Schema(description = "Correo electrónico del usuario autenticado.", example = "juan.perez@ejemplo.com")
    private String email;
    @Schema(description = "Token JWT para la autenticación en futuras solicitudes.", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String token; 
}
