package com.leopoldhsing.digitalhippo.model.dto;

import com.leopoldhsing.digitalhippo.model.elasticsearch.ProductIndex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchingResultIndexDto {

    private List<ProductIndex> results;
    private Integer resultCount;
    private Integer totalPage;
    private Integer current;
    private Integer size;
}
