package com.api.api.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "sesiones")
@EqualsAndHashCode(callSuper = true)
public class Sesiones extends BaseEntity {

    @NotBlank(message = "El token es obligatorio")
    @Column(name = "token", nullable = false, unique = true, length = 255)
    private String token;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne
    @JsonBackReference("sesiones_usuarios")
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_sesiones_usuario"), nullable = false)
    private Usuarios usuario;

    @NotBlank(message = "La IP de origen es obligatoria")
    @Column(name = "ip_origen", nullable = false, length = 45)
    private String ipOrigen;

    @NotBlank(message = "El email del usuario es obligatorio")
    @Email(message = "El email debe ser válido")
    @Column(name = "email_usuario", nullable = false, length = 100)
    private String emailUsuario;

    @Column(name = "informacion_dispositivo", columnDefinition = "TEXT")
    private String informacionDispositivo;

    @NotNull(message = "La fecha de expiración es obligatoria")
    @Column(name = "fecha_expiracion", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime fechaExpiracion;

    @Column(name = "fecha_inicio", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime fechaInicio = OffsetDateTime.now(); // Campo para el inicio de la sesión

    @Column(name = "fecha_fin", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime fechaFin; // Campo para el fin de la sesión (para logout/revocación)

    @NotBlank(message = "El estado es obligatorio")
    @Column(name = "estado", nullable = false, length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'activa' CHECK (estado IN ('activa','cerrada','expirada'))")
    private String estado = "activa";
}