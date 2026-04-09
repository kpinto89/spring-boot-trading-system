# Trading UI & Backend Integration Guide

## Architecture Overview

```
┌─────────────────────────────────────────────────────┐
│  React 18 TypeScript Frontend (trading-ui)          │
│  - Vite dev server: http://localhost:3000           │
│  - Proxies API calls to backend via vite.config.ts  │
└────────────────────┬────────────────────────────────┘
                     │
          ┌──────────┴──────────┐
          │                     │
      REST API (TCP)    WebSocket (STOMP)
          │                     │
          │                     │
┌─────────┴──────────┬──────────┴────────────────────────┐
│  Spring Boot Backend (Spring Boot 3.2.5)              │
│  - API: http://localhost:8080/api                     │
│  - WebSocket: ws://localhost:8080/ws                  │
│                                                        │
│  Components:                                          │
│  - AuthController: /api/auth/{register,login}         │
│  - OrderController: /api/orders (CRUD)                │
│  - MarketController: WebSocket market data            │
│  - OrderService: Matching engine                      │
└────────────────────┬───────────────────────────────────┘
                     │
        ┌────────────┴────────────┐
        │                         │
      PostgreSQL             Redis (optional)
    (localhost:5432)         (localhost:6379)
```

## Setup Instructions

### Step 1: Ensure Backend is Running

```bash
# Terminal 1: Start Spring Boot backend
cd /path/to/spring-boot-trading-system
mvn spring-boot:run
```

Wait for:
```
Started TradingApplication in X.XXX seconds
```

Backend will be available at: **http://localhost:8080**

### Step 2: Verify Backend Health

Check that the backend is responding:

```bash
# Test health endpoint
curl http://localhost:8080/actuator/health

# Should respond with:
# {"status":"UP"}
```

### Step 3: Start React Frontend

```bash
# Terminal 2: Start React dev server
cd trading-ui
npm install
npm run dev
```

Frontend will be available at: **http://localhost:3000**

### Step 4: Access the Application

1. Open browser: **http://localhost:3000**
2. You'll be redirected to `/login`
3. Click **"Register here"** to create a new account
4. Fill in email, password, and full name
5. After registration, you'll be logged in automatically
6. You'll see the **Dashboard** with:
   - Order placement form
   - Market prices (if you've opened orders)
   - Your orders list

## Testing the Trading Flow

### Create a Test Order

1. **Dashboard** → **Place Order** form
2. Fill in:
   - Symbol: `AAPL`
   - Side: `BUY`
   - Type: `LIMIT`
   - Price: `150.00`
   - Quantity: `10`
3. Click **Place Order**
4. Check **Your Orders** table

### Real-time Market Data

1. Open multiple browser windows with the dashboard
2. Place buy/sell orders from different accounts
3. Orders will match in real-time
4. Market prices update via WebSocket
5. Trade execution appears in all connected clients

## API Reference

### Authentication

#### Register
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePass123",
  "fullName": "John Doe"
}

Response 200:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePass123"
}

Response 200:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Orders

#### Place Order
```http
POST /api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "symbol": "AAPL",
  "side": "BUY",
  "type": "LIMIT",
  "price": 150.50,
  "quantity": 10
}

Response 200:
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "symbol": "AAPL",
  "side": "BUY",
  "type": "LIMIT",
  "price": 150.50,
  "quantity": 10,
  "remainingQty": 10,
  "status": "NEW",
  "createdAt": "2026-04-09T10:36:50Z"
}
```

#### Get My Orders
```http
GET /api/orders/me
Authorization: Bearer {token}

Response 200:
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "symbol": "AAPL",
    "side": "BUY",
    "type": "LIMIT",
    "price": 150.50,
    "quantity": 10,
    "remainingQty": 5,
    "status": "PARTIALLY_FILLED",
    "createdAt": "2026-04-09T10:36:50Z"
  }
]
```

### WebSocket Subscriptions

#### Subscribe to Price Updates
```javascript
// Automatically done by useWebSocket hook
client.subscribe('/topic/price/AAPL', (message) => {
  const data = JSON.parse(message.body);
  // { symbol: 'AAPL', price: 150.75, timestamp: '2026-04-09T10:37:00Z' }
});
```

#### Subscribe to Trade Updates
```javascript
// Automatically done by useWebSocket hook
client.subscribe('/topic/trades/AAPL', (message) => {
  const data = JSON.parse(message.body);
  // { symbol: 'AAPL', price: 150.75, quantity: 5, executedAt: '2026-04-09T10:37:00Z' }
});
```

## Troubleshooting

### Frontend won't connect to backend

**Error:** `Failed to fetch http://localhost:8080/api/auth/login`

**Solution:**
1. Ensure Spring Boot backend is running (`mvn spring-boot:run`)
2. Check backend is on `localhost:8080` in `.env.local`
3. Restart frontend: `npm run dev`

### WebSocket connection fails

**Error:** `WebSocket is closed before the connection is established`

**Solution:**
1. Backend must have WebSocket config (already configured in SpringBootTrading)
2. Check backend logs for `/ws` endpoint errors
3. Ensure no firewall blocking port 8080

### "Unauthorized" errors after login

**Error:** `401 Unauthorized when placing orders`

**Solution:**
1. Your JWT token may have expired
2. Logout and login again
3. Check backend JWT secret matches (it auto-generates, should be fine)

### Market data not updating in real-time

**Error:** Price/trade updates not appearing

**Solution:**
1. Place orders from multiple clients to trigger trades
2. Check WebSocket connection in browser DevTools → Network → WS
3. Look for `/topic/price/{symbol}` subscriptions

### Port already in use

**Error:** `Port 3000 is already in use` or `Port 8080 already in use`

**Solution:**
```bash
# Kill process on port 3000 (frontend)
lsof -i :3000 | grep LISTEN | awk '{print $2}' | xargs kill -9

# Kill process on port 8080 (backend)
lsof -i :8080 | grep LISTEN | awk '{print $2}' | xargs kill -9
```

Or change ports in:
- Frontend: `vite.config.ts` → `server.port: 3001`
- Backend: `application.yml` → `server.port: 8081`

## Performance Tips

### Development

- **HMR enabled** — Changes save instantly without page reload
- **TypeScript strict mode** — Catch errors at dev time
- **React DevTools** — Install Chrome extension for debugging

### Production Build

```bash
# Optimize bundle
npm run build

# Output: dist/ folder (~200KB gzipped)
# Serve with: npm run preview
```

### Database Performance

- PostgreSQL indexes on `orders(symbol, user_id)`
- Redis cache for market data (optional)
- Connection pool size: 10 (configurable in backend)

## Next Steps

- [ ] Add more trading pairs (GOOGL, MSFT, TSLA)
- [ ] Implement order cancellation
- [ ] Add order history chart/analytics
- [ ] Build mobile app with React Native
- [ ] Add backtesting engine
- [ ] Implement portfolio analytics
- [ ] Add email notifications for trades

## Support

- Frontend docs: See `trading-ui/README.md`
- Backend docs: See main `README.md` in project root
- Issues? Check logs:
  - Frontend: Browser console (`F12`)
  - Backend: Terminal output or `tail -f app.log`

