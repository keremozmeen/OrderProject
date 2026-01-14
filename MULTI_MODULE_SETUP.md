# Multi-Module Maven Project Setup Guide

## Overview

This project has been restructured into a **multi-module Maven project** with separate modules for each service, following the COMP301 project requirements.

## Project Structure

```
order-system/ (Parent POM)
├── order-common/              # Shared code (DTOs, Entities, Constants, Exceptions)
├── order-discovery-server/    # Eureka Discovery Server
├── order-config-server/       # Spring Cloud Config Server
├── order-user-service/        # User Service (Authentication & User Management)
├── order-product-service/     # Product Service (Product Catalog)
├── order-order-service/       # Order Service (Order Processing)
├── order-return-service/      # Return Service (Return Management)
└── order-stock-service/       # Stock Service (Inventory Management)
```

## Module Details

### 1. order-common
**Purpose**: Shared code used across all services

**Contains**:
- Constants (EOrderStatus, EReturnStatus, ERole, EStockStatus)
- Entities (UserEntity, ProductEntity, OrderEntity, etc.)
- DTOs (All request/response DTOs)
- Exceptions (Custom exceptions)
- Mappers (MapStruct mappers)
- Utilities

**Package**: `com.company.ordersystem.common.*`

### 2. order-discovery-server
**Purpose**: Eureka Service Discovery Server

**Port**: 8761

**Features**:
- Service registration
- Service discovery
- Health monitoring

### 3. order-config-server
**Purpose**: Centralized configuration management

**Port**: 8888

**Features**:
- Centralized configuration
- Environment-specific configs
- Dynamic configuration updates

### 4. order-user-service
**Purpose**: User authentication and management

**Port**: 8081

**Responsibilities**:
- User registration
- User authentication (JWT)
- User profile management
- Role management

**Endpoints**:
- `/api/v1/noauth/register`
- `/api/v1/noauth/login`
- `/api/v1/admin/users/**`

### 5. order-product-service
**Purpose**: Product catalog management

**Port**: 8082

**Responsibilities**:
- Product CRUD operations
- Category management
- Product search

**Endpoints**:
- `/api/v1/admin/products/**`

### 6. order-order-service
**Purpose**: Order processing

**Port**: 8083

**Responsibilities**:
- Order creation
- Order status management
- Order validation

**Endpoints**:
- `/api/v1/customer/orders/**`
- `/api/v1/admin/orders/**`

### 7. order-return-service
**Purpose**: Return request management

**Port**: 8084

**Responsibilities**:
- Return request creation
- Return status management
- Return validation

**Endpoints**:
- `/api/v1/customer/returns/**`
- `/api/v1/admin/returns/**`

### 8. order-stock-service
**Purpose**: Inventory management

**Port**: 8085

**Responsibilities**:
- Stock level management
- Stock updates
- Inventory tracking

**Endpoints**:
- `/api/v1/admin/stock/**`

## Building the Project

### Build All Modules
```bash
mvn clean install
```

### Build Specific Module
```bash
cd order-user-service
mvn clean install
```

## Running the Services

### 1. Start Discovery Server (First)
```bash
cd order-discovery-server
mvn spring-boot:run
```
Access at: http://localhost:8761

### 2. Start Config Server (Optional)
```bash
cd order-config-server
mvn spring-boot:run
```
Access at: http://localhost:8888

### 3. Start Services (In any order)
```bash
# User Service
cd order-user-service
mvn spring-boot:run

# Product Service
cd order-product-service
mvn spring-boot:run

# Order Service
cd order-order-service
mvn spring-boot:run

# Return Service
cd order-return-service
mvn spring-boot:run

# Stock Service
cd order-stock-service
mvn spring-boot:run
```

## Next Steps - Code Migration

The following code needs to be moved from `src/` to appropriate modules:

### User Service Module
- [ ] Copy `controller/AuthController.java` → `order-user-service/src/main/java/com/company/ordersystem/user/controller/`
- [ ] Copy `controller/UserController.java` → `order-user-service/src/main/java/com/company/ordersystem/user/controller/`
- [ ] Copy `service/UserService.java` → `order-user-service/src/main/java/com/company/ordersystem/user/service/`
- [ ] Copy `service/impl/UserServiceImpl.java` → `order-user-service/src/main/java/com/company/ordersystem/user/service/`
- [ ] Copy `service/JwtService.java` → `order-user-service/src/main/java/com/company/ordersystem/user/service/`
- [ ] Copy `service/impl/JwtServiceImpl.java` → `order-user-service/src/main/java/com/company/ordersystem/user/service/`
- [ ] Copy `repository/UserRepository.java` → `order-user-service/src/main/java/com/company/ordersystem/user/repository/`
- [ ] Copy `repository/RoleRepository.java` → `order-user-service/src/main/java/com/company/ordersystem/user/repository/`
- [ ] Copy `config/SecurityConfig.java` → `order-user-service/src/main/java/com/company/ordersystem/user/config/`
- [ ] Copy `filter/JwtAuthFilter.java` → `order-user-service/src/main/java/com/company/ordersystem/user/filter/`
- [ ] Copy `service/impl/UserDetailsServiceImpl.java` → `order-user-service/src/main/java/com/company/ordersystem/user/service/`
- [ ] Copy `service/impl/UserDetailsImpl.java` → `order-user-service/src/main/java/com/company/ordersystem/user/service/`

