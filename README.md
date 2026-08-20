# 🛒 ShopSphere — Spring ORM Mastery Project

A practical Spring Boot project designed to learn and implement **Spring ORM (Object Relational Mapping)** using **Hibernate/JPA** with **PostgreSQL**.

This project focuses on understanding how Java objects are mapped to relational database tables and how Spring Boot simplifies database operations using Spring Data JPA.

The project also demonstrates production-oriented backend concepts such as:

* Layered architecture
* Centralized exception handling
* Standardized API responses
* Validation error handling
* Asynchronous processing
* `CompletableFuture`
* `@Async`
* Centralized async exception handling
* Transaction management
* ORM performance optimization
* Concurrency and locking

---

## 🎯 Project Overview

ShopSphere is a multi-vendor e-commerce and order management system designed specifically to make Spring ORM concepts practical.

The project demonstrates the complete flow:

```text
Client
  ↓
REST Controller
  ↓
Service Layer
  ↓
Repository Layer / EntityManager
  ↓
JPA
  ↓
Hibernate
  ↓
JDBC
  ↓
PostgreSQL
```

For error handling:

```text
Controller
  ↓
Service
  ↓
Exception
  ↓
GlobalExceptionHandler
  ↓
Standard ErrorResponse
  ↓
Client
```

For asynchronous operations:

```text
Controller
  ↓
Async Service
  ↓
@Async
  ↓
Task Executor
  ↓
Background Operation
  ↓
CompletableFuture / AsyncExceptionHandler
```

The goal is not to build a simple CRUD application.

The goal is to understand what actually happens between:

```text
Java Object
    ↓
Entity
    ↓
Persistence Context
    ↓
Hibernate
    ↓
SQL
    ↓
JDBC
    ↓
PostgreSQL
```

and also how application failures travel through:

```text
Exception
    ↓
Centralized Handler
    ↓
Standard Error Response
```

---

# 🛠️ Technologies Used

| Technology        | Purpose                      |
| ----------------- | ---------------------------- |
| Java 21           | Programming Language         |
| Spring Boot       | Backend Framework            |
| Spring Web        | REST APIs                    |
| Spring Data JPA   | Repository & ORM abstraction |
| Hibernate         | ORM implementation           |
| PostgreSQL        | Relational Database          |
| Lombok            | Boilerplate reduction        |
| Maven             | Dependency Management        |
| Postman           | API Testing                  |
| CompletableFuture | Asynchronous operations      |
| Spring `@Async`   | Background task execution    |

---

# 📦 Main Dependencies

```xml
<dependencies>

    <!-- Spring Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- PostgreSQL -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

</dependencies>
```

---

# 🗄️ Database Configuration

PostgreSQL is used as the database.

Example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/shopsphere
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

For development:

```properties
spring.jpa.hibernate.ddl-auto=update
```

For production, blindly using `update` is not recommended.

Schema migrations should normally be handled using:

```text
Flyway
or
Liquibase
```

---

# 🏪 ShopSphere Domain

ShopSphere is a simplified **multi-vendor e-commerce and order management system**.

```text
User & Roles
    ↓
Vendors
    ↓
Categories → Products → Variants → Inventory
    ↓
Cart → Cart Items
    ↓
Orders → Order Items
    ├── Payment
    ├── Shipment
    └── Order Status History
    ↓
Reviews / Wishlist / Coupons
```

## Main Entities

```text
User
Address
UserProfile
Role

Vendor
VendorAddress

Category
Product
ProductVariant
ProductImage

Inventory
StockTransaction

Cart
CartItem

Order
OrderItem
Payment
Shipment
OrderStatusHistory

ProductReview
Wishlist
WishlistItem

Coupon
CouponUsage
```

---

# 🏗️ Project Architecture

The project follows a layered architecture:

