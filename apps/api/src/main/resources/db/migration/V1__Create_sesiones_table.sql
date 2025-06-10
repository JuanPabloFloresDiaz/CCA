-- Crear tabla de sesiones de usuario
CREATE TABLE IF NOT EXISTS sesiones (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token VARCHAR(255) NOT NULL UNIQUE,
    usuario_id UUID NOT NULL,
    ip_origen VARCHAR(45) NOT NULL,
    email_usuario VARCHAR(100) NOT NULL, -- Email del usuario para auditoria
    informacion_dispositivo TEXT,
    fecha_expiracion TIMESTAMP WITH TIME ZONE NOT NULL,
    estado VARCHAR(10) DEFAULT 'activa' CHECK (estado IN ('activa', 'cerrada', 'expirada')),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE NULL DEFAULT NULL
);