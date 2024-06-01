package com.desiato.puresynth.exceptions;

import com.desiato.puresynth.dtos.ErrorLoginResponseDTO;
import com.desiato.puresynth.dtos.LoginResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<LoginResponseDTO> handleAuthenticationException(AuthenticationException e) {
        ErrorLoginResponseDTO response = new ErrorLoginResponseDTO(e.getMessage(), false);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
    }
}
