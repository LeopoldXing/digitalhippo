package com.leopoldhsing.digitalhippo.feign.search;

import com.leopoldhsing.digitalhippo.model.dto.SearchingResultIndexDto;
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo;
import org.springframework.stereotype.Component;

@Component
public class ProductSearchingFeignClientFallback implements ProductSearchingFeignClient {
    @Override
    public SearchingResultIndexDto searchProduct(ProductSearchingConditionVo condition) {
        return new SearchingResultIndexDto();
    }
}
