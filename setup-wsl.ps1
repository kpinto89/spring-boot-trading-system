#!/usr/bin/env pwsh
# Run Trading System in WSL
# This script helps you run the trading system from Windows PowerShell

$projectPath = "/mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system"

Write-Host "🚀 Starting Spring Boot Trading System..." -ForegroundColor Green
Write-Host ""

# Check if WSL is available
try {
    $wslCheck = & wsl --list --quiet 2>$null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ WSL not found or not properly configured" -ForegroundColor Red
        Write-Host "Please ensure WSL2 is installed and running" -ForegroundColor Yellow
        exit 1
    }
} catch {
    Write-Host "❌ Error checking WSL: $_" -ForegroundColor Red
    exit 1
}

Write-Host "✅ WSL is available" -ForegroundColor Green
Write-Host ""

# Function to run commands in WSL
function Run-InWSL {
    param([string]$Command)
    wsl -e bash -c $Command
}

# Step 1: Start Docker containers
Write-Host "📦 Step 1: Starting PostgreSQL Docker container..." -ForegroundColor Cyan
Run-InWSL "cd $projectPath/spring-boot-trading-backend && docker-compose up -d"

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ PostgreSQL started successfully" -ForegroundColor Green
    Start-Sleep -Seconds 3
} else {
    Write-Host "❌ Failed to start PostgreSQL" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Step 2: Build the backend
Write-Host "🔨 Step 2: Building backend..." -ForegroundColor Cyan
Run-InWSL "cd $projectPath/spring-boot-trading-backend && mvn clean package -DskipTests -q"

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Backend built successfully" -ForegroundColor Green
} else {
    Write-Host "❌ Build failed" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "🎉 Setup complete!" -ForegroundColor Green
Write-Host ""
Write-Host "To run the backend, execute:" -ForegroundColor Yellow
Write-Host "  wsl -e bash -c 'cd $projectPath/spring-boot-trading-backend && mvn spring-boot:run'" -ForegroundColor White
Write-Host ""
Write-Host "To run the frontend, execute:" -ForegroundColor Yellow
Write-Host "  wsl -e bash -c 'cd $projectPath/trading-ui && npm run dev'" -ForegroundColor White
Write-Host ""
Write-Host "Then open:" -ForegroundColor Yellow
Write-Host "  Backend:  http://localhost:8080 (Swagger UI at /swagger-ui.html)" -ForegroundColor White
Write-Host "  Frontend: http://localhost:3000" -ForegroundColor White

