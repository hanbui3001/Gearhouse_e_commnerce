package com.example.demo_ecommerce.security;

import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.time.LocalTime;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ErrorResponse<Object> errorResponse = ErrorResponse.builder()
                .status(errorCode.getCode())
                .message(errorCode.getMessage())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .timestamp(LocalTime.now())
                .path(request.getRequestURI())
                .build();
        JsonMapper jsonMapper = new JsonMapper();
        response.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
        response.flushBuffer();
    }
}
