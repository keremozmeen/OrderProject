# Module Explanation - What Each Module Does

## Overview

This is a **multi-module Maven project** where each module is a separate service that can run independently. Here's what each module does:

---

## 1. order-common (Shared Library)

**What it does**: Contains code shared by all other services

**Why it exists**: 
- Avoids code duplication
- Ensures all services use the same DTOs, entities, and constants
- Single source of truth for shared business logic

**Contains**:
- **Constants**: `EOrderStatus`, `EReturnStatus`, `ERole`, `EStockStatus` (enums)
- **Entities**: `UserEntity`, `ProductEntity`, `OrderEntity`, etc. (database models)
- **DTOs**: All request/response objects (`OrderRequestDTO`, `UserResponseDTO`, etc.)
- **Exceptions**: Custom exceptions (`UserNotFoundException`, `OrderNotFoundException`, etc.)
- **Mappers**: MapStruct mappers to convert between entities and DTOs
- **Utilities**: Common utility classes

**Does NOT run as a service** - it's just a library (JAR) that other services depend on

---

## 2. order-discovery-server (Eureka Server)

**What it does**: Service registry - a "phone book" for microservices

**Why it exists**:
- When you have multiple services, they need to find each other
- Instead of hardcoding IP addresses, services register with Eureka
- Services can discover other services by name (e.g., "user-service")

**How it works**:
1. All services register themselves with Eureka when they start
2. Services can query Eureka to find other services
3. Eureka tracks which services are healthy and available

**Port**: 8761

**Access**: http://localhost:8761 (shows dashboard of all registered services)

**Example**: 
- User Service needs to call Product Service
- Instead of `http://localhost:8082/products`, it asks Eureka: "Where is product-service?"
- Eureka responds: "product-service is at http://localhost:8082"
- User Service then calls Product Service

---

## 3. order-config-server (Configuration Server)

**What it does**: Centralized configuration management

**Why it exists**:
- Instead of each service having its own `application.properties`
- All configuration is stored in one place (Git repository or file system)
- Easy to update configuration without redeploying services
- Environment-specific configs (dev, test, prod)

**How it works**:
1. Config Server stores configuration files (e.g., `user-service-dev.properties`)
2. Services connect to Config Server on startup
3. Services fetch their configuration from Config Server
4. Configuration can be updated without restarting services

**Port**: 8888

**Example**:
- Instead of each service having `spring.datasource.url=...` in their own properties
- Config Server has: `user-service.properties`, `product-service.properties`, etc.
- Services fetch their config from Config Server

**Note**: This is optional - you can still use local `application.properties` files

---

## 4. order-user-service (User Management Service)

**What it does**: Handles all user-related operations

**Responsibilities**:
- User registration (sign up)
- User authentication (login) - generates JWT tokens
- User profile management (update, delete)
- Role management (assign roles to users)

**Port**: 8081

**Endpoints**:
- `POST /api/v1/noauth/register` - Register new user
- `POST /api/v1/noauth/login` - Login (get JWT token)
- `GET /api/v1/admin/users` - Get all users (Admin only)
- `PUT /api/v1/admin/users/{id}` - Update user (Admin only)
- `DELETE /api/v1/admin/users/{id}` - Delete user (Admin only)

**Database**: Has its own database (or schema) for users and roles

---

## 5. order-product-service (Product Catalog Service)

**What it does**: Manages product catalog

**Responsibilities**:
- Product CRUD operations (Create, Read, Update, Delete)
- Category management
- Product search and filtering

**Port**: 8082

**Endpoints**:
- `GET /api/v1/admin/products` - Get all products
- `GET /api/v1/admin/products/{id}` - Get product by ID
- `POST /api/v1/admin/products` - Create product (Admin only)
- `PUT /api/v1/admin/products/{id}` - Update product (Admin only)
- `DELETE /api/v1/admin/products/{id}` - Delete product (Admin only)

**Database**: Has its own database for products and categories

---

