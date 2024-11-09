package com.leopoldhsing.digitalhippo.search.api;

import com.leopoldhsing.digitalhippo.model.dto.ErrorResponseDto;
import com.leopoldhsing.digitalhippo.model.dto.SearchingResultIndexDto;
import com.leopoldhsing.digitalhippo.model.vo.ProductSearchingConditionVo;
import com.leopoldhsing.digitalhippo.search.service.ProductSearchingService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Search API", description = "REST APIs for searching products. External access is restricted")
@RestController
@RequestMapping("/api/search/inner/product")
public class SearchProductApi {

    @Autowired
    private ProductSearchingService productSearchingService;

    @Operation(
            summary = "Search products",
            description = "Search products based on various conditions such as keyword, category, price range, and sorting options."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SearchingResultIndexDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid search parameters",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PostMapping
    public SearchingResultIndexDto searchProduct(
            @Parameter(description = "Search conditions", required = true)
            @RequestBody ProductSearchingConditionVo condition
    ) {
        SearchingResultIndexDto res = productSearchingService.searchProducts(condition);

        return res;
    }
}
