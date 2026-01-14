# Order System - Multi-Module Maven Project

## Project Overview

This is a **multi-module Maven project** following COMP301 project requirements. The project is structured as separate modules, each representing a microservice that can run independently.

## Project Structure

```
order-system/ (Parent POM)
├── pom.xml                      # Parent POM
├── mvnw                         # Maven wrapper
├── mvnw.cmd                     # Maven wrapper (Windows)
│
├── order-common/                # Shared library (DTOs, Entities, Constants)
│   ├── pom.xml
│   └── src/
│
├── order-discovery-server/      # Eureka Discovery Server (Port 8761)
│   ├── pom.xml
│   └── src/
│
├── order-config-server/         # Config Server (Port 8888)
│   ├── pom.xml
│   └── src/
│
├── order-user-service/          # User Service (Port 8081)
│   ├── pom.xml
│   └── src/
│
├── order-product-service/       # Product Service (Port 8082)
│   ├── pom.xml
│   └── src/
│
├── order-order-service/         # Order Service (Port 8083)
│   ├── pom.xml
│   └── src/
│
├── order-return-service/        # Return Service (Port 8084)
│   ├── pom.xml
│   └── src/
│
└── order-stock-service/        # Stock Service (Port 8085)
    ├── pom.xml
    └── src/
```

## ✅ Proper Multi-Module Structure

- **Root level**: Only contains parent POM and module directories
- **Each module**: Has its own `src/` folder with its code
- **No `src/` at root**: This is the correct structure for multi-module projects

## Modules

### 1. order-common
**Type**: Library (JAR)  
**Purpose**: Shared code used by all services  
**Contains**: DTOs, Entities, Constants, Exceptions, Mappers

### 2. order-discovery-server
**Type**: Infrastructure Service  
**Port**: 8761  
**Purpose**: Eureka Service Discovery Server  
**Access**: http://localhost:8761

### 3. order-config-server
**Type**: Infrastructure Service  
**Port**: 8888  
**Purpose**: Spring Cloud Config Server (centralized configuration)

### 4. order-user-service
**Type**: Business Service  
**Port**: 8081  
**Purpose**: User authentication and management  
**Endpoints**: `/api/v1/noauth/**`, `/api/v1/admin/users/**`

### 5. order-product-service
**Type**: Business Service  
**Port**: 8082  
**Purpose**: Product catalog management  
**Endpoints**: `/api/v1/admin/products/**`

### 6. order-order-service
**Type**: Business Service  
**Port**: 8083  
**Purpose**: Order processing  
**Endpoints**: `/api/v1/customer/orders/**`, `/api/v1/admin/orders/**`

### 7. order-return-service
**Type**: Business Service  
**Port**: 8084  
**Purpose**: Return request management  
**Endpoints**: `/api/v1/customer/returns/**`, `/api/v1/admin/returns/**`

### 8. order-stock-service
**Type**: Business Service  
**Port**: 8085  
**Purpose**: Inventory/stock management  
**Endpoints**: `/api/v1/admin/stock/**`

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
Access Eureka Dashboard: http://localhost:8761

### 2. Start Config Server (Optional)
```bash
cd order-config-server
mvn spring-boot:run
```

### 3. Start Business Services
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

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.5.5
- **Spring Cloud**: 2024.0.0
- **Maven**: Multi-module project
- **Database**: H2 (dev), PostgreSQL (prod)
- **Service Discovery**: Eureka
- **Configuration**: Spring Cloud Config

## Documentation

- **[MODULE_EXPLANATION.md](MODULE_EXPLANATION.md)** - Detailed explanation of what each module does
- **[PROPER_STRUCTURE.md](PROPER_STRUCTURE.md)** - Multi-module structure guide
- **[MULTI_MODULE_SETUP.md](MULTI_MODULE_SETUP.md)** - Setup and migration guide
- **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** - API reference

## COMP301 Project Alignment

✅ **Multi-Module Maven Project**  
✅ **Spring Boot Microservices**  
✅ **Service Discovery (Eureka)**  
✅ **Configuration Management (Config Server)**  
✅ **RESTful APIs**  
✅ **Database Support**  
✅ **Security (JWT)**  
✅ **Deployment Ready**

## Next Steps

1. Move business logic code to appropriate service modules
2. Update package names in moved code
3. Configure inter-service communication
4. Test each service independently
5. Deploy to cloud platform (Render)

## Notes

- Each service runs on a different port
- Services register with Eureka for discovery
- Shared code is in `order-common` module
- Each module can be built and deployed independently
