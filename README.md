# 🛡️ Nexus AI Gateway & Analytics Engine

The high-performance Java Spring Boot backend engine powering the **Nexus AI** developer gateway. This service manages user account sessions, provisions custom developer API credentials, proxies inference requests to multi-model LLM runtimes via OpenRouter, executes sliding-window rate-limiting, and aggregates real-time usage telemetry.

---

## 🚀 Live Deployment

*   **Production Console**: [https://nexusai-kryj.onrender.com/](https://nexusai-kryj.onrender.com/)

---

## 🚀 Core Features & Modules

### 1. Developer JWT Authentication
*   Uses **Spring Security** filter chains to gatekeep control console endpoints (API key manager, telemetry boards).
*   Registers developers and issues secure JWT tokens. 
*   Loads secrets dynamically at startup via environment settings to ensure database password isolation.

### 2. Custom API Key Generation
*   Generates secure, prefix-bound credentials (e.g., `sk_live_...`) with unique cryptographic hashes.
*   Enforces custom nickname scopes and model constraints per key during generation.

### 3. API Key Authentication (Gateway Interceptor)
*   Integrates a Spring HTTP Filter to intercept incoming chat playground requests.
*   Extracts and validates the custom header `x-api-key`.
*   Blocks deactivated, deleted, or revoked keys immediately without touching downstream model endpoints.
*   Includes a **Soft-Delete** model which permanently deactivates keys (`REVOKED`) while preserving historical usage data for analytics.

### 4. Multi-Model Support
*   Resolves and proxies inference requests to specific LLMs based on the API key used:
    *   `NVIDIA_NANO` -> NVIDIA Nemotron-3 Nano (30B)
    *   `NVIDIA_OMNI_REASONING` -> NVIDIA Nemotron-3 Omni Reasoning
    *   `COHERE_MINI_CODE` -> Cohere North Mini Code

### 5. OpenRouter Integration
*   Acts as a secure proxy between clients and OpenRouter inference engines.
*   Maps incoming user chat messages into the correct OpenRouter JSON formats, attaches bearer API credentials securely, and returns clean, unified replies.

### 6. Stateful Chat Memory
*   Maintains conversation history profiles inside the database per-session/per-key.
*   Ensures that consecutive API calls contain previous contexts so that the LLM models retain conversational thread continuity.

### 7. Redis Sliding Window Rate Limiter
*   Uses a **Redis-based sliding-window algorithm** to protect the gateway.
*   Restricts excessive requests per key using a rolling time window (`rate-limit.max-requests` per `rate-limit.window-seconds`).
*   Returns `429 Too Many Requests` responses with warning headers when thresholds are breached, preventing DDoS attacks and key abuse.

### 8. Telemetry & API Usage Analytics
*   Logs request statuses, exception traces, response sizes, and round-trip response latencies (ms) for every incoming gateway hit.
*   Persists transaction metadata inside MySQL for telemetry parsing.

### 9. Dashboard APIs
*   Provides structured statistics endpoints for the developer UI:
    *   `/dashboard/aggregate-stats`: Pulls global metrics for all keys owned by the developer (Total requests, average latency, and success/failure ratios).
    *   `/dashboard/apikey-stats/{key}`: Displays metrics for a specific credentials token.

---

## 🛠️ Technology Stack
*   **Framework**: Spring Boot 3.3.0 (Java 17)
*   **Security**: Spring Security, JWT (JJWT library)
*   **Database**: Spring Data JPA, Hibernate, MySQL (e.g., TiDB Cloud Serverless)
*   **Caching & Limiting**: Spring Data Redis
*   **Network Client**: RestTemplate / WebClient (for OpenRouter proxying)

---

## ⚙️ Environment Variables (`.env`)
To run the backend, create a `.env` file in the root of the `nexusai/` folder with these variables:

```env
PORT=8081
DB_URL=jdbc:mysql://<host>:<port>/nexusai
DB_USERNAME=<username>
DB_PASSWORD=<password>
OPENROUTER_BASE_URL=https://openrouter.ai/api/v1
OPENROUTER_API_KEY=<your_openrouter_api_key>
REDIS_HOST=localhost
REDIS_PORT=6379
MAX_REQUEST=5
WINDOW_SECOND=60
JWT_SECRET=<your_64_character_hex_jwt_secret>
```

---

## 🛣️ API Endpoint Routing

### Public Auth Endpoints (No Token Required)
*   `POST /auth/signUp`: Register a new developer account.
*   `POST /auth/login`: Authenticate and receive a Bearer JWT token.
*   `GET /auth/health`: Public service check (used to wake up Render free-tier instances).

### Developer Console Endpoints (JWT Required)
*   `POST /apikeys`: Generate a new API key.
*   `GET /apikeys`: Fetch all keys owned by the developer.
*   `DELETE /apikeys/{key}`: Soft-delete/revoke an API key.
*   `GET /dashboard/aggregate-stats`: Fetch aggregate telemetry metrics.
*   `GET /dashboard/apikey-stats/{key}`: Fetch telemetry metrics for a specific key.

### Gateway Inference Endpoint (API Key Required)
*   `POST /chat/send-message`: Sends prompt to LLM. Requires custom header `x-api-key` containing your generated key.

---

## 🚀 How to Run

### Local Maven Command
```bash
mvn clean spring-boot:run
```

### Run using Docker
1.  **Build the Image**:
    ```bash
    docker build -t nexusai .
    ```
2.  **Run the Container**:
    ```bash
    docker run --env-file .env -p 8081:8081 nexusai
    ```
