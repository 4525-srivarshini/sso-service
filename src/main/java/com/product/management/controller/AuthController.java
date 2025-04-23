package com.product.management.controller;

import com.product.management.entity.RegisterRequest;
import com.product.management.entity.User;
import com.product.management.entity.VerifyOtpRequest;
import com.product.management.repositry.UserRepository;
import com.product.management.service.EmailService;
import com.product.management.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        String tenantId = UUID.randomUUID().toString();
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setMobile(request.getMobile());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTenantId(tenantId);
        user.setVerified(false);

        userRepo.save(user);
        String otp = otpService.generateOtp(request.getEmail());
        emailService.sendOtp(request.getEmail(), otp);

        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        if (otpService.validateOtp(request.getEmail(), request.getOtp())) {
            User user = userRepo.findByEmail(request.getEmail()).orElseThrow();
            user.setVerified(true);
            userRepo.save(user);
            return ResponseEntity.ok("Verified successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
    }
}
