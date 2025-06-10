-- Crear tabla para las aplicaciones
CREATE TABLE IF NOT EXISTS aplicaciones (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    url VARCHAR(255) NOT NULL,
    llave_identificadora VARCHAR(100) NOT NULL UNIQUE,
    seccion_id UUID, -- Columna agregada para la FK
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE NULL DEFAULT NULL,
    FOREIGN KEY (seccion_id) REFERENCES secciones(id) ON DELETE SET NULL
);