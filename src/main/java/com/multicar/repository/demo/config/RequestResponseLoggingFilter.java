package com.multicar.repository.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final int MAX_BODY_SIZE = 1024 * 1024; // 1MB

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Wrap request and response to allow reading body multiple times
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, MAX_BODY_SIZE);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        try {
            // Process the request
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            
            // Log request
            logRequest(wrappedRequest, timestamp);
            
            // Log response
            logResponse(wrappedResponse, duration, timestamp);
            
            // Copy response body back to original response
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, String timestamp) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n").append("=".repeat(80)).append("\n");
        logMessage.append("REQUEST LOG - ").append(timestamp).append("\n");
        logMessage.append("-".repeat(80)).append("\n");
        logMessage.append("Method      : ").append(request.getMethod()).append("\n");
        logMessage.append("URI         : ").append(request.getRequestURI()).append("\n");
        logMessage.append("Query String: ").append(request.getQueryString() != null ? request.getQueryString() : "N/A").append("\n");
        logMessage.append("Remote Addr : ").append(request.getRemoteAddr()).append("\n");
        logMessage.append("Remote Host : ").append(request.getRemoteHost()).append("\n");
        
        // Log headers
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        if (!headers.isEmpty()) {
            logMessage.append("Headers     : ").append("\n");
            headers.forEach((key, value) -> logMessage.append("              ").append(key).append(": ").append(value).append("\n"));
        }
        
        // Log request body
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            String body = getContentAsString(content, request.getCharacterEncoding());
            logMessage.append("Body        : ").append(body).append("\n");
        }
        
        logMessage.append("=".repeat(80));
        log.info(logMessage.toString());
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration, String timestamp) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n").append("=".repeat(80)).append("\n");
        logMessage.append("RESPONSE LOG - ").append(timestamp).append("\n");
        logMessage.append("-".repeat(80)).append("\n");
        logMessage.append("Status Code : ").append(response.getStatus()).append("\n");
        logMessage.append("Duration    : ").append(duration).append(" ms").append("\n");
        
        // Log response headers
        Map<String, String> headers = new HashMap<>();
        response.getHeaderNames().forEach(headerName -> 
            headers.put(headerName, response.getHeader(headerName))
        );
        if (!headers.isEmpty()) {
            logMessage.append("Headers     : ").append("\n");
            headers.forEach((key, value) -> logMessage.append("              ").append(key).append(": ").append(value).append("\n"));
        }
        
        // Log response body
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String body = getContentAsString(content, response.getCharacterEncoding());
            logMessage.append("Body        : ").append(body).append("\n");
        }
        
        logMessage.append("=".repeat(80));
        log.info(logMessage.toString());
    }

    private String getContentAsString(byte[] content, String encoding) {
        try {
            return new String(content, encoding != null ? encoding : "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(content);
        }
    }
}