```text
src/main/java/com/shopsphere
│
├── controller
│
├── service
│
├── repository
│
├── entity
│
├── dto
│
├── mapper
│
├── specification
│
├── exception
│   ├── GlobalExceptionHandler
│   ├── AsyncExceptionHandler
│   ├── BusinessException
│   ├── ResourceNotFoundException
│   ├── InvalidRequestException
│   ├── ProductOutOfStockException
│   ├── OrderStateException
│   └── ErrorCode
│
├── response
│   ├── ApiResponse
│   └── ErrorResponse
│
└── config
    └── AsyncConfig
```

The complete request flow is:

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository / EntityManager
  ↓
JPA
  ↓
Hibernate
  ↓
JDBC
  ↓
PostgreSQL
```

Error flow:

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Exception
  ↓
GlobalExceptionHandler
  ↓
ErrorResponse
  ↓
Client
```

Async flow:

```text
Client
  ↓
Controller
  ↓
Service
  ↓
@Async
  ↓
ThreadPoolTaskExecutor
  ↓
Background Task
  ↓
CompletableFuture
```

---

# 🚨 Centralized Exception Handling

ShopSphere does **not** use repetitive `try-catch` blocks inside every controller.

### ❌ Avoid

```java
try {

    productService.createProduct(request);

} catch (ResourceNotFoundException e) {

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(e.getMessage());

} catch (Exception e) {

    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Something went wrong");
}
```

This creates duplicated error-handling logic across the application.

Instead, ShopSphere uses:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
}
```

All controller-level exceptions are handled centrally.

---

# 🧩 Exception Package

```text
exception
│
├── BusinessException.java
├── ResourceNotFoundException.java
├── InvalidRequestException.java
├── ProductOutOfStockException.java
├── OrderStateException.java
├── ErrorCode.java
├── GlobalExceptionHandler.java
└── AsyncExceptionHandler.java
```

---

## `ErrorCode`

Provides centralized machine-readable error codes.

Example:

```java
public enum ErrorCode {

    VALIDATION_ERROR,
    INVALID_REQUEST,
    INTERNAL_SERVER_ERROR,

    UNAUTHORIZED,
    FORBIDDEN,
    INVALID_CREDENTIALS,

    USER_NOT_FOUND,

    PRODUCT_NOT_FOUND,
    PRODUCT_OUT_OF_STOCK,

    ORDER_NOT_FOUND,
    ORDER_STATE_ERROR
}
```

Clients should rely on:

```text
errorCode
```

rather than parsing:

```text
message
```

because messages may change while error codes should remain stable.

---

# `BusinessException`

Base class for expected business failures.

```java
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus status;

    public BusinessException(
            ErrorCode errorCode,
            HttpStatus status,
            String message
    ) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
```

Business exceptions include:

```text
ResourceNotFoundException
InvalidRequestException
ProductOutOfStockException
OrderStateException
```

---

# `ResourceNotFoundException`

Used when a requested resource does not exist.

Example:

```java
throw new ResourceNotFoundException(
        ErrorCode.PRODUCT_NOT_FOUND,
        "Product with id " + productId + " not found"
);
```

Typical HTTP response:

```text
404 NOT FOUND
```

---

# `InvalidRequestException`

Used when a request is valid at the HTTP level but invalid according to business rules.

Example:

```java
if (quantity <= 0) {

    throw new InvalidRequestException(
            ErrorCode.INVALID_REQUEST,
            "Quantity must be greater than zero"
    );
}
```

Typical response:

```text
400 BAD REQUEST
```

---

# `ProductOutOfStockException`

Used for inventory-related business conflicts.

```java
if (product.getStock() < quantity) {

    throw new ProductOutOfStockException(
            "Only " + product.getStock()
                    + " units are available"
    );
}
```

Typical response:

```text
409 CONFLICT
```

---

# `OrderStateException`

Used when an order cannot transition to another state.

Example:

```text
DELIVERED → CANCELLED
CANCELLED → SHIPPED
```

Example:

```java
throw new OrderStateException(
        "Delivered order cannot be cancelled"
);
```

Typical response:

```text
409 CONFLICT
```

---

# 🌐 GlobalExceptionHandler

The centralized handler is responsible for converting exceptions into API responses.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException exception,
            HttpServletRequest request
    ) {

        return buildResponse(
                exception.getStatus(),
                exception.getErrorCode(),
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {

        String message = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField()
                                + ": "
                                + error.getDefaultMessage()
                )
                .collect(Collectors.joining(", "));

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ErrorCode.VALIDATION_ERROR,
                message,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request
        );
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            ErrorCode errorCode,
            String message,
            HttpServletRequest request
    ) {

        ErrorResponse response = new ErrorResponse(
                false,
                status.value(),
                errorCode,
                message,
                request.getRequestURI(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }
}
```

