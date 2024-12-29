package com.example.MedCore.modules.security.securityUser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null) {
            System.out.println("Authorization header is missing");
        } else {
            System.out.println("Authorization header: " + bearerToken);
        }

        String token = jwtUtil.getTokenFromRequest(request);
        if (token != null) {
            if (jwtUtil.isValidToken(token)) {
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);  // Set the authentication context
                System.out.println("Authentication set for user: " + authentication.getName());
            } else {
                System.out.println("Invalid token");
            }
        } else {
            System.out.println("No token provided");
        }

        filterChain.doFilter(request, response);
    }
}
