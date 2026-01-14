# API Documentation

## Base URL

```
Development: http://localhost:8080
Production: [Configure via environment variables]
```

## Authentication

Most endpoints require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

To obtain a token:
1. Register a new user: `POST /api/v1/noauth/register`
2. Login: `POST /api/v1/noauth/login`
3. Use the returned token in subsequent requests

---

## User Module APIs

### Register User
```http
POST /api/v1/noauth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "phoneNumber": "+1234567890"
}
```

**Response:**
```json
{
  "code": "00",
  "message": "Success",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1234567890",
    "roles": ["USER"]
  }
}
```

### Login
```http
POST /api/v1/noauth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "code": "00",
  "message": "Success",
  "data": "<jwt-token>"
}
```

### Get All Users (Admin Only)
```http
GET /api/v1/users
Authorization: Bearer <token>
```

### Get User by ID (Admin Only)
```http
GET /api/v1/users/{id}
Authorization: Bearer <token>
```

### Update User (Admin Only)
```http
PUT /api/v1/users/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Doe",
  "phoneNumber": "+9876543210"
}
```

### Delete User (Admin Only)
```http
DELETE /api/v1/users/{id}
Authorization: Bearer <token>
```

---

## Product Module APIs

### Get All Products
```http
GET /api/v1/admin/products
Authorization: Bearer <admin-token>
```

**Response:**
```json
{
  "code": "00",
  "message": "Success",
  "data": [
    {
      "id": 1,
      "name": "Product Name",
      "description": "Product Description",
      "price": 99.99,
      "stock": 100,
      "category": {
        "id": 1,
        "name": "Category Name"
      }
    }
  ]
}
```

### Get Product by ID
```http
GET /api/v1/admin/products/{id}
Authorization: Bearer <admin-token>
```

### Create Product (Admin Only)
```http
POST /api/v1/admin/products
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "name": "New Product",
  "description": "Product Description",
  "price": 149.99,
  "stock": 50,
  "categoryId": 1
}
```

### Update Product (Admin Only)
```http
PUT /api/v1/admin/products/{id}
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "name": "Updated Product",
  "description": "Updated Description",
  "price": 199.99,
  "stock": 75,
  "categoryId": 1
}
```

### Delete Product (Admin Only)
```http
DELETE /api/v1/admin/products/{id}
Authorization: Bearer <admin-token>
```

---

## Order Module APIs

### Create Order (User Only)
```http
POST /api/v1/customer/orders
Authorization: Bearer <user-token>
Content-Type: application/json

{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

**Response:**
```json
{
  "code": "00",
  "message": "Success",
  "data": {
    "id": 1,
    "customerId": 1,
    "status": "CREATED",
    "totalAmount": 299.98,
    "items": [
      {
        "productId": 1,
        "quantity": 2,
        "price": 199.98
      }
    ],
    "createdAt": "2025-01-15T10:30:00"
  }
}
```

### Get Customer Orders (User Only)
```http
GET /api/v1/customer/orders
Authorization: Bearer <user-token>
```

### Get All Orders (Admin Only)
```http
GET /api/v1/admin/orders
Authorization: Bearer <admin-token>
```

### Update Order Status (Admin Only)
```http
PUT /api/v1/admin/orders/{id}/status
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "id": 1,
  "status": "CONFIRMED"
}
```

**Valid Status Transitions:**
- `CREATED` → `CONFIRMED`
- `CONFIRMED` → `SHIPPED`
- `SHIPPED` → `DELIVERED`
- Any status → `CANCELLED`

### Cancel Order
```http
PUT /api/v1/customer/orders/{id}/cancel
Authorization: Bearer <user-token>
```

### Delete Order (Admin Only)
```http
DELETE /api/v1/admin/orders/{id}
Authorization: Bearer <admin-token>
```

---

## Return Module APIs

### Create Return Request (User Only)
```http
POST /api/v1/customer/returns
Authorization: Bearer <user-token>
Content-Type: application/json

{
  "orderId": 1,
  "reason": "Product defect"
}
```

**Response:**
```json
{
  "code": "00",
  "message": "Success",
  "data": {
    "id": 1,
    "orderId": 1,
    "status": "PENDING",
    "reason": "Product defect",
    "createdAt": "2025-01-15T11:00:00"
  }
}
```

### Get Customer Returns (User Only)
```http
GET /api/v1/customer/returns
Authorization: Bearer <user-token>
```

### Get All Returns (Admin Only)
```http
GET /api/v1/admin/returns
Authorization: Bearer <admin-token>
```

### Update Return Status (Admin Only)
```http
PUT /api/v1/admin/returns/{id}/status
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "id": 1,
  "status": "APPROVED"
}
```

**Valid Status Values:**
- `PENDING`
- `APPROVED`
- `REJECTED`

### Delete Return (Admin Only)
```http
DELETE /api/v1/admin/returns/{id}
Authorization: Bearer <admin-token>
```

---

## Stock Module APIs

### Get Stock by Product ID (Admin Only)
```http
GET /api/v1/admin/stock/{productId}
Authorization: Bearer <admin-token>
```

**Response:**
```json
{
  "code": "00",
  "message": "Success",
  "data": {
    "productId": 1,
    "stock": 100,
    "status": "IN_STOCK"
  }
}
```

### Update Stock (Admin Only)
```http
PUT /api/v1/admin/stock
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "productId": 1,
  "stock": 150
}
```

---

## Error Responses

All endpoints return errors in the following format:

```json
{
  "code": "ERROR_CODE",
  "message": "Error message description",
  "timestamp": "2025-01-15T10:30:00",
  "path": "/api/v1/endpoint"
}
```

### Common Error Codes

- `401` - Unauthorized (Invalid or missing token)
- `403` - Forbidden (Insufficient permissions)
- `404` - Not Found (Resource not found)
- `400` - Bad Request (Validation error)
- `500` - Internal Server Error

### Example Error Response

```json
{
  "code": "404",
  "message": "Product not found with id: 999",
  "timestamp": "2025-01-15T10:30:00",
  "path": "/api/v1/admin/products/999"
}
```

---

## Swagger UI

Interactive API documentation is available at:
```
http://localhost:8080/swagger-ui/index.html
```

---

## Rate Limiting

Currently not implemented. Future enhancement for production.

## CORS Configuration

Currently configured to allow all origins. Update `SecurityConfig` for production.

## API Versioning

Current version: `v1`

Future versions will be available at `/api/v2/`, `/api/v3/`, etc.
