package com.leopoldhsing.digitalhippo.model.elasticsearch;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Objects;

@Document(indexName = "digitalhippo-product")
public class ProductIndex {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String payloadId;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Keyword)
    private String sellerEmail;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Keyword, index = false)
    private String productFileUrl;

    @Field(type = FieldType.Keyword)
    private String approvedForSale;

    // higher the score, higher the popularity
    @Field(type = FieldType.Long)
    private Long popularityScore = 0L;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private Date createdAt;

    public ProductIndex() {
    }

    public ProductIndex(Long id, String payloadId, String name, Double price, String sellerEmail, String description, String category, String productFileUrl, String approvedForSale, Long popularityScore, Date createdAt) {
        this.id = id;
        this.payloadId = payloadId;
        this.name = name;
        this.price = price;
        this.sellerEmail = sellerEmail;
        this.description = description;
        this.category = category;
        this.productFileUrl = productFileUrl;
        this.approvedForSale = approvedForSale;
        this.popularityScore = popularityScore;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ProductIndex{" +
                "id=" + id +
                ", payloadId='" + payloadId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", sellerEmail='" + sellerEmail + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", productFileUrl='" + productFileUrl + '\'' +
                ", approvedForSale='" + approvedForSale + '\'' +
                ", popularityScore=" + popularityScore +
                ", createdAt=" + createdAt +
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(Long popularityScore) {
        this.popularityScore = popularityScore;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductIndex that = (ProductIndex) o;
        return Objects.equals(id, that.id) && Objects.equals(payloadId, that.payloadId) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(sellerEmail, that.sellerEmail) && Objects.equals(description, that.description) && Objects.equals(category, that.category) && Objects.equals(productFileUrl, that.productFileUrl) && Objects.equals(approvedForSale, that.approvedForSale) && Objects.equals(popularityScore, that.popularityScore) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(payloadId);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(price);
        result = 31 * result + Objects.hashCode(sellerEmail);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(category);
        result = 31 * result + Objects.hashCode(productFileUrl);
        result = 31 * result + Objects.hashCode(approvedForSale);
        result = 31 * result + Objects.hashCode(popularityScore);
        result = 31 * result + Objects.hashCode(createdAt);
        return result;
    }
}
