# Tensor-Cube Cipher

A symmetric encryption system based on a **rank-3 tensor (4×4×4)**. Each 64-character plaintext block is loaded into a tensor, transformed by three rounds of cyclic axis shifts derived from an SHA-256 key schedule, then XOR-masked with a key-derived tensor mask. The result is Base64-encoded.

## Algorithm

1. **Key schedule** — SHA-256 hash of the secret key supplies shift amounts and mask bytes
2. **Padding** — plaintext is uppercased and padded with `X` to a multiple of 64 characters
3. **3 rounds of shifts** — each round applies cyclic left-shifts along X, Y, Z axes (4 layers each)
4. **XOR mask** — 4×4×4 mask tensor built from key bytes is XOR-applied to the shifted tensor
5. **Base64 output** — raw bytes encoded to a Base64 string

Decryption reverses the process: XOR (self-inverse) → reverse shifts (rounds 3→2→1, right instead of left) → strip padding.

## Project Structure

```
tensor-cipher/
├── frontend/                   # React + Vite + TypeScript SPA
│   ├── src/
│   │   ├── App.tsx             # Main app (single-file design)
│   │   ├── api/cipherApi.ts    # Axios client
│   │   ├── hooks/useCipher.ts  # State + API logic
│   │   └── types/cipher.ts     # TypeScript types
│   ├── .env.example
│   └── vercel.json
└── backend/                    # Maven multi-module Java 17
    ├── pom.xml                 # Parent POM
    ├── Dockerfile
    ├── render.yaml
    ├── cipher-core/            # Pure Java encryption engine
    │   ├── Tensor3D.java
    │   ├── KeySchedule.java
    │   ├── TensorMask.java
    │   ├── TensorCipher.java
    │   ├── Axis.java
    │   └── test/               # JUnit 5 tests
    └── cipher-api/             # Spring Boot 3.2 REST API
        └── src/main/java/com/tensorcipher/api/
```

## Running Locally

### Backend

**Requirements:** JDK 17+, Maven 3.8+

```bash
cd tensor-cipher/backend
mvn clean package -DskipTests
java -jar cipher-api/target/cipher-api-1.0.0.jar
# API available at http://localhost:8080
```

### Frontend

**Requirements:** Node.js 18+

```bash
cd tensor-cipher/frontend
cp .env.example .env          # set VITE_API_URL=http://localhost:8080
npm install
npm run dev
# App available at http://localhost:5173
```

### Run tests

```bash
cd tensor-cipher/backend
mvn test -pl cipher-core
# 12 tests: KeyScheduleTest (3), Tensor3DTest (3), TensorCipherTest (6)
```

## Deploy on Render (Backend)

See [DEPLOYMENT.md](./DEPLOYMENT.md#render-backend).

## Deploy on Vercel (Frontend)

See [DEPLOYMENT.md](./DEPLOYMENT.md#vercel-frontend).

## API Reference

| Method | Endpoint                  | Body                          | Response                                      |
|--------|---------------------------|-------------------------------|-----------------------------------------------|
| GET    | `/api/cipher/health`      | —                             | `{"status":"ok","version":"1.0.0"}`           |
| POST   | `/api/cipher/encrypt`     | `{"text":"...","key":"..."}`  | `{"result":"<base64>","operation":"encrypt","processingTimeMs":<n>}` |
| POST   | `/api/cipher/decrypt`     | `{"text":"<base64>","key":"..."}` | `{"result":"...","operation":"decrypt","processingTimeMs":<n>}` |

**Error response (400/500):**
```json
{ "error": "Validation failed", "status": 400, "message": "text: must not be blank" }
```

**CORS:** `localhost:5173`, `localhost:3000`, `*.vercel.app`
