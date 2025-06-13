-- Crear tabla de auditoria de accesos
CREATE TABLE IF NOT EXISTS auditoria_accesos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Usar UUID como identificador
    usuario_id UUID NULL, -- Puede ser nulo si el usuario no existe (ej. intentos de login fallidos)
    email_usuario VARCHAR(100) NOT NULL, -- Email del usuario (siempre presente para auditoría)
    aplicacion_id UUID NOT NULL,
    accion_id UUID NOT NULL,
    fecha TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_origen VARCHAR(45) NOT NULL,
    informacion_dispositivo TEXT, -- Información del dispositivo del cliente
    mensaje TEXT, -- Mensaje descriptivo del evento
    estado VARCHAR(10) NOT NULL DEFAULT 'exitoso' CHECK (estado IN ('exitoso', 'fallido')),
    -- Definir las llaves foraneas para relacionar con las tablas de usuarios, aplicaciones y acciones
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL, -- SET NULL si usuario_id puede ser null
    FOREIGN KEY (aplicacion_id) REFERENCES aplicaciones(id) ON DELETE CASCADE,
    FOREIGN KEY (accion_id) REFERENCES acciones(id) ON DELETE CASCADE,
    -- Campos de auditoría (heredados de BaseEntity)
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE NULL DEFAULT NULL
) PARTITION BY RANGE (fecha);
