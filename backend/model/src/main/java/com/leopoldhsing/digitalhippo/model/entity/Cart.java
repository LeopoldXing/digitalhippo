package com.leopoldhsing.digitalhippo.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents an item in a user's shopping cart.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
@Schema(description = "Represents an item in a user's shopping cart.")
public class Cart extends BaseEntity {

    /**
     * The product associated with this cart item.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    @Schema(description = "The product associated with this cart item.")
    private Product product;

    /**
     * The user who owns this cart.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "The user who owns this cart.")
    private User user;
}