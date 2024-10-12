package com.leopoldhsing.digitalhippo.model.dto;

import com.leopoldhsing.digitalhippo.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchingResultDto {

    private List<Product> results;
    private Long resultCount;
    private Long totalPage;
    private Long current;
    private Long size;
}
