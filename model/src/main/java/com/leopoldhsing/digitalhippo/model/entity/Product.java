package com.leopoldhsing.digitalhippo.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
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

    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Product() {
    }

    public Product(String name, String description, BigDecimal price, String category, String productFileUrl, String approvedForSale, List<ProductImage> productImages, User user) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.productFileUrl = productFileUrl;
        this.approvedForSale = approvedForSale;
        this.productImages = productImages;
        this.user = user;
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
                ", productImages=" + productImages +
                ", user=" + user +
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

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(price, product.price) && Objects.equals(category, product.category) && Objects.equals(productFileUrl, product.productFileUrl) && Objects.equals(approvedForSale, product.approvedForSale) && Objects.equals(productImages, product.productImages) && Objects.equals(user, product.user);
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
        result = 31 * result + Objects.hashCode(productImages);
        result = 31 * result + Objects.hashCode(user);
        return result;
    }
}
