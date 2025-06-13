# Centro de Control de Accesos (CCA)

&#x20; &#x20;

---

## 📖 Descripción

El **Centro de Control de Accesos (CCA)** es un sistema de Gestión de Identidades y Accesos (IAM) que ofrece un punto unificado para la autenticación, autorización y auditoría de usuarios en múltiples aplicaciones. Con CCA podrás administrar usuarios, roles y permisos de manera centralizada, garantizando la seguridad y el trazado detallado de todas las interacciones.

## 🔗 Tabla de Contenidos

1. [Características](#-características)
2. [Arquitectura](#-arquitectura)
3. [Tecnologías](#%EF%B8%8F-tecnologías)
4. [Capturas de Pantalla](#-capturas-de-pantalla)
5. [Instalación](#-instalación)
6. [Uso](#-uso)
7. [Despliegue](#-despliegue)
8. [Contribución](#-contribución)

## ✅ Características

* **Gestión Centralizada**: Control de identidades, roles y permisos desde una única plataforma.
* **Autenticación Segura**: Soporte para JWT, cifrado de contraseñas y políticas de seguridad.
* **Autorización Granular**: Asignación de permisos a nivel de recurso o acción.
* **Auditoría Completa**: Registro de todas las operaciones de acceso y cambios de configuración.
* **Escalabilidad**: Diseñado para entornos con alta concurrencia y microservicios.

## 🏗️ Arquitectura

El proyecto Centro de Control de Accesos (CCA) implementa una arquitectura de monolito modular

## 🛠️ Tecnologías

### Backend (API)

* **Lenguaje**: Java
* **Framework**: Spring Boot
* **Seguridad**: Spring Security (JWT, manejo de contraseñas, autorización)
* **Base de Datos**: PostgreSQL

### Frontend (UI)

* **Biblioteca**: React
* **Estado**: Zustand
* **Validación**: Zod
* **Componentes**: Material-UI (MUI)
* **Herramientas de Datos**: React Query, React Table, React Virtual
* **Enrutamiento**: React Router
* **HTTP**: Axios

### Despliegue

* **Servidor**: VPS (DigitalOcean, Oracle Cloud)
* **Base de Datos Gestionada**: Railway (ha-postgres)

## 🚀 Instalación

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/JuanPabloFloresDiaz/CCA.git
   cd CCA
   ```
2. Configurar variables de entorno:

   ```env
   # Archivo .env
   # Nombre de la aplicación
    APPLICATION_NAME=centro-de-control-de-aplicaciones
    # Versión de la aplicación
    APPLICATION_VERSION=
    # Puerto en el que se ejecuta la aplicación
    APPLICATION_PORT=
    # Entorno de la aplicación (dev, test, prod)
    APPLICATION_ENV=
    # Configuración de CORS
    CORS_ALLOWED_ORIGINS=
    CORS_ALLOWED_METHODS=
    # Configuración de la base de datos
    DATABASE_URL=
    DATABASE_USERNAME=
    DATABASE_PASSWORD=""
    # Configuración de JPA
    SPRING_JPA_HIBERNATE_DDL_AUTO=
    SPRING_JPA_HIBERNATE_DIALECT=
    # Configuración del token
    TOKEN_SECRET=
    TOKEN_EXPIRATION_TIME=
    TOKEN_ALGORITHM=
    TOKEN_ITERATIONS=
    TOKEN_KEY_LENGTH=
    TOKEN_SALT=
    # Configuración de niveles de logging para diferentes componentes de Spring Boot
    LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_WEB=
    LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_HTTP=
    LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_SECURITY=
    LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_WEB_SERVLET_MVC_METHOD_ANNOTATION_REQUESTMAPPINGHANDLERADAPTER=
    ```
3. Iniciar Backend:

   ```bash
   cd apps/api && ./mvnw spring-boot:run
   ```
4. Iniciar Frontend:

   ```bash
   cd apps/client && npm install && npm run dev
   ```

## 📦 Uso

* Accede a `http://localhost:3000` para la interfaz web.
* Las rutas de la API están documentadas en `http://localhost:8080/swagger-ui.html`.

## ☁️ Despliegue

Para producción:

1. Construir la API:

   ```bash
   cd backend && ./mvnw clean package -DskipTests
   ```
2. Construir la UI:

   ```bash
   cd frontend && npm run build
   ```
3. Subir artefactos y configurar el servidor/web server (NGINX, Apache).

## 🤝 Contribución

1. Haz un fork del proyecto.
2. Crea una rama feature: `git checkout -b feature/nueva-funcionalidad`.
3. Realiza tus cambios y haz commit: `git commit -m "feat: descripción"`.
4. Envía un Pull Request.

