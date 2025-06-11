package com.api.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "tipo_usuario")
@EqualsAndHashCode(callSuper = true)
public class TipoUsuario extends BaseEntity {

    @NotBlank(message = "El campo 'nombre' es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", nullable = true)
    private String descripcion;

    @NotNull(message = "La Aplicación es obligatoria")
    @ManyToOne
    @JsonBackReference("aplicacion_tipo_usuario")
    @JoinColumn(name = "aplicacion_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_tipo_usuario_aplicacion"), nullable = false)
    private Aplicaciones aplicacion;

    @NotBlank(message = "El campo 'estado' es obligatorio")
    @Column(name = "estado", nullable = false, length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'activo' CHECK (estado IN ('activo', 'inactivo'))")
    private String estado = "activo";

    @JsonManagedReference("permisos_tipo_usuario_tipo_usuario")
    @OneToMany(mappedBy = "permiso_tipo_usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PermisosTipoUsuario> permisosTipoUsuarios;

    @JsonManagedReference("tipo_usuario_usuarios")
    @OneToMany(mappedBy = "tipoUsuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuariosTipoUsuario> usuariosTipoUsuarios;
}
