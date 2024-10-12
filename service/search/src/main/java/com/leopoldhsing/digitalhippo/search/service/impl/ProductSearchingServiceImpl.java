package com.leopoldhsing.digitalhippo.search.service.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.json.JsonData;
import com.leopoldhsing.digitalhippo.common.constants.PaginationConstants;
import com.leopoldhsing.digitalhippo.model.elasticsearch.ProductIndex;
import com.leopoldhsing.digitalhippo.model.enumeration.SortingDirection;
import com.leopoldhsing.digitalhippo.model.enumeration.SortingStrategy;
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo;
import com.leopoldhsing.digitalhippo.search.service.ProductSearchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductSearchingServiceImpl implements ProductSearchingService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public List<ProductIndex> searchProducts(ProductSearchingConditionVo condition) {
        // 1. build DSL
        NativeQueryBuilder searchQuery = NativeQuery.builder();

        // 1.1 keyword
        String keyword = condition.getKeyword();
        if (StringUtils.hasLength(keyword)) {
            searchQuery.withQuery(q -> q.bool(b -> b
                    .should(s -> s.match(m -> m.field("name").query(keyword)))
                    .should(s -> s.match(m -> m.field("description").query(keyword)))
                    .should(s -> s.match(m -> m.field("category").query(keyword)))
            ));
        }

        // 1.2 category
        List<String> categoryList = condition.getCategoryList();
        if (!CollectionUtils.isEmpty(categoryList)) {
            List<FieldValue> finalCategoryList = categoryList.stream()
                    .map(String::toLowerCase)
                    .map(FieldValue::of)
                    .toList();
            searchQuery.withQuery(q -> q
                    .bool(b -> b
                            .must(m -> m
                                    .terms(t -> t
                                            .field("category")
                                            .terms(terms -> terms.value(finalCategoryList))
                                    ))
                    ));
        }

        // 1.3 price
        Double topPrice = condition.getTopPrice();
        Double bottomPrice = condition.getBottomPrice();
        // top price
        if (topPrice != null && !Double.isNaN(topPrice)) {
            searchQuery.withQuery(q -> q
                    .bool(b -> b
                            .must(m -> m
                                    .range(r -> r
                                            .field("price")
                                            .lte(JsonData.of(topPrice))
                                    ))
                    ));
        }
        // bottom price
        if (bottomPrice != null && !Double.isNaN(bottomPrice)) {
            searchQuery.withQuery(q -> q
                    .bool(b -> b
                            .must(m -> m
                                    .range(r -> r
                                            .field("price")
                                            .gte(JsonData.of(bottomPrice))
                                    ))
                    ));
        }

        // 1.4 sorting
        SortingStrategy sortingStrategy = condition.getSortingStrategy();
        SortingDirection sortingDirection = condition.getSortingDirection();
        SortOrder sortOrder = (sortingDirection == SortingDirection.ASC) ? SortOrder.Asc : SortOrder.Desc;
        if (ObjectUtils.isEmpty(sortingStrategy)) {
            sortingStrategy = SortingStrategy.POPULARITY;
        }
        switch (sortingStrategy) {
            case CREATED_TIMESTAMP:
                searchQuery.withSort(s -> s.field(f -> f
                        .field("createdAt")
                        .order(sortOrder)
                ));
                break;

            case POPULARITY:
                searchQuery.withSort(s -> s.field(f -> f
                        .field("popularityScore")
                        .order(sortOrder)
                ));
                break;

            case RELEVANCE:
                break;

            default:
                break;
        }

        // 1.5 pagination
        Integer current = condition.getCurrent();
        Integer size = condition.getSize();
        int from = (current != null && size != null) ? ((current - 1) * size) : 0;
        int pageSize = (size != null) ? size : PaginationConstants.DEFAULT_PAGE_SIZE;
        searchQuery.withPageable(PageRequest.of(from, pageSize));

        // 2. build the final query after adding all conditions
        NativeQuery finalQuery = searchQuery.build();

        // 3. execute the query
        SearchHits<ProductIndex> searchHits = elasticsearchTemplate.search(finalQuery, ProductIndex.class);

        // 4. handle results
        List<ProductIndex> res = new ArrayList<>();
        if (searchHits.hasSearchHits()) {
            res = searchHits.getSearchHits().stream().map(SearchHit::getContent).toList();
        }
        return res;
    }
}
