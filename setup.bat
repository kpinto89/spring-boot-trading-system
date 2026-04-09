@echo off
REM Trading System - Quick Start Script (Windows)
REM Checks prerequisites and guides setup

setlocal enabledelayedexpansion

echo.
echo ============================================
echo Trading System - Quick Start (Windows)
echo ============================================
echo.

REM Check Java
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Java not found. Please install JDK 17+
    exit /b 1
) else (
    for /f "tokens=*" %%i in ('java -version 2^>^&1 ^| findstr /R "version"') do set JAVA_VERSION=%%i
    echo ✅ !JAVA_VERSION!
)

REM Check Node
node --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Node.js not found. Please install Node.js 18+
    exit /b 1
) else (
    for /f "tokens=*" %%i in ('node --version') do echo ✅ Node: %%i
)

REM Check Maven
mvn --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Maven not found. Please install Maven or use IDE
    exit /b 1
) else (
    for /f "tokens=3" %%i in ('mvn --version ^| findstr /R "Apache"') do echo ✅ Maven: %%i
)

echo.
echo 📥 Installing frontend dependencies...
cd trading-ui
call npm install --legacy-peer-deps 2>nul || call npm install
if errorlevel 1 (
    echo ❌ npm install failed
    exit /b 1
)

echo.
echo ✅ Setup complete!
echo.
echo 📝 To start the application:
echo.
echo   PowerShell Window 1 (Backend):
echo     cd %CD%\..
echo     mvn spring-boot:run
echo.
echo   PowerShell Window 2 (Frontend):
echo     cd %CD%
echo     npm run dev
echo.
echo 🌐 Frontend: http://localhost:3000
echo 🔧 Backend: http://localhost:8080
echo.
pause

