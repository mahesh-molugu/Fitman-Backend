# FitMan Nation API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication

All protected endpoints require JWT token in Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

---

## 1. Authentication Endpoints

### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "+919876543210",
  "role": "USER",
  "fitnessGoal": "weight-loss",
  "experienceLevel": "beginner",
  "workoutLocation": "home",
  "availableTime": "30-45",
  "medicalConditions": ""
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "john@example.com",
  "name": "John Doe",
  "role": "USER",
  "userId": 1
}
```

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:** Same as register

---

## 2. Payment Endpoints

### Create Payment Order
```http
POST /api/payment/create-order
Authorization: Bearer <token>
Content-Type: application/json

{
  "plan": "PRO",
  "description": "Pro Transformation Plan"
}
```

**Response:**
```json
{
  "orderId": "order_ABC123",
  "amount": 2999.0,
  "currency": "INR",
  "keyId": "rzp_test_...",
  "description": "Pro Transformation Plan"
}
```

### Verify Payment
```http
POST /api/payment/verify
Authorization: Bearer <token>
Content-Type: application/json

{
  "razorpay_order_id": "order_ABC123",
  "razorpay_payment_id": "pay_XYZ789",
  "razorpay_signature": "signature...",
  "plan": "PRO"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Payment verified and membership activated",
  "paymentId": 1,
  "membershipPlan": "Pro Transformation"
}
```

---

## 3. Membership Endpoints

### Get User Memberships
```http
GET /api/user/memberships
Authorization: Bearer <token>
```

### Get Active Membership
```http
GET /api/user/memberships/active
Authorization: Bearer <token>
```

**Response:**
```json
{
  "hasActiveMembership": true,
  "membership": {
    "id": 1,
    "plan": "PRO",
    "startDate": "2024-03-01",
    "endDate": "2024-04-01",
    "isActive": true
  }
}
```

---

## 4. User Profile Endpoints

### Get Profile
```http
GET /api/user/profile
Authorization: Bearer <token>
```

### Update Profile
```http
PUT /api/user/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "John Updated",
  "phone": "+919876543211",
  "fitnessGoal": "muscle-gain"
}
```

---

## 5. Analytics Endpoints

### Track Event
```http
POST /api/analytics/track
Content-Type: application/json

{
  "eventCategory": "CTA",
  "eventAction": "click",
  "eventLabel": "Hero Get Started",
  "userId": "user123",
  "sessionId": "session456"
}
```

**Response:**
```json
{
  "success": true,
  "eventId": 1,
  "message": "Event tracked successfully"
}
```

---

## 6. Public Endpoints

### Get Membership Plans
```http
GET /api/public/plans
```

**Response:**
```json
[
  {
    "id": "BASIC",
    "name": "Basic Plan",
    "price": 999.0
  },
  {
    "id": "PRO",
    "name": "Pro Transformation",
    "price": 2999.0
  }
]
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-03-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/auth/register"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-03-01T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid token",
  "path": "/api/user/profile"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-03-01T10:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied",
  "path": "/api/admin/users"
}
```

---

## Frontend Integration Example

### Login and Store Token
```javascript
const login = async (email, password) => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  const data = await response.json();
  localStorage.setItem('token', data.token);
  return data;
};
```

### Make Authenticated Request
```javascript
const getProfile = async () => {
  const token = localStorage.getItem('token');
  const response = await fetch('http://localhost:8080/api/user/profile', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return await response.json();
};
```

### Track CTA Click
```javascript
const trackCTA = async (label) => {
  await fetch('http://localhost:8080/api/analytics/track', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      eventCategory: 'CTA',
      eventAction: 'click',
      eventLabel: label,
      userId: getUserId(),
      sessionId: getSessionId()
    })
  });
};
```

---

## Membership Plans

- **BASIC**: ₹999/month
- **PRO**: ₹2,999/month
- **PREMIUM**: ₹4,999/month
- **SPECIAL**: Custom pricing
- **GROUP**: ₹1,999/month
- **FAMILY**: Custom pricing



