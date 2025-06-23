-- Crear tabla de sesiones de usuario
CREATE TABLE IF NOT EXISTS sesiones (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Usar UUID como identificador
    token VARCHAR(255) NOT NULL UNIQUE,
    usuario_id UUID NOT NULL,
    ip_origen VARCHAR(45) NOT NULL,
    email_usuario VARCHAR(100) NOT NULL, -- Email del usuario para auditoria
    informacion_dispositivo TEXT,
    fecha_expiracion TIMESTAMP WITH TIME ZONE NOT NULL,
    fecha_inicio TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Fecha de inicio de la sesión
    fecha_fin TIMESTAMP WITH TIME ZONE NULL, -- Fecha de finalización de la sesión (para logout o expiración)
    estado VARCHAR(10) NOT NULL DEFAULT 'activa' CHECK (estado IN ('activa', 'cerrada', 'expirada')),
    -- Definir la llave foranea para relacionar con la tabla de usuarios
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    -- Campos de auditoría (heredados de BaseEntity)
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE NULL DEFAULT NULL
);
