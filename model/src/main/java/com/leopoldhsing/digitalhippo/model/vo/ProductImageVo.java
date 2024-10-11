package com.leopoldhsing.digitalhippo.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageVo {
    private String payloadId;
    private String filename;
    private Double filesize;
    private Integer height;
    private Integer width;
    private String mimeType;
    private String fileType;
    private String url;

}
