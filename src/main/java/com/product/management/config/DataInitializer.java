package com.product.management.config;

import com.product.management.constants.RoleConstants;
import com.product.management.entity.Role;
import com.product.management.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            addRoleIfNotExists(roleRepository, RoleConstants.STUDENT);
            addRoleIfNotExists(roleRepository, RoleConstants.FACULTY);
            addRoleIfNotExists(roleRepository, RoleConstants.LIBRARIAN);
            addRoleIfNotExists(roleRepository, RoleConstants.PROJECT_HEAD);
            addRoleIfNotExists(roleRepository, RoleConstants.HOD);
            addRoleIfNotExists(roleRepository, RoleConstants.COLLEGE_ADMIN);
            addRoleIfNotExists(roleRepository, RoleConstants.SUPER_ADMIN);
        };
    }

    private void addRoleIfNotExists(RoleRepository roleRepository, String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
