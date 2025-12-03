package com.blibi.product.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic response wrapper for standardizing API responses.
 * 
 * This class provides a consistent structure for all API responses, making it
 * easier for API consumers to handle responses uniformly. It uses Java generics
 * to support any data type while maintaining type safety.
 * 
 * Benefits:
 * - Consistent API response structure across all endpoints
 * - Easy error handling on client side
 * - Success/failure status indication
 * - Descriptive messages for better user experience
 * - Type-safe generic data payload
 * 
 * @Data - Lombok annotation generating getters, setters, equals, hashCode, toString
 * @NoArgsConstructor - Lombok annotation generating no-args constructor
 * @AllArgsConstructor - Lombok annotation generating all-args constructor
 * 
 * @param <T> The type of data payload in the response
 * 
 * @author Product Service Team
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {
    
    /**
     * Indicates whether the operation was successful.
     * 
     * true - Operation completed successfully
     * false - Operation failed (check message for details)
     */
    private boolean success;
    
    /**
     * Human-readable message describing the operation result.
     * 
     * Provides context about success or failure, useful for:
     * - User-facing error messages
     * - Debugging and logging
     * - API documentation
     */
    private String message;
    
    /**
     * The actual data payload of the response.
     * 
     * Generic type T allows this wrapper to hold any type of data:
     * - ProductDTO for single product responses
     * - Page<ProductDTO> for paginated product lists
     * - null for error responses
     */
    private T data;

    /**
     * Static factory method for creating successful responses.
     * 
     * This method provides a convenient way to create success responses with
     * the data and message. The success flag is automatically set to true.
     * 
     * @param <T> The type of data payload
     * @param data The response data payload
     * @param message Success message describing the operation
     * @return GenericResponse with success=true and provided data/message
     */
    public static <T> GenericResponse<T> success(T data, String message) {
        return new GenericResponse<>(true, message, data);
    }

    /**
     * Static factory method for creating error responses.
     * 
     * This method provides a convenient way to create error responses with
     * an error message. The success flag is automatically set to false and
     * data is set to null.
     * 
     * @param <T> The type parameter (can be any type, data will be null)
     * @param message Error message describing what went wrong
     * @return GenericResponse with success=false, message, and data=null
     */
    public static <T> GenericResponse<T> error(String message) {
        return new GenericResponse<>(false, message, null);
    }
}
