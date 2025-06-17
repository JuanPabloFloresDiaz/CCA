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
@Schema(description = "DTO para la respuesta que representa un usuario.")
public class UsuarioResponseDTO {
    @Schema(description = "ID único del usuario.", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef")
    private UUID id;
    
    @Schema(description = "Nombres del usuario.", example = "Carlos Alberto")
    private String nombres;
    
    @Schema(description = "Apellidos del usuario.", example = "González Pérez")
    private String apellidos;
    
    @Schema(description = "Correo electrónico del usuario.", example = "carlos.gonzalez@example.com")
    private String email;

    @Schema(description = "Estado del usuario (ej. 'activo', 'inactivo', 'bloqueado').", example = "activo")
    private String estado;
    
    @Schema(description = "¿Está activo el doble factor de autenticación para este usuario?", example = "true")
    private boolean dosFactorActivo;
    
    @Schema(description = "Secreto TOTP para el doble factor de autenticación (si aplica).", example = "ABCDEF1234567890")
    private String dosFactorSecretoTotp;
    
    @Schema(description = "Número de intentos fallidos de sesión.", example = "0")
    private int intentosFallidosSesion;
    
    @Schema(description = "Fecha y hora del último intento de sesión fallido.", example = "2024-06-15T10:00:00Z")
    private OffsetDateTime fechaUltimoIntentoFallido;
    
    @Schema(description = "Fecha y hora en que la sesión del usuario fue bloqueada.", example = "2024-06-15T10:15:00Z")
    private OffsetDateTime fechaBloqueoSesion;
    
    @Schema(description = "Fecha y hora del último cambio de contraseña.", example = "2024-05-01T09:00:00Z")
    private OffsetDateTime fechaUltimoCambioContrasena;
    
    @Schema(description = "¿Requiere el usuario cambiar su contraseña en el próximo inicio de sesión?", example = "false")
    private boolean requiereCambioContrasena;

    @Schema(description = "Fecha y hora de creación del registro.", example = "2024-06-15T08:00:00Z")
    private OffsetDateTime createdAt;
    
    @Schema(description = "Fecha y hora de la última actualización del registro.", example = "2024-06-15T08:30:00Z")
    private OffsetDateTime updatedAt;
    
    @Schema(description = "Fecha y hora de eliminación lógica del registro (nulo si no está eliminado).", example = "null")
    private OffsetDateTime deletedAt;
}
