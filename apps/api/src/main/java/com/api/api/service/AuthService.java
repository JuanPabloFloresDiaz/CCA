package com.api.api.service;

import com.api.api.repository.UsuariosRepository;
import com.api.api.model.Usuarios;
import com.api.api.model.AuditoriaAccesos;
import com.api.api.model.Sesiones;
import com.api.api.model.Aplicaciones;
import com.api.api.model.Acciones;
import com.api.api.security.JwtTokenProvider;
import com.api.api.security.CustomUserDetails;
import com.api.api.dto.RequestDTO.LoginRequestDTO;
import com.api.api.dto.ResponseDTO.LoginResponseDTO;
import com.api.api.dto.RequestDTO.PasswordChangeRequestDTO;
import com.api.api.exception.BadRequestException;
import com.api.api.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuditoriaAccesosService auditoriaAccesosService;
    private final SesionesService sesionesService;
    private final AplicacionesService aplicacionesService;
    private final AccionesService accionesService;

    // Inyectar la llave identificadora de la aplicación desde las propiedades
    @Value("${app.application.identifier}")
    private String applicationIdentifier;

    // Entidades de Aplicación y Acción que se cargarán al inicio
    private Aplicaciones currentApplication; // La aplicación de este servicio (CCA)
    private Acciones loginExitosoAction;
    private Acciones loginFallidoAction;
    private Acciones cambioContrasenaAction;
    private Acciones cierreSesionAction;

    public AuthService(AuthenticationManager authenticationManager,
            UsuariosRepository usuariosRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            AuditoriaAccesosService auditoriaAccesosService,
            SesionesService sesionesService,
            AplicacionesService aplicacionesService,
            AccionesService accionesService) {
        this.authenticationManager = authenticationManager;
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.auditoriaAccesosService = auditoriaAccesosService;
        this.sesionesService = sesionesService;
        this.aplicacionesService = aplicacionesService;
        this.accionesService = accionesService;
    }

    // Método que se ejecuta después de que el bean se ha inicializado
    @PostConstruct
    public void init() {
        this.currentApplication = aplicacionesService.findByLlaveIdentificadora(applicationIdentifier)
                .orElseThrow(
                        () -> new IllegalStateException("Aplicación con llave identificadora '" + applicationIdentifier
                                + "' no encontrada. Asegúrate de que exista y esté configurada en la base de datos."));

        this.loginExitosoAction = accionesService
                .findByNombreAndAplicacionId("Login Exitoso", currentApplication.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Acción 'Login Exitoso' no encontrada para la aplicación '" + applicationIdentifier + "'."));
        this.loginFallidoAction = accionesService
                .findByNombreAndAplicacionId("Login Fallido", currentApplication.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Acción 'Login Fallido' no encontrada para la aplicación '" + applicationIdentifier + "'."));
        this.cambioContrasenaAction = accionesService
                .findByNombreAndAplicacionId("Cambio de Contraseña", currentApplication.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Acción 'Cambio de Contraseña' no encontrada para la aplicación '" + applicationIdentifier
                                + "'."));
        this.cierreSesionAction = accionesService
                .findByNombreAndAplicacionId("Cierre de Sesión", currentApplication.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Acción 'Cierre de Sesión' no encontrada para la aplicación '" + applicationIdentifier + "'."));
    }

    /**
     * Autentica a un usuario, genera un token JWT y gestiona el estado de sesión y
     * auditoría.
     * 
     * @param loginRequest DTO con las credenciales de login (email, contrasena).
     * @param clientIp     Dirección IP del cliente.
     * @param deviceInfo   Información del dispositivo del cliente.
     * @return DTO de respuesta con el token JWT y detalles del usuario.
     * @throws BadRequestException Si las credenciales son inválidas o la cuenta
     *                             está bloqueada.
     */
    @Transactional
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest, String clientIp, String deviceInfo) {
        Optional<Usuarios> optionalUser = usuariosRepository.findByEmail(loginRequest.getEmail());
        Usuarios userToAuthenticate = optionalUser.orElse(null);

        if (userToAuthenticate != null && userToAuthenticate.getFechaBloqueoSesion() != null &&
                userToAuthenticate.getFechaBloqueoSesion().isAfter(OffsetDateTime.now())) {
            AuditoriaAccesos auditoriaBloqueo = new AuditoriaAccesos();
            auditoriaBloqueo.setUsuario(userToAuthenticate);
            auditoriaBloqueo.setFecha(OffsetDateTime.now());
            auditoriaBloqueo.setUuidId(null);
            auditoriaBloqueo.setIpOrigen(clientIp);
            auditoriaBloqueo.setEstado("fallido");
            auditoriaBloqueo.setEmailUsuario(loginRequest.getEmail());
            auditoriaBloqueo.setInformacionDispositivo(deviceInfo);
            auditoriaBloqueo.setMensaje("Intento de login fallido: Cuenta bloqueada temporalmente.");
            auditoriaBloqueo.setAplicacion(this.currentApplication);
            auditoriaBloqueo.setAccion(this.loginFallidoAction);
            auditoriaAccesosService.create(auditoriaBloqueo);

            throw new BadRequestException("La cuenta de usuario está bloqueada temporalmente. Intente más tarde.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getContrasena()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Usuarios authenticatedUser = usuariosRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado en DB."));

            String jwt = jwtTokenProvider.generateToken(authentication);

            authenticatedUser.setIntentosFallidosSesion(0);
            authenticatedUser.setFechaBloqueoSesion(null);
            authenticatedUser.setFechaUltimoIntentoFallido(null);
            usuariosRepository.save(authenticatedUser);

            AuditoriaAccesos auditoriaExito = new AuditoriaAccesos();
            auditoriaExito.setUsuario(authenticatedUser);
            auditoriaExito.setFecha(OffsetDateTime.now());
            auditoriaExito.setUuidId(null);
            auditoriaExito.setIpOrigen(clientIp);
            auditoriaExito.setEstado("exitoso");
            auditoriaExito.setEmailUsuario(authenticatedUser.getEmail());
            auditoriaExito.setInformacionDispositivo(deviceInfo);
            auditoriaExito.setMensaje("Inicio de sesión exitoso.");
            auditoriaExito.setAplicacion(this.currentApplication);
            auditoriaExito.setAccion(this.loginExitosoAction);
            auditoriaAccesosService.create(auditoriaExito);

            Sesiones nuevaSesion = new Sesiones();
            nuevaSesion.setUsuario(authenticatedUser);
            nuevaSesion.setToken(jwt);
            nuevaSesion.setIpOrigen(clientIp);
            nuevaSesion.setEmailUsuario(authenticatedUser.getEmail());
            nuevaSesion.setInformacionDispositivo(deviceInfo);
            nuevaSesion.setFechaExpiracion(OffsetDateTime.now().plus(jwtTokenProvider.getJwtExpirationInMs(),
                    java.time.temporal.ChronoUnit.MILLIS));
            nuevaSesion.setEstado("activa");
            sesionesService.create(nuevaSesion);

            return new LoginResponseDTO(
                    authenticatedUser.getId(),
                    authenticatedUser.getNombres(),
                    authenticatedUser.getApellidos(),
                    authenticatedUser.getEmail(),
                    jwt);

        } catch (org.springframework.security.core.AuthenticationException e) {
            Usuarios failedUser = userToAuthenticate;
            if (failedUser == null) {
                failedUser = new Usuarios();
                failedUser.setEmail(loginRequest.getEmail());
            }

            failedUser.setIntentosFallidosSesion(failedUser.getIntentosFallidosSesion() + 1);
            failedUser.setFechaUltimoIntentoFallido(OffsetDateTime.now());

            if (failedUser.getIntentosFallidosSesion() >= 5) {
                failedUser.setFechaBloqueoSesion(OffsetDateTime.now().plusMinutes(15));
            }

            if (optionalUser.isPresent()) {
                usuariosRepository.save(failedUser);
            }

            AuditoriaAccesos auditoriaFallo = new AuditoriaAccesos();
            auditoriaFallo.setUsuario(optionalUser.orElse(null));
            auditoriaFallo.setFecha(OffsetDateTime.now());
            auditoriaFallo.setUuidId(null);
            auditoriaFallo.setIpOrigen(clientIp);
            auditoriaFallo.setEstado("fallido");
            auditoriaFallo.setEmailUsuario(loginRequest.getEmail());
            auditoriaFallo.setInformacionDispositivo(deviceInfo);
            auditoriaFallo.setMensaje("Inicio de sesión fallido: " + e.getMessage());
            auditoriaFallo.setAplicacion(this.currentApplication);
            auditoriaFallo.setAccion(this.loginFallidoAction);
            auditoriaAccesosService.create(auditoriaFallo);
            throw new BadRequestException("Credenciales inválidas. Intento " + failedUser.getIntentosFallidosSesion());
        }
    }

    /**
     * Permite a un usuario autenticado cambiar su contraseña.
     * 
     * @param userId  ID del usuario que desea cambiar la contraseña.
     * @param request DTO con la contraseña actual y la nueva contraseña.
     * @throws ResourceNotFoundException Si el usuario no es encontrado.
     * @throws BadRequestException       Si la contraseña actual es incorrecta.
     */
    @Transactional
    public void changePassword(UUID userId, PasswordChangeRequestDTO request) {
        Usuarios user = usuariosRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado para cambiar contraseña."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getContrasena())) {
            AuditoriaAccesos auditoriaFallo = new AuditoriaAccesos();
            auditoriaFallo.setUsuario(user);
            auditoriaFallo.setFecha(OffsetDateTime.now());
            auditoriaFallo.setUuidId(null);
            auditoriaFallo.setIpOrigen(null); // Debe obtenerse de la Http Request
            auditoriaFallo.setEstado("fallido");
            auditoriaFallo.setEmailUsuario(user.getEmail());
            auditoriaFallo.setInformacionDispositivo(null); // Debe obtenerse de la Http Request
            auditoriaFallo.setMensaje("Intento de cambio de contraseña fallido: contraseña actual incorrecta.");
            auditoriaFallo.setAplicacion(this.currentApplication);
            auditoriaFallo.setAccion(this.cambioContrasenaAction);
            auditoriaAccesosService.create(auditoriaFallo);

            throw new BadRequestException("La contraseña actual es incorrecta.");
        }

        user.setContrasena(passwordEncoder.encode(request.getNewPassword()));
        user.setFechaUltimoCambioContrasena(OffsetDateTime.now());
        user.setRequiereCambioContrasena(false);

        usuariosRepository.save(user);

        AuditoriaAccesos auditoriaExito = new AuditoriaAccesos();
        auditoriaExito.setUsuario(user);
        auditoriaExito.setFecha(OffsetDateTime.now());
        auditoriaExito.setUuidId(null); // Permitir que la DB genere el UUID para la parte del ID
        auditoriaExito.setIpOrigen(null); // Debe obtenerse de la Http Request
        auditoriaExito.setEstado("exitoso");
        auditoriaExito.setEmailUsuario(user.getEmail());
        auditoriaExito.setInformacionDispositivo(null); // Debe obtenerse de la Http Request
        auditoriaExito.setMensaje("Cambio de contraseña exitoso.");
        auditoriaExito.setAplicacion(this.currentApplication);
        auditoriaExito.setAccion(this.cambioContrasenaAction);
        auditoriaAccesosService.create(auditoriaExito);
    }

    /**
     * Marca una sesión como inactiva (cierre de sesión lógico).
     * 
     * @param jwtToken El token JWT que se desea invalidar.
     */
    @Transactional
    public void logout(String jwtToken) {
        sesionesService.findByToken(jwtToken).ifPresent(sesion -> {
            sesion.setEstado("cerrada");
            sesion.setFechaExpiracion(OffsetDateTime.now());
            sesion.setFechaFin(OffsetDateTime.now());
            sesionesService.update(sesion.getId(), sesion);

            AuditoriaAccesos auditoriaLogout = new AuditoriaAccesos();
            auditoriaLogout.setUsuario(sesion.getUsuario());
            auditoriaLogout.setFecha(OffsetDateTime.now());
            auditoriaLogout.setUuidId(null);
            auditoriaLogout.setIpOrigen(sesion.getIpOrigen());
            auditoriaLogout.setEstado("exitoso");
            auditoriaLogout.setEmailUsuario(sesion.getEmailUsuario());
            auditoriaLogout.setInformacionDispositivo(sesion.getInformacionDispositivo());
            auditoriaLogout.setMensaje("Cierre de sesión exitoso.");
            auditoriaLogout.setAplicacion(this.currentApplication);
            auditoriaLogout.setAccion(this.cierreSesionAction);
            auditoriaAccesosService.create(auditoriaLogout);
        });
    }
}
