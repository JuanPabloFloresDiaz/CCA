-- V2__Create_Indexes.sql

-- Secciones
-- Consultas comunes: buscar secciones por nombre.
CREATE INDEX IF NOT EXISTS idx_secciones_nombre ON secciones (nombre);

-- Aplicaciones
-- Consultas comunes: buscar aplicaciones por seccion_id, por llave_identificadora, por nombre.
CREATE INDEX IF NOT EXISTS idx_aplicaciones_seccion_id ON aplicaciones (seccion_id);
-- La columna llave_identificadora ya tiene un índice UNIQUE implícito.
CREATE INDEX IF NOT EXISTS idx_aplicaciones_nombre ON aplicaciones (nombre);

-- Acciones
-- Consultas comunes: buscar acciones por aplicacion_id, seccion_id, o combinaciones de ambos.
CREATE INDEX IF NOT EXISTS idx_acciones_aplicacion_id ON acciones (aplicacion_id);
CREATE INDEX IF NOT EXISTS idx_acciones_seccion_id ON acciones (seccion_id);
-- Indice compuesto para búsquedas comunes de acciones dentro de una aplicación y sección específica
CREATE INDEX IF NOT EXISTS idx_acciones_aplicacion_seccion ON acciones (aplicacion_id, seccion_id);

-- Tipo_Usuario
-- Consultas comunes: buscar tipos de usuario por aplicacion_id.
CREATE INDEX IF NOT EXISTS idx_tipo_usuario_aplicacion_id ON tipo_usuario (aplicacion_id);
-- Indice compuesto para búsquedas comunes de tipos de usuario por aplicación y estado
CREATE INDEX IF NOT EXISTS idx_tipo_usuario_aplicacion_estado ON tipo_usuario (aplicacion_id, estado);

-- Permisos_Tipo_Usuario (Tabla pivotal para la lógica de permisos)
-- Consultas muy comunes: ¿Qué permisos tiene un tipo de usuario? ¿Qué tipos de usuario tienen cierta acción?
CREATE INDEX IF NOT EXISTS idx_permisos_tipo_usuario_tipo_usuario_id ON permisos_tipo_usuario (tipo_usuario_id);
CREATE INDEX IF NOT EXISTS idx_permisos_tipo_usuario_accion_id ON permisos_tipo_usuario (accion_id);
-- Indice compuesto crucial para verificar rápidamente si un tipo de usuario tiene un permiso específico
CREATE UNIQUE INDEX IF NOT EXISTS uix_permisos_tipo_usuario_unique ON permisos_tipo_usuario (tipo_usuario_id, accion_id);

-- Usuarios
-- Consultas comunes: buscar usuarios por email, por estado.
-- La columna email ya tiene un índice UNIQUE implícito.
CREATE INDEX IF NOT EXISTS idx_usuarios_estado ON usuarios (estado);
-- Para búsquedas de usuarios por nombre/apellido (si se usa en búsquedas)
CREATE INDEX IF NOT EXISTS idx_usuarios_nombres_apellidos ON usuarios (nombres, apellidos);
-- Para buscar usuarios con 2FA activo o inactivo
CREATE INDEX IF NOT EXISTS idx_usuarios_dos_factor_activo ON usuarios (dos_factor_activo);
-- Para buscar usuarios con intentos fallidos o bloqueados
CREATE INDEX IF NOT EXISTS idx_usuarios_intentos_fallidos ON usuarios (intentos_fallidos_sesion);
CREATE INDEX IF NOT EXISTS idx_usuarios_fecha_bloqueo ON usuarios (fecha_bloqueo_sesion) WHERE fecha_bloqueo_sesion IS NOT NULL;


-- Usuarios_Tipo_Usuario (Tabla de unión)
-- Consultas comunes: ¿Qué tipos de usuario tiene un usuario? ¿Qué usuarios tienen cierto tipo de usuario?
CREATE INDEX IF NOT EXISTS idx_usuarios_tipo_usuario_usuario_id ON usuarios_tipo_usuario (usuario_id);
CREATE INDEX IF NOT EXISTS idx_usuarios_tipo_usuario_tipo_usuario_id ON usuarios_tipo_usuario (tipo_usuario_id);
-- Indice compuesto crucial para verificar rápidamente la asignación
CREATE UNIQUE INDEX IF NOT EXISTS uix_usuarios_tipo_usuario_unique ON usuarios_tipo_usuario (usuario_id, tipo_usuario_id);

-- Sesiones
-- Consultas comunes: buscar sesiones por usuario_id, por token, por estado, por expiración.
-- La columna token ya tiene un índice UNIQUE implícito.
CREATE INDEX IF NOT EXISTS idx_sesiones_usuario_id ON sesiones (usuario_id);
CREATE INDEX IF NOT EXISTS idx_sesiones_estado ON sesiones (estado);
CREATE INDEX IF NOT EXISTS idx_sesiones_fecha_expiracion ON sesiones (fecha_expiracion);

-- Auditoria_Accesos (¡Muy importante por el volumen y las consultas!)
-- Consultas comunes: Filtrar por usuario, aplicación, acción, fecha, estado, IP.
-- Al ser particionada por 'fecha', las consultas por fecha ya se benefician del partition pruning.
-- Sin embargo, los índices dentro de cada partición seguirán siendo importantes para otros filtros.
CREATE INDEX IF NOT EXISTS idx_auditoria_accesos_usuario_id ON auditoria_accesos (usuario_id);
CREATE INDEX IF NOT EXISTS idx_auditoria_accesos_aplicacion_id ON auditoria_accesos (aplicacion_id);
CREATE INDEX IF NOT EXISTS idx_auditoria_accesos_accion_id ON auditoria_accesos (accion_id);
CREATE INDEX IF NOT EXISTS idx_auditoria_accesos_estado ON auditoria_accesos (estado);
CREATE INDEX IF NOT EXISTS idx_auditoria_accesos_ip_origen ON auditoria_accesos (ip_origen);
-- Índices compuestos para consultas comunes:
-- Buscar acciones de un usuario en un rango de tiempo:
CREATE INDEX IF NOT EXISTS idx_auditoria_accesos_usuario_fecha ON auditoria_accesos (usuario_id, fecha DESC);
-- Buscar acciones de una aplicación en un rango de tiempo:
CREATE INDEX IF NOT EXISTS idx_auditoria_accesos_aplicacion_fecha ON auditoria_accesos (aplicacion_id, fecha DESC);
-- Para análisis de fallos en un periodo:
CREATE INDEX IF NOT EXISTS idx_auditoria_accesos_estado_fecha ON auditoria_accesos (estado, fecha DESC);