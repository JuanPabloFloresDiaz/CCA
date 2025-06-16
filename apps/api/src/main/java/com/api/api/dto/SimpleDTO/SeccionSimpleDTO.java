package com.api.api.dto.SimpleDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "DTO simple para representar una sección con solo su ID y nombre.")
public interface SeccionSimpleDTO {
    @Schema(description = "ID único de la sección.", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID getId();
    @Schema(description = "Nombre de la sección.", example = "Configuración de Usuarios")
    String getNombre();
}
