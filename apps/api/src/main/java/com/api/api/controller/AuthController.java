package com.api.api.controller;

import com.api.api.dto.RequestDTO.LoginRequestDTO;
import com.api.api.dto.RequestDTO.PasswordChangeRequestDTO;
import com.api.api.dto.ResponseDTO.LoginResponseDTO;
import com.api.api.dto.ResponseDTO.ErrorResponseDTO;
import com.api.api.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.api.api.security.CustomUserDetails;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para la gestión de usuarios y autenticación en el CCA.")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para iniciar sesión y obtener un token JWT.
     * 
     * @param loginRequest DTO con credenciales (email y contraseña).
     * @param request      HttpServletRequest para obtener IP y User-Agent.
     * @return ResponseEntity con LoginResponseDTO y token JWT si es exitoso, o
     *         ErrorResponseDTO si falla.
     */
    @Operation(summary = "Iniciar sesión de usuario", description = "Autentica a un usuario con email y contraseña, y devuelve un token JWT para futuras solicitudes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas o cuenta bloqueada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest,
            HttpServletRequest request) {
        String clientIp = getClientIp(request);
        String deviceInfo = request.getHeader("User-Agent");
        LoginResponseDTO response = authService.authenticateUser(loginRequest, clientIp, deviceInfo);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para cambiar la contraseña de un usuario autenticado.
     * 
     * @param request            DTO con la contraseña actual y la nueva contraseña.
     * @param httpServletRequest HttpServletRequest para obtener la ruta de la
     *                           solicitud.
     * @return ResponseEntity indicando éxito o fallo.
     */
    @Operation(summary = "Cambiar contraseña de usuario", description = "Permite a un usuario autenticado cambiar su contraseña actual por una nueva.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta o nueva contraseña no cumple los requisitos.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequestDTO request,
            HttpServletRequest httpServletRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UUID userId = userDetails.getId();

        authService.changePassword(userId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para cerrar la sesión de un usuario (invalidar el token JWT).
     * 
     * @param request HttpServletRequest para obtener el token JWT.
     * @return ResponseEntity indicando éxito o fallo.
     */
    @Operation(summary = "Cerrar sesión de usuario", description = "Invalida el token JWT del usuario, marcando la sesión como cerrada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sesión cerrada exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado o token inválido.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String jwtToken = authHeader.substring(7);

        authService.logout(jwtToken);
        return ResponseEntity.ok().build();
    }

    /**
     * Helper para extraer la IP del cliente.
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
