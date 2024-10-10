package com.leopoldhsing.digitalhippo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.leopoldhsing.digitalhippo.model.enumeration.ProductFileType;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "product_images")
public class ProductImage extends BaseEntity {

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

    public ProductImage() {
    }

    public ProductImage(Product product, String url, String filename, Double filesize, Integer width, Integer height, String mimeType, ProductFileType fileType) {
        this.product = product;
        this.url = url;
        this.filename = filename;
        this.filesize = filesize;
        this.width = width;
        this.height = height;
        this.mimeType = mimeType;
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "product=" + product +
                ", url='" + url + '\'' +
                ", filename='" + filename + '\'' +
                ", filesize=" + filesize +
                ", width=" + width +
                ", height=" + height +
                ", mimeType='" + mimeType + '\'' +
                ", fileType=" + fileType +
                '}';
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Double getFilesize() {
        return filesize;
    }

    public void setFilesize(Double filesize) {
        this.filesize = filesize;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public ProductFileType getFileType() {
        return fileType;
    }

    public void setFileType(ProductFileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ProductImage that = (ProductImage) o;
        return Objects.equals(product, that.product) && Objects.equals(url, that.url) && Objects.equals(filename, that.filename) && Objects.equals(filesize, that.filesize) && Objects.equals(width, that.width) && Objects.equals(height, that.height) && Objects.equals(mimeType, that.mimeType) && fileType == that.fileType;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(product);
        result = 31 * result + Objects.hashCode(url);
        result = 31 * result + Objects.hashCode(filename);
        result = 31 * result + Objects.hashCode(filesize);
        result = 31 * result + Objects.hashCode(width);
        result = 31 * result + Objects.hashCode(height);
        result = 31 * result + Objects.hashCode(mimeType);
        result = 31 * result + Objects.hashCode(fileType);
        return result;
    }
}
