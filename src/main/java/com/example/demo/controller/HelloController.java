package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.util.JwtUtils;

import java.util.Base64;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/welcome")
    public String home() {
        System.out.println(">> /welcome endpoint called");
        return "Welcome to the Spring Boot Okta JWT App!";
    }
    @GetMapping("/secure")
    public Map<String, Object> secureApi(
            @AuthenticationPrincipal OidcUser oidcUser,
            @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient authorizedClient) {

        System.out.println(">> /secure endpoint HIT");

        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        String idToken = oidcUser.getIdToken().getTokenValue();

        List<String> roles = JwtUtils.extractRoles(accessToken);
        String currentRole = JwtUtils.determineCurrentRole(roles);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("fullName", oidcUser.getFullName());
        response.put("email", oidcUser.getEmail());
        response.put("accessToken", accessToken);
        response.put("idToken", idToken);
        response.put("roles", roles);
        response.put("currentRole", currentRole);
        response.put("accessTokenExpiresAtIST",
                authorizedClient.getAccessToken().getExpiresAt()
                        .atZone(ZoneId.of("Asia/Kolkata")));

        return response;
    }
}