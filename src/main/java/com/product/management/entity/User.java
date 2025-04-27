package com.product.management.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String mobile;
    private String password;
    private String tenantId;
    private boolean isVerified;
    private String resetToken;
}