The important annotation is:

```java
@RestControllerAdvice
```

It allows exception handling to be applied across REST controllers globally.

---

# 📤 Standard Error Response

Every API error follows the same structure.

```java
public class ErrorResponse {

    private boolean success;
    private int status;
    private ErrorCode errorCode;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private String traceId;
}
```

Example:

```json
{
    "success": false,
    "status": 404,
    "errorCode": "PRODUCT_NOT_FOUND",
    "message": "Product with id 100 not found",
    "path": "/api/products/100",
    "timestamp": "2026-08-21T02:30:00",
    "traceId": null
}
```

The `traceId` field is intentionally included for future distributed tracing/log correlation.

---

# 📥 Standard Success Response

ShopSphere can use:

```java
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
}
```

Example:

```json
{
    "success": true,
    "message": "Product created successfully",
    "data": {
        "id": 10,
        "name": "iPhone"
    }
}
```

This gives the API a predictable contract:

```text
Success → ApiResponse
Failure → ErrorResponse
```

---

# ❌ Controller Without Centralized Handling

Avoid:

```java
@PostMapping
public ResponseEntity<?> createProduct(...) {

    try {

        // business logic

    } catch (Exception e) {

        // duplicated handling

    }
}
```

---

# ✅ Controller With Centralized Handling

```java
@PostMapping
public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
        @RequestBody CreateProductRequest request
) {

    ProductResponse response =
            productService.createProduct(request);

    return ResponseEntity.ok(
            ApiResponse.success(
                    "Product created successfully",
                    response
            )
    );
}
```

If the service throws:

```java
ResourceNotFoundException
```

the exception automatically travels to:

```text
GlobalExceptionHandler
```

---

# ⚡ Centralized Async Processing

ShopSphere also demonstrates asynchronous operations.

Java does not have JavaScript-style:

```text
async / await
```

Instead, asynchronous execution is implemented using mechanisms such as:

```text
@Async
CompletableFuture
Executor
ThreadPoolTaskExecutor
```

---

# `AsyncConfig`

ShopSphere uses a dedicated thread pool.

```java
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Bean(name = "shopsphereTaskExecutor")
    public Executor taskExecutor() {

        ThreadPoolTaskExecutor executor =
                new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);

        executor.setThreadNamePrefix(
                "shopsphere-async-"
        );

        executor.initialize();

        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return taskExecutor();
    }

    @Override
    public AsyncExceptionHandler
    getAsyncUncaughtExceptionHandler() {

        return new AsyncExceptionHandler();
    }
}
```

---

# 🧵 Why Use a Dedicated Thread Pool?

Do not blindly create threads manually:

```java
new Thread(() -> {
    // task
}).start();
```

This is difficult to manage and scale.

Instead:

```text
Application
    ↓
ThreadPoolTaskExecutor
    ↓
Managed Worker Threads
    ↓
Async Tasks
```

The executor controls:

```text
Core Threads
Maximum Threads
Queue Capacity
Thread Naming
Task Scheduling
```

---

# `@Async`

Example:

```java
@Async("shopsphereTaskExecutor")
public void sendOrderConfirmation(Long orderId) {

    // send email
    // generate invoice
    // send notification
}
```

