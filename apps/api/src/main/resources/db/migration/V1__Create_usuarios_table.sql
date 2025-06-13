-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    estado VARCHAR(10) DEFAULT 'activo' CHECK (estado IN ('activo', 'inactivo')),
    -- Campos para Autenticación de Dos Factores (2FA) basada en TOTP
    dos_factor_activo BOOLEAN NOT NULL DEFAULT FALSE, -- Indica si el 2FA está habilitado para el usuario
    dos_factor_secreto_totp VARCHAR(255) NULL, -- Clave secreta para TOTP (debe almacenarse cifrada en la app)
    -- Campos para Seguridad de Sesión e Intentos de Inicio de Sesión
    intentos_fallidos_sesion INT NOT NULL DEFAULT 0, -- Contador de intentos fallidos de inicio de sesión
    fecha_ultimo_intento_fallido TIMESTAMP WITH TIME ZONE NULL, -- Fecha del último intento fallido
    fecha_bloqueo_sesion TIMESTAMP WITH TIME ZONE NULL, -- Si se bloquea la cuenta por intentos fallidos
    -- Campos para Gestión de Contraseñas y Políticas de Seguridad
    fecha_ultimo_cambio_contrasena TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Fecha del último cambio de contraseña (NOT NULL por el modelo)
    requiere_cambio_contrasena BOOLEAN NOT NULL DEFAULT FALSE, -- Indica si el usuario debe cambiar su contraseña en el próximo login
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE NULL DEFAULT NULL
);
