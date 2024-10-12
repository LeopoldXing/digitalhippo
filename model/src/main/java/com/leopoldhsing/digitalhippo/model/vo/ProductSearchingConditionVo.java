package com.leopoldhsing.digitalhippo.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leopoldhsing.digitalhippo.model.enumeration.SortingDirection;
import com.leopoldhsing.digitalhippo.model.enumeration.SortingStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchingConditionVo {

    private String keyword;
    private List<String> categoryList;
    private Double topPrice;
    private Double bottomPrice;
    private Integer size;
    private Integer current;
    private SortingStrategy sortingStrategy;
    private SortingDirection sortingDirection;

}
