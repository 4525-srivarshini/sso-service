package com.product.management.controller;

import com.product.management.dto.*;
import com.product.management.entity.Tenant;
import com.product.management.service.AuthService;
import com.product.management.utils.ExcelHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/register/bulk")
    public ResponseEntity<String> registerMultipleUsers(@Valid @RequestBody List<RegisterRequest> requests) {
        List<String> failed = authService.registerBulk(requests);
        return failed.isEmpty()
                ? ResponseEntity.ok("All users registered successfully")
                : ResponseEntity.status(HttpStatus.MULTI_STATUS).body(failed.toString());    }

    @PostMapping("/register/bulk/excel")
    public ResponseEntity<?> registerBulkUsers(@RequestParam("file") MultipartFile file) {
        if (!ExcelHelper.hasExcelFormat(file)) {
            return ResponseEntity.badRequest().body("Please upload an Excel file!");
        }
        try {
            List<RegisterRequest> users = ExcelHelper.excelToRegisterRequests(file.getInputStream());

            // Use your existing bulk register method here
            authService.registerBulk(users);

            return ResponseEntity.ok("All users registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process file or register users: " + e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

}
