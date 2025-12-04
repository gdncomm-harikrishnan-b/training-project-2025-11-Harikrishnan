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

    private String productId;
    @NotBlank(message = "Product name is required and cannot be blank")
    private String productName;
    @NotBlank(message = "Product description is required and cannot be blank")
    private String description;
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;
    @NotBlank(message = "Category is required and cannot be blank")
    private String category;
    private List<String> images;
}
