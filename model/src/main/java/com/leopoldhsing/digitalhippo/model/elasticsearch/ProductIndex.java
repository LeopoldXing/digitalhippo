package com.leopoldhsing.digitalhippo.model.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
