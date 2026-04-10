# How to Run the Trading System in WSL

## Step 1: Navigate to the project in WSL

```bash
# Get the Windows path converted to WSL path
cd /mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system
```

## Step 2: Start PostgreSQL in Docker

```bash
cd spring-boot-trading-backend
docker-compose up -d
```

Wait for PostgreSQL to be ready (about 5-10 seconds), then verify:
```bash
docker-compose ps
```

## Step 3: Build the backend

```bash
# Using Maven wrapper or mvn if installed
mvn clean package -DskipTests

# Or if using Maven wrapper:
./mvnw clean package -DskipTests
```

## Step 4: Run the backend

```bash
# Option A: Run directly
java -jar target/spring-boot-trading-backend-0.0.1-SNAPSHOT.jar

# Option B: Run via Maven
mvn spring-boot:run
```

The backend will be available at: `http://localhost:8080`

Check Swagger UI: `http://localhost:8080/swagger-ui.html`

## Step 5: Run the frontend (in a new terminal)

```bash
cd /mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system/trading-ui
npm run dev
```

The frontend will be available at: `http://localhost:3000`

## Testing the Market Price API

### Get AAPL market price (no auth required):

```bash
curl http://localhost:8080/api/market/last-price?symbol=AAPL
```

Expected response (will return 151.50 from the demo data):
```json
151.50
```

### Using from the React app:

The Dashboard component will automatically fetch the market price when it loads.

## Troubleshooting

### TimeZone error:
If you see: `FATAL: invalid value for parameter "TimeZone": "Asia/Calcutta"`

This is a PostgreSQL timezone issue. The connection string in WSL needs a valid timezone.

**Fix:** Update `application.yml`:
```yaml
datasource:
  url: jdbc:postgresql://localhost:5432/trading?serverTimezone=UTC
```

### Port already in use:
- Backend: Change port in `application.yml` to 8081
- Frontend: Run with `npm run dev -- --port 3001`

### Docker connection issue:
Make sure Docker Desktop or Docker daemon is running on Windows/WSL.

Check status:
```bash
docker ps
```

## API Endpoints

- **GET** `/api/market/last-price?symbol=AAPL` - Get latest price for a symbol
- **POST** `/api/auth/register` - Register a new user
- **POST** `/api/auth/login` - Login
- **POST** `/api/orders` - Place an order
- **GET** `/api/orders/me` - Get my orders
- **WS** `ws://localhost:8080/ws` - WebSocket for real-time updates