The caller does not need to wait for the operation to complete.

---

# `CompletableFuture`

For asynchronous operations that return a result:

```java
@Async("shopsphereTaskExecutor")
public CompletableFuture<ProductResponse> getProductAsync(
        Long productId
) {

    Product product = productRepository
            .findById(productId)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            ErrorCode.PRODUCT_NOT_FOUND,
                            "Product not found"
                    )
            );

    ProductResponse response =
            productMapper.toResponse(product);

    return CompletableFuture.completedFuture(response);
}
```

The exception is stored inside the `CompletableFuture`.

---

# ⚠️ Important `@Async` Rule

Avoid unnecessarily nesting executors.

Do not blindly do:

```java
@Async
public CompletableFuture<ProductResponse> getProduct() {

    return CompletableFuture.supplyAsync(() -> {

        // operation

    });
}
```

This can introduce another executor layer.

Prefer:

```java
@Async("shopsphereTaskExecutor")
public CompletableFuture<ProductResponse> getProduct() {

    ProductResponse response = // operation

    return CompletableFuture.completedFuture(response);
}
```

`@Async` already moves the method execution to the configured executor.

---

# 🚨 AsyncExceptionHandler

For `void @Async` methods, exceptions cannot be returned through a `CompletableFuture`.

Spring therefore provides:

```java
AsyncUncaughtExceptionHandler
```

ShopSphere implements:

```java
public class AsyncExceptionHandler
        implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(
            Throwable throwable,
            Method method,
            Object... params
    ) {

        log.error(
                "Async operation failed. Method: {}, Message: {}",
                method.getName(),
                throwable.getMessage(),
                throwable
        );
    }
}
```

This ensures that failures from background tasks are not silently ignored.

---

# 🔄 Error Handling Architecture

The complete synchronous flow:

```text
                    HTTP Request
                         │
                         ▼
                    Controller
                         │
                         ▼
                      Service
                         │
              ┌──────────┴──────────┐
              │                     │
           Success               Exception
              │                     │
              ▼                     ▼
        ApiResponse        GlobalExceptionHandler
                                    │
                                    ▼
                              ErrorResponse
                                    │
                                    ▼
                                 Client
```

Async flow:

```text
                    HTTP Request
                         │
                         ▼
                    Controller
                         │
                         ▼
                   Async Service
                         │
                       @Async
                         │
                         ▼
                ThreadPoolTaskExecutor
                         │
              ┌──────────┴──────────┐
              │                     │
           Success               Exception
              │                     │
              ▼                     ▼
      CompletableFuture      AsyncExceptionHandler
                                    │
                                    ▼
                                  Logs
```

---

# 🧠 Exception Categories

ShopSphere separates errors into meaningful categories.

| Exception                    | HTTP Status | Use Case                    |
| ---------------------------- | ----------: | --------------------------- |
| `ResourceNotFoundException`  |         404 | Resource doesn't exist      |
| `InvalidRequestException`    |         400 | Invalid business request    |
| `ProductOutOfStockException` |         409 | Insufficient inventory      |
| `OrderStateException`        |         409 | Invalid order transition    |
| Validation Exception         |         400 | DTO validation failure      |
| Unexpected `Exception`       |         500 | Unknown application failure |

The goal is **not** to create an exception class for every possible failure.

Create a custom exception when it represents a meaningful business or API error category.

---

# 📝 Error Documentation

ShopSphere maintains a centralized error catalog.

| Error Code              | HTTP | Meaning                        |
| ----------------------- | ---: | ------------------------------ |
| `VALIDATION_ERROR`      |  400 | Request validation failed      |
| `INVALID_REQUEST`       |  400 | Invalid business request       |
| `UNAUTHORIZED`          |  401 | Authentication failed          |
| `FORBIDDEN`             |  403 | User lacks permission          |
| `USER_NOT_FOUND`        |  404 | User doesn't exist             |
| `PRODUCT_NOT_FOUND`     |  404 | Product doesn't exist          |
| `ORDER_NOT_FOUND`       |  404 | Order doesn't exist            |
| `PRODUCT_OUT_OF_STOCK`  |  409 | Product cannot be ordered      |
| `ORDER_STATE_ERROR`     |  409 | Invalid order state            |
| `INTERNAL_SERVER_ERROR` |  500 | Unexpected application failure |

