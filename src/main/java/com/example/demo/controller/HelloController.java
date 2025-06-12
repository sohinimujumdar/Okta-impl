package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/welcome")
    public String home() {
        System.out.println(">> /welcome endpoint called");
        return "Welcome to the Spring Boot Okta JWT App!";
    }

    @GetMapping("/secure")
    public String secureApi(Authentication authentication,
                            @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient authorizedClient) {

        System.out.println(">> /secure endpoint called");

        try {
            if (authentication == null || authorizedClient == null) {
                throw new RuntimeException("Authentication or authorized client is missing.");
            }

            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

            String fullName = oidcUser.getFullName();
            String email = oidcUser.getEmail();
            String accessToken = authorizedClient.getAccessToken().getTokenValue();
            String idToken = oidcUser.getIdToken().getTokenValue();
            ZonedDateTime accessTokenExpiry = authorizedClient.getAccessToken().getExpiresAt()
                    .atZone(ZoneId.of("Asia/Kolkata"));

            return """
                Hello, %s<br>
                Email: %s<br><br>
                <strong>Access Token:</strong><br>
                %s<br><br>
                <strong>ID Token:</strong><br>
                %s<br><br>
                <strong>Access Token Expires At (IST):</strong><br>
                %s
                """.formatted(fullName, email, accessToken, idToken, accessTokenExpiry);

        } catch (ClassCastException e) {
            throw new RuntimeException("Invalid user details in authentication token.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to process secure endpoint: " + e.getMessage());
        }
    }

    @RestController
    public static class DebugController {

        @GetMapping("/debug")
        public Map<String, Object> debug(@AuthenticationPrincipal OidcUser oidcUser) {
            if (oidcUser == null) {
                return Map.of("error", "No OIDC user found. Are you logged in?");
            }

            return Map.of(
                    "name", oidcUser.getFullName(),
                    "email", oidcUser.getEmail(),
                    "authorities", oidcUser.getAuthorities(),
                    "claims", oidcUser.getClaims()
            );
        }
    }
}
