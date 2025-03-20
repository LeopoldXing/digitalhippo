package com.leopoldhsing.digitalhippo.search.service;

import com.leopoldhsing.digitalhippo.model.dto.SearchingResultIndexDto;
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo;

public interface ProductSearchingService {
    SearchingResultIndexDto searchProducts(ProductSearchingConditionVo condition);
}
