# Setup Instructions for FitMan Nation Backend

## Prerequisites

1. **Java 17+** - Download from [Oracle](https://www.oracle.com/java/) or [OpenJDK](https://openjdk.org/)
2. **Maven 3.6+** - Download from [Maven](https://maven.apache.org/download.cgi)
3. **MySQL 8.0+** - Download from [MySQL](https://dev.mysql.com/downloads/)
4. **IDE** (Optional) - IntelliJ IDEA, Eclipse, or VS Code

## Step-by-Step Setup

### 1. Install Java

```bash
# Verify Java installation
java -version
# Should show Java 17 or higher
```

### 2. Install Maven

```bash
# Verify Maven installation
mvn -version
```

### 3. Install MySQL

1. Download and install MySQL
2. Start MySQL service
3. Create database:

```sql
CREATE DATABASE fitman_nation;
```

### 4. Configure Application

Edit `src/main/resources/application.properties`:

```properties
# Update database credentials
spring.datasource.username=root
spring.datasource.password=your_password

# Get Razorpay keys from: https://dashboard.razorpay.com/app/keys
razorpay.key.id=rzp_test_xxxxxxxxxxxxx
razorpay.key.secret=your_razorpay_secret

# Get Google Analytics ID from: https://analytics.google.com/
google.analytics.tracking.id=G-XXXXXXXXXX

# Change JWT secret (use a strong random string)
jwt.secret=your-very-secure-secret-key-minimum-256-bits-long
```

### 5. Build Project

```bash
cd Fitman-Backend
mvn clean install
```

### 6. Run Application

```bash
mvn spring-boot:run
```

Or use your IDE to run `FitmanBackendApplication.java`

### 7. Verify Setup

Open browser: `http://localhost:8080/api/public/plans`

You should see JSON response with membership plans.

## Razorpay Setup

1. Go to [Razorpay Dashboard](https://dashboard.razorpay.com/)
2. Sign up or login
3. Go to Settings → API Keys
4. Generate Test Keys (for development)
5. Copy Key ID and Key Secret
6. Update in `application.properties`

## Google Analytics Setup

1. Go to [Google Analytics](https://analytics.google.com/)
2. Create account/property
3. Get Measurement ID (format: G-XXXXXXXXXX)
4. Update in `application.properties`

## Testing Endpoints

### Test Registration

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "role": "USER"
  }'
```

### Test Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

## Common Issues

### Port Already in Use

Change port in `application.properties`:
```properties
server.port=8081
```

### Database Connection Failed

1. Check MySQL is running
2. Verify credentials
3. Check database exists
4. Verify MySQL port (default: 3306)

### Razorpay Errors

1. Verify keys are correct
2. Use test keys for development
3. Check key permissions in Razorpay dashboard

## Production Deployment

1. Use production Razorpay keys
2. Change JWT secret to strong random key
3. Use production database
4. Enable HTTPS
5. Configure proper CORS origins
6. Set up logging
7. Configure backup strategy

## Next Steps

1. Test all endpoints
2. Integrate with frontend
3. Set up CI/CD
4. Deploy to cloud (AWS, Azure, GCP)



