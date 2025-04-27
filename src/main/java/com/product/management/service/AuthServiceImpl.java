package com.product.management.service;

import com.product.management.entity.*;
import com.product.management.repositry.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class AuthServiceImpl implements AuthService{
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private JwtService jwtService;

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

        public JwtResponse refresh(RefreshTokenRequest request) {
            String username = jwtService.validateRefreshToken(request.getRefreshToken());
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String newAccessToken = jwtService.generateToken(user);
            return new JwtResponse(newAccessToken, request.getRefreshToken());
        }

        public void forgotPassword(ForgotPasswordRequest request) {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            userRepository.save(user);
            System.out.println("Reset your password using this token: " + resetToken);
        }

        public void resetPassword(ResetPasswordRequest request) {
            User user = userRepository.findByResetToken(request.getToken())
                    .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setResetToken(null);
            userRepository.save(user);
        }

        public UserProfileResponse me(String email) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return new UserProfileResponse(user.getEmail());
        }
    }
