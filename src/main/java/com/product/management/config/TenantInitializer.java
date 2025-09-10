//package com.product.management.config;
//
//import com.product.management.entity.Tenant;
//import com.product.management.repository.TenantRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TenantInitializer {
//
//    @Autowired
//    private TenantRepository tenantRepository;
//
//    @PostConstruct
//    public void init() {
//        createTenantIfNotExists("MVGR College", "mvgr");
//        createTenantIfNotExists("Andhra University", "and");
//    }
//
//    private void createTenantIfNotExists(String name, String subdomain) {
//            Tenant tenant = new Tenant();
//            tenant.setName(name);
//            tenant.setSubdomain(subdomain);
//            tenantRepository.save(tenant);
//            System.out.println("Initialized tenant: " + name + " (" + subdomain + ")");
//    }
//}
