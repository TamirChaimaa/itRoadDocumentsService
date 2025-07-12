package com.itRoad.documents_service.security;

import com.itRoad.documents_service.dto.UserDto;
import com.itRoad.documents_service.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Si pas d'Authorization header, on continue sans authentification
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            Claims claims = jwtUtil.extractClaims(token);

            String username = claims.getSubject();

            if (username != null && !username.isEmpty()) {
                UserDto user = new UserDto();
                user.setUsername(username);
                user.setId(null);
                user.setRole(null);

                // 🟢 Créer l'authentification avec une autorité générique
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                List.of(new SimpleGrantedAuthority("AUTHENTICATED_USER"))
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            // 🟢 En cas d'erreur, on continue sans authentification
            // Spring Security décidera s'il faut bloquer ou non
            System.err.println("Erreur JWT: " + e.getMessage());
        }

        // 🟢 TOUJOURS continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}