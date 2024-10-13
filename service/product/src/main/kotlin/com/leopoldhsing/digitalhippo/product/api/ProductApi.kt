package com.leopoldhsing.digitalhippo.product.api

import com.leopoldhsing.digitalhippo.product.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/product/inner")
open class ProductApi @Autowired constructor(val productService: ProductService) {

    @PostMapping
    open fun getStripeIdList(@RequestBody productIdList: List<Long>): List<String> {
        val products = productService.getProducts(productIdList)

        val stripeIdList: List<String> = products.map { it.stripeId }

        return stripeIdList
    }
}