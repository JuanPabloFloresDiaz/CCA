package com.api.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret; // La clave secreta para firmar los JWT

    @Value("${app.jwt.expiration}")
    private int jwtExpirationInMs; // Tiempo de expiración del token en milisegundos

    @Value("${app.jwt.algorithm}")
    private String algorithm; // Algoritmo de la clave, por ejemplo, "HmacSHA512"

    @Value("${app.jwt.iterations}")
    private int iterations; // Número de iteraciones para PBKDF2

    @Value("${app.jwt.key-length}")
    private int keyLength; // Longitud de la clave en bits para PBKDF2

    @Value("${app.jwt.salt}")
    private String jwtSalt; // Salt para la derivación de la clave PBKDF2

    // Método para decodificar el salt de Base64
    private byte[] salt() {
        return Base64.getDecoder().decode(jwtSalt);
    }

    // Método para generar la clave de firma JWT usando PBKDF2
    private SecretKey getSigningKey() {
        try {
            // Se utiliza PBKDF2WithHmacSHA512 para derivar una clave fuerte del secreto
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            KeySpec spec = new PBEKeySpec(
                    jwtSecret.toCharArray(), // El secreto JWT como array de caracteres
                    salt(), // El salt decodificado
                    iterations, // Número de iteraciones
                    keyLength // Longitud de la clave
            );
            // Crea una SecretKeySpec a partir de la clave derivada y el algoritmo de firma
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), algorithm);
        } catch (Exception e) {
            logger.error("Error generando clave JWT: {}", e.getMessage());
            throw new RuntimeException("Error generando clave JWT", e);
        }
    }

    /**
     * Genera un token JWT para un usuario autenticado.
     * 
     * @param authentication Objeto Authentication de Spring Security.
     * @return El token JWT generado.
     */
    public String generateToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal(); // Obtiene los detalles del
                                                                                           // usuario
        Date now = new Date(); // Fecha y hora actual
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs); // Fecha y hora de expiración del token

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // El email del usuario como 'subject' del token
                .claim("userId", userDetails.getId().toString()) // Añade el ID del usuario como un claim personalizado
                .setIssuedAt(now) // Fecha de emisión del token
                .setExpiration(expiryDate) // Fecha de expiración del token
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // Firma el token con la clave secreta y el
                                                                     // algoritmo HS512
                .compact(); // Construye el token JWT
    }

    /**
     * Obtiene el nombre de usuario (email) del token JWT.
     * 
     * @param token El token JWT.
     * @return El nombre de usuario (email).
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Establece la clave de firma para parsear el token
                .build()
                .parseClaimsJws(token) // Parsea el token JWT
                .getBody(); // Obtiene el cuerpo de los claims
        return claims.getSubject(); // Retorna el 'subject' (que es el email del usuario)
    }

    /**
     * Obtiene el ID de usuario del token JWT.
     * 
     * @param token El token JWT.
     * @return El ID de usuario como String.
     */
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", String.class); // Retorna el claim 'userId'
    }

    /**
     * Valida la integridad y la expiración de un token JWT.
     * 
     * @param token El token JWT.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Establece la clave de firma
                    .build()
                    .parseClaimsJws(token); // Intenta parsear y validar el token
            return true;
        } catch (SignatureException ex) {
            logger.error("Firma JWT inválida: {}", ex.getMessage()); // Error de firma inválida
        } catch (MalformedJwtException ex) {
            logger.error("Token JWT mal formado: {}", ex.getMessage()); // Token mal formado
        } catch (ExpiredJwtException ex) {
            logger.error("Token JWT expirado: {}", ex.getMessage()); // Token expirado
        } catch (UnsupportedJwtException ex) {
            logger.error("Token JWT no soportado: {}", ex.getMessage()); // Token no soportado
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string está vacío: {}", ex.getMessage()); // Claims vacíos
        } catch (JwtException ex) {
            logger.error("Error general JWT: {}", ex.getMessage()); // Cualquier otra excepción JWT
        }
        return false; // El token no es válido
    }

    // Método para obtener el tiempo de expiración en milisegundos (útil para la
    // sesión)
    public int getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }
}
