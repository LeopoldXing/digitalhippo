package com.leopoldhsing.digitalhippo.model.entity;

import com.leopoldhsing.digitalhippo.model.enumeration.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Table(name = "users")
@Entity
public class User extends BaseEntity {
    private String username;
    private String email;
    private String passwordHash;
    private String salt;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private boolean verified;
    private boolean locked;
    private OffsetDateTime lockUntil;

    public User() {
    }

    public User(Long id, OffsetDateTime createdAt, OffsetDateTime updatedAt, String createdBy, String updatedBy) {
        super(id, createdAt, updatedAt, createdBy, updatedBy);
    }

    public User(String username, String email, String passwordHash, String salt, UserRole role, boolean verified, boolean locked, OffsetDateTime lockUntil) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
        this.verified = verified;
        this.locked = locked;
        this.lockUntil = lockUntil;
    }

    public User(Long id, OffsetDateTime createdAt, OffsetDateTime updatedAt, String createdBy, String updatedBy, String username, String email, String passwordHash, String salt, UserRole role, boolean verified, boolean locked, OffsetDateTime lockUntil) {
        super(id, createdAt, updatedAt, createdBy, updatedBy);
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
        this.verified = verified;
        this.locked = locked;
        this.lockUntil = lockUntil;
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

    public OffsetDateTime getLockUntil() {
        return lockUntil;
    }

    public void setLockUntil(OffsetDateTime lockUntil) {
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
