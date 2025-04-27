package com.product.management.entity;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String password;
    private String token;
}
