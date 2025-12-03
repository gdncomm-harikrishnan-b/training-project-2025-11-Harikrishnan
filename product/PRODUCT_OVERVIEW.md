# Product Service - Overview Documentation

## Introduction

The Product Service is a Spring Boot-based microservice designed to manage product information for an online marketplace application. It provides a RESTful API for creating, retrieving, and searching products with support for pagination, caching, and comprehensive data validation.

This service is built using modern Java technologies and follows best practices for microservice architecture, including separation of concerns, DTO pattern, and repository abstraction. It integrates with MongoDB for data persistence and Redis for caching to ensure optimal performance.

---

## Features

### Core Functionality
- **Product Creation**: Create new products with validation
- **Product Retrieval**: Fetch product details by ID
- **Product Search**: 
  - Search products by name (exact match and wildcard)
  - Search products by category
  - Case-insensitive search capabilities
- **Pagination**: All list/search endpoints support pagination for efficient data retrieval
- **Caching**: Redis-based caching enabled for improved performance
- **API Documentation**: OpenAPI/Swagger integration for interactive API documentation

### Technical Features
- Input validation using Jakarta Bean Validation
- Structured response wrapper (`GenericResponse`) for consistent API responses
- Comprehensive logging for debugging and monitoring
- MongoDB compound indexing for optimized queries
- DTO pattern for data transfer and entity separation

---

## Technology Stack

### Core Framework
- **Spring Boot**: `3.4.12`
- **Java**: `21`
- **Build Tool**: Maven

### Dependencies
- **Spring Boot Starter Web**: RESTful web services
- **Spring Boot Starter Data MongoDB**: MongoDB integration
- **Spring Boot Starter Data Redis**: Redis caching support
- **Lombok**: Reduces boilerplate code
- **SpringDoc OpenAPI**: API documentation (Swagger UI)
- **Jakarta Bean Validation**: Input validation

### Database & Caching
- **MongoDB**: NoSQL database for product storage
- **Redis**: In-memory caching layer

### Development Tools
- **Lombok**: Code generation for getters, setters, constructors
- **SLF4J**: Logging framework

---

## Architecture

The application follows a **layered architecture** pattern with clear separation of concerns:

```
┌─────────────────────────────────────┐
│         Controller Layer            │
│      (ProductController)            │
│   - Handles HTTP requests/responses │
│   - Input validation                │
│   - Response formatting             │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│          Service Layer               │
│      (ProductService)                │
│   - Business logic                   │
│   - Transaction management           │
│   - Error handling                   │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Repository Layer              │
│     (ProductRepository)             │
│   - Data access abstraction          │
│   - Custom queries                   │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│           MongoDB                    │
│      (Data Persistence)              │
└─────────────────────────────────────┘
```

### Component Details

1. **Controller Layer** (`ProductController`)
   - RESTful endpoints
   - Request/response mapping
   - HTTP status code management

2. **Service Layer** (`ProductService` / `ProductServiceImpl`)
   - Business logic implementation
   - DTO to Entity conversion
   - Logging and error handling

3. **Repository Layer** (`ProductRepository`)
   - Extends `MongoRepository`
   - Custom query methods
   - Pagination support

4. **Entity Layer** (`Product`)
   - MongoDB document mapping
   - Validation constraints
   - Index definitions

5. **DTO Layer** (`ProductDTO`)
   - Data transfer objects
   - Input validation
   - Serialization support

6. **Utility Layer** (`CommonUtils`)
   - Entity-DTO conversion utilities

7. **Configuration** (`OpenApiConfig`)
   - OpenAPI/Swagger configuration

---

## API Endpoints

Base URL: `http://localhost:8080/api/product`

### 1. Create Product

**Endpoint**: `POST /createProduct`

**Description**: Creates a new product in the system.

**Request Headers**:
```
Content-Type: application/json
```

**Request Body**:
```json
{
  "productName": "Laptop Pro 15",
  "description": "High-performance laptop with 16GB RAM and 512GB SSD",
  "price": 1299.99,
  "category": "Electronics",
  "images": [
    "https://example.com/images/laptop1.jpg",
    "https://example.com/images/laptop2.jpg"
  ]
}
```

