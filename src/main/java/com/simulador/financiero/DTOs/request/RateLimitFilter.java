package com.simulador.financiero.DTOs.request;

import com.simulador.financiero.Exceptions.RequestDenied;
import com.simulador.financiero.services.LimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final LimiterService limiterService;

    public RateLimitFilter(LimiterService limiterService) {
        this.limiterService = limiterService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            limiterService.allowRequest();

            filterChain.doFilter(request, response);

        } catch (RequestDenied ex) {

            response.setStatus(429);
            response.setContentType("application/json");

            response.getWriter().write("""
                    {
                        "status": 429,
                        "error": "Too Many Requests",
                        "message": "%s",
                        "path": "%s"
                    }
                    """.formatted(
                            ex.getMessage(),
                            request.getRequestURI()
                    ));
        }
    }
}