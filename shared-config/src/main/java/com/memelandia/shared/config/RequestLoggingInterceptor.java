package com.memelandia.shared.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        MDC.put("method", request.getMethod());
        MDC.put("url", request.getRequestURL().toString());
        MDC.put("remoteAddr", request.getRemoteAddr());
        
        logger.info("Incoming request: {} {} from {}", 
                   request.getMethod(), 
                   request.getRequestURL(), 
                   request.getRemoteAddr());
        
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        
        MDC.put("duration", String.valueOf(duration));
        MDC.put("status", String.valueOf(response.getStatus()));
        
        if (ex != null) {
            logger.error("Request completed with error: {} {} - Status: {} - Duration: {}ms - Error: {}", 
                        request.getMethod(), 
                        request.getRequestURL(), 
                        response.getStatus(), 
                        duration, 
                        ex.getMessage());
        } else {
            logger.info("Request completed: {} {} - Status: {} - Duration: {}ms", 
                       request.getMethod(), 
                       request.getRequestURL(), 
                       response.getStatus(), 
                       duration);
        }
        
        MDC.clear();
    }
}