**Response** (201 Created):
```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "productId": "67890abcdef1234567890123",
    "productName": "Laptop Pro 15",
    "description": "High-performance laptop with 16GB RAM and 512GB SSD",
    "price": 1299.99,
    "category": "Electronics",
    "images": [
      "https://example.com/images/laptop1.jpg",
      "https://example.com/images/laptop2.jpg"
    ]
  }
}
```

**Validation Rules**:
- `productName`: Required, cannot be blank
- `description`: Required, cannot be blank
- `price`: Required, must be greater than zero
- `category`: Required, cannot be blank
- `images`: Optional list of strings

---

### 2. Get Product by ID

**Endpoint**: `GET /{id}`

**Description**: Retrieves a single product by its unique identifier.

**Path Parameters**:
- `id` (String): Product ID

**Example Request**:
```
GET /api/product/67890abcdef1234567890123
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Product details fetched successfully",
  "data": {
    "productId": "67890abcdef1234567890123",
    "productName": "Laptop Pro 15",
    "description": "High-performance laptop with 16GB RAM and 512GB SSD",
    "price": 1299.99,
    "category": "Electronics",
    "images": [
      "https://example.com/images/laptop1.jpg",
      "https://example.com/images/laptop2.jpg"
    ]
  }
}
```

**Error Response** (404 Not Found):
```json
{
  "success": false,
  "message": "Product not found with ID: 67890abcdef1234567890123",
  "data": null
}
```

---

### 3. Get Products by Name (Exact Match)

**Endpoint**: `GET /productDetail/productName/{productName}`

**Description**: Retrieves products with exact name match (case-sensitive).

**Path Parameters**:
- `productName` (String): Exact product name to search

**Query Parameters**:
- `page` (int, default: 0): Page number (0-indexed)
- `size` (int, default: 5): Number of items per page

**Example Request**:
```
GET /api/product/productDetail/productName/Laptop%20Pro%2015?page=0&size=10
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Product details fetched successfully",
  "data": {
    "content": [
      {
        "productId": "67890abcdef1234567890123",
        "productName": "Laptop Pro 15",
        "description": "High-performance laptop with 16GB RAM and 512GB SSD",
        "price": 1299.99,
        "category": "Electronics",
        "images": ["https://example.com/images/laptop1.jpg"]
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "first": true,
    "numberOfElements": 1
  }
}
```

---

### 4. Search Products by Name (Wildcard)

**Endpoint**: `POST /searchProduct/productName/{productName}`

**Description**: Searches products by name using case-insensitive partial matching.

**Path Parameters**:
- `productName` (String): Product name pattern to search

**Query Parameters**:
- `page` (int, default: 0): Page number (0-indexed)
- `size` (int, default: 5): Number of items per page

**Example Request**:
```
POST /api/product/searchProduct/productName/laptop?page=0&size=5
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Products found successfully",
  "data": {
    "content": [
      {
        "productId": "67890abcdef1234567890123",
        "productName": "Laptop Pro 15",
        "description": "High-performance laptop",
        "price": 1299.99,
        "category": "Electronics",
        "images": []
      },
      {
        "productId": "67890abcdef1234567890124",
        "productName": "Gaming Laptop",
        "description": "Gaming laptop with RTX 4060",
        "price": 1599.99,
        "category": "Electronics",
        "images": []
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 5
    },
    "totalElements": 2,
    "totalPages": 1,
    "last": true,
    "first": true,
    "numberOfElements": 2
  }
}
```

---

### 5. Search Products by Category

**Endpoint**: `GET /searchProduct/category/{category}`

**Description**: Searches products by category using case-insensitive partial matching.

**Path Parameters**:
- `category` (String): Category name to search

**Query Parameters**:
- `page` (int, default: 0): Page number (0-indexed)
- `size` (int, default: 5): Number of items per page

