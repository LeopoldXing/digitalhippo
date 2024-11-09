package com.leopoldhsing.digitalhippo.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base entity class containing common fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "Base entity class containing common fields.")
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6163675075289529459L;

    /**
     * Primary key ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key ID.")
    private Long id;

    /**
     * Creation timestamp.
     */
    @CreatedDate
    @Column(updatable = false)
    @Schema(description = "Creation timestamp.")
    private LocalDateTime createdAt;

    /**
     * Last update timestamp.
     */
    @LastModifiedDate
    @Column(nullable = false)
    @Schema(description = "Last update timestamp.")
    private LocalDateTime updatedAt;

    /**
     * Username of the creator.
     */
    @CreatedBy
    @Column(updatable = false)
    @Schema(description = "Username of the creator.")
    private String createdBy;

    /**
     * Username of the last modifier.
     */
    @LastModifiedBy
    @Schema(description = "Username of the last modifier.")
    private String updatedBy;
}
