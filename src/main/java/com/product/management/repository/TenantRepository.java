package com.product.management.repository;

import com.product.management.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID>  {
    Optional<Tenant> findByName(String name);
    boolean existsByName(String name);
    boolean existsBySubdomain(String subdomain);
    Optional<Tenant> findBySubdomain(String subdomain);

}
