# Spring Boot Trading System (MVP)

A clean-architecture inspired Trading System backend implemented with **Spring Boot 3**, **Java 21**, **PostgreSQL**, **Flyway**, **JWT Security**, and **WebSocket (STOMP)**.

## Features
- ✅ JWT authentication (`/api/auth/register`, `/api/auth/login`)
- ✅ Wallet & balances (seed wallet created on register)
- ✅ Place orders: **LIMIT** & **MARKET** (`POST /api/orders`)
- ✅ Matching engine (price-time priority)
- ✅ Trades persisted to DB
- ✅ Live updates via WebSocket:
  - `/topic/trades/{symbol}`
  - `/topic/price/{symbol}`
- ✅ OpenAPI/Swagger UI: `/swagger-ui.html`

## Quick Start

### 1) Run infra
```bash
docker compose up -d
```

### 2) Run the app
```bash
mvn spring-boot:run
```

### 3) Register & Login
```bash
curl -s -X POST http://localhost:8080/api/auth/register   -H 'Content-Type: application/json'   -d '{"email":"kevin@example.com","password":"Passw0rd!","fullName":"Kevin Pinto"}'

TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login   -H 'Content-Type: application/json'   -d '{"email":"kevin@example.com","password":"Passw0rd!"}' | jq -r .token)

echo $TOKEN
```

### 4) Place orders
```bash
# Place a SELL limit order
curl -s -X POST http://localhost:8080/api/orders   -H "Authorization: Bearer $TOKEN"   -H 'Content-Type: application/json'   -d '{"symbol":"AAPL","side":"SELL","type":"LIMIT","price":180.50,"quantity":10}'

# Place a BUY market order (will match immediately)
curl -s -X POST http://localhost:8080/api/orders   -H "Authorization: Bearer $TOKEN"   -H 'Content-Type: application/json'   -d '{"symbol":"AAPL","side":"BUY","type":"MARKET","quantity":5}'

# Fetch your orders
curl -s http://localhost:8080/api/orders/me   -H "Authorization: Bearer $TOKEN"
```

## WebSocket
Endpoint: `ws://localhost:8080/ws` (SockJS supported)

Subscribe:
- `/topic/trades/AAPL`
- `/topic/price/AAPL`

## Notes
- This is an MVP for learning/system design demos. It persists orders/trades and uses an in-memory order book for fast matching.
- You can extend it to multi-instance by moving order book state to Redis or using Kafka partitioning.
