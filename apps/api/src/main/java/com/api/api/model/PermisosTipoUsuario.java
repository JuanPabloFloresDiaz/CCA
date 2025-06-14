package com.api.api.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "permisos_tipo_usuario")
@EqualsAndHashCode(callSuper = true)
public class PermisosTipoUsuario extends BaseEntity {

    @NotNull(message = "El Permiso de acción es obligatorio")
    @ManyToOne
    @JsonBackReference("permisos_tipo_usuario_acciones")
    @JoinColumn(name = "accion_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_permisos_tipo_usuario_acciones"), nullable = false)
    private Acciones accion;

    @NotNull(message = "El Tipo de Usuario es obligatorio")
    @ManyToOne
    @JsonBackReference("permisos_tipo_usuario_tipo_usuario")
    @JoinColumn(name = "tipo_usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_permisos_tipo_usuario_tipo_usuario"), nullable = false)
    private TipoUsuario tipoUsuario;

}
