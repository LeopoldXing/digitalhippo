package com.leopoldhsing.digitalhippo.product.service.impl

import co.elastic.clients.elasticsearch._types.FieldValue
import co.elastic.clients.elasticsearch._types.SortOrder
import co.elastic.clients.json.JsonData
import com.leopoldhsing.digitalhippo.common.constants.PaginationConstants
import com.leopoldhsing.digitalhippo.model.dto.SearchingResultIndexDto
import com.leopoldhsing.digitalhippo.model.elasticsearch.ProductIndex
import com.leopoldhsing.digitalhippo.model.enumeration.SortingDirection
import com.leopoldhsing.digitalhippo.model.enumeration.SortingStrategy
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo
import com.leopoldhsing.digitalhippo.product.service.ProductSearchingService
import org.springframework.data.domain.PageRequest
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder
import org.springframework.data.elasticsearch.core.SearchHits
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class ProductSearchingServiceImpl(
    private val elasticsearchTemplate: ElasticsearchTemplate
) : ProductSearchingService {

    override fun searchProducts(condition: ProductSearchingConditionVo): SearchingResultIndexDto {
        // 1. build DSL
        val searchQuery: NativeQueryBuilder = NativeQuery.builder()

        searchQuery.withQuery { q ->
            q.bool { b ->
                // 1.1 keyword
                val keyword = condition.keyword
                if (StringUtils.hasLength(keyword)) {
                    b.must { s ->
                        s.multiMatch { m ->
                            m.fields("name", "description", "category")
                                .query(keyword.lowercase())
                        }
                    }
                }

                // 1.2 category
                val category = condition.category
                val categoryList = mutableListOf<String>()
                if ("all".equals(category, ignoreCase = true) || !StringUtils.hasLength(category)) {
                    categoryList.add("ui_kits")
                    categoryList.add("icons")
                } else {
                    categoryList.add(category)
                }
                val finalCategoryList = categoryList.map { it.lowercase() }.map { FieldValue.of(it) }
                b.must { m ->
                    m.terms { t ->
                        t.field("category")
                            .terms { terms -> terms.value(finalCategoryList) }
                    }
                }

                // 1.3 price
                b.must { m ->
                    m.range { r ->
                        val topPrice = condition.topPrice
                        val bottomPrice = condition.bottomPrice
                        // top price
                        if (topPrice != null && !topPrice.isNaN() && topPrice > 0) {
                            r.field("price")
                                .lte(JsonData.of(topPrice))
                        }
                        // bottom price
                        if (bottomPrice != null && !bottomPrice.isNaN() && bottomPrice >= 0) {
                            r.field("price")
                                .gte(JsonData.of(bottomPrice))
                        } else {
                            r.field("price")
                                .gte(JsonData.of(0))
                        }
                        r
                    }
                }
                b
            }
        }

        // 1.4 sorting
        var sortingStrategy = condition.sortingStrategy ?: SortingStrategy.POPULARITY
        val sortingDirection = condition.sortingDirection
        val sortOrder: SortOrder = if (sortingDirection == SortingDirection.ASC) SortOrder.Asc else SortOrder.Desc
        when (sortingStrategy) {
            SortingStrategy.CREATED_TIMESTAMP -> {
                searchQuery.withSort { s ->
                    s.field { f ->
                        f.field("createdAt")
                            .order(sortOrder)
                    }
                }
            }
            SortingStrategy.POPULARITY -> {
                searchQuery.withSort { s ->
                    s.field { f ->
                        f.field("popularityScore")
                            .order(sortOrder)
                    }
                }
            }
            SortingStrategy.PRICE -> {
                searchQuery.withSort { s ->
                    s.field { f ->
                        f.field("price")
                            .order(sortOrder)
                    }
                }
            }
            SortingStrategy.RELEVANCE -> {
            }
        }

        // 1.5 pagination
        var current = condition.current
        var size = condition.size
        val pageNumber = if (current != null && current > 0) current - 1 else 0
        val pageSize = size ?: PaginationConstants.DEFAULT_PAGE_SIZE
        searchQuery.withPageable(PageRequest.of(pageNumber, pageSize))

        // 2. build the final query after adding all conditions
        val finalQuery: NativeQuery = searchQuery.build()

        // 3. execute the query
        val searchHits: SearchHits<ProductIndex> =
            elasticsearchTemplate.search(finalQuery, ProductIndex::class.java)

        // 4. handle results
        val productIndexList = if (searchHits.hasSearchHits()) searchHits.searchHits.map { it.content } else emptyList()

        val totalHits = searchHits.totalHits
        return SearchingResultIndexDto().apply {
            results = productIndexList
            resultCount = totalHits.toInt()
            size = pageSize
            current = condition.current
            totalPage = kotlin.math.ceil(totalHits / pageSize.toDouble()).toInt()
        }
    }
}
