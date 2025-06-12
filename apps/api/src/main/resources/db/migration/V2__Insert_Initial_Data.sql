-- V2__Insert_Initial_Data.sql

INSERT INTO usuarios (
    id,
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
    '3f4da029-a2d6-41c1-8dce-28688f81fd6c',
    'Admin',
    'Developer',
    'admin_dev@gmail.com',
    '$2a$10$iasjl.5Iv29cdhF50UFoHeTnVRmCmit9WBu5dUnNCvPC.zYh5zFQ.', -- 6n35s4#Pjf'r4t
    'activo',
    FALSE,
    0,
    CURRENT_TIMESTAMP,
    FALSE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    NULL
);
