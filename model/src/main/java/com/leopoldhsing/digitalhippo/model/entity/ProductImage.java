package com.leopoldhsing.digitalhippo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.leopoldhsing.digitalhippo.model.enumeration.ProductFileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_images")
public class ProductImage extends BaseEntity {

    private String payloadId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String url;
    private String filename;
    private Double filesize;
    private Integer width;
    private Integer height;
    private String mimeType;

    @Enumerated(EnumType.STRING)
    private ProductFileType fileType;
}
