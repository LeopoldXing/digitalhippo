package com.leopoldhsing.digitalhippo.payment.service.impl;

import com.leopoldhsing.digitalhippo.model.entity.Product;
import com.leopoldhsing.digitalhippo.payment.config.StripeProperties;
import com.leopoldhsing.digitalhippo.payment.service.ProductStripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductUpdateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductStripeServiceImpl implements ProductStripeService {

    private final StripeProperties stripeProperties;

    public ProductStripeServiceImpl(StripeProperties stripeProperties) {
        this.stripeProperties = stripeProperties;
    }

    @PostConstruct
    public void configureStripeClient() {
        Stripe.apiKey = stripeProperties.getSecretKey();
    }

    @Override
    public List<com.stripe.model.Product> getStripeProducts(List<String> stripeIdList) throws StripeException {
        List<com.stripe.model.Product> res = new ArrayList<>();

        if (CollectionUtils.isEmpty(stripeIdList)) {
            return res;
        }

        for (String stripeId : stripeIdList) {
            com.stripe.model.Product stripeProduct = com.stripe.model.Product.retrieve(stripeId);
            res.add(stripeProduct);
        }

        return res;
    }

    @Override
    public Product createStripeProduct(Product product) throws StripeException {
        // 1. create stripe product
        ProductCreateParams params = ProductCreateParams
                .builder()
                .setName(product.getName())
                .setDefaultPriceData(ProductCreateParams
                        .DefaultPriceData
                        .builder()
                        .setCurrency("CAD")
                        .setUnitAmount(product.getPrice().longValue() * 100L)
                        .build())
                .build();
        com.stripe.model.Product stripeProduct = com.stripe.model.Product.create(params);

        // 2. return result
        // 2.1 set stripe id
        String stripeId = stripeProduct.getId();
        product.setStripeId(stripeId);
        // 2.2 set default price id
        String priceId = stripeProduct.getDefaultPrice();
        product.setPriceId(priceId);
        return product;
    }

    @Override
    public Product updateStripeProduct(Product product) throws StripeException {
        // 1. get existing Stripe product
        com.stripe.model.Product stripeProduct = com.stripe.model.Product.retrieve(product.getStripeId());

        // 2. construct update params
        ProductUpdateParams params = ProductUpdateParams
                .builder()
                .setName(product.getName())
                .build();

        // 3. update stripe product
        stripeProduct = stripeProduct.update(params);

        // 4. if the price changes
        if (product.getPrice() != null) {
            PriceCreateParams priceCreateParams = PriceCreateParams
                    .builder()
                    .setUnitAmount(product.getPrice().longValue())
                    .setCurrency("CAD")
                    .setProduct(stripeProduct.getId())
                    .build();

            // create new stripe price
            Price newPrice = Price.create(priceCreateParams);
            String newPriceId = newPrice.getId();

            // set price id
            product.setPriceId(newPriceId);
        }

        // 5. return updated product
        return product;
    }

    @Override
    public Boolean deleteStripeProduct(String stripeId) throws StripeException {
        // 1. find stripe product
        com.stripe.model.Product stripeProduct = com.stripe.model.Product.retrieve(stripeId);

        // 2. delete
        stripeProduct.delete();

        return true;
    }

}
