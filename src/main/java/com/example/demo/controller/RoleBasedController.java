package com.example.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RoleBasedController {

    @RestController
    @RequestMapping("/admin")
    public static class AdminController {
        @GetMapping
        public String adminOnly() {
            return "Welcome, Admin!";
        }
    }

    @RestController
    @RequestMapping("/user")
    public static class UserController {
        @GetMapping
        public String userOnly() {
            return "Welcome, User!";
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
