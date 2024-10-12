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

    @JsonProperty("category-list")
    private List<String> categoryList;

    @JsonProperty("top-price")
    private Double topPrice;

    @JsonProperty("bottom-price")
    private Double bottomPrice;

    private Integer size;

    private Integer current;

    @JsonProperty("sorting-strategy")
    private SortingStrategy sortingStrategy;

    @JsonProperty("sorting-direction")
    private SortingDirection sortingDirection;
}
