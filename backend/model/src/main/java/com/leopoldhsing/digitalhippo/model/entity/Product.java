package com.leopoldhsing.digitalhippo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a product in the system.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Schema(description = "Represents a product in the system.")
public class Product extends BaseEntity {

    /**
     * The user who created this product.
     */
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Schema(description = "The user who created this product.")
    private User user;

    /**
     * Payload ID of the product.
     */
    @Schema(description = "Payload ID of the product.")
    private String payloadId;

    /**
     * Name of the product.
     */
    @Schema(description = "Name of the product.")
    private String name;

    /**
     * Description of the product.
     */
    @Schema(description = "Description of the product.")
    private String description;

    /**
     * Price of the product.
     */
    @Schema(description = "Price of the product.")
    private BigDecimal price;

    /**
     * Stripe price ID.
     */
    @Schema(description = "Stripe price ID.")
    private String priceId;

    /**
     * Stripe product ID.
     */
    @Schema(description = "Stripe product ID.")
    private String stripeId;

    /**
     * Category of the product.
     */
    @Schema(description = "Category of the product.")
    private String category;

    /**
     * URL of the product file.
     */
    @Schema(description = "URL of the product file.")
    private String productFileUrl;

    /**
     * Indicates whether the product is approved for sale.
     */
    @Schema(description = "Indicates whether the product is approved for sale.")
    private String approvedForSale;

    /**
     * List of images associated with the product.
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "List of images associated with the product.")
    private List<ProductImage> productImages;

    /**
     * Constructs a product with the specified ID.
     *
     * @param id Product ID
     */
    public Product(Long id) {
        this.setId(id);
    }
}