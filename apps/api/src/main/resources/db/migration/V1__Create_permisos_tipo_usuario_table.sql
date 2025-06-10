-- Crear tabla de detalle de permisos por tipo de usuario
CREATE TABLE IF NOT EXISTS permisos_tipo_usuario (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tipo_usuario_id UUID NOT NULL,
    accion_id UUID NOT NULL,
    FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (accion_id) REFERENCES acciones(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE NULL DEFAULT NULL
);