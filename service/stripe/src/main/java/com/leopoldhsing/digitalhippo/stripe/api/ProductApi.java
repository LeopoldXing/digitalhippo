package com.leopoldhsing.digitalhippo.stripe.api;

import com.leopoldhsing.digitalhippo.model.entity.Product;
import com.leopoldhsing.digitalhippo.stripe.service.ProductStripeService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe/inner/product")
public class ProductApi {

    @Autowired
    private ProductStripeService productStripeService;

    @PostMapping
    public Product createStripeProduct(@RequestBody Product product) {
        try {
            return productStripeService.createStripeProduct(product);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping
    public Product updateStripeProduct(@RequestBody Product product) {
        try {
            return productStripeService.updateStripeProduct(product);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{stripeId}")
    public Boolean deleteStripeProduct(@PathVariable String stripeId) {
        try {
            return productStripeService.deleteStripeProduct(stripeId);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
