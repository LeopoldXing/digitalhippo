package com.leopoldhsing.digitalhippo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private String productFileUrl;
    private String approvedForSale;

    public Product() {
    }

    public Product(String name, String description, BigDecimal price, String category, String productFileUrl, String approvedForSale) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.productFileUrl = productFileUrl;
        this.approvedForSale = approvedForSale;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", productFileUrl='" + productFileUrl + '\'' +
                ", approvedForSale='" + approvedForSale + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getApprovedForSale() {
        return approvedForSale;
    }

    public void setApprovedForSale(String approvedForSale) {
        this.approvedForSale = approvedForSale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(price, product.price) && Objects.equals(category, product.category) && Objects.equals(productFileUrl, product.productFileUrl) && Objects.equals(approvedForSale, product.approvedForSale);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(price);
        result = 31 * result + Objects.hashCode(category);
        result = 31 * result + Objects.hashCode(productFileUrl);
        result = 31 * result + Objects.hashCode(approvedForSale);
        return result;
    }
}
