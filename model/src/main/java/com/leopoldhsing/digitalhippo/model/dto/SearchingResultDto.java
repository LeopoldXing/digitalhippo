package com.leopoldhsing.digitalhippo.model.dto;

import com.leopoldhsing.digitalhippo.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchingResultDto {

    private List<Product> results;
    private Integer resultCount;
    private Integer totalPage;
    private Integer current;
    private Integer size;
}
