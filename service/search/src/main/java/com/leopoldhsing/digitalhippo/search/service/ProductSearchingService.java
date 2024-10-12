package com.leopoldhsing.digitalhippo.search.service;

import com.leopoldhsing.digitalhippo.model.elasticsearch.ProductIndex;
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo;

import java.util.List;

public interface ProductSearchingService {
    List<ProductIndex> searchProducts(ProductSearchingConditionVo condition);
}
