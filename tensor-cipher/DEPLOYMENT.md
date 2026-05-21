# Deployment Guide

## Render (Backend)

1. Create an account at [render.com](https://render.com)
2. Click **New → Web Service**
3. Connect your GitHub repository
4. Set **Root Directory**: `tensor-cipher/backend`
5. Set **Runtime**: Docker
6. Set **Dockerfile Path**: `./Dockerfile`
7. Click **Deploy**
8. Wait for the build (~3–5 min on free tier)
9. Copy your service URL — it will look like `https://tensor-cipher-api.onrender.com`

> **Note:** Free tier spins down after 15 min of inactivity. The first request may take ~30 s to wake up.

Optional environment variable:
| Key        | Value     | Description              |
|------------|-----------|--------------------------|
| `JAVA_OPTS`| `-Xmx256m`| Limit heap on free tier  |

---

## Vercel (Frontend)

1. Create an account at [vercel.com](https://vercel.com)
2. Click **New Project → Import Git Repository**
3. Select your repository
4. Set **Root Directory**: `tensor-cipher/frontend`
5. Set **Framework Preset**: Vite
6. Add **Environment Variable**:
   - Key: `VITE_API_URL`
   - Value: `https://tensor-cipher-api.onrender.com` ← your Render URL from step 9 above
7. Click **Deploy**
8. Vercel assigns a URL like `https://tensor-cipher.vercel.app`

---

## Local Docker (optional)

```bash
cd tensor-cipher/backend
mvn clean package -DskipTests
docker build -t tensor-cipher-api .
docker run -p 8080:8080 tensor-cipher-api
```

---

## Environment Variables Reference

### Frontend (`frontend/.env`)
```
VITE_API_URL=http://localhost:8080
```

### Backend
No required environment variables. Spring Boot reads `application.properties` from the JAR.
