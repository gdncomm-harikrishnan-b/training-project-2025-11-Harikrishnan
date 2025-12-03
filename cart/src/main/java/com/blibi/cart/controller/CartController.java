package com.blibi.cart.controller;

import com.blibi.cart.dto.AddToCartRequest;
import com.blibi.cart.dto.CartResponseDTO;
import com.blibi.cart.dto.GenericResponse;
import com.blibi.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for handling cart-related HTTP requests.
 * 
 * This controller provides endpoints for:
 * - Adding items to a user's cart
 * - Viewing cart contents
 * - Removing items from cart
 * 
 * All endpoints return responses wrapped in GenericResponse for consistent API structure.
 * 
 * @RestController: Marks this class as a REST controller, automatically serializes return objects to JSON
 * @Slf4j: Provides logging functionality via SLF4J
 * @RequiredArgsConstructor: Generates constructor for final fields (dependency injection)
 * @RequestMapping: Base path for all endpoints in this controller
 * 
 * @author Cart Service Team
 * @version 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    
    /**
     * Service layer dependency for cart business logic.
     * Injected via constructor using @RequiredArgsConstructor.
     */
    private final CartService cartService;

    /**
     * Endpoint to add a product to the user's cart.
     * 
     * If the product already exists in the cart, the quantity is updated.
     * The service fetches product details from Product Service to get current pricing.
     * 
     * @param userId Unique identifier for the user (UUID format)
     * @param request Request body containing productId and quantity to add
     * @return GenericResponse containing CartResponseDTO with updated cart details
     * 
     * HTTP Method: POST
     * Path: /api/cart/{userId}/add
     * 
     * Example Request:
     * POST /api/cart/550e8400-e29b-41d4-a716-446655440000/add
     * Body: {"productId": "123", "quantity": 2}
     */
    @PostMapping("/{userId}/add")
    public GenericResponse<CartResponseDTO> add(
            @PathVariable UUID userId,
            @RequestBody AddToCartRequest request) {
        // Build and return success response with cart data
        return GenericResponse.<CartResponseDTO>builder()
                .status("SUCCESS")
                .message("Item added to cart")
                .data(cartService.addToCart(userId, request))
                .build();
    }
    
    /**
     * Endpoint to retrieve the complete cart for a user.
     * 
     * Returns all items in the cart along with the total cart price.
     * Throws CartNotFoundException if cart doesn't exist.
     * 
     * @param userId Unique identifier for the user (UUID format)
     * @return GenericResponse containing CartResponseDTO with cart details
     * 
     * HTTP Method: GET
     * Path: /api/cart/{userId}
     * 
     * Example Request:
     * GET /api/cart/550e8400-e29b-41d4-a716-446655440000
     */
    @GetMapping("/{userId}")
    public GenericResponse<CartResponseDTO> view(@PathVariable UUID userId) {
        // Build and return success response with cart data
        return GenericResponse.<CartResponseDTO>builder()
                .status("SUCCESS")
                .message("Cart fetched")
                .data(cartService.getCart(userId))
                .build();
    }
    
    /**
     * Endpoint to remove a specific product from the user's cart.
     * 
     * Removes the product if it exists in the cart and recalculates the total price.
     * Throws CartNotFoundException if cart doesn't exist.
     * 
     * @param userId Unique identifier for the user (UUID format)
     * @param productId Product identifier to remove from cart
     * @return GenericResponse containing CartResponseDTO with updated cart details
     * 
     * HTTP Method: DELETE
     * Path: /api/cart/{userId}/remove/{productId}
     * 
     * Example Request:
     * DELETE /api/cart/550e8400-e29b-41d4-a716-446655440000/remove/123
     */
    @DeleteMapping("/{userId}/remove/{productId}")
    public GenericResponse<CartResponseDTO> remove(
            @PathVariable UUID userId,
            @PathVariable String productId) {
        // Build and return success response with updated cart data
        return GenericResponse.<CartResponseDTO>builder()
                .status("SUCCESS")
                .message("Item removed")
                .data(cartService.removeItem(userId, productId))
                .build();
    }
}