This catalog should eventually be synchronized with:

```text
Swagger / OpenAPI
```

so API consumers can understand the possible error responses.

---

# 🧩 ORM Concepts Covered

This project covers:

```text
@Entity
@Table
@Id
@GeneratedValue
@SequenceGenerator
@Column

@OneToOne
@OneToMany
@ManyToOne
@ManyToMany

@JoinColumn
@JoinTable
mappedBy

CascadeType
orphanRemoval

FetchType
LAZY
EAGER

EntityManager
Persistence Context
Entity Lifecycle
Dirty Checking

@Transactional

JPQL
Native Query
Derived Query

Pagination
Sorting

N+1 Problem
JOIN FETCH
EntityGraph

DTO Projection
Specifications

Optimistic Locking
Pessimistic Locking
```

---

# 🛡️ Error Handling Concepts Covered

The project additionally covers:

```text
BusinessException
ResourceNotFoundException
InvalidRequestException
ProductOutOfStockException
OrderStateException

@RestControllerAdvice
@ExceptionHandler

ErrorCode
ErrorResponse
ApiResponse

Validation Error Handling
Unexpected Exception Handling

@Async
CompletableFuture
AsyncUncaughtExceptionHandler

ThreadPoolTaskExecutor
Centralized Async Error Handling
```

---

# 🔄 Transactions

Database operations should execute inside appropriate transaction boundaries.

Example:

```java
@Transactional
public void createOrder(CreateOrderRequest request) {

    // validate order

    // reserve inventory

    // create order

    // create order items

    // save payment

}
```

If an appropriate runtime exception occurs:

```text
Exception
    ↓
Transaction Rollback
```

This becomes especially important when working with:

```text
Order
OrderItem
Inventory
Payment
Shipment
```

---

# ⚡ Dirty Checking

Hibernate automatically detects changes to managed entities.

```java
@Transactional
public void updateUser(Long id) {

    User user = userRepository
            .findById(id)
            .orElseThrow();

    user.setName("Rajat");
}
```

Hibernate can detect the modification and generate:

```sql
UPDATE users
SET name = ?
WHERE id = ?
```

An explicit `save()` is not necessarily required for the update when the entity is managed inside the transaction.

---

# ⚠️ N+1 Query Problem

Example:

```text
SELECT * FROM users;

SELECT * FROM orders WHERE user_id = 1;
SELECT * FROM orders WHERE user_id = 2;
SELECT * FROM orders WHERE user_id = 3;
...
```

Possible solutions:

```text
JOIN FETCH
EntityGraph
Batch Fetching
DTO Projections
Proper Fetch Strategy
```

---

# 🔐 Concurrency Scenario

Inventory is deliberately included to demonstrate real ORM concurrency problems.

Example:

```text
Stock = 1

Customer A ──┐
             ├── Purchase
Customer B ──┘
```

Without concurrency control, both requests could potentially read the same stock.

ShopSphere demonstrates:

```text
Optimistic Locking
        +
Pessimistic Locking
        +
@Version
        +
Transaction Isolation
        +
Atomic Database Updates
```

---

# 🧪 Deliberate ORM Experiments

## Experiment 1 — First-Level Cache

Load the same entity twice inside one persistence context.

Inspect generated SQL.

---

## Experiment 2 — Dirty Checking

Load an entity, modify it, don't explicitly call `save()`, and commit the transaction.

Observe the generated `UPDATE`.

---

## Experiment 3 — Detached Entity

Detach an entity and modify it.

Then compare the behavior with:

```java
entityManager.merge(entity);
```

---

