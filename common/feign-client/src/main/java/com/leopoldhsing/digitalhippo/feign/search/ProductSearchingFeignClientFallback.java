package com.leopoldhsing.digitalhippo.feign.search;

import com.leopoldhsing.digitalhippo.model.elasticsearch.ProductIndex;
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductSearchingFeignClientFallback implements ProductSearchingFeignClient {
    @Override
    public List<ProductIndex> searchProduct(ProductSearchingConditionVo condition) {
        return List.of();
    }
}
