#!/bin/bash

# Trading System - Quick Start Script
# Starts both backend and frontend concurrently

set -e

PROJECT_ROOT=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)

echo "🚀 Trading System - Quick Start"
echo "=================================="
echo ""

# Check dependencies
echo "📦 Checking prerequisites..."
command -v java >/dev/null 2>&1 || { echo "❌ Java not found. Please install JDK 17+"; exit 1; }
command -v node >/dev/null 2>&1 || { echo "❌ Node.js not found. Please install Node.js 18+"; exit 1; }
command -v mvn >/dev/null 2>&1 || { echo "❌ Maven not found. Please install Maven"; exit 1; }

echo "✅ Java: $(java -version 2>&1 | head -1)"
echo "✅ Node: $(node --version)"
echo "✅ Maven: $(mvn --version | head -1)"
echo ""

# Install frontend dependencies
echo "📥 Installing frontend dependencies..."
cd "$PROJECT_ROOT/trading-ui"
npm install --legacy-peer-deps 2>/dev/null || npm install

echo ""
echo "✅ Setup complete!"
echo ""
echo "📝 To start the application:"
echo ""
echo "  Terminal 1 (Backend):"
echo "    cd $PROJECT_ROOT"
echo "    mvn spring-boot:run"
echo ""
echo "  Terminal 2 (Frontend):"
echo "    cd $PROJECT_ROOT/trading-ui"
echo "    npm run dev"
echo ""
echo "🌐 Frontend: http://localhost:3000"
echo "🔧 Backend: http://localhost:8080"
echo ""

