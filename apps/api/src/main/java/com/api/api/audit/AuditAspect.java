package com.api.api.audit;

import com.api.api.model.Acciones;
import com.api.api.model.Aplicaciones;
import com.api.api.model.AuditoriaAccesos;
import com.api.api.model.Usuarios;
import com.api.api.service.AccionesService;
import com.api.api.service.AplicacionesService;
import com.api.api.service.AuditoriaAccesosService;
import com.api.api.security.CustomUserDetails;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.OffsetDateTime;
@Aspect
@Component
public class AuditAspect {

    private final AuditoriaAccesosService auditoriaAccesosService;
    private final AplicacionesService aplicacionesService;
    private final AccionesService accionesService;

    // Inyectar la llave identificadora de la aplicación desde las propiedades
    @Value("${app.application.identifier}")
    private String applicationIdentifier;

    // Entidades de Aplicación y Acciones cargadas al inicio para optimización
    private Aplicaciones currentApplication;
    private Acciones defaultAccionSuccess; 
    private Acciones defaultAccionFail;  

    public AuditAspect(AuditoriaAccesosService auditoriaAccesosService,
                       AplicacionesService aplicacionesService,
                       AccionesService accionesService) {
        this.auditoriaAccesosService = auditoriaAccesosService;
        this.aplicacionesService = aplicacionesService;
        this.accionesService = accionesService;
    }

    @PostConstruct
    public void init() {
        // Cargar la aplicación y las acciones por defecto al iniciar el aspecto
        this.currentApplication = aplicacionesService.findByLlaveIdentificadora(applicationIdentifier)
            .orElseThrow(() -> new IllegalStateException("Aplicación con llave identificadora '" + applicationIdentifier + "' no encontrada para auditoría AOP."));
        // Cargar las acciones por defecto para éxito y fallo
        this.defaultAccionSuccess = accionesService.findByNombreAndAplicacionId("Operación Exitosa", currentApplication.getId())
            .orElseThrow(() -> new IllegalStateException("Acción 'Operación Exitosa' no encontrada para la aplicación '" + applicationIdentifier + "'."));
        this.defaultAccionFail = accionesService.findByNombreAndAplicacionId("Operación Fallida", currentApplication.getId())
            .orElseThrow(() -> new IllegalStateException("Acción 'Operación Fallida' no encontrada para la aplicación '" + applicationIdentifier + "'."));
    }

    /**
     * Define el "pointcut" (dónde se aplicará el aspecto) y el "advice" (qué hacer).
     * Este @Around advice interceptará cualquier método anotado con @AuditableAction.
     * @param joinPoint El punto de unión donde se intercepta el método.
     * @param auditableAction La instancia de la anotación @AuditableAction aplicada.
     * @return El resultado del método original.
     * @throws Throwable Cualquier excepción lanzada por el método original.
     */
    @Around("@annotation(auditableAction)")
    public Object auditAround(ProceedingJoinPoint joinPoint, AuditableAction auditableAction) throws Throwable {
        AuditoriaAccesos auditoria = new AuditoriaAccesos();
        auditoria.setAplicacion(this.currentApplication);
        auditoria.setFecha(OffsetDateTime.now());
        auditoria.setUuidId(null);

        // Obtener información del usuario autenticado
        Usuarios usuario = getCurrentAuthenticatedUser();
        auditoria.setUsuario(usuario);
        auditoria.setEmailUsuario(usuario != null ? usuario.getEmail() : "anonimo@cca.com");

        // Obtener IP y Device Info de la solicitud HTTP si está disponible
        HttpServletRequest request = getHttpServletRequest();
        if (request != null) {
            auditoria.setIpOrigen(getClientIp(request));
            auditoria.setInformacionDispositivo(request.getHeader("User-Agent"));
        } else {
            // Si no hay solicitud HTTP (ej. llamadas internas, tareas programadas), usar valores por defecto
            auditoria.setIpOrigen("N/A");
            auditoria.setInformacionDispositivo("Internal Process");
        }

        // Determinar la acción específica desde la anotación, si existe
        Acciones accion = accionesService.findByNombreAndAplicacionId(auditableAction.actionName(), currentApplication.getId())
                            .orElse(null);
        
        auditoria.setAccion(accion != null ? accion : this.defaultAccionSuccess);

        // Mensaje inicial de la auditoría
        auditoria.setMensaje(auditableAction.message());

        Object result;
        try {
            result = joinPoint.proceed(); 
            // Si el método se ejecuta sin excepciones, es un éxito
            if (auditableAction.auditResult().equals(AuditableAction.AuditResultType.SUCCESS) ||
                auditableAction.auditResult().equals(AuditableAction.AuditResultType.BOTH)) {
                auditoria.setEstado("exitoso");
                auditoriaAccesosService.create(auditoria);
            }
            return result;

        } catch (Throwable ex) {
            if (auditableAction.auditResult().equals(AuditableAction.AuditResultType.FAIL) ||
                auditableAction.auditResult().equals(AuditableAction.AuditResultType.BOTH)) {
                auditoria.setEstado("fallido");
                auditoria.setAccion(accion != null ? accion : this.defaultAccionFail); 
                auditoria.setMensaje(auditableAction.message() + " (Fallo: " + ex.getMessage() + ")");
                auditoriaAccesosService.create(auditoria);
            }
            throw ex;
        }
    }

    /**
     * Obtiene el usuario autenticado actualmente del contexto de seguridad de Spring.
     * @return El objeto Usuarios autenticado, o null si no hay usuario autenticado.
     */
    private Usuarios getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Usuarios usuario = new Usuarios();
            usuario.setId(userDetails.getId());
            usuario.setEmail(userDetails.getUsername());
            return usuario;
        }
        return null;
    }

    /**
     * Obtiene el HttpServletRequest del contexto, si la llamada viene de una solicitud web.
     * @return HttpServletRequest o null.
     */
    private HttpServletRequest getHttpServletRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest();
        } catch (IllegalStateException e) {
            return null;
        }
    }

    /**
     * Extrae la IP del cliente de la solicitud HTTP.
     * @param request La solicitud HTTP.
     * @return La dirección IP del cliente.
     */
    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}
