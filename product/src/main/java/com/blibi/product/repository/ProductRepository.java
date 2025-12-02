package com.blibi.product.repository;

import com.blibi.product.entity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, ObjectId> {
    // Exact match search by product name
    Page<Product> findByProductName(String productName, Pageable pageable);

    // Wildcard search by product name (case-insensitive regex)
    @Query("{ 'productName': { $regex: ?0, $options: 'i' } }")
    Page<Product> searchProductByName(String productName, Pageable pageable);

    // Wildcard search by category (case-insensitive regex)
    @Query("{ 'category': { $regex: ?0, $options: 'i' } }")
    Page<Product> searchProductByCategory(String productName, Pageable pageable);
}
