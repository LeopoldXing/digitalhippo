package com.leopoldhsing.digitalhippo.feign.search;

import com.leopoldhsing.digitalhippo.model.dto.SearchingResultIndexDto;
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "search", fallback = ProductSearchingFeignClientFallback.class)
public interface ProductSearchingFeignClient {

    @PostMapping("/api/search/inner/product")
    SearchingResultIndexDto searchProduct(@RequestBody ProductSearchingConditionVo condition);
}
