package com.example.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the Spring Boot Okta JWT App!";
    }

    @GetMapping("/public")
    public String publicApi() {
        return "This is a public endpoint.";
    }

    @GetMapping("/secure")
    public String secureApi(@AuthenticationPrincipal OidcUser user) {
        return "Hello, " + user.getFullName() + "<br>" +
                "Email: " + user.getEmail() + "<br><br>" +
                "ID Token:<br>" + user.getIdToken().getTokenValue();
    }
    // ✅ Add this endpoint
    @GetMapping("/users/me")
    public String getCurrentUser(@AuthenticationPrincipal OidcUser user) {
        return "User Info:<br>" +
                "Name: " + user.getFullName() + "<br>" +
                "Email: " + user.getEmail() + "<br>" +
                "Username: " + user.getPreferredUsername();
    }
}
