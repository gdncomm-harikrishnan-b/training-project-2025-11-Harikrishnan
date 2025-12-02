package com.blibi.product.utils;

import com.blibi.product.dto.ProductDTO;
import com.blibi.product.entity.Product;
import org.springframework.beans.BeanUtils;

public class CommonUtils {
    // Entity will be converted to DTO for Product
    public  static ProductDTO getProductDTO(Product product) {
        ProductDTO targetDTO = new ProductDTO();
        BeanUtils.copyProperties(product, targetDTO);
        return  targetDTO;
    }
    // DTO will be converted to Entity for Product
    public static Product getProductFromDTO(ProductDTO productDTO) {
        Product target = new Product();
        BeanUtils.copyProperties(productDTO, target);
        return target;
    }


}
