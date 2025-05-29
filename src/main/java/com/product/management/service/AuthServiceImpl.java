package com.product.management.service;

import com.product.management.constants.RoleConstants;
import com.product.management.dto.*;
import com.product.management.entity.*;
import com.product.management.exceptions.BadRequestException;
import com.product.management.exceptions.ForbiddenException;
import com.product.management.exceptions.InvalidCredentialsException;
import com.product.management.exceptions.ResourceNotFoundException;
import com.product.management.repository.RoleRepository;
import com.product.management.repository.TenantRepository;
import com.product.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentRole = auth.getAuthorities().iterator().next().getAuthority();


        // Restrict college admin from creating super admins
        if (currentRole.equals("ROLE_COLLEGE_ADMIN") && RoleConstants.SUPER_ADMIN.equals(role.getName())) {
            throw new ForbiddenException("COLLEGE_ADMIN is not allowed to register SUPER_ADMINs");
        }

        // SUPER_ADMIN should not have tenantId
        if (RoleConstants.SUPER_ADMIN.equals(role.getName()) && request.getTenantId() != null) {
            throw new BadRequestException("SUPER_ADMIN should not have a tenant ID");
        }

        // Other roles must have a tenantId
        if (!RoleConstants.SUPER_ADMIN.equals(role.getName()) && request.getTenantId() == null) {
            throw new BadRequestException("Tenant ID is required for this role");
        }
        Tenant tenant = null;

        if (request.getTenantId() != null) {
            tenant = tenantRepository.findById(request.getTenantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));
        }
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

    public List<String> registerBulk(List<RegisterRequest> requests) {
        List<String> failed = new ArrayList<>();

        for (RegisterRequest request : requests) {
            try {
                this.register(request); // reuse existing single register method
            } catch (Exception e) {
                failed.add(request.getEmail() + ": " + e.getMessage());
            }
        }
        return failed;
    }

    // Login method
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
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
