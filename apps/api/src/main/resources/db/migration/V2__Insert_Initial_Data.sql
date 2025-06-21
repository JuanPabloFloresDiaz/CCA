-- V2__Insert_Initial_Data.sql

-- Este script inicializa secciones, la aplicación principal, el tipo de usuario
-- "Super Admin", un usuario administrador, y todas las acciones y permisos
-- asociados para el sistema de Control de Acceso.

-- 1. Insertar Secciones y capturar sus IDs
WITH inserted_secciones AS (
    INSERT INTO secciones (nombre, descripcion, created_at, updated_at, deleted_at) VALUES
    ('Autenticación', 'Sección dedicada a las funcionalidades de autenticación y seguridad de usuarios.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Cambiar Contraseña', 'Sección para la gestión del cambio de contraseña de los usuarios.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Dashboard', 'Sección de visualización general y métricas del sistema.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Gestión de Secciones', 'Sección para la administración de las diferentes agrupaciones funcionales del sistema.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Gestión de Aplicaciones', 'Sección para la administración de las aplicaciones registradas en el sistema.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Gestión de Acciones', 'Sección para la administración de las acciones que los usuarios pueden realizar dentro de las aplicaciones.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Gestión de Tipos de Usuarios', 'Sección para la administración de roles y perfiles de usuario.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Gestión de Permisos', 'Sección para la administración de las asignaciones de acciones a los tipos de usuario.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Gestión de Usuarios', 'Sección para la administración de cuentas de usuario del sistema.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Gestión de Sesiones', 'Sección para la visualización y gestión de las sesiones activas de los usuarios.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Auditoría de Accesos', 'Sección para la consulta de los registros detallados de actividad y accesos al sistema.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Envío de Correos', 'Sección para la funcionalidad de envío de notificaciones y comunicaciones por correo electrónico.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Reportes', 'Sección para la generación y consulta de reportes del sistema.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Gráficos', 'Sección para la visualización de datos estadísticos mediante gráficos.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL)
    RETURNING id, nombre
),
-- 2. Insertar la aplicación "Centro de Control de Acceso" y capturar su ID
inserted_app AS (
    INSERT INTO aplicaciones (nombre, descripcion, url, llave_identificadora, created_at, updated_at, deleted_at, estado) VALUES
    (
        'Centro de Control de Acceso',
        'Sistema centralizado de autenticación, autorización y auditoría.',
        'http://localhost:3000/',
        'CCA_AUTH_SERVICE',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, 'activo'
    )
    RETURNING id
),
-- 3. Insertar el Tipo de Usuario "Super Admin" y capturar su ID
inserted_tipo_usuario AS (
    INSERT INTO tipo_usuario (aplicacion_id, nombre, descripcion, estado, created_at, updated_at, deleted_at) VALUES
    (
        (SELECT id FROM inserted_app),
        'Super Admin',
        'Tiene control total sobre todas las funcionalidades y configuraciones del sistema.',
        'activo',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL
    )
    RETURNING id
),
-- 4. Insertar el Usuario inicial "admin_dev@gmail.com"
inserted_user AS (
    INSERT INTO usuarios (
        nombres,
        apellidos,
        email,
        contrasena, -- Contraseña hasheada para '6n35s4#Pjf'r4t' (PasswordEncoder BCrypt)
        estado,
        dos_factor_activo,
        intentos_fallidos_sesion,
        fecha_ultimo_cambio_contrasena,
        requiere_cambio_contrasena,
        created_at,
        updated_at,
        deleted_at
    ) VALUES (
        'Admin',
        'Developer',
        'admin_dev@gmail.com',
        '$2a$10$iasjl.5Iv29cdhF50UFoHeTnVRmCmit9WBu5dUnNCvPC.zYh5zFQ.',
        'activo',
        FALSE,
        0,
        CURRENT_TIMESTAMP,
        FALSE,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        NULL
    )
    RETURNING id
),
-- 5. Asignar el Tipo de Usuario "Super Admin" al usuario inicial
inserted_usuarios_tipo_usuario AS (
    INSERT INTO usuarios_tipo_usuario (usuario_id, tipo_usuario_id, created_at, updated_at, deleted_at) VALUES
    (
        (SELECT id FROM inserted_user),
        (SELECT id FROM inserted_tipo_usuario),
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL
    )
    RETURNING id
),
-- 6. Insertar Acciones para cada sección y capturar sus IDs
inserted_acciones AS (
    INSERT INTO acciones (aplicacion_id, seccion_id, nombre, descripcion, created_at, updated_at, deleted_at) VALUES
    -- Autenticación
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Autenticación'), 'ACCESO_LOGIN', 'Permite el acceso al formulario de login del sistema.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Autenticación'), 'INICIO_SESION_EXITOSO', 'Indica un inicio de sesión de usuario exitoso.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Autenticación'), 'INICIO_SESION_FALLIDO', 'Registra un intento fallido de inicio de sesión.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Autenticación'), 'CIERRE_SESION', 'Registra el cierre de sesión de un usuario.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Cambiar Contraseña
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Cambiar Contraseña'), 'ACCESO_CAMBIO_CONTRASENA', 'Permite el acceso a la página de cambio de contraseña.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Cambiar Contraseña'), 'CAMBIO_CONTRASENA', 'Registra el cambio de contraseña de un usuario.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Dashboard
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Dashboard'), 'ACCESO_DASHBOARD', 'Permite el acceso a la vista principal del dashboard.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Dashboard'), 'VISUALIZACION_METRICAS_DASHBOARD', 'Permite visualizar las métricas y resúmenes del dashboard.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Gestión de Secciones
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Secciones'), 'ACCESO_GESTION_SECCIONES', 'Permite el acceso al módulo de administración de secciones.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Secciones'), 'BUSQUEDA_SECCIONES', 'Permite buscar y listar secciones.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Secciones'), 'CREACION_SECCION', 'Permite crear nuevas secciones.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Secciones'), 'ACTUALIZACION_SECCION', 'Permite modificar secciones existentes.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Secciones'), 'ELIMINACION_LOGICA_SECCION', 'Permite inhabilitar secciones lógicamente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Secciones'), 'ELIMINACION_DEFINITIVA_SECCION', 'Permite eliminar secciones permanentemente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Secciones'), 'BUSQUEDA_SECCIONES_SIMPLE', 'Permite obtener secciones en formato simplificado para selectores.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Gestión de Aplicaciones
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Aplicaciones'), 'ACCESO_GESTION_APLICACIONES', 'Permite el acceso al módulo de administración de aplicaciones.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Aplicaciones'), 'BUSQUEDA_APLICACIONES', 'Permite buscar y listar aplicaciones.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Aplicaciones'), 'CREACION_APLICACION', 'Permite crear nuevas aplicaciones.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Aplicaciones'), 'ACTUALIZACION_APLICACION', 'Permite modificar aplicaciones existentes.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Aplicaciones'), 'ELIMINACION_LOGICA_APLICACION', 'Permite inhabilitar aplicaciones lógicamente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Aplicaciones'), 'ELIMINACION_DEFINITIVA_APLICACION', 'Permite eliminar aplicaciones permanentemente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Aplicaciones'), 'FILTRADO_APLICACIONES_POR_ESTADO', 'Permite filtrar aplicaciones por su estado (activo/inactivo).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Aplicaciones'), 'BUSQUEDA_APLICACIONES_SIMPLE', 'Permite obtener aplicaciones en formato simplificado para selectores.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Gestión de Acciones
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Acciones'), 'ACCESO_GESTION_ACCIONES', 'Permite el acceso al módulo de administración de acciones.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Acciones'), 'BUSQUEDA_ACCIONES', 'Permite buscar y listar acciones.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Acciones'), 'CREACION_ACCION', 'Permite crear nuevas acciones.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Acciones'), 'ACTUALIZACION_ACCION', 'Permite modificar acciones existentes.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Acciones'), 'ELIMINACION_LOGICA_ACCION', 'Permite inhabilitar acciones lógicamente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Acciones'), 'ELIMINACION_DEFINITIVA_ACCION', 'Permite eliminar acciones permanentemente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Acciones'), 'FILTRADO_ACCIONES_POR_APLICACION', 'Permite filtrar acciones por la aplicación a la que pertenecen.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Acciones'), 'FILTRADO_ACCIONES_POR_SECCION', 'Permite filtrar acciones por la sección a la que pertenecen.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Acciones'), 'BUSQUEDA_ACCIONES_SIMPLE', 'Permite obtener acciones en formato simplificado para selectores.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Gestión de Tipos de Usuarios
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Tipos de Usuarios'), 'ACCESO_GESTION_TIPOS_USUARIOS', 'Permite el acceso al módulo de administración de tipos de usuarios.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Tipos de Usuarios'), 'BUSQUEDA_TIPOS_USUARIOS', 'Permite buscar y listar tipos de usuarios.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Tipos de Usuarios'), 'CREACION_TIPO_USUARIO', 'Permite crear nuevos tipos de usuarios.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Tipos de Usuarios'), 'ACTUALIZACION_TIPO_USUARIO', 'Permite modificar tipos de usuarios existentes.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Tipos de Usuarios'), 'ELIMINACION_LOGICA_TIPO_USUARIO', 'Permite inhabilitar tipos de usuarios lógicamente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Tipos de Usuarios'), 'ELIMINACION_DEFINITIVA_TIPO_USUARIO', 'Permite eliminar tipos de usuarios permanentemente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Tipos de Usuarios'), 'FILTRADO_TIPOS_USUARIOS_POR_ESTADO', 'Permite filtrar tipos de usuarios por su estado.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Tipos de Usuarios'), 'FILTRADO_TIPOS_USUARIOS_POR_APLICACION', 'Permite filtrar tipos de usuarios por aplicación.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Tipos de Usuarios'), 'BUSQUEDA_TIPOS_USUARIOS_SIMPLE', 'Permite obtener tipos de usuario en formato simplificado para selectores.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Gestión de Permisos
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Permisos'), 'ACCESO_GESTION_PERMISOS', 'Permite el acceso al módulo de administración de permisos.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Permisos'), 'BUSQUEDA_PERMISOS', 'Permite buscar y listar permisos.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Permisos'), 'CREACION_PERMISO', 'Permite crear nuevas asignaciones de permisos.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Permisos'), 'ACTUALIZACION_PERMISO', 'Permite modificar asignaciones de permisos existentes.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Permisos'), 'ELIMINACION_LOGICA_PERMISO', 'Permite inhabilitar permisos lógicamente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Permisos'), 'ELIMINACION_DEFINITIVA_PERMISO', 'Permite eliminar permisos permanentemente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Permisos'), 'FILTRADO_PERMISOS_POR_TIPO_USUARIO', 'Permite filtrar permisos por tipo de usuario.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Permisos'), 'FILTRADO_PERMISOS_POR_APLICACION', 'Permite filtrar permisos por aplicación.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Permisos'), 'CONSULTA_PERMISOS_USUARIO_APLICACION_SECCION', 'Permite consultar permisos de un usuario para una aplicación, agrupados por sección.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Gestión de Usuarios
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Usuarios'), 'ACCESO_GESTION_USUARIOS', 'Permite el acceso al módulo de administración de usuarios.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Usuarios'), 'BUSQUEDA_USUARIOS', 'Permite buscar y listar usuarios.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Usuarios'), 'CREACION_USUARIO', 'Permite crear nuevos usuarios.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Usuarios'), 'ACTUALIZACION_USUARIO', 'Permite modificar usuarios existentes.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Usuarios'), 'ELIMINACION_LOGICA_USUARIO', 'Permite inhabilitar usuarios lógicamente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Usuarios'), 'ELIMINACION_DEFINITIVA_USUARIO', 'Permite eliminar usuarios permanentemente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Usuarios'), 'FILTRADO_USUARIOS_POR_ESTADO', 'Permite filtrar usuarios por su estado (activo/inactivo/bloqueado).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Usuarios'), 'FILTRADO_USUARIOS_POR_2FA', 'Permite filtrar usuarios por si tienen la autenticación de doble factor activa.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Usuarios'), 'FILTRADO_USUARIOS_POR_CAMBIO_CONTRASENA_REQUERIDO', 'Permite filtrar usuarios que requieren cambio de contraseña.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Usuarios'), 'CONSULTA_BLOQUEO_SESION_USUARIO', 'Permite consultar si la sesión de un usuario está bloqueada.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Usuarios'), 'BUSQUEDA_USUARIOS_SIMPLE', 'Permite obtener usuarios en formato simplificado para selectores.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Gestión de Sesiones
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Sesiones'), 'ACCESO_GESTION_SESIONES', 'Permite el acceso al módulo de administración de sesiones.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Sesiones'), 'BUSQUEDA_SESIONES', 'Permite buscar y listar sesiones.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Sesiones'), 'BUSQUEDA_SESION_POR_ID', 'Permite buscar una sesión por su ID.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Sesiones'), 'CREACION_SESION_MANUAL', 'Permite la creación manual de sesiones (principalmente interno, pero se incluye).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Sesiones'), 'ACTUALIZACION_SESION', 'Permite modificar sesiones existentes.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Sesiones'), 'ACTUALIZACION_ESTADO_SESION', 'Permite actualizar el estado de una sesión (ej. cerrar sesión).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Sesiones'), 'ELIMINACION_LOGICA_SESION', 'Permite inhabilitar sesiones lógicamente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Sesiones'), 'ELIMINACION_DEFINITIVA_SESION', 'Permite eliminar sesiones permanentemente (usar con precaución).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Sesiones'), 'FILTRADO_SESIONES_POR_ESTADO', 'Permite filtrar sesiones por su estado (activa/cerrada/expirada).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gestión de Sesiones'), 'BUSQUEDA_SESION_POR_TOKEN', 'Permite buscar una sesión por su token JWT (uso interno/depuración).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Auditoría de Accesos
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Auditoría de Accesos'), 'ACCESO_AUDITORIA_ACCESOS', 'Permite el acceso al módulo de consulta de auditorías de accesos.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Auditoría de Accesos'), 'CONSULTA_AUDITORIAS_TODAS', 'Permite consultar todos los registros de auditoría.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Auditoría de Accesos'), 'CONSULTA_AUDITORIA_POR_ID_FECHA', 'Permite consultar un registro de auditoría por su ID y fecha exacta.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Auditoría de Accesos'), 'ELIMINACION_LOGICA_AUDITORIA', 'Permite inhabilitar registros de auditoría lógicamente.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Auditoría de Accesos'), 'ELIMINACION_DEFINITIVA_AUDITORIA', 'Permite eliminar registros de auditoría permanentemente (usar con precaución).', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Auditoría de Accesos'), 'FILTRADO_AUDITORIAS_POR_APLICACION', 'Permite filtrar registros de auditoría por la aplicación.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Auditoría de Accesos'), 'FILTRADO_AUDITORIAS_POR_ACCION', 'Permite filtrar registros de auditoría por la acción realizada.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Envío de Correos
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Envío de Correos'), 'ACCESO_ENVIO_CORREOS', 'Permite el acceso al módulo para el envío de correos electrónicos.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Envío de Correos'), 'ENVIAR_CORREO', 'Permite enviar correos electrónicos a usuarios o grupos.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Reportes
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Reportes'), 'ACCESO_REPORTES', 'Permite el acceso al módulo de generación y consulta de reportes.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Reportes'), 'GENERACION_REPORTE', 'Permite generar nuevos reportes basados en criterios definidos.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Reportes'), 'VISUALIZACION_REPORTES', 'Permite visualizar reportes previamente generados.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),

    -- Gráficos
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gráficos'), 'ACCESO_GRAFICOS', 'Permite el acceso al módulo de visualización de gráficos.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL ),
    ( (SELECT id FROM inserted_app), (SELECT id FROM inserted_secciones WHERE nombre = 'Gráficos'), 'VISUALIZACION_GRAFICOS', 'Permite visualizar diferentes gráficos de datos del sistema.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL )
    RETURNING id
)
-- 7. Insertar todos los Permisos para el Tipo de Usuario "Super Admin" (acceso a todas las acciones de CCA)
INSERT INTO permisos_tipo_usuario (tipo_usuario_id, accion_id, created_at, updated_at, deleted_at)
SELECT
    (SELECT id FROM inserted_tipo_usuario),
    ia.id,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL
FROM inserted_acciones ia;
