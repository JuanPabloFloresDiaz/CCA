package com.api.api.audit;

public final class AuditActions {

    private AuditActions() {

    }

    // Nombres de acciones para la sección de Autenticación
    public static final String ACCESO_LOGIN = "ACCESO_LOGIN";
    public static final String INICIO_SESION_EXITOSO = "INICIO_SESION_EXITOSO";
    public static final String INICIO_SESION_FALLIDO = "INICIO_SESION_FALLIDO";
    public static final String CIERRE_SESION = "CIERRE_SESION";

    // Nombres de acciones para la sección de Cambiar Contraseña
    public static final String ACCESO_CAMBIO_CONTRASENA = "ACCESO_CAMBIO_CONTRASENA";
    public static final String CAMBIO_CONTRASENA_USUARIO = "CAMBIO_CONTRASENA_USUARIO";

    // Nombres de acciones para la sección de Dashboard
    public static final String ACCESO_DASHBOARD = "ACCESO_DASHBOARD";
    public static final String VISUALIZACION_METRICAS_DASHBOARD = "VISUALIZACION_METRICAS_DASHBOARD";

    // Nombres de acciones para la sección de Gestión de Secciones
    public static final String ACCESO_GESTION_SECCIONES = "ACCESO_GESTION_SECCIONES";
    public static final String BUSQUEDA_SECCIONES = "BUSQUEDA_SECCIONES";
    public static final String CREACION_SECCION = "CREACION_SECCION";
    public static final String ACTUALIZACION_SECCION = "ACTUALIZACION_SECCION";
    public static final String ELIMINACION_LOGICA_SECCION = "ELIMINACION_LOGICA_SECCION";
    public static final String ELIMINACION_DEFINITIVA_SECCION = "ELIMINACION_DEFINITIVA_SECCION";
    public static final String BUSQUEDA_SECCIONES_SIMPLE = "BUSQUEDA_SECCIONES_SIMPLE";

    // Nombres de acciones para la sección de Gestión de Aplicaciones
    public static final String ACCESO_GESTION_APLICACIONES = "ACCESO_GESTION_APLICACIONES";
    public static final String BUSQUEDA_APLICACIONES = "BUSQUEDA_APLICACIONES";
    public static final String CREACION_APLICACION = "CREACION_APLICACION";
    public static final String ACTUALIZACION_APLICACION = "ACTUALIZACION_APLICACION";
    public static final String ELIMINACION_LOGICA_APLICACION = "ELIMINACION_LOGICA_APLICACION";
    public static final String ELIMINACION_DEFINITIVA_APLICACION = "ELIMINACION_DEFINITIVA_APLICACION";
    public static final String FILTRADO_APLICACIONES_POR_ESTADO = "FILTRADO_APLICACIONES_POR_ESTADO";
    public static final String BUSQUEDA_APLICACIONES_SIMPLE = "BUSQUEDA_APLICACIONES_SIMPLE";

    // Nombres de acciones para la sección de Gestión de Acciones
    public static final String ACCESO_GESTION_ACCIONES = "ACCESO_GESTION_ACCIONES";
    public static final String BUSQUEDA_ACCIONES = "BUSQUEDA_ACCIONES";
    public static final String CREACION_ACCION = "CREACION_ACCION";
    public static final String ACTUALIZACION_ACCION = "ACTUALIZACION_ACCION";
    public static final String ELIMINACION_LOGICA_ACCION = "ELIMINACION_LOGICA_ACCION";
    public static final String ELIMINACION_DEFINITIVA_ACCION = "ELIMINACION_DEFINITIVA_ACCION";
    public static final String FILTRADO_ACCIONES_POR_APLICACION = "FILTRADO_ACCIONES_POR_APLICACION";
    public static final String FILTRADO_ACCIONES_POR_SECCION = "FILTRADO_ACCIONES_POR_SECCION";
    public static final String BUSQUEDA_ACCIONES_SIMPLE = "BUSQUEDA_ACCIONES_SIMPLE";

    // Nombres de acciones para la sección de Gestión de Tipos de Usuarios
    public static final String ACCESO_GESTION_TIPOS_USUARIOS = "ACCESO_GESTION_TIPOS_USUARIOS";
    public static final String BUSQUEDA_TIPOS_USUARIOS = "BUSQUEDA_TIPOS_USUARIOS";
    public static final String CREACION_TIPO_USUARIO = "CREACION_TIPO_USUARIO";
    public static final String ACTUALIZACION_TIPO_USUARIO = "ACTUALIZACION_TIPO_USUARIO";
    public static final String ELIMINACION_LOGICA_TIPO_USUARIO = "ELIMINACION_LOGICA_TIPO_USUARIO";
    public static final String ELIMINACION_DEFINITIVA_TIPO_USUARIO = "ELIMINACION_DEFINITIVA_TIPO_USUARIO";
    public static final String FILTRADO_TIPOS_USUARIOS_POR_ESTADO = "FILTRADO_TIPOS_USUARIOS_POR_ESTADO";
    public static final String FILTRADO_TIPOS_USUARIOS_POR_APLICACION = "FILTRADO_TIPOS_USUARIOS_POR_APLICACION";
    public static final String BUSQUEDA_TIPOS_USUARIOS_SIMPLE = "BUSQUEDA_TIPOS_USUARIOS_SIMPLE";

