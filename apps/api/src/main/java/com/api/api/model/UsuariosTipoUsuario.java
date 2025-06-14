package com.api.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "usuarios_tipo_usuario")
@EqualsAndHashCode(callSuper = true)
public class UsuariosTipoUsuario extends BaseEntity{
    
    @NotNull(message = "El Usuario es obligatorio")
    @ManyToOne
    @JsonBackReference("usuario_tipo_usuario")
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuarios_tipo_usuario_usuario"), nullable = false)
    private Usuarios usuario;

    @NotNull(message = "El Tipo de Usuario es obligatorio")
    @ManyToOne
    @JsonBackReference("tipo_usuario_usuarios")
    @JoinColumn(name = "tipo_usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuarios_tipo_usuario_tipo_usuario"), nullable = false)
    private TipoUsuario tipoUsuario;
}
