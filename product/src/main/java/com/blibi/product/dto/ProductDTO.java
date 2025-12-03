package com.blibi.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Data Transfer Object (DTO) for Product data.
 * 
 * This class is used for transferring product data between layers (Controller,
 * Service) and over the network. DTOs provide several benefits:
 * - Separation of concerns: API structure can differ from database structure
 * - Security: Can exclude sensitive fields from API responses
 * - Validation: Different validation rules for API vs database
 * - Versioning: API can evolve independently of database schema
 * 
 * Key differences from Product entity:
 * - Uses @NotBlank instead of @NotNull for string validation (stricter)
 * - Implements Serializable for network transfer
 * - No MongoDB-specific annotations
 * 
 * @Data - Lombok annotation generating getters, setters, equals, hashCode, toString
 * @NoArgsConstructor - Lombok annotation generating no-args constructor
 * @AllArgsConstructor - Lombok annotation generating all-args constructor
 * @ToString - Lombok annotation generating toString method
 * Serializable - Allows object serialization for network transfer
 * 
 * @author HKB
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDTO implements Serializable {
    
    /**
     * Unique identifier for the product.
     * 
     * This field is populated when retrieving existing products from the database.
     * It's optional when creating new products (MongoDB will generate it).
     */
    private String productId;

    /**
     * Product name - required field with strict validation.
     * 
     * @NotBlank - Ensures the field is not null, not empty, and not just whitespace.
     *            This is stricter than @NotNull, which only checks for null.
     */
    @NotBlank(message = "Product name is required and cannot be blank")
    private String productName;

    /**
     * Product description providing detailed information.
     * 
     * @NotBlank - Ensures description is not null, empty, or whitespace-only
     */
    @NotBlank(message = "Product description is required and cannot be blank")
    private String description;

    /**
     * Product price in the application's currency.
     * 
     * Must be a positive value greater than zero. The combination of @NotNull
     * and @Positive ensures both presence and validity of the price.
     * 
     * @NotNull - Ensures price is not null
     * @Positive - Ensures price is greater than zero
     */
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    /**
     * Product category for classification and filtering.
     * 
     * @NotBlank - Ensures category is not null, empty, or whitespace-only
     */
    @NotBlank(message = "Category is required and cannot be blank")
    private String category;

    /**
     * List of product image URLs.
     * 
     * Optional field allowing multiple images per product. Can be null or empty
     * for products without images. Images are stored as URLs (strings) pointing
     * to external storage or CDN.
     */
    private List<String> images;
}
