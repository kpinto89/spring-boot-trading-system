🚀 HOW TO RUN THE TRADING SYSTEM PROJECT

═══════════════════════════════════════════════════════════════════════════════

📋 PREREQUISITES CHECK
───────────────────────────────────────────────────────────────────────────────

Before starting, verify you have these installed:

1. Java 17+ (for Spring Boot backend)
   PowerShell:
   java -version
   Expected: openjdk version "17" or higher

2. Node.js 18+ (for React frontend)
   PowerShell:
   node --version
   npm --version
   Expected: v18.x.x or higher

3. Docker & Docker Compose (for database)
   PowerShell:
   docker --version
   docker-compose --version
   Expected: Docker version 20.x or higher

4. Maven (for building backend)
   PowerShell:
   mvn --version

⚠️ If any are missing, install them first before proceeding.


═══════════════════════════════════════════════════════════════════════════════

🎯 QUICK START (4 STEPS)
───────────────────────────────────────────────────────────────────────────────

STEP 1: Start the Database (Docker) - Terminal 1
─────────────────────────────────────────────────

Open PowerShell and run:

cd C:\Users\t_kevinpin\IdeaProjects\spring-boot-trading-system
docker-compose up -d

Wait for output:
Creating trading_postgres ... done
Creating trading_redis ... done

✅ Database is now running:
   - PostgreSQL: localhost:5432
   - Redis: localhost:6379


STEP 2: Start Spring Boot Backend - Terminal 2
───────────────────────────────────────────────

Open new PowerShell and run:

cd C:\Users\t_kevinpin\IdeaProjects\spring-boot-trading-system
mvn clean spring-boot:run

Wait for output:
Started TradingApplication in X.XXX seconds
Tomcat started on port(s): 8080

✅ Backend is running on http://localhost:8080


STEP 3: Start React Frontend - Terminal 3
──────────────────────────────────────────

Open new PowerShell and run:

cd C:\Users\t_kevinpin\IdeaProjects\spring-boot-trading-system\trading-ui
npm install
npm run dev

Wait for output:
VITE v5.0.8  ready in XXX ms
Local:   http://localhost:3000/

✅ Frontend is running on http://localhost:3000


STEP 4: Open in Browser
───────────────────────

Run in PowerShell:

Start-Process http://localhost:3000

OR manually visit: http://localhost:3000

✅ You'll see the Login screen!


═══════════════════════════════════════════════════════════════════════════════

✍️ FIRST TIME SETUP
───────────────────────────────────────────────────────────────────────────────

1. Click "Register here"

2. Fill registration form:
   - Full Name: John Trader
   - Email: trader@example.com
   - Password: SecurePass123
   - Confirm Password: SecurePass123

3. Click "Register"

4. ✅ Logged in! See Dashboard with:
   - Place Order form
   - Market Prices section
   - Your Orders table

5. Test place order:
   - Symbol: AAPL
   - Side: BUY
   - Type: LIMIT
   - Price: 150.00
   - Quantity: 10
   - Click "Place Order"

6. ✅ Order appears in table with status "NEW"


═══════════════════════════════════════════════════════════════════════════════

📊 DETAILED TERMINAL COMMANDS
───────────────────────────────────────────────────────────────────────────────

DATABASE SETUP (Terminal 1)
──────────────────────────

cd C:\Users\t_kevinpin\IdeaProjects\spring-boot-trading-system
docker-compose up -d

# Verify running
docker ps

# View logs
docker logs trading_postgres
docker logs trading_redis

# Stop
docker-compose down


BACKEND SETUP (Terminal 2)
──────────────────────────

cd C:\Users\t_kevinpin\IdeaProjects\spring-boot-trading-system

# Start backend
mvn clean spring-boot:run

# Alternative: Use IDE
# Right-click TradingApplication.java → Run

# Verify health
curl http://localhost:8080/actuator/health

# To stop: Press Ctrl+C


