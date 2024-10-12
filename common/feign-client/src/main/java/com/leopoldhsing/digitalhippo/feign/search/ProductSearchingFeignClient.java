package com.leopoldhsing.digitalhippo.feign.search;

import com.leopoldhsing.digitalhippo.model.elasticsearch.ProductIndex;
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "search", fallback = ProductSearchingFeignClientFallback.class)
public interface ProductSearchingFeignClient {

    @PostMapping("/api/search/inner/product")
    List<ProductIndex> searchProduct(@RequestBody ProductSearchingConditionVo condition);
}
