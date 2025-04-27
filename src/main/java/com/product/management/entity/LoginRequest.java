package com.product.management.entity;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String mobile;
    private String password;
}
