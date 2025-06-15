package com.api.api.dto.ResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime; // Importar OffsetDateTime

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO estandarizado para respuestas de error de la API.")
public class ErrorResponseDTO {

    @Schema(description = "Mensaje de error legible para el usuario.", example = "Credenciales inválidas.")
    private String error;

    @Schema(description = "Código de estado HTTP del error.", example = "400")
    private int code;

    @Schema(description = "Nombre de la clase de excepción que causó el error (para depuración).", example = "BadRequestException")
    private String exception;

    @Schema(description = "Marca de tiempo cuando ocurrió el error (formato ISO 8601).", example = "2024-06-15T12:00:00Z")
    private OffsetDateTime timestamp;

    @Schema(description = "Ruta de la solicitud que generó el error.", example = "/api/auth/login")
    private String path;
}
