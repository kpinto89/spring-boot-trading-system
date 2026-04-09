════════════════════════════════════════════════════════════════════════════════
                    ✅ HOW TO RUN THE PROJECT - COMPLETE GUIDE
════════════════════════════════════════════════════════════════════════════════

🎯 OBJECTIVE: Get the entire trading system running in 5 minutes


════════════════════════════════════════════════════════════════════════════════
                              ⏱️ TIMELINE
════════════════════════════════════════════════════════════════════════════════

- Prerequisites check: 1 minute
- Start Database: 30 seconds
- Start Backend: 30 seconds (first time: 3-5 mins)
- Start Frontend: 1 minute (first time: 2-3 mins with npm install)
- Test & Verify: 1 minute

TOTAL: ~5 minutes


════════════════════════════════════════════════════════════════════════════════
                          📋 PREREQUISITES (1 MIN)
════════════════════════════════════════════════════════════════════════════════

Check you have these installed (run in PowerShell):

  ✓ Java 17+
    java -version
    Should output: openjdk version "17" (or higher)

  ✓ Node.js 18+
    node --version
    Should output: v18.x.x (or higher)

  ✓ npm 9+
    npm --version
    Should output: 9.x.x (or higher)

  ✓ Docker
    docker --version
    Should output: Docker version 20.x (or higher)

  ✓ Docker Compose
    docker-compose --version
    Should output: Docker Compose version (any)

  ✓ Maven (optional)
    mvn --version
    Should output: Apache Maven 3.x


════════════════════════════════════════════════════════════════════════════════
                      🚀 RUNNING THE SYSTEM (4 STEPS)
════════════════════════════════════════════════════════════════════════════════

IMPORTANT: You'll need to open 3 separate PowerShell terminals!
           Keep all 3 running while using the application.


STEP 1️⃣: START DATABASE (Terminal 1)
─────────────────────────────────────

PowerShell Window 1:

cd C:\Users\t_kevinpin\IdeaProjects\spring-boot-trading-system
docker-compose up -d

Wait for:
  ✓ Creating trading_postgres ... done
  ✓ Creating trading_redis ... done

✅ DATABASE READY


STEP 2️⃣: START BACKEND (Terminal 2)
────────────────────────────────────

PowerShell Window 2:

cd C:\Users\t_kevinpin\IdeaProjects\spring-boot-trading-system
mvn clean spring-boot:run

Wait for:
  ✓ Started TradingApplication in X.XXX seconds
  ✓ Tomcat started on port(s): 8080

✅ BACKEND READY (http://localhost:8080)


STEP 3️⃣: START FRONTEND (Terminal 3)
────────────────────────────────────

PowerShell Window 3:

cd C:\Users\t_kevinpin\IdeaProjects\spring-boot-trading-system\trading-ui
npm install
npm run dev

Wait for:
  ✓ VITE v5.0.8  ready in XXX ms
  ✓ Local:   http://localhost:3000/

✅ FRONTEND READY (http://localhost:3000)


STEP 4️⃣: OPEN IN BROWSER
─────────────────────────

PowerShell:
Start-Process http://localhost:3000

OR visit manually:
http://localhost:3000

✅ LOGIN PAGE APPEARS!


════════════════════════════════════════════════════════════════════════════════
                         📝 REGISTER & TEST
════════════════════════════════════════════════════════════════════════════════

1. Click "Register here"

2. Fill form:
   Full Name: Test User
   Email: test@example.com
   Password: Test@123
   Confirm: Test@123

3. Click "Register"

4. ✅ See Dashboard!

5. Place order:
   Symbol: AAPL
   Side: BUY
   Type: LIMIT
   Price: 150.00
   Quantity: 10
   Click "Place Order"

6. ✅ Order in table!


════════════════════════════════════════════════════════════════════════════════
                         🛑 HOW TO STOP
════════════════════════════════════════════════════════════════════════════════

Terminal 1: docker-compose down
Terminal 2: Ctrl+C
Terminal 3: Ctrl+C


════════════════════════════════════════════════════════════════════════════════
                      🔧 TROUBLESHOOTING
════════════════════════════════════════════════════════════════════════════════

❌ Port 3000 already in use
✅ Get-Process | Where-Object {$_.ProcessName -like "*node*"} | Stop-Process -Force

❌ Port 8080 already in use
✅ netstat -ano | findstr :8080
   taskkill /PID <PID> /F

❌ Docker not found
✅ Install Docker Desktop: https://www.docker.com/products/docker-desktop

❌ npm install fails
✅ npm cache clean --force
   npm install --legacy-peer-deps

❌ Backend won't start
✅ Check: docker ps
   Must see trading_postgres and trading_redis running


════════════════════════════════════════════════════════════════════════════════
                        📚 DOCUMENTATION
════════════════════════════════════════════════════════════════════════════════

HOW_TO_RUN.md              ← Detailed guide
QUICK_START.txt            ← Visual reference
INTEGRATION_GUIDE.md       ← Setup & API
REACT_UI_COMPLETE.md       ← Features
trading-ui/README.md       ← Frontend docs


════════════════════════════════════════════════════════════════════════════════
                    🎉 YOU'RE ALL SET! HAPPY TRADING!
════════════════════════════════════════════════════════════════════════════════

System includes:
  ✅ React 18 frontend (localhost:3000)
  ✅ Spring Boot backend (localhost:8080)
  ✅ PostgreSQL (localhost:5432)
  ✅ Redis (localhost:6379)
  ✅ Real-time WebSocket
  ✅ JWT auth
  ✅ Order matching

KEEP ALL 3 TERMINALS OPEN WHILE RUNNING!

═════════════════════════════════════════════════════════════════════════════════

