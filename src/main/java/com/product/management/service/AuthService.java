package com.product.management.service;

import com.product.management.entity.*;

import java.util.UUID;

public interface AuthService {

    public JwtResponse login(LoginRequest request);

    public JwtResponse refresh(RefreshTokenRequest request);

    public void forgotPassword(ForgotPasswordRequest request);

    public void resetPassword(ResetPasswordRequest request);

    public UserProfileResponse me(String email);
}
