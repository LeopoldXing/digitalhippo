package com.leopoldhsing.digitalhippo.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.leopoldhsing.digitalhippo.model.enumeration.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a user in the system.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Schema(description = "Represents a user in the system.")
public class User extends BaseEntity {

    /**
     * Payload ID of the user.
     */
    @Schema(description = "Payload ID of the user.")
    private String payloadId;

    /**
     * Username of the user.
     */
    @Schema(description = "Username of the user.")
    private String username;

    /**
     * Email address of the user.
     */
    @Schema(description = "Email address of the user.")
    private String email;

    /**
     * Password hash of the user.
     */
    @Schema(description = "Password hash of the user.")
    private String passwordHash;

    /**
     * Salt used for hashing the password.
     */
    @Schema(description = "Salt used for hashing the password.")
    private String salt;

    /**
     * Role of the user.
     */
    @Enumerated(EnumType.STRING)
    @Schema(description = "Role of the user.")
    private UserRole role;

    /**
     * Indicates whether the user's email is verified.
     */
    @Schema(description = "Indicates whether the user's email is verified.")
    private boolean verified;

    /**
     * Indicates whether the user account is locked.
     */
    @Schema(description = "Indicates whether the user account is locked.")
    private boolean locked;

    /**
     * Timestamp until when the account is locked.
     */
    @Schema(description = "Timestamp until when the account is locked.")
    private LocalDateTime lockUntil;

    /**
     * List of products created by the user.
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "List of products created by the user.")
    private List<Product> products;

    /**
     * Constructs a user with the specified parameters.
     *
     * @param payloadId    Payload ID
     * @param username     Username
     * @param email        Email address
     * @param passwordHash Password hash
     * @param salt         Salt used for hashing
     * @param role         User role
     * @param verified     Whether email is verified
     * @param locked       Whether account is locked
     */
    public User(String payloadId, String username, String email, String passwordHash, String salt, UserRole role, boolean verified, boolean locked) {
        this.payloadId = payloadId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
        this.verified = verified;
        this.locked = locked;
    }
}