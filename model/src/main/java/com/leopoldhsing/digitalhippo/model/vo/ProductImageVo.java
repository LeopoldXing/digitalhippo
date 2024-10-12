package com.leopoldhsing.digitalhippo.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageVo {
    private String payloadId;
    private String url;
    private String filename;
    private Double filesize;
    private Integer width;
    private Integer height;
    private String mimeType;
    private String fileType;

}
