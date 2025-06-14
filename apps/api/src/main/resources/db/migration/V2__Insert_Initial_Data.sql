-- V2__Insert_Initial_Data.sql

-- 1. Insertar Secciones iniciales y capturar sus IDs
WITH inserted_secciones AS (
    INSERT INTO secciones (nombre, descripcion, created_at, updated_at, deleted_at) VALUES
    ('Autenticación', 'Sección dedicada a las funcionalidades de autenticación y seguridad.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Dashboard', 'Sección de visualización general del sistema.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Gestión de Usuarios', 'Sección para la administración de usuarios del sistema.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Gestión de Tipos de Usuarios', 'Sección para la administración de roles y tipos de usuario.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Gestión de Aplicaciones', 'Sección para la administración de aplicaciones registradas.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
    ('Gestión de Permisos', 'Sección para la administración de permisos de los tipos de usuario.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL)
    RETURNING id, nombre
),
-- 2. Insertar la aplicación "Centro de Control de Acceso" y capturar su ID
inserted_app AS (
    INSERT INTO aplicaciones (nombre, descripcion, url, llave_identificadora, created_at, updated_at, deleted_at) VALUES
    (
        'Centro de Control de Acceso',
        'Sistema centralizado de autenticación, autorización y auditoría.',
        'http://localhost:8080/api', -- TODO: Cambiar la url de la api, a la url del frontend
        'app_key.cca_3c6d64fa-5dcb-45f3-bb66-770eff1b7614', 
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL
    )
    RETURNING id
),
-- 3. Insertar Acciones para la aplicación "Centro de Control de Acceso"
-- Se asocian a la sección 'Autenticación'
inserted_acciones AS (
    INSERT INTO acciones (nombre, descripcion, aplicacion_id, seccion_id, created_at, updated_at, deleted_at)
    SELECT
        actions_data.nombre_accion,
        actions_data.descripcion_accion,
        (SELECT id FROM inserted_app),
        (SELECT id FROM inserted_secciones WHERE nombre = 'Autenticación'),
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL
    FROM (VALUES
        ('Login Exitoso', 'El usuario inició sesión correctamente.'),
        ('Login Fallido', 'El usuario intentó iniciar sesión sin éxito.'),
        ('Cambio de Contraseña', 'El usuario cambió su contraseña.'),
        ('Cierre de Sesión', 'El usuario cerró su sesión activa.')
    ) AS actions_data(nombre_accion, descripcion_accion)
    RETURNING id, nombre
),
-- 4. Insertar Tipo de Usuario "Super Admin" para la aplicación "Centro de Control de Acceso"
inserted_tipo_usuario AS (
    INSERT INTO tipo_usuario (nombre, descripcion, aplicacion_id, estado, created_at, updated_at, deleted_at)
    SELECT
        'Super Admin',
        'Rol con privilegios administrativos completos sobre el Centro de Control de Acceso.',
        (SELECT id FROM inserted_app),
        'activo',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL
    RETURNING id
),
-- 5. Insertar Permisos para el Tipo de Usuario "Super Admin" (acceso a todas las acciones de CCA)
inserted_permisos AS (
    INSERT INTO permisos_tipo_usuario (tipo_usuario_id, accion_id, created_at, updated_at, deleted_at)
    SELECT
        (SELECT id FROM inserted_tipo_usuario),
        ia.id,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL
    FROM inserted_acciones ia
    RETURNING id
),
-- 6. Insertar el Usuario inicial
inserted_user AS (
    INSERT INTO usuarios (
        nombres,
        apellidos,
        email,
        contrasena,
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
        '$2a$10$iasjl.5Iv29cdhF50UFoHeTnVRmCmit9WBu5dUnNCvPC.zYh5zFQ.', -- Contraseña hasheada para '6n35s4#Pjf'r4t'
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
)
-- 7. Asignar el tipo de usuario "Super Admin" al usuario "Admin Developer"
INSERT INTO usuarios_tipo_usuario (usuario_id, tipo_usuario_id, created_at, updated_at, deleted_at)
SELECT
    (SELECT id FROM inserted_user),
    (SELECT id FROM inserted_tipo_usuario),
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL;

