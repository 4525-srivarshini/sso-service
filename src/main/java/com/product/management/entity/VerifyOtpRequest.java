package com.product.management.entity;


import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String email;
    private String otp;
}