package com.leopoldhsing.digitalhippo.search.repository;

import com.leopoldhsing.digitalhippo.model.elasticsearch.ProductIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductIndexRepository extends ElasticsearchRepository<ProductIndex, Long> {
}