## Experiment 4 — Lazy Loading

Access a lazy relationship:

```text
Inside Transaction
vs
Outside Transaction
```

Observe:

```text
LazyInitializationException
```

---

## Experiment 5 — N+1

Load orders and access their items.

Count generated queries.

Then solve the problem using:

```text
JOIN FETCH
EntityGraph
Batch Fetching
DTO Projection
```

---

## Experiment 6 — Cascade

Create an Order with OrderItems.

Compare different cascade configurations.

---

## Experiment 7 — orphanRemoval

Remove an OrderItem from an Order collection and inspect the generated SQL.

---

## Experiment 8 — Optimistic Locking

Run concurrent inventory updates using:

```java
@Version
private Long version;
```

Observe the version conflict.

---

## Experiment 9 — Pessimistic Locking

Reserve inventory concurrently using a database lock.

Observe transaction blocking.

---

## Experiment 10 — Centralized Exception Handling

Intentionally trigger:

```text
PRODUCT_NOT_FOUND
PRODUCT_OUT_OF_STOCK
INVALID_REQUEST
VALIDATION_ERROR
```

Verify that all errors return the same response structure.

---

## Experiment 11 — Async Exception Handling

Create:

```java
@Async
public void sendNotification() {
    throw new RuntimeException("Notification failed");
}
```

Verify that the exception reaches:

```text
AsyncExceptionHandler
```

instead of being silently ignored.

---

## Experiment 12 — CompletableFuture Failure

Create an asynchronous method returning:

```java
CompletableFuture<T>
```

intentionally throw an exception and observe how the exception is stored inside the future.

---

# 🧪 Testing Strategy

ORM behavior should be tested against a real PostgreSQL database.

```text
Unit Tests
    ↓
Repository Tests
    ↓
Service Integration Tests
    ↓
PostgreSQL Integration Tests
    ↓
Testcontainers
```

Important tests:

```text
Entity persistence
Relationships
Cascade
orphanRemoval
Rollback
Dirty checking
Lazy loading
N+1 behavior
Optimistic locking
Pessimistic locking
Pagination
Dynamic queries

Exception handling
Validation errors
Business exceptions
Unexpected exceptions

Async execution
Async exceptions
CompletableFuture failures
```

---

# 📚 Recommended Learning Order

```text
1. JDBC Basics
      ↓
2. ORM Fundamentals
      ↓
3. JPA Specification
      ↓
4. Hibernate Architecture
      ↓
5. Entity Mapping
      ↓
6. Primary Keys & PostgreSQL Sequences
      ↓
7. Relationships
      ↓
8. Owning Side & mappedBy
      ↓
9. Cascade & orphanRemoval
      ↓
10. Persistence Context
      ↓
11. Entity Lifecycle
      ↓
12. EntityManager
      ↓
13. Dirty Checking
      ↓
14. Flush
      ↓
15. Transactions
      ↓
16. Spring Data JPA
      ↓
17. Derived Queries
      ↓
18. JPQL
      ↓
19. Native Queries
      ↓
20. LAZY / EAGER
      ↓
21. N+1 Problem
      ↓
22. JOIN FETCH
      ↓
23. EntityGraph
      ↓
24. DTO Projections
      ↓
25. Pagination & Sorting
      ↓
26. Specifications / Criteria API
      ↓
27. Optimistic Locking
      ↓
28. Pessimistic Locking
      ↓
29. Transaction Isolation
      ↓
30. Centralized Exception Handling
      ↓
31. Async Processing
      ↓
32. CompletableFuture
      ↓
33. Async Exception Handling
      ↓
34. Auditing
      ↓
35. Database Indexing
      ↓
36. ORM Performance Optimization
      ↓
37. Integration Testing
      ↓
38. Production ORM Practices
```

---

# 🗺️ Development Phases

## Phase 1 — Foundation

Build:

```text
User
Vendor
Category
Product
```

Learn:

* Entity mapping
* IDs
* Columns
* Sequences
* Repositories
* Basic queries

