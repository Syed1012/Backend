package com.example.ai_vms.config;

import com.example.ai_vms.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // JWT Token is in the form "Bearer token"
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);

            try {
                username = jwtUtil.extractUsername(jwtToken);
                Long userId = jwtUtil.extractUserId(jwtToken);  // Extract userId
                Integer role = jwtUtil.extractRole(jwtToken);    // Extract role
                
                // Here you can pass userId and role to security context if needed
                System.out.println("Extracted userId: " + userId);
                System.out.println("Extracted role: " + role);

            } catch (Exception e) {
                logger.error("JWT Token processing failed", e);
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // You could set the Authentication here if required, similar to your old logic
        }
        chain.doFilter(request, response);
    }
}
