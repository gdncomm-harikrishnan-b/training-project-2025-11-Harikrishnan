package com.blibi.product.service;

import com.blibi.product.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);

    Page<ProductDTO> viewProductDetailsByName(String productName, Pageable pageable);

    Page<ProductDTO> searchProductByName(String productName, Pageable pageable);

    Page<ProductDTO> searchProductByCategory(String category, Pageable pageable);
}
