package com.example.hospital_manager.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ErrorResponseWriter {

    public void write(HttpServletRequest req, HttpServletResponse res, HttpStatus status, String message) throws IOException {
        res.setStatus(status.value());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");

        String safeMessage = (message == null) ? "" : message.replace("\"", "\\\"");
        String json = String.format(
            "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            safeMessage,
            req.getRequestURI()
        );

        res.getWriter().write(json);
    }
}