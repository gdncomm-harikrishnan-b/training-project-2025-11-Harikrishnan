package com.blibi.product.utils;

import com.blibi.product.dto.ProductDTO;
import com.blibi.product.entity.Product;
import org.springframework.beans.BeanUtils;

/**
 * Utility class for common data transformation operations.
 * 
 * This class provides static utility methods for converting between Entity and DTO
 * objects. Using a utility class centralizes conversion logic, making it easier to
 * maintain and ensuring consistency across the application.
 * 
 * Key benefits:
 * - Centralized conversion logic
 * - Reusable across service layer
 * - Easy to maintain and update
 * - Uses Spring's BeanUtils for property copying
 * 
 * @author Product Service Team
 */
public class CommonUtils {
    
    /**
     * Converts a Product entity to a ProductDTO.
     * 
     * This method is used when returning data from the service layer to the
     * controller layer. It copies all matching properties from the entity to
     * the DTO using Spring's BeanUtils, which performs shallow copying of
     * properties with matching names and types.
     * 
     * Usage: Typically called after retrieving entities from the repository
     *        to convert them to DTOs before returning to the controller.
     * 
     * @param product The Product entity to convert
     * @return ProductDTO with properties copied from the entity
     */
    public static ProductDTO getProductDTO(Product product) {
        // Create a new DTO instance
        ProductDTO targetDTO = new ProductDTO();
        // Copy all matching properties from entity to DTO
        // BeanUtils.copyProperties() copies properties with matching names and types
        // It handles null values and type conversions automatically
        BeanUtils.copyProperties(product, targetDTO);
        // Return the populated DTO
        return targetDTO;
    }
    
    /**
     * Converts a ProductDTO to a Product entity.
     * 
     * This method is used when receiving data from the controller layer to convert
     * it to an entity for persistence. It copies all matching properties from the
     * DTO to the entity using Spring's BeanUtils.
     * 
     * Usage: Typically called before saving to the repository to convert DTOs
     *        received from the controller to entities.
     * 
     * Note: The productId field will be copied if present in the DTO, but MongoDB
     *        will generate a new ID if productId is null during save.
     * 
     * @param productDTO The ProductDTO to convert
     * @return Product entity with properties copied from the DTO
     */
    public static Product getProductFromDTO(ProductDTO productDTO) {
        // Create a new entity instance
        Product target = new Product();
        // Copy all matching properties from DTO to entity
        // BeanUtils.copyProperties() handles property name matching and type conversion
        BeanUtils.copyProperties(productDTO, target);
        // Return the populated entity
        return target;
    }
}
