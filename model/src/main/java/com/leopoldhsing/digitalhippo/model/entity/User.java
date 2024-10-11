package com.leopoldhsing.digitalhippo.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private String username;
    private String email;
    private String passwordHash;
    private String salt;

    @Enumerated(EnumType.STRING)
    private UserRole role;
    private boolean verified;
    private boolean locked;
    private LocalDateTime lockUntil;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    public User(String username, String email, String passwordHash, String salt, UserRole role, boolean verified, boolean locked) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
        this.verified = verified;
        this.locked = locked;
    }
}
