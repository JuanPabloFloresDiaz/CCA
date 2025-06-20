package com.api.api.dto.ResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la respuesta que representa una sesión de usuario.")
public class SesionResponseDTO {
    @Schema(description = "ID único de la sesión.", example = "b1c2d3e4-f5a6-7890-1234-567890abcdef")
    private UUID id;

    @Schema(description = "Token JWT asociado a la sesión.", example = "eyJhbGciOiJIUzI1Ni...")
    private String token;

    @Schema(description = "ID del usuario propietario de la sesión.", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef")
    private UUID usuarioId;

    @Schema(description = "Nombres del usuario propietario de la sesión.", example = "Juan")
    private String usuarioNombres;

    @Schema(description = "Apellidos del usuario propietario de la sesión.", example = "Pérez")
    private String usuarioApellidos;

    @Schema(description = "Correo electrónico del usuario propietario de la sesión.", example = "juan.perez@example.com")
    private String emailUsuario;

    @Schema(description = "Dirección IP de origen de la sesión.", example = "192.168.1.100")
    private String ipOrigen;

    @Schema(description = "Información del dispositivo desde el que se inició la sesión (User-Agent).", example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
    private String informacionDispositivo;

    @Schema(description = "Fecha y hora de inicio de la sesión.", example = "2024-06-15T10:00:00Z")
    private OffsetDateTime fechaInicio;

    @Schema(description = "Fecha y hora de expiración del token/sesión.", example = "2024-06-15T11:00:00Z")
    private OffsetDateTime fechaExpiracion;

    @Schema(description = "Fecha y hora de finalización de la sesión (ej. por logout).", example = "2024-06-15T10:50:00Z")
    private OffsetDateTime fechaFin;

    @Schema(description = "Estado actual de la sesión (activa, cerrada, expirada).", example = "activa")
    private String estado;

    @Schema(description = "Fecha y hora de creación del registro.", example = "2024-06-15T09:59:00Z")
    private OffsetDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización del registro.", example = "2024-06-15T10:05:00Z")
    private OffsetDateTime updatedAt;

    @Schema(description = "Fecha y hora de eliminación lógica del registro (nulo si no está eliminado).", example = "null")
    private OffsetDateTime deletedAt;
}