---

## Phase 2 — Relationships

Add:

```text
ProductVariant
ProductImage
Address
UserProfile
Role
```

Learn:

* One-to-One
* One-to-Many
* Many-to-One
* Many-to-Many
* mappedBy
* Join columns
* Join tables
* Cascade
* orphanRemoval

---

## Phase 3 — Persistence Context

Focus on:

```text
EntityManager
Persistence Context
Entity Lifecycle
Managed vs Detached
First-Level Cache
Dirty Checking
Flush
```

---

## Phase 4 — Cart and Orders

Build:

```text
Cart
CartItem
Order
OrderItem
Payment
Shipment
```

Focus on:

```text
@Transactional
Rollback
Cascade
Entity State
Transaction Boundaries
```

---

## Phase 5 — Querying

Implement:

```text
Derived Queries
JPQL
Native SQL
Pagination
Sorting
DTO Projections
Specifications
```

---

## Phase 6 — Centralized Error Handling

Implement:

```text
BusinessException
ResourceNotFoundException
InvalidRequestException
ProductOutOfStockException
OrderStateException

ErrorCode
ErrorResponse
ApiResponse

@RestControllerAdvice
@ExceptionHandler
```

The objective is:

```text
No repetitive try-catch blocks
inside controllers.
```

---

## Phase 7 — Async Processing

Implement asynchronous ShopSphere operations such as:

```text
Order confirmation email
Invoice generation
Notification delivery
Background report generation
Async inventory-related processing
```

Learn:

```text
@Async
CompletableFuture
ThreadPoolTaskExecutor
AsyncUncaughtExceptionHandler
```

---

## Phase 8 — Performance

Intentionally introduce:

```text
N+1
EAGER loading
Large object graphs
Unbounded queries
```

Then fix them using:

```text
JOIN FETCH
EntityGraph
DTO Projections
Batch Fetching
Pagination
Specifications
Indexes
```

---

## Phase 9 — Concurrency

Use inventory to demonstrate:

```text
Optimistic Locking
Pessimistic Locking
Transaction Isolation
Concurrent Updates
Lost Updates
```

---

## Phase 10 — Production ORM Practices

Add:

```text
Flyway
Database Indexes
Integration Tests
Testcontainers
Auditing
Query Optimization
SQL Logging
Trace IDs
OpenAPI Error Documentation
```

---

# 🏆 ORM Mastery Checklist

By the end of the project:

```text
□ ORM
□ JPA vs Hibernate vs Spring Data JPA
□ EntityManager
□ EntityManagerFactory
□ Persistence Context
□ First-Level Cache
□ Entity Lifecycle
□ Transient / Managed / Detached / Removed
□ persist()
□ find()
□ merge()
□ remove()
□ detach()
□ clear()
□ refresh()
□ flush()
□ Dirty Checking
□ @Transactional
□ Transaction Propagation
□ Transaction Isolation

□ Entity Mapping
□ Primary Key Generation
□ PostgreSQL Sequences
□ @OneToOne
□ @OneToMany
□ @ManyToOne
□ @ManyToMany
□ @JoinColumn
□ @JoinTable
□ mappedBy
□ Owning Side
□ Cascade
□ orphanRemoval
□ LAZY
□ EAGER
□ LazyInitializationException

□ Derived Queries
□ JPQL
□ Native SQL
□ JOIN FETCH
□ EntityGraph
□ N+1 Problem
□ Pagination
□ Sorting
□ DTO Projections
□ Specifications
□ Criteria API

□ Optimistic Locking
□ Pessimistic Locking
□ @Version
□ Auditing
□ Database Indexing
□ Hibernate SQL Generation
□ Query Optimization

□ BusinessException
□ GlobalExceptionHandler
□ @RestControllerAdvice
□ @ExceptionHandler
□ ErrorCode
□ ErrorResponse
□ Standard API Response
□ Validation Error Handling

□ @Async
□ CompletableFuture
□ ThreadPoolTaskExecutor
□ AsyncUncaughtExceptionHandler
□ Async Exception Handling

□ Integration Testing
□ Testcontainers
□ Production ORM Practices
```

