package com.leopoldhsing.digitalhippo.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVo {
    private Long id;
    private String payloadId;
    private String name;
    private String description;
    private Double price;
    private String priceId;
    private String stripeId;
    private String category;
    private String productFileUrl;
    private List<ProductImageVo> productImages;
}
