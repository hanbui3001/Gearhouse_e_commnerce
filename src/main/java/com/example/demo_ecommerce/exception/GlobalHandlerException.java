package com.example.demo_ecommerce.exception;

import com.example.demo_ecommerce.dto.response.ApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e, WebRequest webRequest) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getCode())
                .message(errorCode.getMessage())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .timestamp( LocalTime.now())
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .build();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e, WebRequest webRequest) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errrors = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        String messsage = String.join(",", errrors);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(messsage)
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp( LocalTime.now())
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e, WebRequest webRequest) {
        ErrorCode errorCode = ErrorCode.USER_EXISTED;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getCode())
                .message(errorCode.getMessage())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .timestamp( LocalTime.now())
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .build();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }
}
