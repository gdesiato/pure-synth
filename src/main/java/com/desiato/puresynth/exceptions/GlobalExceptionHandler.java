package com.desiato.puresynth.exceptions;

import com.desiato.puresynth.dtos.ErrorLoginResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorLoginResponseDTO> handleAuthenticationException(
            AuthenticationException e) {
        ErrorLoginResponseDTO response = new ErrorLoginResponseDTO(
                "Authentication failed. Please check your credentials and try again.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorLoginResponseDTO> handleInvalidTokenException(
            BadCredentialsException e) {
        ErrorLoginResponseDTO response = new ErrorLoginResponseDTO(
                "Unauthorized: Invalid or missing token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
