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
@Schema(description = "DTO para la respuesta que representa un registro de auditoría de acceso.")
public class AuditoriaAccesoResponseDTO {

    @Schema(description = "ID único del registro de auditoría (parte del ID compuesto).", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID uuidId;

    @Schema(description = "Fecha y hora exacta del registro (parte del ID compuesto).", example = "2024-06-15T14:30:00Z")
    private OffsetDateTime fecha;

    @Schema(description = "ID del usuario que realizó la acción (puede ser nulo si la acción no está asociada a un usuario logueado).", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef", nullable = true)
    private UUID usuarioId;

    @Schema(description = "Nombres del usuario que realizó la acción.", example = "Nombre Usuario")
    private String usuarioNombres;

    @Schema(description = "Apellidos del usuario que realizó la acción.", example = "Apellido Usuario")
    private String usuarioApellidos;

    @Schema(description = "Email del usuario que realizó la acción.", example = "usuario@example.com")
    private String emailUsuario;

    @Schema(description = "ID de la aplicación donde ocurrió la acción.", example = "f1e2d3c4-b5a6-7890-1234-567890fedcba")
    private UUID aplicacionId;

    @Schema(description = "Nombre de la aplicación donde ocurrió la acción.", example = "Sistema de Gestión")
    private String aplicacionNombre;

    @Schema(description = "ID de la acción realizada.", example = "09876543-21ab-cdef-1234-567890abcdef")
    private UUID accionId;

    @Schema(description = "Nombre de la acción realizada.", example = "LOGIN_EXITOSO")
    private String accionNombre;

    @Schema(description = "Descripción de la acción realizada.", example = "Inicio de sesión exitoso del usuario.")
    private String accionDescripcion;

    @Schema(description = "Dirección IP desde donde se realizó la acción.", example = "192.168.1.1")
    private String ipOrigen;

    @Schema(description = "Información del dispositivo que realizó la acción (User-Agent).", example = "Mozilla/5.0 (Windows NT 10.0)")
    private String informacionDispositivo;

    @Schema(description = "Mensaje detallado del evento de auditoría.", example = "Inicio de sesión exitoso desde IP 192.168.1.1")
    private String mensaje;

    @Schema(description = "Estado del resultado de la acción (exitoso, fallido).", example = "exitoso")
    private String estado;

    @Schema(description = "Fecha y hora de creación del registro.", example = "2024-06-15T14:29:55Z")
    private OffsetDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización del registro.", example = "2024-06-15T14:30:00Z")
    private OffsetDateTime updatedAt;

    @Schema(description = "Fecha y hora de eliminación lógica del registro (nulo si no está eliminado).", example = "null")
    private OffsetDateTime deletedAt;
}
