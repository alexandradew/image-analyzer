package io.github.alexandradew.image_analyzer.exception;

import io.github.alexandradew.image_analyzer.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(400, "Validation failed", "prompt", ex.getMessage())
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(400, "Missing required field", ex.getParameterName(), "Parameter is required")
        );
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingFile(MissingServletRequestPartException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(400, "Missing required field", ex.getRequestPartName(), "Missing required file part: " + ex.getRequestPartName())
        );
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMultipart(MultipartException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(400, "Invalid request", "file", "Expected a multipart/form-data request with a file")
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.internalServerError().body(
                new ErrorResponse(500, "Unexpected error, sorry bro :(", null, ex.getMessage())
        );
    }

}