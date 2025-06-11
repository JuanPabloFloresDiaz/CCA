package com.api.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "aplicaciones")
@EqualsAndHashCode(callSuper = true)
public class Aplicaciones extends BaseEntity {
    @NotBlank(message = "El campo 'nombre' es obligatorio")
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "descripcion", nullable = true)
    private String descripcion;

    @NotBlank(message = "El campo 'url' es obligatorio")
    @Column(name = "url", nullable = false, length = 255)
    private String url;

    @NotBlank(message = "El campo 'llave_identificadora' es obligatorio")
    @Column(name = "llave_identificadora", nullable = false, unique = true, length = 100)
    private String llaveIdentificadora;

    @JsonManagedReference("aplicacion_acciones")
    @OneToMany(mappedBy = "aplicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Acciones> acciones;

    @JsonManagedReference("aplicacion_tipo_usuario")
    @OneToMany(mappedBy = "aplicaciones", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TipoUsuario> tipoUsuarios;

    @JsonManagedReference("auditoria_accesos_aplicaciones")
    @OneToMany(mappedBy = "auditoria_aplicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditoriaAccesos> AuditoriaAccesos;
    
}