**Example Request**:
```
GET /api/product/searchProduct/category/Electronics?page=0&size=10
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Products found by category successfully",
  "data": {
    "content": [
      {
        "productId": "67890abcdef1234567890123",
        "productName": "Laptop Pro 15",
        "description": "High-performance laptop",
        "price": 1299.99,
        "category": "Electronics",
        "images": []
      },
      {
        "productId": "67890abcdef1234567890125",
        "productName": "Wireless Mouse",
        "description": "Ergonomic wireless mouse",
        "price": 29.99,
        "category": "Electronics",
        "images": []
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10
    },
    "totalElements": 2,
    "totalPages": 1,
    "last": true,
    "first": true,
    "numberOfElements": 2
  }
}
```

---

## Data Models

### Product Entity

The `Product` entity represents the MongoDB document structure:

```java
@Document(collection = "products")
public class Product {
    @Id
    private String productId;           // Auto-generated MongoDB ID
    
    @NotNull(message = "Product Name cannot be Empty")
    private String productName;         // Product name (indexed)
    
    @NotNull(message = "Product Description cannot be Empty")
    private String description;         // Product description
    
    @Positive(message = "Price must be greater than zero")
    private Double price;               // Product price
    
    @NotNull(message = "Category cannot be Empty")
    private String category;            // Product category
    
    private List<String> images;       // Optional list of image URLs
}
```

**Database Collection**: `products`

**Indexes**:
- Compound index on `productName` for optimized queries

---

### ProductDTO

The `ProductDTO` is used for data transfer between layers:

```java
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
```

**Key Differences from Entity**:
- Uses `@NotBlank` instead of `@NotNull` for string validation
- Implements `Serializable` for network transfer
- Used for API request/response

---

### GenericResponse

Standardized response wrapper for all API endpoints:

```java
public class GenericResponse<T> {
    private boolean success;    // Operation success status
    private String message;     // Descriptive message
    private T data;            // Response payload (generic type)
}
```

**Static Factory Methods**:
- `GenericResponse.success(T data, String message)`: Success response
- `GenericResponse.error(String message)`: Error response

---

## Error Handling

### Validation Errors

The application uses Jakarta Bean Validation for input validation. Invalid requests return HTTP 400 (Bad Request) with validation error details.

**Example Validation Error Response**:
```json
{
  "timestamp": "2025-01-15T10:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "price",
      "message": "Price must be greater than zero"
    },
    {
      "field": "productName",
      "message": "Product name is required and cannot be blank"
    }
  ]
}
```

### Not Found Errors

When a product is not found by ID, the service throws a `RuntimeException`:

**Error Response**:
```json
{
  "success": false,
  "message": "Product not found with ID: invalid-id",
  "data": null
}
```

### Exception Handling Strategy

1. **Service Layer**: 
   - Catches exceptions during product creation
   - Logs errors with context
   - Re-throws exceptions for controller handling
   - Uses `orElseThrow()` for optional product retrieval

2. **Controller Layer**:
   - Uses `@Valid` annotation for automatic validation
   - Returns appropriate HTTP status codes
   - Wraps responses in `GenericResponse`

3. **Logging**:
   - Comprehensive logging using SLF4J
   - Log levels: INFO, DEBUG, ERROR
   - Includes context information (product IDs, names, etc.)

### Common Error Scenarios

| Scenario | HTTP Status | Response |
|----------|-------------|----------|
| Invalid input data | 400 | Validation error details |
| Product not found | 500* | Error message in GenericResponse |
| Database connection failure | 500 | Internal server error |
| Invalid JSON format | 400 | Bad request error |

*Note: Currently returns 500, but could be improved to return 404 with proper exception handling.

---

## Configuration

### Application Properties

The application configuration is defined in `application.properties`:

```properties
# Application Name
spring.application.name=product

# Server Configuration
server.port=8080

# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=test
spring.data.mongodb.repositories.enabled=true

# Logging Configuration
logging.level.org.springframework.data.mongodb.core.MongoTemplate=DEBUG

# Redis Configuration
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.timeout=2000ms

# Caching Configuration
spring.cache.type=redis
spring.cache.redis.time-to-live=600000

# Cache Logging
logging.level.org.springframework.cache=TRACE
logging.level.org.springframework.data.redis=DEBUG
```

### Configuration Details