    // Nombres de acciones para la sección de Gestión de Permisos
    public static final String ACCESO_GESTION_PERMISOS = "ACCESO_GESTION_PERMISOS";
    public static final String BUSQUEDA_PERMISOS = "BUSQUEDA_PERMISOS";
    public static final String CREACION_PERMISO = "CREACION_PERMISO";
    public static final String ACTUALIZACION_PERMISO = "ACTUALIZACION_PERMISO";
    public static final String ELIMINACION_LOGICA_PERMISO = "ELIMINACION_LOGICA_PERMISO";
    public static final String ELIMINACION_DEFINITIVA_PERMISO = "ELIMINACION_DEFINITIVA_PERMISO";
    public static final String FILTRADO_PERMISOS_POR_TIPO_USUARIO = "FILTRADO_PERMISOS_POR_TIPO_USUARIO";
    public static final String FILTRADO_PERMISOS_POR_APLICACION = "FILTRADO_PERMISOS_POR_APLICACION";
    public static final String CONSULTA_PERMISOS_USUARIO_APLICACION_SECCION = "CONSULTA_PERMISOS_USUARIO_APLICACION_SECCION";

    // Nombres de acciones para la sección de Gestión de Usuarios
    public static final String ACCESO_GESTION_USUARIOS = "ACCESO_GESTION_USUARIOS";
    public static final String BUSQUEDA_USUARIOS = "BUSQUEDA_USUARIOS";
    public static final String CREACION_USUARIO = "CREACION_USUARIO";
    public static final String ACTUALIZACION_USUARIO = "ACTUALIZACION_USUARIO";
    public static final String ELIMINACION_LOGICA_USUARIO = "ELIMINACION_LOGICA_USUARIO";
    public static final String ELIMINACION_DEFINITIVA_USUARIO = "ELIMINACION_DEFINITIVA_USUARIO";
    public static final String FILTRADO_USUARIOS_POR_ESTADO = "FILTRADO_USUARIOS_POR_ESTADO";
    public static final String FILTRADO_USUARIOS_POR_2FA = "FILTRADO_USUARIOS_POR_2FA";
    public static final String FILTRADO_USUARIOS_POR_CAMBIO_CONTRASENA_REQUERIDO = "FILTRADO_USUARIOS_POR_CAMBIO_CONTRASENA_REQUERIDO";
    public static final String CONSULTA_BLOQUEO_SESION_USUARIO = "CONSULTA_BLOQUEO_SESION_USUARIO";
    public static final String BUSQUEDA_USUARIOS_SIMPLE = "BUSQUEDA_USUARIOS_SIMPLE";

    // Nombres de acciones para la sección de Gestión de Sesiones
    public static final String ACCESO_GESTION_SESIONES = "ACCESO_GESTION_SESIONES";
    public static final String BUSQUEDA_SESIONES = "BUSQUEDA_SESIONES";
    public static final String BUSQUEDA_SESION_POR_ID = "BUSQUEDA_SESION_POR_ID";
    public static final String CREACION_SESION_MANUAL = "CREACION_SESION_MANUAL";
    public static final String ACTUALIZACION_SESION = "ACTUALIZACION_SESION";
    public static final String ACTUALIZACION_ESTADO_SESION = "ACTUALIZACION_ESTADO_SESION";
    public static final String ELIMINACION_LOGICA_SESION = "ELIMINACION_LOGICA_SESION";
    public static final String ELIMINACION_DEFINITIVA_SESION = "ELIMINACION_DEFINITIVA_SESION";
    public static final String FILTRADO_SESIONES_POR_ESTADO = "FILTRADO_SESIONES_POR_ESTADO";
    public static final String BUSQUEDA_SESION_POR_TOKEN = "BUSQUEDA_SESION_POR_TOKEN";

    // Nombres de acciones para la sección de Auditoría de Accesos
    public static final String ACCESO_AUDITORIA_ACCESOS = "ACCESO_AUDITORIA_ACCESOS";
    public static final String CONSULTA_AUDITORIAS_TODAS = "CONSULTA_AUDITORIAS_TODAS";
    public static final String CONSULTA_AUDITORIA_POR_ID_FECHA = "CONSULTA_AUDITORIA_POR_ID_FECHA";
    public static final String ELIMINACION_LOGICA_AUDITORIA = "ELIMINACION_LOGICA_AUDITORIA";
    public static final String ELIMINACION_DEFINITIVA_AUDITORIA = "ELIMINACION_DEFINITIVA_AUDITORIA";
    public static final String FILTRADO_AUDITORIAS_POR_APLICACION = "FILTRADO_AUDITORIAS_POR_APLICACION";
    public static final String FILTRADO_AUDITORIAS_POR_ACCION = "FILTRADO_AUDITORIAS_POR_ACCION";

    // Nombres de acciones para la sección de Envío de Correos
    public static final String ACCESO_ENVIO_CORREOS = "ACCESO_ENVIO_CORREOS";
    public static final String ENVIAR_CORREO = "ENVIAR_CORREO";

    // Nombres de acciones para la sección de Reportes
    public static final String ACCESO_REPORTES = "ACCESO_REPORTES";
    public static final String GENERACION_REPORTE = "GENERACION_REPORTE";
    public static final String VISUALIZACION_REPORTES = "VISUALIZACION_REPORTES";

    // Nombres de acciones para la sección de Gráficos
    public static final String ACCESO_GRAFICOS = "ACCESO_GRAFICOS";
    public static final String VISUALIZACION_GRAFICOS = "VISUALIZACION_GRAFICOS";
}
