package com.product.management.service;

import com.product.management.entity.Tenant;
import com.product.management.repository.TenantRepository;
import com.product.management.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }
}
