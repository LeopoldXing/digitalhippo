package com.leopoldhsing.digitalhippo.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVo {
    private Long id;
    private String payloadId;
    private String filename;
    private String description;
    private BigDecimal price;
    private String category;
    private String productFileUrl;
    private List<ProductImageVo> productImages;
}
