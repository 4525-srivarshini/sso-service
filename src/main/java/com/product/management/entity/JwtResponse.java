package com.product.management.entity;

import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;
    private String refreshToken;

    public JwtResponse(String token, String refreshToken) {
        this.accessToken = token;
        this.refreshToken = refreshToken;
    }
}