package com.blibi.product.controller;

import com.blibi.product.dto.ProductDTO;
import com.blibi.product.service.ProductService;
import com.blibi.product.wrapper.GenericResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling Product-related HTTP requests.
 * 
 * This controller provides RESTful endpoints for product management operations
 * including creation, retrieval, and search functionality. All endpoints return
 * standardized responses wrapped in GenericResponse objects.
 * 
 * Base URL: /api/product
 * 
 * @RestController - Marks this class as a REST controller, automatically
 *                 serializes
 *                 return values to JSON/XML
 * @RequestMapping - Maps all methods in this controller to /api/product base
 *                 path
 * @Slf4j - Provides logger instance via Lombok
 * 
 * @author HKB
 */
@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {

    /**
     * Service layer dependency for business logic operations.
     * Injected via constructor for better testability and immutability.
     */
    private final ProductService productService;

    /**
     * Constructor-based dependency injection.
     * 
     * @param productService The product service implementation
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product in the system.
     * This endpoint accepts a ProductDTO, validates it, and persists it to the
     * database.
     * The @Valid annotation triggers Jakarta Bean Validation to ensure all required
     * fields are present and valid before processing.
     * 
     * @param productDTO The product data transfer object containing product
     *                   information
     * @return ResponseEntity containing the created product wrapped in
     *         GenericResponse
     *         with HTTP 201 (CREATED) status
     * 
     * @PostMapping - Maps HTTP POST requests to this method
     * @Valid - Enables automatic validation of the request body
     * @RequestBody - Binds the HTTP request body to the ProductDTO parameter
     */
    @PostMapping("/createProduct")
    public ResponseEntity<GenericResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        // Delegate to service layer for business logic
        ProductDTO savedProduct = productService.createProduct(productDTO);
        // Wrap response in GenericResponse for consistent API structure
        return new ResponseEntity<>(GenericResponse.success(savedProduct, "Product created successfully"),
                HttpStatus.CREATED);
    }

    /**
     * Retrieves products by exact product name match.
     * 
     * This endpoint performs a case-sensitive exact match search for products
     * with the specified name. Results are paginated for efficient data retrieval.
     * 
     * @param productName The exact product name to search for (case-sensitive)
     * @param page        Page number (0-indexed, default: 0)
     * @param size        Number of items per page (default: 5)
     * @return ResponseEntity containing a paginated list of products with HTTP 200
     *         (OK) status
     * 
     * @GetMapping - Maps HTTP GET requests to this method
     * @PathVariable - Extracts productName from the URL path
     * @RequestParam - Extracts query parameters with default values
     */
    @GetMapping("/productDetail/productName/{productName}")
    public ResponseEntity<GenericResponse<Page<ProductDTO>>> getProductByName(
            @PathVariable String productName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        // Create Pageable object for pagination
        Pageable pageable = PageRequest.of(page, size);
        // Fetch products from service layer
        Page<ProductDTO> products = productService.viewProductDetailsByName(productName, pageable);
        // Return paginated results
        return new ResponseEntity<>(GenericResponse.success(products, "Product details fetched successfully"),
                HttpStatus.OK);
    }

    /**
     * Searches products by name using case-insensitive partial matching.
     * 
     * This endpoint performs a wildcard search using MongoDB regex, allowing
     * partial matches. The search is case-insensitive, so "laptop" will match
     * "Laptop", "LAPTOP", etc.
     * 
     * @param productName The product name pattern to search for (case-insensitive)
     * @param page        Page number (0-indexed, default: 0)
     * @param size        Number of items per page (default: 5)
     * @return ResponseEntity containing a paginated list of matching products with
     *         HTTP 200 (OK) status
     * 
     * @PostMapping - Maps HTTP POST requests (used for search operations)
     * @PathVariable - Extracts productName from the URL path
     * @RequestParam - Extracts pagination parameters with default values
     */
    @PostMapping("/searchProduct/productName/{productName}")
    public ResponseEntity<GenericResponse<Page<ProductDTO>>> searchProductByName(
            @PathVariable String productName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        // Create Pageable object for pagination
        Pageable pageable = PageRequest.of(page, size);
        // Perform case-insensitive search via service layer
        Page<ProductDTO> products = productService.searchProductByName(productName, pageable);
        // Return search results
        return new ResponseEntity<>(GenericResponse.success(products, "Products found successfully"), HttpStatus.OK);
    }

    /**
     * Searches products by category using case-insensitive partial matching.
     * 
     * This endpoint retrieves all products belonging to a specific category.
     * The search uses MongoDB regex for case-insensitive matching, allowing
     * partial category name matches.
     * 
     * @param category The category name to search for (case-insensitive)
     * @param page     Page number (0-indexed, default: 0)
     * @param size     Number of items per page (default: 5)
     * @return ResponseEntity containing a paginated list of products in the
     *         category with HTTP 200 (OK) status
     * 
     * @GetMapping - Maps HTTP GET requests to this method
     * @PathVariable - Extracts category from the URL path
     * @RequestParam - Extracts pagination parameters with default values
     */
    @GetMapping("/searchProduct/category/{category}")
    public ResponseEntity<GenericResponse<Page<ProductDTO>>> searchProductByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        // Create Pageable object for pagination
        Pageable pageable = PageRequest.of(page, size);
        // Fetch products by category from service layer
        Page<ProductDTO> products = productService.searchProductByCategory(category, pageable);
        // Return category-based results
        return new ResponseEntity<>(GenericResponse.success(products, "Products found by category successfully"),
                HttpStatus.OK);
    }

    /**
     * Retrieves a single product by its business product identifier.
     * 
     * This endpoint fetches detailed information about a specific product
     * using its business productId (e.g., MTA-000001), not the MongoDB _id.
     * If the product is not found, the service layer will throw a RuntimeException.
     * 
     * @param id The business product identifier (e.g., MTA-000001)
     * @return ResponseEntity containing the product details wrapped in
     *         GenericResponse
     *         with HTTP 200 (OK) status
     * 
     * @GetMapping - Maps HTTP GET requests to this method
     * @PathVariable - Extracts product ID from the URL path
     */
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<ProductDTO>> details(@PathVariable String id) {
        // Log the request for debugging and monitoring
        log.info("Fetching details for product ID: {}", id);
        // Fetch product from service layer (throws exception if not found)
        ProductDTO product = productService.getProductDetail(id);
        // Return product details
        return new ResponseEntity<>(
                GenericResponse.success(product, "Product details fetched successfully"),
                HttpStatus.OK);
    }

}