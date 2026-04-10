#!/bin/bash
# Test script for Trading System APIs

BASE_URL="http://localhost:8080"
SYMBOL="AAPL"

echo "🧪 Testing Trading System APIs"
echo ""

# Test 1: Get market price (no auth required)
echo "Test 1: Get market price for $SYMBOL"
echo "----------------------------------------"
curl -s -X GET "$BASE_URL/api/market/last-price?symbol=$SYMBOL" \
  -H "Accept: application/json" \
  -H "Content-Type: application/json"
echo ""
echo ""

# Test 2: Register a new user
echo "Test 2: Register a new user"
echo "----------------------------------------"
EMAIL="testuser$(date +%s)@example.com"
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d "{
    \"email\": \"$EMAIL\",
    \"password\": \"Passw0rd!\",
    \"fullName\": \"Test User\"
  }")
echo "$REGISTER_RESPONSE" | jq . 2>/dev/null || echo "$REGISTER_RESPONSE"
echo ""

# Test 3: Login
echo "Test 3: Login"
echo "----------------------------------------"
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d "{
    \"email\": \"$EMAIL\",
    \"password\": \"Passw0rd!\"
  }")
echo "$LOGIN_RESPONSE" | jq . 2>/dev/null || echo "$LOGIN_RESPONSE"
TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token' 2>/dev/null)
echo ""

if [ -n "$TOKEN" ] && [ "$TOKEN" != "null" ]; then
  echo "✅ Got token: ${TOKEN:0:20}..."
  echo ""

  # Test 4: Place an order
  echo "Test 4: Place a BUY order"
  echo "----------------------------------------"
  ORDER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/orders" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d "{
      \"symbol\": \"$SYMBOL\",
      \"side\": \"BUY\",
      \"type\": \"LIMIT\",
      \"price\": 150.00,
      \"quantity\": 10
    }")
  echo "$ORDER_RESPONSE" | jq . 2>/dev/null || echo "$ORDER_RESPONSE"
  echo ""

  # Test 5: Get my orders
  echo "Test 5: Get my orders"
  echo "----------------------------------------"
  ORDERS_RESPONSE=$(curl -s -X GET "$BASE_URL/api/orders/me" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json")
  echo "$ORDERS_RESPONSE" | jq . 2>/dev/null || echo "$ORDERS_RESPONSE"
else
  echo "❌ Failed to get token, skipping authenticated tests"
fi

echo ""
echo "✅ Tests completed"

