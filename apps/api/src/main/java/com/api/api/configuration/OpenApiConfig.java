package com.api.api.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType; 
import io.swagger.v3.oas.annotations.security.SecurityScheme; 

import org.springframework.context.annotation.Configuration;

// Anotación principal para definir la documentación OpenAPI
@OpenAPIDefinition(
    info = @Info(
        title = "API del Centro de Control de Acceso",
        version = "1.0",
        description = "Documentación completa de la API del Centro de Control de Acceso (CCA). " +
                      "Esta API gestiona la autenticación, autorización y auditoría para diversas aplicaciones.",
        contact = @Contact(
            name = "Juan Pablo Flores Díaz",
            email = "pablojuanfd@gmail.com",
            url = "https://midominio.com" 
        ),
        license = @License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"
        )
    )
)
// Define un esquema de seguridad global para JWT (Bearer Token)
@SecurityScheme(
    name = "bearerAuth", // Nombre de referencia para este esquema de seguridad
    type = SecuritySchemeType.HTTP, // Tipo de esquema HTTP
    scheme = "bearer", // Esquema de autenticación (Bearer Token)
    bearerFormat = "JWT", // Formato del token (JSON Web Token)
    description = "Autenticación JWT usando un Bearer Token. Añade 'Bearer ' antes de tu token."
)
@Configuration
public class OpenApiConfig {
    // Esta clase está vacía porque la configuración de OpenAPI se define a través de las anotaciones
    // No es necesario implementar ningún método aquí, ya que la configuración se maneja automáticamente
    // por Spring Boot y Swagger/OpenAPI.
}
