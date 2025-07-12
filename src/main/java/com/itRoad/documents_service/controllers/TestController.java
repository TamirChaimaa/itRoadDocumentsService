package com.itRoad.documents_service.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documents")
public class TestController {

    @GetMapping("/test-auth")
    public String testAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("=== TEST AUTH ENDPOINT ===");
        System.out.println("Authentication: " + auth);
        System.out.println("Is Authenticated: " + (auth != null && auth.isAuthenticated()));
        System.out.println("Principal: " + (auth != null ? auth.getPrincipal() : "null"));
        System.out.println("Authorities: " + (auth != null ? auth.getAuthorities() : "null"));

        if (auth != null && auth.isAuthenticated()) {
            return "✅ Authentifié ! Utilisateur: " + auth.getName() + ", Autorités: " + auth.getAuthorities();
        } else {
            return "❌ Non authentifié";
        }
    }
}