### Product Service Module
- [ ] Copy `controller/AdminController.java` (product-related endpoints) → `order-product-service/src/main/java/com/company/ordersystem/product/controller/`
- [ ] Copy `service/ProductService.java` → `order-product-service/src/main/java/com/company/ordersystem/product/service/`
- [ ] Copy `service/impl/ProductServiceImpl.java` → `order-product-service/src/main/java/com/company/ordersystem/product/service/`
- [ ] Copy `repository/ProductRepository.java` → `order-product-service/src/main/java/com/company/ordersystem/product/repository/`
- [ ] Copy `repository/CategoryRepository.java` → `order-product-service/src/main/java/com/company/ordersystem/product/repository/`

### Order Service Module
- [ ] Copy `controller/CustomerController.java` (order endpoints) → `order-order-service/src/main/java/com/company/ordersystem/order/controller/`
- [ ] Copy `controller/AdminController.java` (order endpoints) → `order-order-service/src/main/java/com/company/ordersystem/order/controller/`
- [ ] Copy `service/OrderService.java` → `order-order-service/src/main/java/com/company/ordersystem/order/service/`
- [ ] Copy `service/impl/OrderServiceImpl.java` → `order-order-service/src/main/java/com/company/ordersystem/order/service/`
- [ ] Copy `repository/OrderRepository.java` → `order-order-service/src/main/java/com/company/ordersystem/order/repository/`
- [ ] Copy `validator/OrderStatusTransitionValidator.java` → `order-order-service/src/main/java/com/company/ordersystem/order/validator/`

### Return Service Module
- [ ] Copy `controller/CustomerController.java` (return endpoints) → `order-return-service/src/main/java/com/company/ordersystem/return_/controller/`
- [ ] Copy `controller/AdminController.java` (return endpoints) → `order-return-service/src/main/java/com/company/ordersystem/return_/controller/`
- [ ] Copy `service/ReturnService.java` → `order-return-service/src/main/java/com/company/ordersystem/return_/service/`
- [ ] Copy `service/impl/ReturnServiceImpl.java` → `order-return-service/src/main/java/com/company/ordersystem/return_/service/`
- [ ] Copy `repository/ReturnRepository.java` → `order-return-service/src/main/java/com/company/ordersystem/return_/repository/`

### Stock Service Module
- [ ] Copy `controller/AdminController.java` (stock endpoints) → `order-stock-service/src/main/java/com/company/ordersystem/stock/controller/`
- [ ] Copy `service/StockService.java` → `order-stock-service/src/main/java/com/company/ordersystem/stock/service/`
- [ ] Copy `service/impl/StockServiceImpl.java` → `order-stock-service/src/main/java/com/company/ordersystem/stock/service/`

## Package Name Updates Required

After copying files, update package names:

1. **User Service**: Change `com.company.ordersystem.*` to `com.company.ordersystem.user.*`
2. **Product Service**: Change `com.company.ordersystem.*` to `com.company.ordersystem.product.*`
3. **Order Service**: Change `com.company.ordersystem.*` to `com.company.ordersystem.order.*`
4. **Return Service**: Change `com.company.ordersystem.*` to `com.company.ordersystem.return_.*`
5. **Stock Service**: Change `com.company.ordersystem.*` to `com.company.ordersystem.stock.*`

**Important**: All imports from `com.company.ordersystem.common.*` should remain as-is since they reference the common module.

## Inter-Service Communication

Currently, services communicate through:
- **Direct calls** (in monolithic mode)
- **REST APIs** (when fully separated)

Future enhancements:
- **Feign Client** for service-to-service calls
- **RabbitMQ** for async communication
- **API Gateway** for routing

## Database Configuration

Each service can have its own database:
- User Service: `userdb`
- Product Service: `productdb`
- Order Service: `orderdb`
- Return Service: `returndb`
- Stock Service: `stockdb`

For production, configure PostgreSQL for each service.

## Testing

Run tests for all modules:
```bash
mvn test
```

Run tests for specific module:
```bash
cd order-user-service
mvn test
```

## Deployment

Each service can be deployed independently:
1. Build the service: `mvn clean package`
2. Run the JAR: `java -jar target/order-*-service-0.0.1-SNAPSHOT.jar`
3. Or deploy to Render/Cloud platform

## Notes

- The old `src/` directory can be kept for reference but should eventually be removed
- All services depend on `order-common` module
- Services register with Eureka for discovery
- Configuration can be centralized via Config Server
- Each service runs on a different port to avoid conflicts
