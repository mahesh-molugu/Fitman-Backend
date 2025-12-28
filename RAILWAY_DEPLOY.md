# Railway Deployment - Quick Guide

## 🚀 Deploy to Railway in 5 Minutes

### Step 1: Push Backend to GitHub (If Not Already)

```bash
cd Fitman-Backend
git init
git add .
git commit -m "Initial commit - FitMan Nation backend"
git branch -M main

# Create new repo on GitHub, then:
git remote add origin https://github.com/mahesh-molugu/Fitman-Backend.git
git push -u origin main
```

### Step 2: Deploy on Railway

1. **Go to**: https://railway.app
2. **Sign up** with GitHub
3. **New Project** → **Deploy from GitHub repo**
4. **Select**: `Fitman-Backend` repository
5. Railway auto-detects Spring Boot ✅

### Step 3: Add MySQL

1. In project, click **"New"**
2. **Database** → **MySQL**
3. Railway creates database automatically
4. **Copy connection details**

### Step 4: Set Variables

Go to your service → **Variables** tab:

**Required Variables:**
```
SPRING_DATASOURCE_URL=<from-railway-mysql>
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=<from-railway>
RAZORPAY_KEY_ID=rzp_test_xxxxx
RAZORPAY_KEY_SECRET=xxxxx
JWT_SECRET=<generate-random-32-chars>
GOOGLE_ANALYTICS_TRACKING_ID=G-XXXXXXXXXX
```

**How to get:**
- Database: Railway shows when you create MySQL
- Razorpay: https://dashboard.razorpay.com/app/keys
- JWT: Use `openssl rand -base64 32`
- GA: From Google Analytics

### Step 5: Deploy

Railway **auto-deploys** when you:
- Push to GitHub, OR
- Click **"Deploy"** button

Wait 2-5 minutes for build.

### Step 6: Get URL

1. Go to **Settings** → **Networking**
2. Click **"Generate Domain"**
3. Your backend URL: `https://your-app.railway.app`

### Step 7: Test

Open: `https://your-app.railway.app/api/public/plans`

Should see JSON response! ✅

---

## Update Frontend

After backend is deployed:

1. Edit `src/services/api.js`:
   ```javascript
   const API_BASE_URL = 'https://your-app.railway.app/api'
   ```

2. Rebuild and deploy:
   ```bash
   npm run build
   npm run deploy
   ```

---

**That's it! Your backend is live!** 🎉

