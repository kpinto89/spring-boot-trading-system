# Get AAPL Market Price - Complete Guide

## 🎯 Goal
Retrieve the market price for AAPL symbol from the Trading System API.

## ✅ What's Been Fixed

1. **Database Timezone Issue** - Fixed PostgreSQL timezone error by adding `?serverTimezone=UTC` to connection string
2. **Demo Data** - Added V2__demo_data.sql migration with:
   - Demo users for testing
   - Demo AAPL orders and trades
   - Last trade price: **$151.50**
3. **API Implementation** - MarketController is correctly configured to:
   - Accept symbol as query parameter
   - Return the latest trade price for that symbol
   - Return 0 if no trades exist

## 🚀 Quick Start (3 Steps)

### Step 1: Open WSL Terminal
```bash
# Start WSL
# Then navigate to project
cd /mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system
```

### Step 2: Start Database & Backend
```bash
cd spring-boot-trading-backend
docker-compose up -d
sleep 5
mvn clean package -DskipTests
mvn spring-boot:run
```

### Step 3: Test the API
```bash
# In another terminal
curl http://localhost:8080/api/market/last-price?symbol=AAPL
```

**Response:** `151.50`

---

## 📊 Understanding the Response

The API returns the price as a number:

```
151.50
```

This is the price from the most recent trade for AAPL in the system.

### What the system shows:
- **Symbol**: AAPL
- **Last Price**: $151.50
- **Based on**: Most recent trade between Buy and Sell orders
- **Demo Trade Details**:
  - 50 shares traded
  - Between Buy Order (100 @ $150) and Sell Order (100 @ $152)
  - Settlement price: $151.50

---

## 🔌 API Details

### Endpoint
```
GET /api/market/last-price
```

### URL
```
http://localhost:8080/api/market/last-price?symbol=AAPL
```

### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| symbol | String | Yes | Stock symbol (e.g., AAPL, GOOGL, MSFT) |

### Response
| Status | Type | Description |
|--------|------|-------------|
| 200 | BigDecimal | Latest price for the symbol |

### Example Requests

```bash
# AAPL
curl http://localhost:8080/api/market/last-price?symbol=AAPL

# Other symbols
curl http://localhost:8080/api/market/last-price?symbol=GOOGL
curl http://localhost:8080/api/market/last-price?symbol=MSFT

# Using jq to format JSON (if needed)
curl http://localhost:8080/api/market/last-price?symbol=AAPL | jq .
```

### Using from React/TypeScript

In your frontend code, the market service is already configured:

```typescript
import { marketService } from '@/services/market';

// Get AAPL price
const price = await marketService.lastPrice('AAPL');
console.log(`AAPL Price: $${price}`);
```

---

## 📁 File Structure

```
spring-boot-trading-system/
├── spring-boot-trading-backend/
│   ├── src/main/
│   │   ├── java/com/example/trading/
│   │   │   └── api/controller/
│   │   │       └── MarketController.java    ← API endpoint
│   │   └── resources/
│   │       ├── application.yml              ← Database config
│   │       └── db/migration/
│   │           ├── V1__init_schema.sql      ← Schema
│   │           └── V2__demo_data.sql        ← Demo data with AAPL
│   └── docker-compose.yml                   ← PostgreSQL setup
├── trading-ui/
│   └── src/services/
│       └── market.ts                        ← Frontend API client
└── [Documentation files]
    ├── QUICK_START_MARKET_PRICE.md
    ├── WSL_RUN_GUIDE.md
    ├── TROUBLESHOOTING.md
    └── setup-complete.sh
```

---

## 🔍 How It Works

### Database Flow
```
1. User places BUY order for AAPL
2. User places SELL order for AAPL
3. Orders match (Buy price ≤ Sell price)
4. Trade is executed and stored
5. API queries latest trade
6. Returns trade price to client
```

### Demo Data Flow
```
Trade Table:
- Symbol: AAPL
- Buy Order: 100 shares @ $150.00
- Sell Order: 100 shares @ $152.00
- Matched: 50 shares @ $151.50 ← Returned by API
```

---

## 🛠️ Architecture

```
┌─────────────────────────────────────────────────────┐
│          React Frontend (localhost:3000)             │
│  ┌──────────────────────────────────────────────┐  │
│  │ Dashboard → marketService.lastPrice('AAPL')  │  │
│  └──────────────────────────────────────────────┘  │
└────────────────────┬────────────────────────────────┘
                     │ HTTP GET
                     ↓
┌─────────────────────────────────────────────────────┐
│      Spring Boot Backend (localhost:8080)           │
│  ┌──────────────────────────────────────────────┐  │
│  │ MarketController.lastPrice('AAPL')           │  │
│  │ ↓                                              │  │
│  │ JpaTradeRepository.findTopBySymbol...()      │  │
│  │ ↓                                              │  │
│  │ Returns: Trade with price = 151.50           │  │
│  └──────────────────────────────────────────────┘  │
└────────────────────┬────────────────────────────────┘
                     │ HTTP 200
                     ↓
           Response: 151.50
```

---

## 📋 Implementation Checklist

- [x] Database configured with PostgreSQL
- [x] Schema created with trades table
- [x] Demo data seeded (V2__demo_data.sql)
- [x] MarketController implemented
- [x] JpaTradeRepository method added
- [x] CORS configured for frontend communication
- [x] Timezone issue fixed
- [x] Frontend API client ready
- [x] Documentation provided

---

## ✨ What's Included

### Backend
- Spring Boot REST API
- PostgreSQL database
- Flyway database migrations
- JWT Authentication
- WebSocket for real-time updates
- Swagger UI documentation

### Frontend
- React + TypeScript
- TailwindCSS styling
- Vite build tool
- Zustand state management
- WebSocket client integration

### Demo Features
- Sample user accounts
- Pre-populated trades
- Real market price calculation
- Order matching engine

---

## 🔐 Security

The market price endpoint (`/api/market/last-price`) is **publicly accessible** - no authentication required.

Other endpoints require JWT token:
- POST `/api/auth/register` - Create account
- POST `/api/auth/login` - Get token
- POST `/api/orders` - Place order (requires token)
- GET `/api/orders/me` - View orders (requires token)

---

## 📞 Support Resources

- **Quick Start**: QUICK_START_MARKET_PRICE.md
- **WSL Guide**: WSL_RUN_GUIDE.md
- **Troubleshooting**: TROUBLESHOOTING.md
- **Setup Script**: setup-complete.sh
- **API Testing**: test-apis.sh
- **Swagger UI**: http://localhost:8080/swagger-ui.html

---

## 🎓 Next Steps

1. ✅ Run the market price API
2. Place an order and trigger a trade
3. Watch the price update in the frontend
4. Explore WebSocket real-time updates
5. Integrate with your trading strategy

---

## 📝 Notes

- Demo data includes AAPL with last price of **$151.50**
- Database resets when Docker container is removed
- All timestamps are in UTC
- Prices are stored as BigDecimal for precision
- System supports multiple symbols (AAPL, GOOGL, MSFT, etc.)

---

Created: April 10, 2026
Last Updated: April 10, 2026

