package com.leopoldhsing.digitalhippo.model.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageIndex {
    @Field(type = FieldType.Keyword)
    private String payloadImageId;

    @Field(type = FieldType.Keyword)
    private String imageUrl;

    @Field(type = FieldType.Keyword, index = false)
    private String imageName;

    @Field(type = FieldType.Double)
    private Double imageSize;

    @Field(type = FieldType.Integer)
    private Integer imageWidth;

    @Field(type = FieldType.Integer)
    private Integer imageHeight;

    @Field(type = FieldType.Keyword, index = false)
    private String mimeType;

    @Field(type = FieldType.Keyword)
    private String imageType;
}