## 6. order-order-service (Order Processing Service)

**What it does**: Handles order creation and management

**Responsibilities**:
- Create orders (when customer places order)
- Manage order status (Created → Confirmed → Shipped → Delivered)
- Order validation
- Order history

**Port**: 8083

**Endpoints**:
- `POST /api/v1/customer/orders` - Create order (User only)
- `GET /api/v1/customer/orders` - Get customer's orders (User only)
- `GET /api/v1/admin/orders` - Get all orders (Admin only)
- `PUT /api/v1/admin/orders/{id}/status` - Update order status (Admin only)

**Database**: Has its own database for orders and order items

**Note**: This service might need to call:
- User Service (to validate customer exists)
- Product Service (to check product availability)

---

## 7. order-return-service (Return Management Service)

**What it does**: Handles return requests

**Responsibilities**:
- Create return requests
- Manage return status (Pending → Approved/Rejected)
- Return validation

**Port**: 8084

**Endpoints**:
- `POST /api/v1/customer/returns` - Create return request (User only)
- `GET /api/v1/customer/returns` - Get customer's returns (User only)
- `GET /api/v1/admin/returns` - Get all returns (Admin only)
- `PUT /api/v1/admin/returns/{id}/status` - Update return status (Admin only)

**Database**: Has its own database for returns

**Note**: This service might need to call:
- Order Service (to validate order exists)
- Stock Service (to restore stock when return is approved)

---

## 8. order-stock-service (Inventory Service)

**What it does**: Manages inventory/stock levels

**Responsibilities**:
- Stock level management
- Stock updates
- Inventory tracking

**Port**: 8085

**Endpoints**:
- `GET /api/v1/admin/stock/{productId}` - Get stock by product ID
- `PUT /api/v1/admin/stock` - Update stock

**Database**: Has its own database for stock/inventory

**Note**: This service might be called by:
- Order Service (to check/update stock when order is placed)
- Return Service (to restore stock when return is approved)

---

## How They Work Together

### Example: Customer Places an Order

1. **Customer** calls `order-order-service`: `POST /api/v1/customer/orders`
2. **Order Service** needs to:
   - Validate customer exists → calls `order-user-service`
   - Check product availability → calls `order-product-service` or `order-stock-service`
   - Create order → saves to its own database
   - Update stock → calls `order-stock-service`

### Service Discovery Flow

1. All services start and register with **Eureka** (discovery-server)
2. When Order Service needs User Service:
   - Asks Eureka: "Where is user-service?"
   - Eureka responds: "user-service is at http://localhost:8081"
   - Order Service calls User Service

### Configuration Flow

1. Services start and connect to **Config Server**
2. Config Server provides configuration (database URLs, JWT secrets, etc.)
3. Services use this configuration

---

## Summary Table

| Module | Type | Port | Purpose |
|--------|------|------|---------|
| order-common | Library | N/A | Shared code |
| order-discovery-server | Service | 8761 | Service registry |
| order-config-server | Service | 8888 | Configuration management |
| order-user-service | Service | 8081 | User management & auth |
| order-product-service | Service | 8082 | Product catalog |
| order-order-service | Service | 8083 | Order processing |
| order-return-service | Service | 8084 | Return management |
| order-stock-service | Service | 8085 | Inventory management |

---

## Why This Structure?

1. **Separation of Concerns**: Each service has one responsibility
2. **Independent Deployment**: Deploy services independently
3. **Scalability**: Scale services independently (e.g., more order-service instances)
4. **Technology Flexibility**: Use different databases/technologies per service
5. **Fault Isolation**: If one service fails, others continue working
6. **Team Autonomy**: Different teams can work on different services

---

## Current State vs. Future State

**Current (Multi-Module Monolithic)**:
- All services in one project
- Can run all services together
- Shared code in `order-common`
- Easy to develop and test

**Future (True Microservices)**:
- Each service is a separate project/repository
- Services communicate via REST APIs
- Each service has its own database
- Services can be deployed independently
