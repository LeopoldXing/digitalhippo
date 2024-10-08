package com.leopoldhsing.digitalhippo.product

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EntityScan(basePackages = ["com.leopoldhsing.digitalhippo"])
@EnableJpaAuditing
open class ProductApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ProductApplication>(*args)
        }
    }
}