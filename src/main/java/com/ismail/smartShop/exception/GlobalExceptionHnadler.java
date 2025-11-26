package com.ismail.smartShop.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ismail.smartShop.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHnadler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest req) {

        List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(err -> err.getField() + " : "+ err.getDefaultMessage())
                                .toList();

        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("there is err in the validation").details(errors).build());
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handelIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest req) {
        return buildError(ex.getMessage(), req, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handelGeneral(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildError("Internal server error", request, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    private ResponseEntity<ErrorResponse> buildError(String message , HttpServletRequest req, HttpStatus status) {
        ErrorResponse error = ErrorResponse.builder()
        .status(status.value())
        .message(message)
        .path(req.getRequestURI())
        .timestamp(LocalDateTime.now())
        .build();
        return ResponseEntity.ok().body(error);
    }
}
