package com.product.management.repository;

import com.product.management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByName(String name);
    Optional<Role> findByName(String name);
}
