package com.api.api.dto.SimpleDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "DTO simple para representar un usuario con ID, nombres, apellidos y email.")
public interface UsuarioSimpleDTO {
    @Schema(description = "ID único del usuario.", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef")
    UUID getId();
    @Schema(description = "Nombres del usuario.", example = "Ana María")
    String getNombre();
    @Schema(description = "Apellidos del usuario.", example = "González")
    String getApellidos();
    @Schema(description = "Correo electrónico del usuario.", example = "ana.gonzalez@example.com")
    String getEmail();
}