#### MongoDB
- **Host**: `localhost`
- **Port**: `27017`
- **Database**: `test`
- **Collection**: `products` (defined in entity)

#### Redis
- **Host**: `localhost`
- **Port**: `6379`
- **Timeout**: `2000ms`
- **Cache TTL**: `600000ms` (10 minutes)

#### Server
- **Port**: `8080`
- **Base Path**: `/api/product`

### OpenAPI/Swagger Configuration

Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

API documentation endpoint: `http://localhost:8080/v3/api-docs`

---

## Getting Started

### Prerequisites

1. **Java 21** or higher
2. **Maven 3.6+**
3. **MongoDB** (running on localhost:27017)
4. **Redis** (running on localhost:6379) - Optional but recommended for caching

### Installation Steps

#### 1. Clone the Repository
```bash
cd /path/to/project/product
```

#### 2. Install Dependencies
```bash
./mvnw clean install
```

Or on Windows:
```bash
mvnw.cmd clean install
```

#### 3. Start MongoDB
Ensure MongoDB is running on `localhost:27017`:
```bash
# macOS/Linux
mongod

# Or using Docker
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

#### 4. Start Redis (Optional)
```bash
# macOS
brew services start redis

# Or using Docker
docker run -d -p 6379:6379 --name redis redis:latest
```

#### 5. Configure Application
Update `application.properties` if your MongoDB or Redis instances are running on different hosts/ports.

#### 6. Run the Application
```bash
./mvnw spring-boot:run
```

Or build and run the JAR:
```bash
./mvnw clean package
java -jar target/product-0.0.1-SNAPSHOT.jar
```

#### 7. Verify Application
- Application should start on `http://localhost:8080`
- Check Swagger UI: `http://localhost:8080/swagger-ui.html`
- Health check: Verify MongoDB connection in logs

### Testing the API

#### Using cURL

**Create a Product**:
```bash
curl -X POST http://localhost:8080/api/product/createProduct \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "Test Product",
    "description": "This is a test product",
    "price": 99.99,
    "category": "Test Category",
    "images": ["https://example.com/image.jpg"]
  }'
```

**Get Product by ID**:
```bash
curl http://localhost:8080/api/product/{productId}
```

**Search Products by Name**:
```bash
curl -X POST "http://localhost:8080/api/product/searchProduct/productName/laptop?page=0&size=5"
```

**Search Products by Category**:
```bash
curl "http://localhost:8080/api/product/searchProduct/category/Electronics?page=0&size=10"
```

#### Using Swagger UI

1. Navigate to `http://localhost:8080/swagger-ui.html`
2. Explore available endpoints
3. Test endpoints directly from the UI
4. View request/response schemas

### Development Tips

1. **Logging**: Check application logs for debugging information
2. **MongoDB Compass**: Use MongoDB Compass to view database contents
3. **Redis CLI**: Use `redis-cli` to monitor cache operations
4. **Postman/Insomnia**: Import API endpoints for easier testing

### Troubleshooting

**Issue**: Application fails to start
- **Solution**: Verify MongoDB is running and accessible

**Issue**: Connection refused to MongoDB
- **Solution**: Check MongoDB host and port in `application.properties`

**Issue**: Redis connection errors
- **Solution**: Redis is optional; caching will be disabled if Redis is unavailable

**Issue**: Port 8080 already in use
- **Solution**: Change `server.port` in `application.properties` or stop the conflicting service

---

## Additional Information