---

# 🎯 Final Learning Objective

At the end of ShopSphere, you should be able to explain this complete lifecycle:

```text
HTTP Request
     ↓
Controller
     ↓
Service
     ↓
@Transactional
     ↓
EntityManager / Repository
     ↓
Persistence Context
     ↓
Managed Entity
     ↓
Dirty Checking
     ↓
Flush
     ↓
Hibernate SQL Generation
     ↓
JDBC
     ↓
PostgreSQL
     ↓
Transaction Commit
     ↓
Database State
```

And when something fails:

```text
Business Operation
       ↓
Exception
       ↓
BusinessException
       ↓
GlobalExceptionHandler
       ↓
ErrorCode
       ↓
ErrorResponse
       ↓
HTTP Response
```

For asynchronous work:

```text
Service
   ↓
@Async
   ↓
ThreadPoolTaskExecutor
   ↓
Background Operation
   ↓
     ┌─────────────────────┐
     │                     │
  Success               Failure
     │                     │
     ▼                     ▼
CompletableFuture   AsyncExceptionHandler
                         │
                         ▼
                       Logs
```

The real goal is to answer questions such as:

> Why did Hibernate execute this SQL?

> Why did it execute the SQL at this point?

> Why did Hibernate issue one query instead of ten?

> Why is this entity managed or detached?

> Why did dirty checking update the database without calling `save()`?

> Why did lazy loading fail?

> Why did the transaction roll back?

> Why did two concurrent inventory updates conflict?

> Why was this exception converted into HTTP 404 instead of HTTP 500?

> How does an exception travel from the service layer to the API response?

> What happens when an `@Async` operation fails?

> What is the difference between an exception in `CompletableFuture` and an exception from a `void @Async` method?

If you can answer these questions from the application's behavior and generated SQL/logs, you have moved beyond simply knowing JPA annotations and have started understanding **Spring ORM at an implementation and production level**.

---

# 🚧 Future Improvements

The project can be extended with:

* Flyway database migrations
* Redis caching
* Spring Security
* JWT authentication
* Optimistic locking
* Pessimistic locking
* Auditing
* Specifications
* Criteria API
* EntityGraph
* QueryDSL
* Hibernate Envers
* Testcontainers
* Integration testing
* Database indexing
* Query optimization
* Dockerized PostgreSQL
* OpenAPI / Swagger documentation
* Correlation IDs / distributed tracing
* Structured logging
* Centralized monitoring

---

# 👨‍💻 Author

**Rajat Das**

Spring Boot / Flutter Developer

---

## ⭐ Key Takeaway

Spring ORM is not just about writing:

```java
@Entity
```

and:

```java
JpaRepository
```

The real objective is understanding the complete ORM lifecycle:

```text
Java Object
     ↓
JPA Entity
     ↓
Persistence Context
     ↓
Hibernate
     ↓
SQL Generation
     ↓
JDBC
     ↓
PostgreSQL
     ↓
Database Result
     ↓
Hibernate
     ↓
Java Object
```

At the same time, a production backend must have a predictable failure mechanism:

```text
Exception
     ↓
Centralized Handler
     ↓
Standard Error Code
     ↓
Standard Error Response
     ↓
Client
```

and controlled asynchronous execution:

```text
@Async
   ↓
Managed Thread Pool
   ↓
Background Operation
   ↓
CompletableFuture / Async Exception Handler
```

**ShopSphere therefore serves two purposes:**

```text
Spring ORM Mastery
        +
Production Backend Architecture
```

The objective is not merely to make the application work. The objective is to understand **why it works, what Hibernate is doing underneath, how transactions affect it, how concurrency affects it, and how failures should be handled centrally without polluting business logic with repetitive error-handling code.**
