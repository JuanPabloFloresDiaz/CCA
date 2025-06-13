package com.api.api.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "auditoria_accesos")
@EqualsAndHashCode(callSuper = true)
public class AuditoriaAccesos extends BaseEntity {

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne
    @JsonBackReference("auditoria_accesos_usuarios")
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_auditoria_accesos_usuario"), nullable = false)
    private Usuarios usuario;

    @NotBlank(message = "El email del usuario es obligatorio para la auditoría")
    @Email(message = "El email del usuario debe ser válido")
    @Column(name = "email_usuario", nullable = false, length = 100)
    private String emailUsuario;

    @ManyToOne
    @JsonBackReference("auditoria_accesos_aplicaciones")
    @JoinColumn(name = "aplicacion_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_auditoria_accesos_aplicacion"), nullable = false)
    private Aplicaciones aplicacion;

    @ManyToOne
    @JoinColumn(name = "accion_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_auditoria_accesos_accion"), nullable = false)
    private Acciones accion;

    @Column(name = "fecha", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime fecha = OffsetDateTime.now();

    @NotBlank(message = "La IP de origen es obligatoria")
    @Column(name = "ip_origen", nullable = false, length = 45)
    private String ipOrigen;

    @Column(name = "informacion_dispositivo", columnDefinition = "TEXT")
    private String informacionDispositivo;

    @Column(name = "mensaje", columnDefinition = "TEXT")
    private String mensaje;

    @NotBlank(message = "El estado es obligatorio")
    @Column(name = "estado", nullable = false, length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'exitoso' CHECK (estado IN ('exitoso','fallido'))")
    private String estado = "exitoso";
}