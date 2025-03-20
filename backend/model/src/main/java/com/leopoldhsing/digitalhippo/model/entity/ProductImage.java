package com.leopoldhsing.digitalhippo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.leopoldhsing.digitalhippo.model.enumeration.ProductFileType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents an image associated with a product.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_images")
@Schema(description = "Represents an image associated with a product.")
public class ProductImage extends BaseEntity {

    /**
     * Payload ID of the image.
     */
    @Schema(description = "Payload ID of the image.")
    private String payloadId;

    /**
     * The product to which this image belongs.
     */
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @Schema(description = "The product to which this image belongs.")
    private Product product;

    /**
     * URL of the image.
     */
    @Schema(description = "URL of the image.")
    private String url;

    /**
     * Filename of the image.
     */
    @Schema(description = "Filename of the image.")
    private String filename;

    /**
     * File size of the image in bytes.
     */
    @Schema(description = "File size of the image in bytes.")
    private Double filesize;

    /**
     * Width of the image in pixels.
     */
    @Schema(description = "Width of the image in pixels.")
    private Integer width;

    /**
     * Height of the image in pixels.
     */
    @Schema(description = "Height of the image in pixels.")
    private Integer height;

    /**
     * MIME type of the image.
     */
    @Schema(description = "MIME type of the image.")
    private String mimeType;

    /**
     * Type of the product file.
     */
    @Enumerated(EnumType.STRING)
    @Schema(description = "Type of the product file.")
    private ProductFileType fileType;
}