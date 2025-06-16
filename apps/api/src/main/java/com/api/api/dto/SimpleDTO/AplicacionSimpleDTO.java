package com.api.api.dto.SimpleDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "DTO simple para representar una aplicación con solo su ID y nombre.")
public interface AplicacionSimpleDTO {
    @Schema(description = "ID único de la aplicación.", example = "40eebc99-9c0b-4ef8-bb6d-6bb9bd380a10")
    UUID getId();
    @Schema(description = "Nombre de la aplicación.", example = "CCA Web")
    String getNombre();
}
