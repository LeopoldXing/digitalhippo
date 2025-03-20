package com.leopoldhsing.digitalhippo.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents an order placed by a user.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@Schema(description = "Represents an order placed by a user.")
public class Order extends BaseEntity {

    /**
     * Payload ID of the order.
     */
    @Schema(description = "Payload ID of the order.")
    private String payloadId;

    /**
     * The user who placed the order.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Schema(description = "The user who placed the order.")
    private User user;

    /**
     * List of products included in the order.
     */
    @ManyToMany
    @JoinTable(
            name = "link_orders_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Schema(description = "List of products included in the order.")
    private List<Product> products;

    /**
     * Indicates whether the order has been paid.
     */
    @Schema(description = "Indicates whether the order has been paid.")
    private Boolean isPaid;
}