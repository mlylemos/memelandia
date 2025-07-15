package com.memelandia.shared;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorDTO> handleNotFound(EntityNotFoundException ex) {
    ErrorDTO body = new ErrorDTO("NOT_FOUND", ex.getMessage());
    return ResponseEntity.status(404).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDTO> handleAll(Exception ex) {
    ErrorDTO body = new ErrorDTO("INTERNAL_ERROR", "Ocorreu um erro inesperado");
    return ResponseEntity.status(500).body(body);
  }

  public static class ErrorDTO {
    public String code;
    public String message;
    public ErrorDTO(String code, String message) {
      this.code = code;
      this.message = message;
    }
  }
}
