package com.product.management.service;

import com.product.management.dto.*;
import com.product.management.entity.*;
import com.product.management.repository.RoleRepository;
import com.product.management.repository.TenantRepository;
import com.product.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public void register(RegisterRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Invalid role ID"));

        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Invalid tenant ID"));
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists with this email");
        }


        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setTenant(tenant);
        userRepository.save(user);
    }

    // Login method
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new JwtResponse(accessToken, refreshToken);
    }

    // Refresh token method
    public JwtResponse refresh(RefreshTokenRequest request) {
        String username = jwtService.validateRefreshToken(request.getRefreshToken());
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtService.generateToken(user);
        return new JwtResponse(newAccessToken, request.getRefreshToken());
    }
    
}
