-- Crear tabla de tipo de usuario para las aplicaciones
CREATE TABLE IF NOT EXISTS tipo_usuario (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    aplicacion_id UUID NOT NULL,
    FOREIGN KEY (aplicacion_id) REFERENCES aplicaciones(id) ON DELETE CASCADE,
    -- Usar VARCHAR para 'estado' y un CHECK constraint para validación si no quieres un ENUM nativo
    estado VARCHAR(10) DEFAULT 'activo' CHECK (estado IN ('activo', 'inactivo')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE NULL DEFAULT NULL
);
