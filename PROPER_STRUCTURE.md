# Proper Multi-Module Maven Structure

## ✅ Correct Structure (What We Have Now)

```
order-system/                    # Root - Parent POM only
├── pom.xml                      # Parent POM (defines all modules)
├── mvnw                         # Maven wrapper
├── mvnw.cmd                     # Maven wrapper (Windows)
│
├── order-common/                # Module 1: Shared Library
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/company/ordersystem/common/
│           │       ├── constant/
│           │       ├── entity/
│           │       ├── dto/
│           │       ├── exception/
│           │       └── mapper/
│           └── resources/
│
├── order-discovery-server/      # Module 2: Eureka Server
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/company/ordersystem/discovery/
│           └── resources/
│               └── application.properties
│
├── order-config-server/         # Module 3: Config Server
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/company/ordersystem/config/
│           └── resources/
│               └── application.properties
│
├── order-user-service/          # Module 4: User Service
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/company/ordersystem/user/
│           │       ├── controller/
│           │       ├── service/
│           │       ├── repository/
│           │       └── config/
│           └── resources/
│               └── application.properties
│
├── order-product-service/       # Module 5: Product Service
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/company/ordersystem/product/
│           └── resources/
│
├── order-order-service/         # Module 6: Order Service
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/company/ordersystem/order/
│           └── resources/
│
├── order-return-service/        # Module 7: Return Service
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/company/ordersystem/return_/
│           └── resources/
│
└── order-stock-service/         # Module 8: Stock Service
    ├── pom.xml
    └── src/
        └── main/
            ├── java/
            │   └── com/company/ordersystem/stock/
            └── resources/
```

## ❌ Wrong Structure (What We Should NOT Have)

```
order-system/
├── src/                         # ❌ NO! This should NOT exist at root
│   └── main/
│       └── java/...
├── pom.xml
└── order-common/
    └── src/...
```

**Why it's wrong**: In multi-module Maven projects, the root should ONLY contain:
- Parent POM (`pom.xml`)
- Module directories (each with their own `src/`)
- Build files (`mvnw`, `mvnw.cmd`)
- Documentation files (`.md` files)

## Current Status

⚠️ **The old `src/` folder still exists** - it contains the monolithic code that needs to be:
1. Moved to appropriate service modules
2. Then deleted

**Action Required**: 
- Move code from `src/` to service modules
- Delete `src/` folder once migration is complete

## Module Responsibilities

### order-common (Library - No Service)
- **Does NOT run** - it's just a JAR file
- Contains shared code (DTOs, Entities, Constants)
- Other services depend on it

### order-discovery-server (Infrastructure Service)
- **Runs on port 8761**
- Eureka Server - service registry
- All other services register here
- Access dashboard at: http://localhost:8761

### order-config-server (Infrastructure Service)
- **Runs on port 8888**
- Spring Cloud Config Server
- Centralized configuration management
- Optional - services can use local properties instead

### order-user-service (Business Service)
- **Runs on port 8081**
- User registration, login, management
- JWT token generation
- User CRUD operations

### order-product-service (Business Service)
- **Runs on port 8082**
- Product catalog management
- Product CRUD operations
- Category management

### order-order-service (Business Service)
- **Runs on port 8083**
- Order creation and processing
- Order status management
- Order validation

### order-return-service (Business Service)
- **Runs on port 8084**
- Return request management
- Return status updates
- Return validation

### order-stock-service (Business Service)
- **Runs on port 8085**
- Inventory/stock management
- Stock level updates
- Stock tracking

## Building and Running

### Build All Modules
```bash
mvn clean install
```

### Run Specific Service
```bash
# Run discovery server first
cd order-discovery-server
mvn spring-boot:run

# Then run other services
cd ../order-user-service
mvn spring-boot:run
```

### Build Single Module
```bash
cd order-user-service
mvn clean install
```

## Key Points

1. ✅ **Each module has its own `src/` folder**
2. ✅ **Root has NO `src/` folder** (only after migration)
3. ✅ **Each module is independent** - can be built/run separately
4. ✅ **Common code is in `order-common`** - shared by all services
5. ✅ **Infrastructure services** (discovery, config) support business services
6. ✅ **Each service has its own port** - no conflicts
