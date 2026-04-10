# 📋 Quick Reference Card

## Get AAPL Market Price - One Page Cheat Sheet

### The Goal
Retrieve AAPL market price via REST API
**Expected Response: `151.50`**

---

## Run It (3 Steps)

### Step 1: Start Database
```bash
cd /mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system/spring-boot-trading-backend
docker-compose up -d
sleep 5
```

### Step 2: Run Backend
```bash
mvn clean package -DskipTests
mvn spring-boot:run
```

### Step 3: Test API
```bash
curl http://localhost:8080/api/market/last-price?symbol=AAPL
```

**Result**: `151.50` ✅

---

## API Details

| Item | Value |
|------|-------|
| **Endpoint** | GET /api/market/last-price |
| **URL** | http://localhost:8080/api/market/last-price?symbol=AAPL |
| **Parameter** | symbol (required) |
| **Response** | 151.50 |
| **Auth** | None required |
| **Status** | 200 OK |

---

## Testing Methods

1. **cURL**
   ```bash
   curl http://localhost:8080/api/market/last-price?symbol=AAPL
   ```

2. **Browser**
   ```
   http://localhost:8080/api/market/last-price?symbol=AAPL
   ```

3. **Swagger UI**
   ```
   http://localhost:8080/swagger-ui.html
   → Find: GET /api/market/last-price
   → Try it out
   ```

4. **Frontend**
   ```
   http://localhost:3000
   ```

---

## Port Reference

| Service | Port | URL |
|---------|------|-----|
| Backend | 8080 | http://localhost:8080 |
| Frontend | 3000 | http://localhost:3000 |
| Database | 5432 | localhost:5432 |
| Swagger | 8080 | http://localhost:8080/swagger-ui.html |

---

## Troubleshooting

| Problem | Fix |
|---------|-----|
| Port in use | `sudo lsof -i :8080; kill -9 <PID>` |
| DB not ready | Wait 5 more seconds |
| mvn not found | Use WSL terminal |
| Build error | Run `mvn clean` |
| No price returned | Backend not fully started |

---

## Documentation Files

```
README.md
  ↓
IMPLEMENTATION_COMPLETE.md ← Start here for full details
  ↓
ACTION_PLAN.md ← Step-by-step guide
  ↓
QUICK_START_MARKET_PRICE.md ← Quick ref
TROUBLESHOOTING.md ← Problem solutions
WSL_RUN_GUIDE.md ← WSL instructions
MARKET_PRICE_GUIDE.md ← Architecture
```

---

## Database Content

```sql
-- Demo AAPL Trade
SELECT * FROM trades WHERE symbol = 'AAPL';

-- Result:
-- symbol='AAPL', price=151.50, quantity=50, executed_at=<now>
```

---

## Key Code Locations

```
Backend:
  Controller: src/main/java/.../api/controller/MarketController.java
  Repository: src/.../infrastructure/persistence/repository/JpaTradeRepository.java
  Config: src/main/resources/application.yml
  Migrations: src/main/resources/db/migration/

Frontend:
  Service: src/services/market.ts
  Store: src/stores/marketStore.ts
  Component: src/components/Dashboard.tsx
```

---

## HTTP Examples

### cURL
```bash
curl -X GET "http://localhost:8080/api/market/last-price?symbol=AAPL"
```

### Python
```python
import requests
r = requests.get('http://localhost:8080/api/market/last-price?symbol=AAPL')
print(r.text)  # 151.50
```

### JavaScript
```javascript
fetch('http://localhost:8080/api/market/last-price?symbol=AAPL')
  .then(r => r.text())
  .then(price => console.log(price))  // 151.50
```

### PowerShell
```powershell
Invoke-RestMethod "http://localhost:8080/api/market/last-price?symbol=AAPL"
```

---

## Environment Variables (if needed)

```bash
export SERVER_PORT=8080
export DB_URL=jdbc:postgresql://localhost:5432/trading?serverTimezone=UTC
export DB_USER=trading
export DB_PASSWORD=trading
export JWT_SECRET=your-secret-here
```

---

## Important Notes

- ✅ No authentication required for market prices
- ✅ Demo data included and pre-seeded
- ✅ All timestamps in UTC
- ✅ Prices stored as BigDecimal (precise)
- ✅ WebSocket support for real-time updates
- ✅ CORS configured for frontend

---

## Quick Checklist

- [ ] WSL terminal open
- [ ] Docker Desktop running
- [ ] Maven/Java installed
- [ ] Run Step 1 (docker-compose up)
- [ ] Run Step 2 (mvn clean package)
- [ ] Backend shows "Started TradingApplication"
- [ ] Run Step 3 (curl test)
- [ ] Got response: 151.50 ✅

---

## Success Criteria

✅ Backend running on :8080
✅ Database connected
✅ Demo data loaded
✅ API returns 151.50
✅ No errors in logs

---

## Help

1. Check logs: `docker-compose logs postgres`
2. Check API: `curl -v http://localhost:8080/api/market/last-price?symbol=AAPL`
3. See guide: Read `TROUBLESHOOTING.md`
4. Full docs: Read `IMPLEMENTATION_COMPLETE.md`

---

## Files Modified/Created

- ✏️ Modified: `application.yml`
- ✨ Created: `V2__demo_data.sql`
- 📖 Created: 8 documentation files
- 🐚 Created: 4 helper scripts

---

**Last Updated**: April 10, 2026
**Status**: Ready to Use ✅
**Time to Execute**: 5 minutes

