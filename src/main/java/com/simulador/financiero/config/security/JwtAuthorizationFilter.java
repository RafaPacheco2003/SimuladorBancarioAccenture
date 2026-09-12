package com.simulador.financiero.config.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsServiceImpl userDetailsServiceImpl;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  private static final String[] PUBLIC_URLS = {
      "/api/v1/auth/**",
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/v3/api-docs/**"
  };

  public JwtAuthorizationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsServiceImpl) {
    this.jwtService = jwtService;
    this.userDetailsServiceImpl = userDetailsServiceImpl;
  }

  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    boolean isPublicPath = Arrays.stream(PUBLIC_URLS)
        .anyMatch(path -> pathMatcher.match(path, request.getRequestURI()));

    if (isPublicPath) {
      filterChain.doFilter(request, response);
      return;
    }

    String BearerToken = request.getHeader("Authorization");

    if (Objects.nonNull(BearerToken) && BearerToken.startsWith("Bearer ")) {
      String token = BearerToken.substring(7);

      if (jwtService.isTokenValid(token)) {
        String username = jwtService.getUsernameFromToken(token);
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
            null,
            userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }

    filterChain.doFilter(request, response);
  }

}