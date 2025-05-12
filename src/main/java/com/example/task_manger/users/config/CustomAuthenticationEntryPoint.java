package com.example.task_manger.users.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        response.setContentType("application/json");

        response.getWriter().write(
                "{\"message\": \"Authentication failed. Please log in to continue.\"\n" +
                        " \"error\": \"Unauthorized\" \n" +
                        " \"status\": \"401\" \n" +
                        " \"timestamp\": "+ LocalDateTime.now()+" }");

}
}
