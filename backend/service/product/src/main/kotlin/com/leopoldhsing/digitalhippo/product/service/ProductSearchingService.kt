package com.leopoldhsing.digitalhippo.product.service

import com.leopoldhsing.digitalhippo.model.dto.SearchingResultIndexDto
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo

interface ProductSearchingService {
    fun searchProducts(condition: ProductSearchingConditionVo): SearchingResultIndexDto
}