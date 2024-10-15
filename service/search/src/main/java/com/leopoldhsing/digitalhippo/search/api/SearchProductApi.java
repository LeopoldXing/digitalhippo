package com.leopoldhsing.digitalhippo.search.api;

import com.leopoldhsing.digitalhippo.model.dto.SearchingResultIndexDto;
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo;
import com.leopoldhsing.digitalhippo.search.service.ProductSearchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search/inner/product")
public class SearchProductApi {

    @Autowired
    private ProductSearchingService productSearchingService;

    @PostMapping
    public SearchingResultIndexDto searchProduct(@RequestBody ProductSearchingConditionVo condition) {
        SearchingResultIndexDto res = productSearchingService.searchProducts(condition);

        return res;
    }
}
