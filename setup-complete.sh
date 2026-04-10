#!/bin/bash
# Complete setup and run script for Trading System in WSL

set -e  # Exit on any error

echo "🚀 Trading System - Complete Setup"
echo "===================================="
echo ""

PROJECT_PATH="/mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system"
BACKEND_PATH="$PROJECT_PATH/spring-boot-trading-backend"
FRONTEND_PATH="$PROJECT_PATH/trading-ui"

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Step 1: Navigate to project
echo -e "${YELLOW}Step 1: Navigating to project...${NC}"
cd "$BACKEND_PATH"
echo -e "${GREEN}✓ In backend directory${NC}"
echo ""

# Step 2: Start Docker containers
echo -e "${YELLOW}Step 2: Starting PostgreSQL Docker container...${NC}"
docker-compose down 2>/dev/null || true
docker-compose up -d
echo -e "${GREEN}✓ PostgreSQL starting...${NC}"
sleep 5
docker-compose logs postgres | tail -5
echo ""

# Step 3: Verify database connection
echo -e "${YELLOW}Step 3: Verifying database connection...${NC}"
COUNTER=0
while [ $COUNTER -lt 30 ]; do
    if docker exec spring-boot-trading-system-postgres-1 pg_isready -U trading >/dev/null 2>&1; then
        echo -e "${GREEN}✓ Database is ready${NC}"
        break
    fi
    COUNTER=$((COUNTER + 1))
    sleep 1
done

if [ $COUNTER -eq 30 ]; then
    echo -e "${RED}✗ Database failed to start${NC}"
    exit 1
fi
echo ""

# Step 4: Build backend
echo -e "${YELLOW}Step 4: Building backend (this may take a minute)...${NC}"
mvn clean package -DskipTests -q
echo -e "${GREEN}✓ Backend built successfully${NC}"
echo ""

# Step 5: Show how to run
echo -e "${GREEN}===================================${NC}"
echo -e "${GREEN}✓ Setup complete!${NC}"
echo -e "${GREEN}===================================${NC}"
echo ""

echo -e "${YELLOW}To run the backend, execute:${NC}"
echo -e "${GREEN}cd $BACKEND_PATH${NC}"
echo -e "${GREEN}mvn spring-boot:run${NC}"
echo ""

echo -e "${YELLOW}In another terminal, run the frontend:${NC}"
echo -e "${GREEN}cd $FRONTEND_PATH${NC}"
echo -e "${GREEN}npm install && npm run dev${NC}"
echo ""

echo -e "${YELLOW}Then access:${NC}"
echo -e "${GREEN}Backend API: http://localhost:8080${NC}"
echo -e "${GREEN}Swagger UI:  http://localhost:8080/swagger-ui.html${NC}"
echo -e "${GREEN}Frontend:    http://localhost:3000${NC}"
echo ""

echo -e "${YELLOW}To test the API immediately:${NC}"
echo -e "${GREEN}curl http://localhost:8080/api/market/last-price?symbol=AAPL${NC}"
echo ""

echo -e "${YELLOW}To stop the database:${NC}"
echo -e "${GREEN}docker-compose down${NC}"

