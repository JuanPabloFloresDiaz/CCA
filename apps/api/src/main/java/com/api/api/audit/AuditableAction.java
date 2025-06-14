package com.api.api.audit; // Nuevo paquete para la lógica de auditoría AOP

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Indica que esta anotación se puede aplicar a MÉTODOS
@Target(ElementType.METHOD)
// Indica que la anotación estará disponible en tiempo de ejecución (para que AOP la lea)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditableAction {
    // Nombre de la acción que se registrará en AuditoriaAccesos
    String actionName();
    // Mensaje base para la auditoría (puede ser extendido dinámicamente)
    String message();
    // Determina si la acción se audita en caso de éxito, fallo, o ambos
    AuditResultType auditResult() default AuditResultType.BOTH;

    // Enum para definir el tipo de resultado a auditar
    public enum AuditResultType {
        SUCCESS, FAIL, BOTH
    }
}
