# Frontend Integration Guide

This guide shows how to integrate the Spring Boot backend with your React frontend.

## 1. Create API Service

Create `src/services/api.js`:

```javascript
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

class ApiService {
  constructor() {
    this.baseURL = API_BASE_URL;
  }

  getAuthToken() {
    return localStorage.getItem('token');
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const token = this.getAuthToken();

    const config = {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...(token && { Authorization: `Bearer ${token}` }),
        ...options.headers,
      },
    };

    try {
      const response = await fetch(url, config);
      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || 'Request failed');
      }

      return data;
    } catch (error) {
      console.error('API Error:', error);
      throw error;
    }
  }

  // Auth
  async register(userData) {
    return this.request('/auth/register', {
      method: 'POST',
      body: JSON.stringify(userData),
    });
  }

  async login(email, password) {
    return this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
  }

  // Payment
  async createPaymentOrder(plan, description) {
    return this.request('/payment/create-order', {
      method: 'POST',
      body: JSON.stringify({ plan, description }),
    });
  }

  async verifyPayment(paymentData) {
    return this.request('/payment/verify', {
      method: 'POST',
      body: JSON.stringify(paymentData),
    });
  }

  // Memberships
  async getMemberships() {
    return this.request('/user/memberships');
  }

  async getActiveMembership() {
    return this.request('/user/memberships/active');
  }

  // Profile
  async getProfile() {
    return this.request('/user/profile');
  }

  async updateProfile(profileData) {
    return this.request('/user/profile', {
      method: 'PUT',
      body: JSON.stringify(profileData),
    });
  }

  // Analytics
  async trackEvent(category, action, label, userId, sessionId) {
    return this.request('/analytics/track', {
      method: 'POST',
      body: JSON.stringify({
        eventCategory: category,
        eventAction: action,
        eventLabel: label,
        userId,
        sessionId,
      }),
    });
  }

  // Public
  async getPlans() {
    return this.request('/public/plans');
  }
}

export default new ApiService();
```

## 2. Update Contact Form

Update `src/pages/Contact.jsx`:

```javascript
import api from '../services/api';

const handleSubmit = async (e) => {
  e.preventDefault();
  
  try {
    // Track event
    await api.trackEvent('Contact Form', 'submit', 'Form Submission', null, getSessionId());
    
    // Register user if new, or login if exists
    // Then send form data to backend
    
    alert('Thank you! We\'ll contact you soon.');
  } catch (error) {
    console.error('Error:', error);
    alert('Something went wrong. Please try again.');
  }
};
```

## 3. Update Programs Page for Payment

Update `src/pages/Programs.jsx`:

```javascript
import api from '../services/api';

const handleEnroll = async (program) => {
  try {
    // Track CTA click
    await api.trackEvent('CTA', 'click', `Enroll ${program.name}`, userId, sessionId);
    
    // Check if user is logged in
    const token = localStorage.getItem('token');
    if (!token) {
      // Redirect to login/register
      window.location.href = '/contact';
      return;
    }
    
    // Create payment order
    const order = await api.createPaymentOrder(program.id.toUpperCase(), program.description);
    
    // Initialize Razorpay
    const options = {
      key: order.keyId,
      amount: order.amount * 100, // Convert to paise
      currency: order.currency,
      name: 'FitMan Nation',
      description: order.description,
      order_id: order.orderId,
      handler: async function(response) {
        // Verify payment
        await api.verifyPayment({
          razorpay_order_id: response.razorpay_order_id,
          razorpay_payment_id: response.razorpay_payment_id,
          razorpay_signature: response.razorpay_signature,
          plan: program.id.toUpperCase()
        });
        
        alert('Payment successful! Your membership is activated.');
        window.location.reload();
      },
      prefill: {
        email: userEmail,
        contact: userPhone
      },
      theme: {
        color: '#DC143C'
      }
    };
    
    const razorpay = new window.Razorpay(options);
    razorpay.open();
  } catch (error) {
    console.error('Payment error:', error);
    alert('Payment failed. Please try again.');
  }
};
```

## 4. Add Razorpay Script to index.html

```html
<script src="https://checkout.razorpay.com/v1/checkout.js"></script>
```

## 5. Update CTA Buttons

Update all CTA buttons to track events:

```javascript
const handleCTAClick = async (label) => {
  // Track in backend
  await api.trackEvent('CTA', 'click', label, userId, sessionId);
  
  // Also track in Google Analytics (frontend)
  if (window.gtag) {
    window.gtag('event', 'click', {
      event_category: 'CTA',
      event_label: label
    });
  }
};
```

## 6. Environment Variables

Create `.env` file:

```
VITE_API_URL=http://localhost:8080/api
```

For production:

```
VITE_API_URL=https://your-backend-domain.com/api
```

## 7. Authentication Flow

```javascript
// Login component
const login = async (email, password) => {
  try {
    const response = await api.login(email, password);
    localStorage.setItem('token', response.token);
    localStorage.setItem('user', JSON.stringify({
      id: response.userId,
      email: response.email,
      name: response.name,
      role: response.role
    }));
    return response;
  } catch (error) {
    throw error;
  }
};

// Check if user is authenticated
const isAuthenticated = () => {
  return !!localStorage.getItem('token');
};

// Get current user
const getCurrentUser = () => {
  const user = localStorage.getItem('user');
  return user ? JSON.parse(user) : null;
};

// Logout
const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  window.location.href = '/';
};
```

## 8. Protected Routes

Create `src/components/ProtectedRoute.jsx`:

```javascript
import { Navigate } from 'react-router-dom';

export default function ProtectedRoute({ children, requiredRole }) {
  const token = localStorage.getItem('token');
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  
  if (!token) {
    return <Navigate to="/contact" replace />;
  }
  
  if (requiredRole && user.role !== requiredRole) {
    return <Navigate to="/" replace />;
  }
  
  return children;
}
```

## 9. Usage Example

```javascript
// In your component
import api from '../services/api';
import { useEffect, useState } from 'react';

function MyComponent() {
  const [user, setUser] = useState(null);
  
  useEffect(() => {
    const loadUser = async () => {
      try {
        const profile = await api.getProfile();
        setUser(profile);
      } catch (error) {
        console.error('Failed to load profile:', error);
      }
    };
    
    if (localStorage.getItem('token')) {
      loadUser();
    }
  }, []);
  
  // ... rest of component
}
```

## Next Steps

1. Install Razorpay script in index.html
2. Create API service file
3. Update contact form to use API
4. Update programs page for payment
5. Add authentication to protected pages
6. Test all endpoints



