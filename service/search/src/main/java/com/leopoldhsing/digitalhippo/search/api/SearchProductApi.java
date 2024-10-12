package com.leopoldhsing.digitalhippo.search.api;

import com.leopoldhsing.digitalhippo.model.elasticsearch.ProductIndex;
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo;
import com.leopoldhsing.digitalhippo.search.service.ProductSearchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search/inner/product")
public class SearchProductApi {

    @Autowired
    private ProductSearchingService productSearchingService;

    @PostMapping
    public List<ProductIndex> searchProduct(@RequestBody ProductSearchingConditionVo condition) {
        List<ProductIndex> res = productSearchingService.searchProducts(condition);

        return res;
    }
}
