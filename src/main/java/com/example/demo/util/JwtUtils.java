package com.example.demo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.List;
import java.util.Map;

public class JwtUtils {

    public static Map<String, Object> parseAccessTokenPayload(String token) {
        try {
            String[] chunks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse access token", e);
        }
    }

    public static List<String> extractRoles(String token) {
        Map<String, Object> claims = parseAccessTokenPayload(token);

        if (claims.containsKey("roles")) {
            return (List<String>) claims.get("roles");
        }

        if (claims.containsKey("groups")) {
            return (List<String>) claims.get("groups");
        }

        return null;
    }

    public static String determineCurrentRole(List<String> roles) {
        if (roles == null || roles.isEmpty()) return "UNKNOWN";
        if (roles.contains("ROLE_ADMIN") || roles.contains("ADMIN")) return "ADMIN";
        if (roles.contains("ROLE_USER") || roles.contains("USER")) return "USER";
        return String.join(",", roles);
    }
}
