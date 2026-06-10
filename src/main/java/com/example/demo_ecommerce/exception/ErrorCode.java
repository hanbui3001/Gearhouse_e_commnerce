package com.example.demo_ecommerce.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    USER_EXISTED(409, "User existed", HttpStatus.CONFLICT),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    GENERATE_JWT_ERROR(500, "Generate JWT Error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_JWT_TOKEN(502, "Only Access Token Is Allow", HttpStatus.UNAUTHORIZED),
    ROLE_INVALID(400,  "Invalid Role", HttpStatus.BAD_REQUEST),
    ;
    private int code;
    private String message;
    private HttpStatus httpStatus;
}
