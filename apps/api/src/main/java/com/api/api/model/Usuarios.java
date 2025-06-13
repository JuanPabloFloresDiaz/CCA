package com.api.api.model;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "usuarios")
@EqualsAndHashCode(callSuper = true)
public class Usuarios extends BaseEntity {

    @NotBlank(message = "El campo 'nombres' es obligatorio")
    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @NotBlank(message = "El campo 'apellidos' es obligatorio")
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @NotBlank(message = "El campo 'email' es obligatorio")
    @Email(message = "El campo 'email' debe ser una dirección válida")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "El campo 'contrasena' es obligatorio")
    @Column(name = "contrasena", nullable = false, length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;

    @NotBlank(message = "El campo 'estado' es obligatorio")
    @Column(name = "estado", nullable = false, length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'activo' CHECK (estado IN ('activo','inactivo'))")
    private String estado = "activo";

    // Autenticación de Dos Factores (2FA)
    @Column(name = "dos_factor_activo", nullable = false)
    private boolean dosFactorActivo = false;

    @Column(name = "dos_factor_secreto_totp", length = 255)
    private String dosFactorSecretoTotp;

    // Seguridad de sesión e intentos de inicio de sesión
    @Column(name = "intentos_fallidos_sesion", nullable = false)
    private int intentosFallidosSesion = 0;

    @Column(name = "fecha_ultimo_intento_fallido")
    private OffsetDateTime fechaUltimoIntentoFallido;

    @Column(name = "fecha_bloqueo_sesion")
    private OffsetDateTime fechaBloqueoSesion;

    // Gestión de contraseñas y políticas de seguridad
    @NotNull(message = "La fecha de último cambio de contraseña es obligatoria")
    @Column(name = "fecha_ultimo_cambio_contrasena", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime fechaUltimoCambioContrasena = OffsetDateTime.now();
    
    @Column(name = "requiere_cambio_contrasena", nullable = false)
    private boolean requiereCambioContrasena = false;

    @JsonManagedReference("usuario_tipo_usuario")
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuariosTipoUsuario> usuariosTipoUsuarios;

    @JsonManagedReference("sesiones_usuarios")
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sesiones> sesiones;

    @JsonManagedReference("auditoria_accesos_usuarios")
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditoriaAccesos> auditoriaAccesos;

}
