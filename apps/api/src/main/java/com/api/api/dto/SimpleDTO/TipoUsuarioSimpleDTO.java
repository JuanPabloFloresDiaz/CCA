package com.api.api.dto.SimpleDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "DTO simple para representar un tipo de usuario con solo su ID y nombre.")
public interface TipoUsuarioSimpleDTO {
    @Schema(description = "ID único del tipo de usuario.", example = "5a6b7c8d-e9f0-1234-5678-90abcdef1234")
    UUID getId();
    @Schema(description = "Nombre del tipo de usuario.", example = "Administrador Global")
    String getNombre();
}
