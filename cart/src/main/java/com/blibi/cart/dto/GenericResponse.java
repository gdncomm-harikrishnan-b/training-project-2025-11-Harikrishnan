package com.blibi.cart.dto;

import lombok.*;

/**
 * Generic response wrapper for all API endpoints.
 * 
 * This class provides a consistent response structure across all endpoints.
 * It wraps the actual response data with status and message information.
 * 
 * Key Features:
 * - Generic type parameter allows wrapping any data type
 * - Consistent structure: status, message, and data
 * - Used by all controller endpoints for uniform API responses
 * 
 * Response Structure:
 * - status: "SUCCESS" or "ERROR"
 * - message: Descriptive message about the operation
 * - data: The actual response payload (can be any type)
 * 
 * @NoArgsConstructor: Default constructor
 * @AllArgsConstructor: Constructor with all fields
 * @Builder: Provides builder pattern for object creation
 * @Data: Generates getters, setters, toString, equals, and hashCode
 * 
 * @param <T> The type of data being wrapped in the response
 * 
 * @author Cart Service Team
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Data
public class GenericResponse<T> {
    
    /**
     * Status of the operation.
     * 
     * Typically "SUCCESS" for successful operations or "ERROR" for failures.
     * Used by clients to quickly determine if the operation succeeded.
     */
    private String status;
    
    /**
     * Descriptive message about the operation result.
     * 
     * Provides human-readable information about what happened,
     * such as "Item added to cart" or "Cart not found".
     */
    private String message;
    
    /**
     * The actual response data.
     * 
     * Generic type parameter allows this to hold any type of data.
     * For cart endpoints, this is typically CartResponseDTO.
     * Can be null in error cases.
     */
    private T data;
}