### Project Structure
```
product/
├── src/
│   ├── main/
│   │   ├── java/com/blibi/product/
│   │   │   ├── ProductApplication.java
│   │   │   ├── controller/
│   │   │   │   └── ProductController.java
│   │   │   ├── service/
│   │   │   │   └── ProductService.java
│   │   │   ├── serviceimpl/
│   │   │   │   └── ProductServiceImpl.java
│   │   │   ├── repository/
│   │   │   │   └── ProductRepository.java
│   │   │   ├── entity/
│   │   │   │   └── Product.java
│   │   │   ├── dto/
│   │   │   │   └── ProductDTO.java
│   │   │   ├── wrapper/
│   │   │   │   └── GenericResponse.java
│   │   │   ├── utils/
│   │   │   │   └── CommonUtils.java
│   │   │   └── configuration/
│   │   │       └── OpenApiConfig.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

### Key Annotations Used
- `@SpringBootApplication`: Main application class
- `@RestController`: REST controller
- `@Service`: Service layer component
- `@Repository`: Data access layer
- `@Document`: MongoDB document mapping
- `@Id`: MongoDB document ID
- `@Valid`: Request validation
- `@EnableCaching`: Enable Spring Cache
- `@EnableMongoRepositories`: Enable MongoDB repositories

---

## Topics to Learn

This project covers various important concepts and technologies. Here are the key topics you should learn to fully understand and work with this codebase:

### Core Java & Spring Framework
Spring Boot Fundamentals, Spring Dependency Injection, Spring Bean Lifecycle, Spring Configuration, Spring Profiles, Java 21 Features, Java Generics, Java Annotations, Java Reflection, Object-Oriented Programming (OOP), SOLID Principles, Design Patterns (Factory, Builder, Singleton)

### Spring Data & MongoDB
Spring Data MongoDB, MongoDB Repository Pattern, MongoDB Query Methods, MongoDB Custom Queries, MongoDB Indexing, MongoDB Aggregation, Spring Data Pagination, Spring Data Sorting, Document-Oriented Database Concepts, NoSQL Database Design, MongoDB CRUD Operations, MongoDB Regex Queries

### RESTful API Development
REST API Design Principles, HTTP Methods (GET, POST, PUT, DELETE), HTTP Status Codes, Request/Response Handling, Path Variables, Query Parameters, Request Body Mapping, Response Entity, Content Negotiation, API Versioning, RESTful Best Practices

### Data Transfer Objects (DTOs) & Entity Mapping
DTO Pattern, Entity-DTO Conversion, BeanUtils, Property Mapping, Data Transformation, Layer Separation, Serialization, Deserialization, Object Mapping Strategies

### Validation & Error Handling
Jakarta Bean Validation, Validation Annotations (@NotNull, @NotBlank, @Positive), Custom Validators, Exception Handling, Global Exception Handlers, Error Response Formatting, Validation Error Messages, Input Sanitization

### Caching
Spring Cache Abstraction, Redis Caching, Cache Annotations (@Cacheable, @CacheEvict), Cache Configuration, TTL (Time To Live), Cache Invalidation Strategies, Cache Performance Optimization

### Logging & Monitoring
SLF4J Logging, Log4j/Logback, Log Levels (DEBUG, INFO, WARN, ERROR), Structured Logging, Logging Best Practices, Application Monitoring, Performance Logging

### API Documentation
OpenAPI Specification, Swagger UI, API Documentation Generation, API Testing Tools, Postman/Insomnia, API Contract Testing

### Build Tools & Dependency Management
Maven Build Tool, Maven Lifecycle, Maven Dependencies, Maven Plugins, Project Object Model (POM), Dependency Resolution, Build Automation

### Testing
Unit Testing, Integration Testing, Mockito, JUnit, Spring Boot Test, Test Containers, API Testing, Repository Testing, Service Layer Testing, Controller Testing

### Database Concepts
Database Indexing, Query Optimization, Pagination Strategies, Database Connection Pooling, Transaction Management, ACID Properties, Database Design Patterns

### Microservices Architecture
Microservices Principles, Service Layer Pattern, Repository Pattern, Layered Architecture, Separation of Concerns, Service Communication, API Gateway Concepts

### Code Quality & Best Practices
Code Comments and Documentation, JavaDoc, Clean Code Principles, Code Review Practices, Refactoring Techniques, Code Maintainability, SOLID Principles Application

### Development Tools
IDE Usage (IntelliJ IDEA/Eclipse), Debugging Techniques, Version Control (Git), Code Formatting, Static Code Analysis, Build Tools Usage

---

## Version Information

- **Application Version**: `0.0.1-SNAPSHOT`
- **Spring Boot Version**: `3.4.12`
- **Java Version**: `21`
- **Documentation Version**: `1.0`

---

*Last Updated: January 2025*

