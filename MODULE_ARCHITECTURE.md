# Modular Monolithic Architecture Documentation

## Overview

This Order System is structured as a **Modular Monolithic** application, designed to be easily migrated to a microservices architecture when needed. The application is organized into distinct modules, each representing a potential microservice boundary.

## Architecture Principles

1. **Modular Structure**: Clear separation of concerns with module boundaries
2. **API Versioning**: RESTful APIs with versioning (`/api/v1/`)
3. **Independent Modules**: Each module can operate independently
4. **Microservices Ready**: Prepared for Spring Cloud Eureka and Config Server
5. **Database Per Module**: Currently shared database, but structured for future separation

## Module Structure

### 1. User Module (`com.company.ordersystem.module.user`)

**Responsibilities:**
- User registration and authentication
- User profile management
- Role-based access control (RBAC)
- JWT token generation and validation

**API Endpoints:**
- `POST /api/v1/noauth/register` - User registration
- `POST /api/v1/noauth/login` - User authentication
- `GET /api/v1/users` - Get all users (Admin only)
- `GET /api/v1/users/{id}` - Get user by ID (Admin only)
- `PUT /api/v1/users/{id}` - Update user (Admin only)
- `DELETE /api/v1/users/{id}` - Delete user (Admin only)

**Entities:**
- `UserEntity`
- `RoleEntity`

**Future Microservice:** `user-service`

---

### 2. Product Module (`com.company.ordersystem.module.product`)

**Responsibilities:**
- Product catalog management
- Product CRUD operations
- Category management
- Product search and filtering

**API Endpoints:**
- `GET /api/v1/admin/products` - Get all products
- `GET /api/v1/admin/products/{id}` - Get product by ID
- `POST /api/v1/admin/products` - Create product (Admin only)
- `PUT /api/v1/admin/products/{id}` - Update product (Admin only)
- `DELETE /api/v1/admin/products/{id}` - Delete product (Admin only)

**Entities:**
- `ProductEntity`
- `CategoryEntity`

**Future Microservice:** `product-catalog-service`

---

### 3. Order Module (`com.company.ordersystem.module.order`)

**Responsibilities:**
- Order creation and management
- Order status transitions
- Order history and tracking
- Order validation and business rules

**API Endpoints:**
- `POST /api/v1/customer/orders` - Create order (User only)
- `GET /api/v1/customer/orders` - Get customer orders (User only)
- `GET /api/v1/admin/orders` - Get all orders (Admin only)
- `PUT /api/v1/admin/orders/{id}/status` - Update order status (Admin only)
- `DELETE /api/v1/admin/orders/{id}` - Delete order (Admin only)

**Entities:**
- `OrderEntity`
- `OrderItemEntity`

**Future Microservice:** `order-service`

---

### 4. Return Module (`com.company.ordersystem.module.return_`)

**Responsibilities:**
- Return request creation
- Return status management
- Return validation
- Stock restoration on returns

**API Endpoints:**
- `POST /api/v1/customer/returns` - Create return request (User only)
- `GET /api/v1/customer/returns` - Get customer returns (User only)
- `GET /api/v1/admin/returns` - Get all returns (Admin only)
- `PUT /api/v1/admin/returns/{id}/status` - Update return status (Admin only)
- `DELETE /api/v1/admin/returns/{id}` - Delete return (Admin only)

**Entities:**
- `ReturnEntity`

**Future Microservice:** `return-service`

---

### 5. Stock Module (`com.company.ordersystem.module.stock`)

**Responsibilities:**
- Stock level management
- Stock updates and tracking
- Inventory operations
- Stock status monitoring

**API Endpoints:**
- `GET /api/v1/admin/stock/{productId}` - Get stock by product ID (Admin only)
- `PUT /api/v1/admin/stock` - Update stock (Admin only)

**Entities:**
- Uses `ProductEntity.stock` field

**Future Microservice:** `inventory-service`

---

## Module Communication

Currently, modules communicate through:
1. **Direct Method Calls**: Within the same application
2. **Shared Database**: All modules use the same database
3. **Spring Security Context**: For authentication and authorization

### Future Microservices Communication

When migrated to microservices, communication will use:
1. **REST APIs**: HTTP/REST for synchronous communication
2. **Service Discovery**: Spring Cloud Eureka
3. **API Gateway**: For routing and load balancing
4. **Message Queue**: RabbitMQ (already configured) for asynchronous communication

## Database Schema

### Current State (Monolithic)
- Single database shared by all modules
- H2 for development, PostgreSQL for production

### Future State (Microservices)
Each microservice will have its own database:
- `user-service` → `user_db`
- `product-catalog-service` → `product_db`
- `order-service` → `order_db`
- `return-service` → `return_db`
- `inventory-service` → `inventory_db`

## Security Architecture

### Authentication
- JWT-based authentication
- Token expiration: 86400 seconds (24 hours)
- Secret key configurable via environment variable

### Authorization
- Role-based access control (RBAC)
- Roles: `ADMIN`, `USER`
- Spring Security filters for endpoint protection

## Configuration Management

### Current Configuration
- `application.properties`: Main configuration file
- `bootstrap.properties`: Spring Cloud Config bootstrap

### Module-Specific Configuration
Each module has a configuration class:
- `UserModuleConfig`
- `ProductModuleConfig`
- `OrderModuleConfig`
- `ReturnModuleConfig`
- `StockModuleConfig`

## Deployment Architecture

### Current Deployment (Monolithic)
- Single JAR file deployment
- Single application instance
- Shared database

### Future Deployment (Microservices)
- Each module as separate service
- Independent scaling
- Service discovery via Eureka
- API Gateway for routing
- Config Server for centralized configuration

## Migration Path to Microservices

1. **Phase 1**: Extract User Module → `user-service`
2. **Phase 2**: Extract Product Module → `product-catalog-service`
3. **Phase 3**: Extract Order Module → `order-service`
4. **Phase 4**: Extract Return Module → `return-service`
5. **Phase 5**: Extract Stock Module → `inventory-service`
6. **Phase 6**: Implement API Gateway
7. **Phase 7**: Set up Service Discovery (Eureka)
8. **Phase 8**: Implement Config Server

## Technology Stack

- **Framework**: Spring Boot 3.5.5
- **Security**: Spring Security + JWT
- **Database**: H2 (Dev), PostgreSQL (Prod)
- **ORM**: Spring Data JPA / Hibernate
- **API Documentation**: Swagger/OpenAPI
- **Monitoring**: Spring Boot Actuator
- **Cloud Ready**: Spring Cloud (Eureka, Config)

## Module Dependencies

```
User Module
  └── (No dependencies on other modules)

Product Module
  └── (No dependencies on other modules)

Order Module
  ├── Product Module (for product validation)
  └── User Module (for customer validation)

Return Module
  ├── Order Module (for order validation)
  └── Stock Module (for stock restoration)

Stock Module
  └── Product Module (for product stock management)
```

## Best Practices

1. **Module Boundaries**: Keep modules independent
2. **API Contracts**: Use DTOs for inter-module communication
3. **Error Handling**: Centralized exception handling
4. **Logging**: Module-specific logging
5. **Testing**: Unit tests per module
6. **Documentation**: API documentation per module

## Future Enhancements

- [ ] Implement API Gateway
- [ ] Add Circuit Breaker (Resilience4j)
- [ ] Implement Distributed Tracing (Zipkin)
- [ ] Add Message Queue for async communication
- [ ] Implement CQRS pattern for order module
- [ ] Add Event Sourcing for audit trail
