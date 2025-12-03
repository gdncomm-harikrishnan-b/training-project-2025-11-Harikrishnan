package com.blibi.product.serviceimpl;

import com.blibi.product.dto.ProductDTO;
import com.blibi.product.entity.Product;
import com.blibi.product.repository.ProductRepository;
import com.blibi.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.blibi.product.utils.CommonUtils.getProductDTO;
import static com.blibi.product.utils.CommonUtils.getProductFromDTO;

/**
 * Service implementation for Product business logic operations.
 * 
 * This class implements the ProductService interface and contains the core business
 * logic for product management. It acts as an intermediary between the controller
 * and repository layers, handling data transformation and business rules.
 * 
 * Key responsibilities:
 * - Converting between DTO and Entity objects
 * - Executing business logic and validation
 * - Handling exceptions and logging
 * - Coordinating with the repository layer for data persistence
 * 
 * @Service - Marks this class as a Spring service component, making it eligible
 *           for component scanning and dependency injection
 * @Slf4j - Provides logger instance via Lombok for logging operations
 * 
 * @author HKB
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    
    /**
     * Repository dependency for data access operations.
     * Injected via constructor for better testability and immutability.
     */
    private final ProductRepository productRepository;

    /**
     * Constructor-based dependency injection.
     * 
     * @param productRepository The product repository for database operations
     */
   public ProductServiceImpl(ProductRepository productRepository)
   {
        this.productRepository = productRepository;
   }

    /**
     * Creates a new product in the system.
     * 
     * This method performs the following operations:
     * 1. Converts ProductDTO to Product entity using utility method
     * 2. Persists the entity to MongoDB via repository
     * 3. Converts the saved entity back to DTO for response
     * 4. Handles exceptions and logs errors for debugging
     * 
     * @param productDTO The product data transfer object containing product information
     * @return ProductDTO representing the created product with MongoDB-generated ID
     * @throws Exception if product creation fails (e.g., database connection issues)
     */
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        // Log the creation attempt for monitoring and debugging
        log.info("Creating new product: {}", productDTO.getProductName());

        try {
            // Convert DTO to Entity for persistence
            // This separation allows us to have different validation rules for DTO vs Entity
            Product productEntity = getProductFromDTO(productDTO);
            
            // Save to MongoDB - MongoDB will auto-generate the productId if not provided
            Product createdProduct = productRepository.save(productEntity);
            
            // Log successful creation with generated ID
            log.info("Product created successfully with ID: {}", createdProduct.getProductId());
            
            // Convert Entity back to DTO for response (includes generated ID)
            return getProductDTO(createdProduct);
        } catch (Exception e) {
            // Log error with context for troubleshooting
            log.error("Error creating product: {}", productDTO.getProductName(), e);
            // Re-throw exception to be handled by controller/global exception handler
            throw e;
        }
    }

    /**
     * Retrieves products by exact product name match.
     * 
     * This method performs a case-sensitive exact match search. It's useful when
     * you know the exact product name and want precise results.
     * 
     * @param productName The exact product name to search for (case-sensitive)
     * @param pageable Pagination information (page number and size)
     * @return Page of ProductDTO objects matching the exact product name
     */
    @Override
    public Page<ProductDTO> viewProductDetailsByName(String productName, Pageable pageable) {
        // Log the search operation with pagination details
        log.info("Fetching product details by name: {} (page: {}, size: {})",
                productName, pageable.getPageNumber(), pageable.getPageSize());

        // Query repository for exact match (case-sensitive)
        Page<Product> products = productRepository.findByProductName(productName, pageable);
        
        // Log debug information about results (only visible in DEBUG log level)
        log.debug("Found {} products with name: {}", products.getTotalElements(), productName);

        // Convert each Product entity to ProductDTO using Stream map operation
        // This ensures we return DTOs instead of entities, maintaining layer separation
        return products.map(product -> getProductDTO(product));
    }

    /**
     * Searches products by name using case-insensitive partial matching.
     * 
     * This method performs a wildcard search using MongoDB regex, allowing users
     * to find products with partial name matches. The search is case-insensitive,
     * making it more user-friendly.
     * 
     * @param productName The product name pattern to search for (case-insensitive)
     * @param pageable Pagination information (page number and size)
     * @return Page of ProductDTO objects matching the search pattern
     */
    @Override
    public Page<ProductDTO> searchProductByName(String productName, Pageable pageable) {
        // Log the search operation with pagination details
        log.info("Searching products by name: {} (page: {}, size: {})",
                productName, pageable.getPageNumber(), pageable.getPageSize());
        
        // Query repository for case-insensitive partial match using MongoDB regex
        Page<Product> products = productRepository.searchProductByName(productName, pageable);
        
        // Log debug information about search results
        log.debug("Search found {} products matching: {}", products.getTotalElements(), productName);

        // Convert each Product entity to ProductDTO
        return products.map(product -> getProductDTO(product));
    }

    /**
     * Retrieves a single product by its unique identifier.
     * 
     * This method fetches a product using its MongoDB-generated ID. If the product
     * doesn't exist, it throws a RuntimeException which should be caught by a
     * global exception handler (recommended improvement).
     * 
     * @param productId The unique product identifier (MongoDB ObjectId as String)
     * @return ProductDTO representing the product details
     * @throws RuntimeException if product is not found with the given ID
     */
    @Override
    public ProductDTO getProductDetail(String productId) {
        // Log the retrieval attempt
        log.info("Fetching product details for ID: {}", productId);
        
        // Find product by ID - returns Optional<Product>
        // orElseThrow() converts Optional to Product or throws exception if not found
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        
        // Convert Entity to DTO for response
        return getProductDTO(product);
    }

    /**
     * Searches products by category using case-insensitive partial matching.
     * 
     * This method retrieves all products belonging to a specific category using
     * MongoDB regex for flexible matching. Useful for browsing products by category.
     * 
     * @param category The category name to search for (case-insensitive)
     * @param pageable Pagination information (page number and size)
     * @return Page of ProductDTO objects in the specified category
     */
    @Override
    public Page<ProductDTO> searchProductByCategory(String category, Pageable pageable) {
        // Log the category search operation with pagination details
        log.info("Searching products by category: {} (page: {}, size: {})",
                category, pageable.getPageNumber(), pageable.getPageSize());
        
        // Query repository for case-insensitive category match using MongoDB regex
        Page<Product> products = productRepository.searchProductByCategory(category, pageable);
        
        // Log debug information about category search results
        log.debug("Search found {} products in category: {}", products.getTotalElements(), category);

        // Convert each Product entity to ProductDTO
        return products.map(product -> getProductDTO(product));
    }
}
