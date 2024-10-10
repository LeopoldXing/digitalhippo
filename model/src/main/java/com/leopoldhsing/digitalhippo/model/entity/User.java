package com.leopoldhsing.digitalhippo.model.entity;

import com.leopoldhsing.digitalhippo.model.enumeration.UserRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    public User() {
    }

    public User(String username, String email, String passwordHash, String salt, UserRole role, boolean verified, boolean locked) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
        this.verified = verified;
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", salt='" + salt + '\'' +
                ", role=" + role +
                ", verified=" + verified +
                ", locked=" + locked +
                ", lockUntil=" + lockUntil +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getLockUntil() {
        return lockUntil;
    }

    public void setLockUntil(LocalDateTime lockUntil) {
        this.lockUntil = lockUntil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;
        return verified == user.verified && locked == user.locked && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(passwordHash, user.passwordHash) && Objects.equals(salt, user.salt) && role == user.role && Objects.equals(lockUntil, user.lockUntil);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(username);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(passwordHash);
        result = 31 * result + Objects.hashCode(salt);
        result = 31 * result + Objects.hashCode(role);
        result = 31 * result + Boolean.hashCode(verified);
        result = 31 * result + Boolean.hashCode(locked);
        result = 31 * result + Objects.hashCode(lockUntil);
        return result;
    }
}