FRONTEND SETUP (Terminal 3)
───────────────────────────

cd C:\Users\t_kevinpin\IdeaProjects\spring-boot-trading-system\trading-ui

# Install dependencies (first time)
npm install

# Start dev server
npm run dev

# Build for production
npm run build

# To stop: Press Ctrl+C


═══════════════════════════════════════════════════════════════════════════════

🧪 TESTING SCENARIOS
───────────────────────────────────────────────────────────────────────────────

Test 1: Register & Login
────────────────────────
1. Visit http://localhost:3000
2. Click "Register here"
3. Fill form and click "Register"
4. ✅ See Dashboard


Test 2: Place Order
───────────────────
1. On Dashboard, fill "Place Order":
   Symbol: AAPL
   Side: BUY
   Type: LIMIT
   Price: 150.00
   Quantity: 10
2. Click "Place Order"
3. ✅ Order appears in "Your Orders" table


Test 3: Real-time Matching (2 Users)
─────────────────────────────────────
1. Browser 1: Register as user1@example.com
2. Browser 2: Register as user2@example.com
3. Browser 1: BUY AAPL @ $150, qty 5
4. Browser 2: SELL AAPL @ $150, qty 5
5. ✅ Both orders show status "FILLED" in real-time


Test 4: API Documentation
──────────────────────────
Visit: http://localhost:8080/swagger-ui.html
See all endpoints with "Try it out" testing


═══════════════════════════════════════════════════════════════════════════════

🛑 STOPPING
───────────────────────────────────────────────────────────────────────────────

Stop Database (Terminal 1):
docker-compose down

Stop Backend (Terminal 2):
Ctrl+C

Stop Frontend (Terminal 3):
Ctrl+C


═══════════════════════════════════════════════════════════════════════════════

🔧 TROUBLESHOOTING
───────────────────────────────────────────────────────────────────────────────

❌ Port already in use (3000, 8080, 5432, 6379)
✅ Solution: Kill the process using that port
   netstat -ano | findstr :<PORT>
   taskkill /PID <PID> /F

❌ npm install fails
✅ Solution:
   npm cache clean --force
   npm install --legacy-peer-deps

❌ Docker not found
✅ Solution: Install Docker Desktop for Windows
   https://www.docker.com/products/docker-desktop

❌ Backend won't start - connection refused
✅ Solution:
   - Check docker ps (containers must be running)
   - Wait 10 seconds for PostgreSQL to be ready
   - Check docker logs trading_postgres

❌ "Failed to fetch http://localhost:8080"
✅ Solution:
   - Backend not running? Start: mvn clean spring-boot:run
   - Check backend is on correct port
   - Restart both services

❌ WebSocket not connecting
✅ Solution:
   - Check backend is running
   - DevTools → Network → WS tab should show connection
   - Backend must have WebSocket enabled (it does)


═══════════════════════════════════════════════════════════════════════════════

📖 USEFUL REFERENCES
───────────────────────────────────────────────────────────────────────────────

Frontend URLs:
- Application: http://localhost:3000
- DevTools: F12
- React DevTools extension (Chrome)

Backend URLs:
- Health: http://localhost:8080/actuator/health
- Swagger API: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

Database:
- PostgreSQL: localhost:5432 (user: trading, pass: trading, db: trading)
- Redis: localhost:6379

Documentation:
- INTEGRATION_GUIDE.md - Full setup guide
- REACT_UI_COMPLETE.md - Frontend features
- trading-ui/README.md - Frontend docs


═══════════════════════════════════════════════════════════════════════════════

✅ SUMMARY

You now have a complete trading system running with:

✓ PostgreSQL database (docker-compose)
✓ Spring Boot backend (http://localhost:8080)
✓ React TypeScript frontend (http://localhost:3000)
✓ Real-time WebSocket updates
✓ JWT authentication
✓ Order matching engine

Happy trading! 🚀

═══════════════════════════════════════════════════════════════════════════════

