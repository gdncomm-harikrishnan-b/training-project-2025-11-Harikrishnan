package com.blibi.product.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
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
 * @Data - Lombok annotation generating getters, setters, equals, hashCode, toString
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
@Document(collection = Product.COLLECTION_NAME)
public class Product {
    
    /**
     * MongoDB collection name constant.
     * Using a constant ensures consistency and makes refactoring easier.
     */
    static final String COLLECTION_NAME = "products";

    /**
     * Unique identifier for the product.
     * 
     * MongoDB automatically generates this ID if not provided during save.
     * The @Id annotation marks this field as the document's primary key.
     * 
     * @Id - Marks this field as the MongoDB document identifier
     */
    @Id
    private String productId;
    
    /**
     * Product name - required field with validation.
     * 
     * This field is indexed (via @CompoundIndex) for faster search operations.
     * The index is non-unique, allowing multiple products with the same name.
     * 
     * @NotNull - Ensures product name is not null (validation at entity level)
     */
    @NotNull(message = "Product Name cannot be Empty")
    private String productName;
    
    /**
     * Product description providing detailed information about the product.
     * 
     * @NotNull - Ensures description is not null
     */
    @NotNull(message = "Product Description cannot be Empty")
    private String description;
    
    /**
     * Product price in the application's currency.
     * 
     * Must be a positive value greater than zero. Using Double allows for
     * decimal prices (e.g., 99.99).
     * 
     * @Positive - Ensures price is greater than zero
     */
    @Positive(message = "Price must be greater than zero")
    private Double price;
    
    /**
     * Product category for classification and filtering.
     * 
     * Categories help organize products and enable category-based searches.
     * Examples: "Electronics", "Clothing", "Books", etc.
     * 
     * @NotNull - Ensures category is not null
     */
    @NotNull(message = "Category cannot be Empty")
    private String category;
    
    /**
     * List of product image URLs.
     * 
     * Optional field allowing multiple images per product. Images are stored
     * as URLs (strings) rather than binary data, following best practices
     * for storing media references in databases.
     */
    private List<String> images;
}
