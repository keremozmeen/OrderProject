# Deployment Guide - Render Platform

This guide provides step-by-step instructions for deploying the Modular Monolithic Order System to Render.

## Prerequisites

- Render account (sign up at https://render.com)
- GitHub/GitLab repository with the code
- PostgreSQL database (Render provides managed PostgreSQL)

## Step 1: Create PostgreSQL Database on Render

1. Log in to Render Dashboard
2. Click **"New +"** → **"PostgreSQL"**
3. Configure:
   - **Name**: `order-system-db`
   - **Database**: `ordersystem`
   - **User**: `ordersystem_user` (or auto-generated)
   - **Region**: Choose closest to your users
   - **PostgreSQL Version**: 15 or higher
4. Click **"Create Database"**
5. **Save the connection details** (Internal Database URL and External Database URL)

## Step 2: Create Web Service on Render

1. In Render Dashboard, click **"New +"** → **"Web Service"**
2. Connect your repository:
   - Select your Git provider (GitHub/GitLab)
   - Choose the repository
   - Authorize Render if needed

## Step 3: Configure Build Settings

### Build Configuration

- **Name**: `order-system` (or your preferred name)
- **Environment**: `Docker` or `Maven`
- **Region**: Same as database region
- **Branch**: `main` (or your default branch)
- **Root Directory**: `/` (root of repository)

### Build Command

```bash
mvn clean install -DskipTests
```

### Start Command

```bash
java -jar target/ordersystem-0.0.1-SNAPSHOT.jar
```

**OR** if using Docker:

```bash
docker build -t order-system .
docker run -p 8080:8080 order-system
```

## Step 4: Configure Environment Variables

Add the following environment variables in Render Dashboard:

### Database Configuration

```
DATABASE_URL=jdbc:postgresql://<host>:<port>/ordersystem
DATABASE_USERNAME=<your-db-username>
DATABASE_PASSWORD=<your-db-password>
```

**OR** use the Internal Database URL from Render:

```
SPRING_DATASOURCE_URL=${DATABASE_URL}
SPRING_DATASOURCE_USERNAME=${DB_USER}
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
```

### Security Configuration

```
JWT_SECRET=<generate-a-secure-random-string>
JWT_EXPIRATION=86400
```

**Generate JWT Secret:**
```bash
openssl rand -base64 32
```

### Application Configuration

```
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
```

### Spring Cloud Configuration (Optional - for microservices)

```
EUREKA_CLIENT_ENABLED=false
SPRING_CLOUD_CONFIG_ENABLED=false
```

## Step 5: Update application.properties for Production

Create `application-prod.properties`:

```properties
# Production Profile
spring.profiles.active=prod

# PostgreSQL Configuration
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
spring.datasource.driverClassName=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Disable H2 Console
spring.h2.console.enabled=false

# Security
app.jwtSecret=${JWT_SECRET}
app.jwtExpiration=${JWT_EXPIRATION}

# Actuator
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized

# Logging
logging.level.com.company.ordersystem=INFO
logging.level.org.springframework=WARN
```

## Step 6: Deploy

1. Click **"Create Web Service"**
2. Render will:
   - Clone your repository
   - Run the build command
   - Start the application
   - Provide a public URL

## Step 7: Verify Deployment

1. **Check Health Endpoint:**
   ```
   https://your-app.onrender.com/actuator/health
   ```

2. **Access Swagger UI:**
   ```
   https://your-app.onrender.com/swagger-ui/index.html
   ```

3. **Test API:**
   ```bash
   curl https://your-app.onrender.com/api/v1/noauth/register \
     -H "Content-Type: application/json" \
     -d '{"firstName":"Test","lastName":"User","email":"test@example.com","password":"password123","phoneNumber":"+1234567890"}'
   ```

## Step 8: Database Migration

The application uses `spring.jpa.hibernate.ddl-auto=update`, which will automatically create/update tables on first startup.

For production, consider:
- Using Flyway or Liquibase for migrations
- Setting `ddl-auto=validate` after initial setup

## Troubleshooting

### Build Failures

1. **Check Build Logs:**
   - Go to Render Dashboard → Your Service → Logs
   - Look for Maven build errors

2. **Common Issues:**
   - Java version mismatch (ensure Java 17)
   - Memory issues (increase build memory)
   - Dependency download failures (check network)

### Runtime Errors

1. **Database Connection:**
   - Verify DATABASE_URL is correct
   - Check database is accessible from Render
   - Verify credentials

2. **Port Configuration:**
   - Render uses port from `PORT` environment variable
   - Update `server.port=${PORT:8080}` in application.properties

3. **Application Startup:**
   - Check logs for startup errors
   - Verify all environment variables are set
   - Check database connectivity

### Health Check Failures

1. **Actuator Endpoint:**
   - Ensure `/actuator/health` is accessible
   - Check database connection in health check

2. **Custom Health Checks:**
   - Verify all dependencies are available
   - Check external service connectivity

## Environment-Specific Configuration

### Development (Local)
```properties
spring.profiles.active=dev
spring.datasource.url=jdbc:h2:file:./data/mydb
spring.h2.console.enabled=true
```

### Production (Render)
```properties
spring.profiles.active=prod
spring.datasource.url=${DATABASE_URL}
spring.h2.console.enabled=false
```

## Scaling

Render allows horizontal scaling:
1. Go to Service Settings
2. Adjust instance count
3. Configure auto-scaling if needed

## Monitoring

1. **Render Dashboard:**
   - View logs in real-time
   - Monitor metrics
   - Check deployment history

2. **Spring Boot Actuator:**
   - `/actuator/metrics` - Application metrics
   - `/actuator/health` - Health status
   - `/actuator/info` - Application info

## Security Best Practices

1. **Environment Variables:**
   - Never commit secrets to repository
   - Use Render's environment variable management
   - Rotate JWT secrets regularly

2. **Database:**
   - Use Internal Database URL (not external)
   - Enable SSL connections
   - Regular backups

3. **API Security:**
   - Enable HTTPS (Render provides automatically)
   - Implement rate limiting
   - Add CORS restrictions

## Cost Optimization

1. **Free Tier Limits:**
   - Services sleep after 15 minutes of inactivity
   - Database has size limits
   - Consider paid plans for production

2. **Optimization Tips:**
   - Use connection pooling
   - Enable caching
   - Optimize database queries

## Next Steps

After successful deployment:

1. ✅ Set up custom domain (optional)
2. ✅ Configure SSL certificate (automatic with Render)
3. ✅ Set up monitoring and alerts
4. ✅ Create backup strategy
5. ✅ Document API endpoints for users
6. ✅ Set up CI/CD pipeline

## Support

- Render Documentation: https://render.com/docs
- Render Support: support@render.com
- Application Issues: Check logs in Render Dashboard
