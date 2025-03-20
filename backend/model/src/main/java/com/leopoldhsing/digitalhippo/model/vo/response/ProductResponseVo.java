package com.leopoldhsing.digitalhippo.model.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseVo {

    private String id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private String product_files;
    private String approvedForSale;
    private UserResponseVo user;
}
