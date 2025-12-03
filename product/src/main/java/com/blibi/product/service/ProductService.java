package com.blibi.product.service;

import com.blibi.product.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Product business logic operations.
 * 
 * This interface defines the contract for product-related operations, following
 * the Service Layer pattern to separate business logic from the controller and
 * repository layers. The interface is implemented by ProductServiceImpl.
 * 
 * Key responsibilities:
 * - Product creation and persistence
 * - Product retrieval by various criteria
 * - Product search operations with pagination
 * - Data transformation between DTO and Entity
 * 
 * @author HKB
 */
public interface ProductService {
    
    /**
     * Creates a new product in the system.
     * 
     * This method converts the ProductDTO to a Product entity, persists it to
     * the database, and returns the created product as a DTO.
     * 
     * @param productDTO The product data transfer object containing product information
     * @return ProductDTO representing the created product with generated ID
     * @throws Exception if product creation fails (e.g., database errors)
     */
    ProductDTO createProduct(ProductDTO productDTO);

    /**
     * Retrieves products by exact product name match.
     * 
     * Performs a case-sensitive exact match search for products with the specified
     * name. Results are paginated for efficient data retrieval.
     * 
     * @param productName The exact product name to search for (case-sensitive)
     * @param pageable Pagination information (page number and size)
     * @return Page of ProductDTO objects matching the exact product name
     */
    Page<ProductDTO> viewProductDetailsByName(String productName, Pageable pageable);

    /**
     * Searches products by name using case-insensitive partial matching.
     * 
     * Performs a wildcard search using MongoDB regex, allowing partial matches.
     * The search is case-insensitive, enabling flexible product discovery.
     * 
     * @param productName The product name pattern to search for (case-insensitive)
     * @param pageable Pagination information (page number and size)
     * @return Page of ProductDTO objects matching the search pattern
     */
    Page<ProductDTO> searchProductByName(String productName, Pageable pageable);

    /**
     * Retrieves a single product by its unique identifier.
     * 
     * Fetches detailed information about a specific product using its MongoDB ID.
     * 
     * @param productId The unique product identifier (MongoDB ObjectId as String)
     * @return ProductDTO representing the product details
     * @throws RuntimeException if product is not found with the given ID
     */
    ProductDTO getProductDetail(String productId);

    /**
     * Searches products by category using case-insensitive partial matching.
     * 
     * Retrieves all products belonging to a specific category using MongoDB regex
     * for case-insensitive matching, allowing partial category name matches.
     * 
     * @param category The category name to search for (case-insensitive)
     * @param pageable Pagination information (page number and size)
     * @return Page of ProductDTO objects in the specified category
     */
    Page<ProductDTO> searchProductByCategory(String category, Pageable pageable);
}
