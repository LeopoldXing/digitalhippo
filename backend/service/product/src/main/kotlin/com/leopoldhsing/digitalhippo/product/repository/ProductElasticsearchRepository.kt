package com.leopoldhsing.digitalhippo.product.repository

import com.leopoldhsing.digitalhippo.model.elasticsearch.ProductIndex
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductElasticsearchRepository : ElasticsearchRepository<ProductIndex, Long> {


}