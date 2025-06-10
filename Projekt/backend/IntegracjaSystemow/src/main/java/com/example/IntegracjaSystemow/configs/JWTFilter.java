package com.example.IntegracjaSystemow.configs;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@Log4j2
public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jWTService;

    public JWTFilter(JWTService jWTService) {
        this.jWTService = jWTService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header =request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ") || header.replace("Bearer ", "").isBlank() || header.replace("Bearer ", "").equals("null")){
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.replace("Bearer ", "");
        try {
            String username = jWTService.getUsername(token);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex){
            filterChain.doFilter(request, response);
        }
    }
}
