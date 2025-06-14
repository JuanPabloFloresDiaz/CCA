package com.api.api.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "auditoria_accesos")
public class AuditoriaAccesos { 
    @EmbeddedId
    private AuditoriaAccesosId id; 

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP WITH TIME ZONE NULL")
    private OffsetDateTime deletedAt;

    public void softDelete() {
        this.deletedAt = OffsetDateTime.now();
    }
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    @ManyToOne
    @JsonBackReference("auditoria_accesos_usuarios")
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_auditoria_accesos_usuario"), nullable = true) // nullable = true si puede ser nulo
    private Usuarios usuario;

    @NotBlank(message = "El email del usuario es obligatorio para la auditoría")
    @Email(message = "El email del usuario debe ser válido")
    @Column(name = "email_usuario", nullable = false, length = 100)
    private String emailUsuario;

    @NotNull(message = "La aplicación es obligatoria")
    @ManyToOne
    @JsonBackReference("auditoria_accesos_aplicaciones")
    @JoinColumn(name = "aplicacion_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_auditoria_accesos_aplicacion"), nullable = false)
    private Aplicaciones aplicacion;

    @NotNull(message = "La acción es obligatoria")
    @ManyToOne
    @JoinColumn(name = "accion_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_auditoria_accesos_accion"), nullable = false)
    private Acciones accion;

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

    public UUID getUuidId() {
        return this.id != null ? this.id.getId() : null;
    }

    public void setUuidId(UUID uuid) {
        if (this.id == null) {
            this.id = new AuditoriaAccesosId();
        }
        this.id.setId(uuid);
    }

    public OffsetDateTime getFecha() {
        return this.id != null ? this.id.getFecha() : null;
    }

    public void setFecha(OffsetDateTime fecha) {
        if (this.id == null) {
            this.id = new AuditoriaAccesosId();
        }
        this.id.setFecha(fecha);
    }
}