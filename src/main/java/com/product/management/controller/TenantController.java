package com.product.management.controller;

import com.product.management.entity.Tenant;
import com.product.management.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @GetMapping("/tenants")
    public List<Tenant> getAllTenants() {
        return tenantService.getAllTenants();
    }
}
