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
    CANNOT_SELF_REVOKE_ADMIN(401, "Cannot Self Revoke Admin", HttpStatus.UNAUTHORIZED),
    ROLE_INVALID(400, "Invalid Role", HttpStatus.BAD_REQUEST),
    ROLE_NOT_ASSIGN_TO_USER(400, "Role not assigned to user", HttpStatus.BAD_REQUEST),
    COOKIE_REQUIRED(401, "Cookie Required", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_FOUND(404, "Role not found", HttpStatus.NOT_FOUND),
    ROLE_REQUIRED(404, "Role Required", HttpStatus.UNAUTHORIZED),
    ROLE_EXISTED(409, "Role existed", HttpStatus.CONFLICT),
    SOCIAL_ACCOUNT_ALREADY_LINK(401, "Social Account Already Link", HttpStatus.UNAUTHORIZED),

    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "Forbidden", HttpStatus.FORBIDDEN),

    GENERATE_JWT_ERROR(500, "Generate JWT Error", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_SEND_FAILED(500, "Email Send Failed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_JWT_TOKEN(502, "Only Access Token Is Allow", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(401, "Token Expired", HttpStatus.UNAUTHORIZED),


    CATEGORY_EXISTED(409, "Category existed", HttpStatus.CONFLICT),
    CATEGORY_NOT_FOUND(404, "Category not found", HttpStatus.NOT_FOUND),
    BRAND_NOT_FOUND(404, "Brand not found", HttpStatus.NOT_FOUND),

    FILE_LIMIT_CAPACITY(500, "File Limit Exceeded", HttpStatus.INTERNAL_SERVER_ERROR),
    UNSUPPORTED_MEDIA_TYPE(500, "Unsupported Media Type", HttpStatus.INTERNAL_SERVER_ERROR),
    ADDRESS_NOT_FOUND(404, "Address not found", HttpStatus.NOT_FOUND),
    ;
    private int code;
    private String message;
    private HttpStatus httpStatus;
}
