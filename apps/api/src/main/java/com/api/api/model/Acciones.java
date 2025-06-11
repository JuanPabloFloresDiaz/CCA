package com.api.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "acciones")
@EqualsAndHashCode(callSuper = true)
public class Acciones extends BaseEntity {

    @NotBlank(message = "El campo 'nombre' es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", nullable = true)
    private String descripcion;

    @NotNull(message = "La Aplicación es obligatoria")
    @ManyToOne
    @JsonBackReference("aplicacion_acciones")
    @JoinColumn(name = "aplicacion_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_acciones_aplicacion"), nullable = true)
    private Aplicaciones aplicacion;

    @NotNull(message = "La Sección es obligatoria")
    @ManyToOne
    @JsonBackReference("seccion_acciones")
    @JoinColumn(name = "seccion_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_acciones_seccion"), nullable = true)
    private Secciones seccion;

    @JsonManagedReference("permisos_tipo_usuario_acciones")
    @OneToMany(mappedBy = "permiso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PermisosTipoUsuario> permisosTipoUsuarios;
}
