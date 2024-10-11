package com.leopoldhsing.digitalhippo.model.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ProductVo {
    private Long id;
    private String payloadId;
    private String filename;
    private String description;
    private BigDecimal price;
    private String category;
    private String productFileUrl;
    private List<ProductImageVo> productImages;

    public ProductVo() {
    }

    public ProductVo(Long id, String payloadId, String filename, String description, BigDecimal price, String category, String productFileUrl, List<ProductImageVo> productImages) {
        this.id = id;
        this.payloadId = payloadId;
        this.filename = filename;
        this.description = description;
        this.price = price;
        this.category = category;
        this.productFileUrl = productFileUrl;
        this.productImages = productImages;
    }

    @Override
    public String toString() {
        return "ProductVo{" +
                "id=" + id +
                ", payloadId='" + payloadId + '\'' +
                ", filename='" + filename + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", productFileUrl='" + productFileUrl + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayloadId() {
        return payloadId;
    }

    public void setPayloadId(String payloadId) {
        this.payloadId = payloadId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductFileUrl() {
        return productFileUrl;
    }

    public void setProductFileUrl(String productFileUrl) {
        this.productFileUrl = productFileUrl;
    }

    public List<ProductImageVo> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImageVo> productImages) {
        this.productImages = productImages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductVo productVo = (ProductVo) o;
        return Objects.equals(id, productVo.id) && Objects.equals(payloadId, productVo.payloadId) && Objects.equals(filename, productVo.filename) && Objects.equals(description, productVo.description) && Objects.equals(price, productVo.price) && Objects.equals(category, productVo.category) && Objects.equals(productFileUrl, productVo.productFileUrl) && Objects.equals(productImages, productVo.productImages);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(payloadId);
        result = 31 * result + Objects.hashCode(filename);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(price);
        result = 31 * result + Objects.hashCode(category);
        result = 31 * result + Objects.hashCode(productFileUrl);
        result = 31 * result + Objects.hashCode(productImages);
        return result;
    }
}
