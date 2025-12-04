package com.blibi.product.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Product entity class representing a product document in MongoDB.
 * 
 * This class maps to the "products" collection in MongoDB and represents the
 * core product data structure. It includes validation constraints and indexing
 * for optimized database queries.
 * 
 * Key features:
 * - MongoDB document mapping via @Document annotation
 * - Automatic ID generation by MongoDB
 * - Validation constraints for data integrity
 * - Compound index on productName for query optimization
 * - Lombok annotations for reducing boilerplate code
 * 
 * @Document - Maps this class to a MongoDB collection
 * @CompoundIndex - Creates a database index on productName for faster queries
 * @Data - Lombok annotation generating getters, setters, equals, hashCode,
 *       toString
 * @NoArgsConstructor - Lombok annotation generating no-args constructor
 * @AllArgsConstructor - Lombok annotation generating all-args constructor
 * @ToString - Lombok annotation generating toString method
 * 
 * @author HKB
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@CompoundIndex(name = "product_name_idx", def = "{'productName'= 1}", unique = false)
@CompoundIndex(name = "product_id_idx", def = "{'productId'= 1}", unique = true)
@Document(collection = Product.COLLECTION_NAME)
public class Product {

    static final String COLLECTION_NAME = "products";

    @Id
    private String id;

    @NotNull(message = "Product ID cannot be Empty")
    @Indexed(unique = true)
    private String productId;
    @NotNull(message = "Product Name cannot be Empty")
    private String productName;
    @NotNull(message = "Product Description cannot be Empty")
    private String description;
    @Positive(message = "Price must be greater than zero")
    private Double price;
    @NotNull(message = "Category cannot be Empty")
    private String category;
    private List<String> images;
}
