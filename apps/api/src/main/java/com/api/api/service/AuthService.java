package com.api.api.service;

import com.api.api.repository.UsuariosRepository;
import com.api.api.repository.AuditoriaAccesosRepository;
import com.api.api.repository.SesionesRepository;
import com.api.api.model.Usuarios;
import com.api.api.model.AuditoriaAccesos;
import com.api.api.model.Sesiones;
import com.api.api.security.JwtTokenProvider;
import com.api.api.security.CustomUserDetails;
import com.api.api.dto.RequestDTO.LoginRequestDTO;
import com.api.api.dto.ResponseDTO.LoginResponseDTO;
import com.api.api.dto.RequestDTO.PasswordChangeRequestDTO;
import com.api.api.exception.BadRequestException;
import com.api.api.exception.ResourceNotFoundException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // Constructor con todas las dependencias inyectadas
    public AuthService(AuthenticationManager authenticationManager,
            UsuariosRepository usuariosRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            AuditoriaAccesosService auditoriaAccesosService,
            SesionesService sesionesService) {
        this.authenticationManager = authenticationManager;
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.auditoriaAccesosService = auditoriaAccesosService;
        this.sesionesService = sesionesService;
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
    @Transactional // Asegura que todas las operaciones de DB sean atómicas
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest, String clientIp, String deviceInfo) {
        // Buscar el usuario por email (para verificar bloqueo y existencias)
        Optional<Usuarios> optionalUser = usuariosRepository.findByEmail(loginRequest.getEmail());
        Usuarios userToAuthenticate = optionalUser.orElse(null); // Podría ser null si el email no existe

        // 1. Verificar si la cuenta está bloqueada temporalmente
        if (userToAuthenticate != null && userToAuthenticate.getFechaBloqueoSesion() != null &&
                userToAuthenticate.getFechaBloqueoSesion().isAfter(OffsetDateTime.now())) {
            // Registrar intento de login fallido (bloqueado)
            AuditoriaAccesos auditoriaBloqueo = new AuditoriaAccesos();
            auditoriaBloqueo.setUsuario(userToAuthenticate);
            auditoriaBloqueo.setFecha(OffsetDateTime.now());
            auditoriaBloqueo.setIpOrigen(clientIp);
            auditoriaBloqueo.setEstado("fallido");
            auditoriaBloqueo.setEmailUsuario(loginRequest.getEmail());
            auditoriaBloqueo.setMensaje("Intento de login fallido: Cuenta bloqueada temporalmente.");
            auditoriaBloqueo.setAplicacion(null);
            auditoriaBloqueo.setAccion(null);
            auditoriaAccesosService.create(auditoriaBloqueo);

            throw new BadRequestException("La cuenta de usuario está bloqueada temporalmente. Intente más tarde.");
        }

        try {
            // 2. Autenticar al usuario usando el AuthenticationManager de Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getContrasena()));

            // 3. Si la autenticación es exitosa, establecerla en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Usuarios authenticatedUser = usuariosRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado en DB."));

            // 4. Generar el token JWT
            String jwt = jwtTokenProvider.generateToken(authentication);

            // 5. Restablecer intentos fallidos y actualizar campos de seguridad en Usuarios
            authenticatedUser.setIntentosFallidosSesion(0);
            authenticatedUser.setFechaBloqueoSesion(null); // Desbloquear si estaba bloqueado
            authenticatedUser.setFechaUltimoIntentoFallido(null); // Limpiar registro de intento fallido
            usuariosRepository.save(authenticatedUser); // Persistir los cambios

            // 6. Registrar el evento de inicio de sesión exitoso en AuditoriaAccesos
            AuditoriaAccesos auditoriaExito = new AuditoriaAccesos();
            auditoriaExito.setUsuario(authenticatedUser);
            auditoriaExito.setFecha(OffsetDateTime.now());
            auditoriaExito.setIpOrigen(clientIp);
            auditoriaExito.setEstado("exitoso");
            auditoriaExito.setEmailUsuario(authenticatedUser.getEmail());
            auditoriaExito.setInformacionDispositivo(deviceInfo);
            auditoriaExito.setMensaje("Inicio de sesión exitoso.");
            // Asigna los campos 'aplicacion' y 'accion' si los tienes definidos para el
            // evento de login.
            // Por ejemplo, podrías tener una aplicación 'Centro de Control de Acceso' y una
            // acción 'LOGIN_EXITOSO'.
            auditoriaAccesosService.create(auditoriaExito);

            // 7. Crear una nueva sesión en la tabla Sesiones
            Sesiones nuevaSesion = new Sesiones();
            nuevaSesion.setUsuario(authenticatedUser);
            nuevaSesion.setToken(jwt);
            nuevaSesion.setIpOrigen(clientIp);
            nuevaSesion.setEmailUsuario(authenticatedUser.getEmail());
            nuevaSesion.setInformacionDispositivo(deviceInfo);
            nuevaSesion.setFechaExpiracion(OffsetDateTime.now().plus(jwtTokenProvider.getJwtExpirationInMs(),
                    java.time.temporal.ChronoUnit.MILLIS));
            nuevaSesion.setEstado("activa");
            sesionesService.create(nuevaSesion); // Usar el servicio de sesiones

            // 8. Retornar el DTO de respuesta con el token y datos básicos del usuario
            return new LoginResponseDTO(
                    authenticatedUser.getId(),
                    authenticatedUser.getNombres(),
                    authenticatedUser.getApellidos(),
                    authenticatedUser.getEmail(),
                    jwt);

        } catch (org.springframework.security.core.AuthenticationException e) {
            // Manejo de errores de autenticación (credenciales inválidas)
            Usuarios failedUser = userToAuthenticate; // Si el usuario existe
            if (failedUser == null) {
                // Si el email no existe, creamos un objeto Usuario temporal para auditoría
                failedUser = new Usuarios(); // No persistimos, solo para auditoría
                failedUser.setEmail(loginRequest.getEmail());
            }

            // Incrementar contador de intentos fallidos y potencialmente bloquear
            failedUser.setIntentosFallidosSesion(failedUser.getIntentosFallidosSesion() + 1);
            failedUser.setFechaUltimoIntentoFallido(OffsetDateTime.now());

            if (failedUser.getIntentosFallidosSesion() >= 5) {
                failedUser.setFechaBloqueoSesion(OffsetDateTime.now().plusMinutes(15)); // Bloquear por 15 minutos
            }

            // Solo guardar si el usuario ya existe en la DB
            if (optionalUser.isPresent()) {
                usuariosRepository.save(failedUser);
            }

            // Registrar el evento de inicio de sesión fallido en AuditoriaAccesos
            AuditoriaAccesos auditoriaFallo = new AuditoriaAccesos();
            auditoriaFallo.setUsuario(optionalUser.orElse(null)); // Asigna el usuario si existe, null si no
            auditoriaFallo.setFecha(OffsetDateTime.now());
            auditoriaFallo.setIpOrigen(clientIp);
            auditoriaFallo.setEstado("fallido");
            auditoriaFallo.setEmailUsuario(loginRequest.getEmail());
            auditoriaFallo.setInformacionDispositivo(deviceInfo);
            auditoriaFallo.setMensaje("Inicio de sesión fallido: " + e.getMessage());
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
     * @throws BadRequestException Si la contraseña actual es incorrecta.
     */
    @Transactional
    public void changePassword(UUID userId, PasswordChangeRequestDTO request) {
        Usuarios user = usuariosRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado para cambiar contraseña."));

        // Verificar la contraseña actual antes de permitir el cambio
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getContrasena())) {
            // Registrar en auditoría el intento de cambio de contraseña fallido (contraseña
            // actual incorrecta)
            AuditoriaAccesos auditoriaFallo = new AuditoriaAccesos();
            auditoriaFallo.setUsuario(user);
            auditoriaFallo.setFecha(OffsetDateTime.now());
            auditoriaFallo.setIpOrigen(null); // Obtener IP del contexto si es posible
            auditoriaFallo.setEstado("fallido");
            auditoriaFallo.setEmailUsuario(user.getEmail());
            auditoriaFallo.setMensaje("Intento de cambio de contraseña fallido: contraseña actual incorrecta.");
            auditoriaAccesosService.create(auditoriaFallo);

            throw new BadRequestException("La contraseña actual es incorrecta.");
        }

        // Hashear y actualizar la nueva contraseña
        user.setContrasena(passwordEncoder.encode(request.getNewPassword()));
        user.setFechaUltimoCambioContrasena(OffsetDateTime.now()); // Actualizar la fecha del último cambio
        user.setRequiereCambioContrasena(false); // Una vez que cambia, ya no requiere

        usuariosRepository.save(user); // Persistir los cambios

        // Registrar en auditoría el cambio de contraseña exitoso
        AuditoriaAccesos auditoriaExito = new AuditoriaAccesos();
        auditoriaExito.setUsuario(user);
        auditoriaExito.setFecha(OffsetDateTime.now());
        auditoriaExito.setIpOrigen(null); // Obtener IP del contexto si es posible
        auditoriaExito.setEstado("exitoso");
        auditoriaExito.setEmailUsuario(user.getEmail());
        auditoriaExito.setMensaje("Cambio de contraseña exitoso.");
        auditoriaAccesosService.create(auditoriaExito);
    }

    /**
     * Marca una sesión como inactiva (cierre de sesión lógico).
     * 
     * @param jwtToken El token JWT que se desea invalidar.
     */
    @Transactional
    public void logout(String jwtToken) {
        // Buscar la sesión por el token y marcarla como inactiva
        sesionesService.findByToken(jwtToken).ifPresent(sesion -> {
            sesion.setEstado("cerrada"); // Cambiar estado a 'cerrada' o 'inactiva'
            sesion.setFechaExpiracion(OffsetDateTime.now()); // Invalidar inmediatamente
            sesionesService.update(sesion.getId(), sesion); // Persistir el cambio

            // Registrar en auditoría el cierre de sesión
            AuditoriaAccesos auditoriaLogout = new AuditoriaAccesos();
            auditoriaLogout.setUsuario(sesion.getUsuario());
            auditoriaLogout.setFecha(OffsetDateTime.now());
            auditoriaLogout.setIpOrigen(sesion.getIpOrigen()); // Usar la IP de la sesión
            auditoriaLogout.setEstado("exitoso");
            auditoriaLogout.setEmailUsuario(sesion.getEmailUsuario());
            auditoriaLogout.setMensaje("Cierre de sesión exitoso.");
            auditoriaAccesosService.create(auditoriaLogout);
        });
    }
}
