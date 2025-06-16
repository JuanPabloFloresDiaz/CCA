package com.api.api.exception;

import com.api.api.dto.ResponseDTO.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de tipo ResourceNotFoundException (HTTP 404 Not Found).
     * @param ex La excepción ResourceNotFoundException.
     * @param request La solicitud web actual.
     * @return ResponseEntity con ErrorResponseDTO y HttpStatus.NOT_FOUND.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            ex.getClass().getSimpleName(),
            OffsetDateTime.now(),
            request.getDescription(false).replace("uri=", "") // Extrae la ruta de la URI
        );
        System.err.println("ResourceNotFoundException en " + request.getDescription(false) + ": " + ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de tipo BadRequestException (HTTP 400 Bad Request).
     * @param ex La excepción BadRequestException.
     * @param request La solicitud web actual.
     * @return ResponseEntity con ErrorResponseDTO y HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            ex.getClass().getSimpleName(),
            OffsetDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        System.err.println("BadRequestException en " + request.getDescription(false) + ": " + ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de validación de campos (@Valid en DTOs) (HTTP 400 Bad Request).
     * @param ex La excepción MethodArgumentNotValidException.
     * @param request La solicitud web actual.
     * @return ResponseEntity con ErrorResponseDTO y HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            "Error de validación: " + errors,
            HttpStatus.BAD_REQUEST.value(),
            ex.getClass().getSimpleName(),
            OffsetDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        System.err.println("ValidationException en " + request.getDescription(false) + ": " + errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Maneja IllegalArgumentException (HTTP 409 Conflict o 400 Bad Request).
     * Utilizado para casos como "llave identificadora ya existe".
     * @param ex La excepción IllegalArgumentException.
     * @param request La solicitud web actual.
     * @return ResponseEntity con ErrorResponseDTO y HttpStatus.CONFLICT o HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // Por defecto
        if (ex.getMessage() != null && (ex.getMessage().contains("ya está en uso") || ex.getMessage().contains("ya existe"))) {
            status = HttpStatus.CONFLICT; // 409 Conflict para duplicados
        }
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            ex.getMessage(),
            status.value(),
            ex.getClass().getSimpleName(),
            OffsetDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        System.err.println("IllegalArgumentException en " + request.getDescription(false) + ": " + ex.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }


    /**
     * Maneja cualquier otra excepción no capturada (HTTP 500 Internal Server Error).
     * Este es el manejador de "último recurso".
     * @param ex La excepción genérica.
     * @param request La solicitud web actual.
     * @return ResponseEntity con ErrorResponseDTO y HttpStatus.INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(
            Exception ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            "Ocurrió un error inesperado en el servidor.",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.getClass().getSimpleName(),
            OffsetDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        System.err.println("Excepción global en " + request.getDescription(false) + ": " + ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
