# FitMan Nation Backend API

Spring Boot backend for FitMan Nation fitness platform with authentication, payment integration, and analytics.

## Features

- ✅ Three user types: USER, ADMIN, MENTOR
- ✅ JWT-based authentication
- ✅ Razorpay payment gateway integration
- ✅ Google Analytics event tracking
- ✅ Membership management
- ✅ RESTful API endpoints
- ✅ MySQL database
- ✅ CORS enabled for frontend

## Tech Stack

- **Spring Boot 3.2.0**
- **Spring Security** (JWT)
- **Spring Data JPA**
- **MySQL**
- **Razorpay Java SDK**
- **Maven**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Razorpay account (for payment integration)
- Google Analytics account (for tracking)

## Setup Instructions

### 1. Database Setup

```sql
CREATE DATABASE fitman_nation;
```

### 2. Configuration

Update `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# Razorpay
razorpay.key.id=YOUR_RAZORPAY_KEY_ID
razorpay.key.secret=YOUR_RAZORPAY_KEY_SECRET

# Google Analytics
google.analytics.tracking.id=YOUR_GA_TRACKING_ID

# JWT Secret (change in production)
jwt.secret=your-very-secure-secret-key-min-256-bits
```

### 3. Build and Run

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run
```

Server will start on `http://localhost:8080`

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Payment (Protected)

- `POST /api/payment/create-order` - Create Razorpay order
- `POST /api/payment/verify` - Verify payment and activate membership

### Memberships (Protected)

- `GET /api/user/memberships` - Get user memberships
- `GET /api/user/memberships/active` - Get active membership

### User Profile (Protected)

- `GET /api/user/profile` - Get user profile
- `PUT /api/user/profile` - Update user profile

### Analytics (Public)

- `POST /api/analytics/track` - Track CTA events

### Public

- `GET /api/public/plans` - Get all membership plans

## Authentication

All protected endpoints require JWT token in header:

```
Authorization: Bearer <token>
```

## User Roles

- **USER**: Regular members
- **MENTOR**: Fitness coaches/mentors
- **ADMIN**: Platform administrators

## Razorpay Integration

1. Create order: `POST /api/payment/create-order`
2. Use Razorpay checkout on frontend
3. Verify payment: `POST /api/payment/verify`
4. Membership automatically activated on successful payment

## Google Analytics Integration

Track CTA button clicks:

```javascript
fetch('/api/analytics/track', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    eventCategory: 'CTA',
    eventAction: 'click',
    eventLabel: 'Hero Get Started',
    userId: 'user123',
    sessionId: 'session456'
  })
})
```

## Database Schema

- **users**: User accounts (USER, ADMIN, MENTOR)
- **memberships**: User membership subscriptions
- **payments**: Payment transactions
- **analytics_events**: Google Analytics events

## Frontend Integration

Update frontend API base URL:

```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

## Production Deployment

1. Change JWT secret to strong random key
2. Update database credentials
3. Set Razorpay production keys
4. Configure CORS for production domain
5. Enable HTTPS
6. Set up proper logging

## License

© 2024 FitMan Nation. All rights reserved.


