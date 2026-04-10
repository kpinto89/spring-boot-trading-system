# 🚀 Quick Start - Get AAPL Market Price

## Overview
This guide shows you how to quickly get the market price for AAPL symbol using the Trading System API.

## Prerequisites
- WSL2 (Windows Subsystem for Linux)
- Docker Desktop running and configured for WSL
- Node.js 18+ (for frontend)
- Java 17+ (for backend, or Maven will handle it)

## 📍 WSL Path
```
/mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system
```

---

## Option 1: Quick Start (Fastest - ~2 minutes)

### In WSL Terminal:

```bash
# Navigate to project
cd /mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system

# Start PostgreSQL
cd spring-boot-trading-backend
docker-compose up -d

# Wait 3 seconds for DB to start
sleep 3

# Build backend
mvn clean package -DskipTests -q

# Run backend
mvn spring-boot:run &

# Wait for startup
sleep 10

# Test the API
curl http://localhost:8080/api/market/last-price?symbol=AAPL
```

**Expected output:** `151.50` (from demo data)

---

## Option 2: Complete Setup (Backend + Frontend)

### Terminal 1 - Start Backend:
```bash
cd /mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system/spring-boot-trading-backend

# Start database
docker-compose up -d

# Build and run
mvn clean package -DskipTests
java -jar target/spring-boot-trading-backend-0.0.1-SNAPSHOT.jar
```

Wait for: `Started TradingApplication in X seconds`

### Terminal 2 - Start Frontend:
```bash
cd /mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system/trading-ui

npm install
npm run dev
```

### Access URLs:
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Frontend**: http://localhost:3000

---

## API Endpoint Details

### Get Market Price
**No authentication required**

```bash
curl http://localhost:8080/api/market/last-price?symbol=AAPL
```

#### Parameters:
- `symbol` (required): Stock symbol (e.g., AAPL, GOOGL, MSFT)

#### Response:
```
151.50
```

#### cURL Examples:
```bash
# AAPL
curl http://localhost:8080/api/market/last-price?symbol=AAPL

# Multiple symbols
curl http://localhost:8080/api/market/last-price?symbol=GOOGL
curl http://localhost:8080/api/market/last-price?symbol=MSFT
```

---

## Demo Data

The system comes with demo data in the database:

### Demo Trades (AAPL):
- **Buy Order**: 100 shares @ $150.00 (50 filled)
- **Sell Order**: 100 shares @ $152.00 (fully filled)
- **Last Trade Price**: $151.50 for 50 shares

### Demo Users:
- **User 1**: demo@example.com / Password123!
- **User 2**: trader1@example.com / Password123!

---

## Troubleshooting

### Port Already in Use
```bash
# Find what's using port 8080
sudo lsof -i :8080

# Kill the process
kill -9 <PID>

# Or use a different port
JAVA_OPTS="-Dserver.port=8081" java -jar target/spring-boot-trading-backend-0.0.1-SNAPSHOT.jar
```

### PostgreSQL Connection Error
Make sure Docker container is running:
```bash
docker ps | grep postgres
docker-compose logs postgres
```

### Maven Build Issues
```bash
# Clean build
mvn clean
mvn install -DskipTests

# Or use Maven wrapper if available
./mvnw clean install -DskipTests
```

### Frontend Not Loading
```bash
# Clear npm cache and reinstall
rm -rf node_modules package-lock.json
npm install
npm run dev
```

---

## What's Next?

1. **View all market prices** in the Dashboard
2. **Place orders** (requires authentication)
3. **Monitor real-time updates** via WebSocket
4. **Check order history** in My Orders
5. **Export trading data** via API

---

## API Documentation

Full API documentation available at:
```
http://localhost:8080/swagger-ui.html
```

Or access the OpenAPI spec:
```
http://localhost:8080/v3/api-docs
```

---

## Support

For issues or questions:
1. Check logs: `docker-compose logs -f`
2. Check backend console for errors
3. Check browser console for frontend errors
4. Review README.md in project root

