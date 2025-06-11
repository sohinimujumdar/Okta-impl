package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        String fullName = oidcUser.getFullName();
        String email = oidcUser.getEmail();
        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        String idToken = oidcUser.getIdToken().getTokenValue();
        ZonedDateTime accessTokenExpiry = authorizedClient.getAccessToken().getExpiresAt()
                .atZone(ZoneId.of("Asia/Kolkata"));

        // Console output for debugging
        System.out.println("Authenticated user:");
        System.out.println("  Full Name: " + fullName);
        System.out.println("  Email: " + email);
        System.out.println("  Access Token: " + accessToken);
        System.out.println("  ID Token: " + idToken);
        System.out.println("  Access Token Expiry (IST): " + accessTokenExpiry);

        return """
                Hello, %s<br>
                Email: %s<br><br>

                <strong>Access Token:</strong><br>
                %s<br><br>

                <strong>ID Token:</strong><br>
                %s<br><br>

                <strong>Access Token Expires At (IST):</strong><br>
                %s
                """.formatted(
                fullName,
                email,
                accessToken,
                idToken,
                accessTokenExpiry
        );
    }
}
