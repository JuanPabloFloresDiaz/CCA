-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    estado VARCHAR(10) DEFAULT 'activo' CHECK (estado IN ('activo', 'inactivo')),
    -- Campos para Autenticación de Dos Factores (2FA) basada en TOTP
    dos_factor_activo BOOLEAN DEFAULT FALSE, -- Indica si el 2FA está habilitado para el usuario
    dos_factor_secreto_totp VARCHAR(255) NULL, -- Clave secreta para TOTP (Time-based One-Time Password)
    -- Campos para Seguridad de Sesión e Intentos de Inicio de Sesión
    intentos_fallidos_sesion INT DEFAULT 0, -- Contador de intentos fallidos de inicio de sesión
    fecha_ultimo_intento_fallido TIMESTAMP WITH TIME ZONE NULL, -- Fecha del último intento fallido
    fecha_bloqueo_sesion TIMESTAMP WITH TIME ZONE NULL, -- Si se bloquea la cuenta por intentos fallidos
    -- Campos para Gestión de Contraseñas y Políticas de Seguridad
    fecha_ultimo_cambio_contrasena TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- Fecha del último cambio de contraseña
    requiere_cambio_contrasena BOOLEAN DEFAULT FALSE, -- Indica si el usuario debe cambiar su contraseña en el próximo login
    -- Campos de auditoría
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE NULL DEFAULT NULL
);