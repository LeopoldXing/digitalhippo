package com.leopoldhsing.digitalhippo.common.mapper.product;

import com.leopoldhsing.digitalhippo.model.entity.Product;
import com.leopoldhsing.digitalhippo.model.entity.ProductImage;
import com.leopoldhsing.digitalhippo.model.enumeration.ProductFileType;
import com.leopoldhsing.digitalhippo.model.vo.ProductImageVo;
import org.springframework.beans.BeanUtils;

public class ProductImageMapper {
    private ProductImageMapper() {
    }

    public static ProductImage mapToProductImage(ProductImageVo productImageVo, Product product) {
        ProductImage productImage = new ProductImage();

        BeanUtils.copyProperties(productImageVo, productImage);
        productImage.setFileType(ProductFileType.fromValue(productImageVo.getFileType()));
        productImage.setProduct(product);

        return productImage;
    }

    public static ProductImageVo mapToProductImageVo(ProductImage productImage) {
        ProductImageVo productImageVo = new ProductImageVo();

        BeanUtils.copyProperties(productImage, productImageVo);
        productImageVo.setFileType(productImage.getFileType().getValue());

        return productImageVo;
    }
}
