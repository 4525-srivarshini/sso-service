package com.product.management.service;

import com.product.management.dto.*;
import com.product.management.entity.Tenant;
import java.util.List;

public interface AuthService {
    public void register(RegisterRequest request);
    public List<String> registerBulk(List<RegisterRequest> requests);
    public JwtResponse login(LoginRequest request);
    public JwtResponse refresh(RefreshTokenRequest request);
}
