package com.blibi.product.repository;

import com.blibi.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Product data access operations.
 * 
 * This interface extends MongoRepository, providing CRUD operations and custom
 * query methods for Product entities. Spring Data MongoDB automatically
 * implements
 * this interface at runtime, eliminating the need for manual implementation.
 * 
 * Key features:
 * - Inherits standard CRUD operations from MongoRepository
 * - Custom query methods using Spring Data method naming conventions
 * - Custom MongoDB queries using @Query annotation
 * - Built-in pagination support via Pageable parameter
 * 
 * @Repository - Marks this interface as a repository component, eligible for
 *             Spring Data repository scanning
 *             MongoRepository<Product, String> - Extends Spring Data MongoDB
 *             repository with
 *             Product entity and String as ID type
 * 
 * @author HKB
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    /**
     * Finds a product by its business product ID.
     * 
     * This method searches for a product using the business productId field
     * (e.g., MTA-000001), not the MongoDB _id. Spring Data MongoDB automatically
     * generates the query implementation based on the method name.
     * 
     * @param productId The business product identifier
     * @return Optional containing the Product if found, empty otherwise
     */
    java.util.Optional<Product> findByProductId(String productId);

    /**
     * Finds products by exact product name match (case-sensitive).
     * 
     * Spring Data MongoDB automatically generates the query implementation based on
     * the method name. The method name "findByProductName" tells Spring to:
     * - Find documents where the "productName" field matches the parameter
     * - Return results as a Page for pagination support
     * 
     * @param productName The exact product name to search for (case-sensitive)
     * @param pageable    Pagination information (page number and size)
     * @return Page of Product entities matching the exact product name
     */
    Page<Product> findByProductName(String productName, Pageable pageable);

    /**
     * Searches products by name using case-insensitive partial matching.
     * 
     * This method uses MongoDB's $regex operator with case-insensitive option ('i')
     * to perform flexible pattern matching. The query allows partial matches,
     * so searching for "laptop" will match "Laptop Pro", "Gaming Laptop", etc.
     * 
     * MongoDB Query Explanation:
     * - { 'productName': { $regex: ?0, $options: 'i' } }
     * - $regex: ?0 - Uses the first method parameter as the regex pattern
     * - $options: 'i' - Makes the regex case-insensitive
     * 
     * @param productName The product name pattern to search for (case-insensitive)
     * @param pageable    Pagination information (page number and size)
     * @return Page of Product entities matching the search pattern
     */
    @Query("{ 'productName': { $regex: ?0, $options: 'i' } }")
    Page<Product> searchProductByName(String productName, Pageable pageable);

    /**
     * Searches products by category using case-insensitive partial matching.
     * 
     * Similar to searchProductByName, this method uses MongoDB regex for flexible
     * category matching. Useful for browsing products within a category or finding
     * products with similar category names.
     * 
     * MongoDB Query Explanation:
     * - { 'category': { $regex: ?0, $options: 'i' } }
     * - Searches the 'category' field using regex pattern from first parameter
     * - Case-insensitive matching enabled via 'i' option
     * 
     * @param category The category name pattern to search for (case-insensitive)
     * @param pageable Pagination information (page number and size)
     * @return Page of Product entities in the matching category
     */
    @Query("{ 'category': { $regex: ?0, $options: 'i' } }")
    Page<Product> searchProductByCategory(String category, Pageable pageable);
}
