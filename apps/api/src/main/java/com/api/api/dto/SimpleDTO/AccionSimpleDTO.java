package com.api.api.dto.SimpleDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "DTO simple para representar una acción con solo su ID y nombre.")
public interface AccionSimpleDTO {
    @Schema(description = "ID único de la acción.", example = "a2b3c4d5-e6f7-8901-2345-67890abcdef1")
    UUID getId();
    @Schema(description = "Nombre de la acción.", example = "VIEW_DASHBOARD")
    String getNombre();
}
