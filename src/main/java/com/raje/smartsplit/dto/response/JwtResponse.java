package com.raje.smartsplit.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private final List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
