-- Crear tabla de las acciones que se podran realizar en las aplicaciones
CREATE TABLE IF NOT EXISTS acciones (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    aplicacion_id UUID NOT NULL, -- Corregido a UUID
    seccion_id UUID NOT NULL,   -- Corregido a UUID
    FOREIGN KEY (aplicacion_id) REFERENCES aplicaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (seccion_id) REFERENCES secciones(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE NULL DEFAULT NULL
);