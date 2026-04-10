#!/bin/bash
# Verification Checklist for AAPL Market Price Setup

echo "🔍 Trading System - Verification Checklist"
echo "=========================================="
echo ""

PROJECT_PATH="/mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system"

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

PASSED=0
FAILED=0

# Helper function
check_item() {
    local description=$1
    local command=$2

    echo -n "Checking: $description... "

    if eval "$command" > /dev/null 2>&1; then
        echo -e "${GREEN}✓${NC}"
        ((PASSED++))
    else
        echo -e "${RED}✗${NC}"
        ((FAILED++))
    fi
}

# Verification checks
echo -e "${YELLOW}Backend Checks:${NC}"
check_item "pom.xml exists" "[ -f '$PROJECT_PATH/spring-boot-trading-backend/pom.xml' ]"
check_item "application.yml exists" "[ -f '$PROJECT_PATH/spring-boot-trading-backend/src/main/resources/application.yml' ]"
check_item "V1__init_schema.sql exists" "[ -f '$PROJECT_PATH/spring-boot-trading-backend/src/main/resources/db/migration/V1__init_schema.sql' ]"
check_item "V2__demo_data.sql exists" "[ -f '$PROJECT_PATH/spring-boot-trading-backend/src/main/resources/db/migration/V2__demo_data.sql' ]"
check_item "MarketController.java exists" "[ -f '$PROJECT_PATH/spring-boot-trading-backend/src/main/java/com/example/trading/api/controller/MarketController.java' ]"
check_item "docker-compose.yml exists" "[ -f '$PROJECT_PATH/spring-boot-trading-backend/docker-compose.yml' ]"

echo ""
echo -e "${YELLOW}Frontend Checks:${NC}"
check_item "package.json exists" "[ -f '$PROJECT_PATH/trading-ui/package.json' ]"
check_item "market.ts exists" "[ -f '$PROJECT_PATH/trading-ui/src/services/market.ts' ]"
check_item "marketStore.ts exists" "[ -f '$PROJECT_PATH/trading-ui/src/stores/marketStore.ts' ]"
check_item "Dashboard.tsx exists" "[ -f '$PROJECT_PATH/trading-ui/src/components/Dashboard.tsx' ]"

echo ""
echo -e "${YELLOW}Documentation Checks:${NC}"
check_item "QUICK_START_MARKET_PRICE.md exists" "[ -f '$PROJECT_PATH/QUICK_START_MARKET_PRICE.md' ]"
check_item "WSL_RUN_GUIDE.md exists" "[ -f '$PROJECT_PATH/WSL_RUN_GUIDE.md' ]"
check_item "TROUBLESHOOTING.md exists" "[ -f '$PROJECT_PATH/TROUBLESHOOTING.md' ]"
check_item "MARKET_PRICE_GUIDE.md exists" "[ -f '$PROJECT_PATH/MARKET_PRICE_GUIDE.md' ]"

echo ""
echo -e "${YELLOW}File Content Checks:${NC}"

# Check for UTC timezone in application.yml
if grep -q "serverTimezone=UTC" "$PROJECT_PATH/spring-boot-trading-backend/src/main/resources/application.yml"; then
    echo -e "Checking: PostgreSQL timezone fix... ${GREEN}✓${NC}"
    ((PASSED++))
else
    echo -e "Checking: PostgreSQL timezone fix... ${RED}✗${NC}"
    ((FAILED++))
fi

# Check for demo data migration
if grep -q "INSERT INTO trades" "$PROJECT_PATH/spring-boot-trading-backend/src/main/resources/db/migration/V2__demo_data.sql"; then
    echo -e "Checking: Demo trades data... ${GREEN}✓${NC}"
    ((PASSED++))
else
    echo -e "Checking: Demo trades data... ${RED}✗${NC}"
    ((FAILED++))
fi

# Check for parameters flag in pom.xml
if grep -q "<parameters>true</parameters>" "$PROJECT_PATH/spring-boot-trading-backend/pom.xml"; then
    echo -e "Checking: Maven parameters flag... ${GREEN}✓${NC}"
    ((PASSED++))
else
    echo -e "Checking: Maven parameters flag... ${RED}✗${NC}"
    ((FAILED++))
fi

# Check for lastPrice method
if grep -q "lastPrice.*AAPL" "$PROJECT_PATH/trading-ui/src/services/market.ts" || grep -q "lastPrice" "$PROJECT_PATH/trading-ui/src/services/market.ts"; then
    echo -e "Checking: Frontend market service... ${GREEN}✓${NC}"
    ((PASSED++))
else
    echo -e "Checking: Frontend market service... ${RED}✗${NC}"
    ((FAILED++))
fi

echo ""
echo "=========================================="
echo -e "Results: ${GREEN}$PASSED passed${NC}, ${RED}$FAILED failed${NC}"
echo "=========================================="
echo ""

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}✓ All checks passed!${NC}"
    echo ""
    echo "Ready to run:"
    echo "  1. cd $PROJECT_PATH/spring-boot-trading-backend"
    echo "  2. docker-compose up -d"
    echo "  3. mvn spring-boot:run"
    echo "  4. curl http://localhost:8080/api/market/last-price?symbol=AAPL"
    exit 0
else
    echo -e "${RED}✗ Some checks failed. Review the output above.${NC}"
    exit 1
fi

