package com.leopoldhsing.digitalhippo.common.mapper.product;

import com.leopoldhsing.digitalhippo.model.elasticsearch.ProductIndex;
import com.leopoldhsing.digitalhippo.model.entity.Product;
import com.leopoldhsing.digitalhippo.model.entity.ProductImage;
import com.leopoldhsing.digitalhippo.model.vo.ProductImageVo;
import com.leopoldhsing.digitalhippo.model.vo.ProductVo;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    private ProductMapper() {
    }

    public static Product mapToProduct(ProductVo productVo, String approvedForSale) {
        if (!StringUtils.hasLength(approvedForSale)) approvedForSale = "pending";
        Product product = new Product();

        BeanUtils.copyProperties(productVo, product);
        product.setApprovedForSale(approvedForSale);
        List<ProductImage> productImageList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(productVo.getProductImages())) {
            productImageList = productVo.getProductImages().stream().map(imageVo -> ProductImageMapper.mapToProductImage(imageVo, product)).toList();
        }
        product.setProductImages(productImageList);
        product.setPrice(BigDecimal.valueOf(productVo.getPrice()));

        return product;
    }

    public static Product mapToProduct(ProductIndex productIndex) {
        Product product = new Product();
        BeanUtils.copyProperties(productIndex, product);
        product.setPrice(BigDecimal.valueOf(productIndex.getPrice()));

        return product;
    }

    public static ProductVo mapToProductVo(Product product) {
        ProductVo productVo = new ProductVo();
        BeanUtils.copyProperties(product, productVo);
        productVo.setPrice(product.getPrice().doubleValue());
        List<ProductImageVo> productImageVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(product.getProductImages())) {
            productImageVoList = product.getProductImages().stream().map(ProductImageMapper::mapToProductImageVo).toList();
            productImageVoList = productImageVoList.stream().filter(imageVo -> "TABLET".equalsIgnoreCase(imageVo.getFileType())).toList();
        }
        productVo.setProductImages(productImageVoList);

        return productVo;
    }

    public static ProductIndex mapToProductIndex(Product product) {
        ProductIndex productIndex = new ProductIndex();
        BeanUtils.copyProperties(product, productIndex);
        productIndex.setPrice(product.getPrice().doubleValue());
        productIndex.setSellerEmail(product.getUser().getEmail());
        productIndex.setCategory(product.getCategory().toLowerCase());
        return productIndex;
    }
}
