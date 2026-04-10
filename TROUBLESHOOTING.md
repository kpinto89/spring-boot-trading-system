# Troubleshooting Guide

## Common Issues and Solutions

### 1. **"docker-compose: command not found"**

**Solution:**
```bash
# Install Docker Compose in WSL
sudo apt update
sudo apt install -y docker-compose

# Or use the newer docker compose (with space)
docker compose up -d  # instead of docker-compose up -d
```

### 2. **"Cannot connect to Docker daemon"**

**Solution:**
- Ensure Docker Desktop is running on Windows
- Check if WSL Docker integration is enabled:
  ```bash
  docker ps
  ```
- If not working, export the DOCKER_HOST:
  ```bash
  export DOCKER_HOST=unix:///var/run/docker.sock
  ```

### 3. **"FATAL: invalid value for parameter 'TimeZone': 'Asia/Calcutta'"**

**Solution:**
The application.yml has been updated with `?serverTimezone=UTC` parameter. If you still see this:

Edit `/mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system/spring-boot-trading-backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/trading?serverTimezone=UTC
```

### 4. **Port 8080 already in use**

**Solution:**
```bash
# Find what's using port 8080
sudo lsof -i :8080
kill -9 <PID>

# Or use environment variable to change port
export SERVER_PORT=8081
mvn spring-boot:run

# Or add to application.yml
server:
  port: 8081
```

### 5. **"Name for argument of type [java.lang.String] not specified"**

**Solution:**
This is a Spring parameter name resolution issue. The pom.xml should have:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <parameters>true</parameters>
    </configuration>
</plugin>
```

Then rebuild:
```bash
mvn clean compile
```

### 6. **Frontend shows "Cannot find module '@/components/...'"**

**Solution:**
The vite.config.ts needs alias configuration. Check:

```typescript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
})
```

### 7. **Frontend shows "inotify error: unsupported platform"**

**Solution:**
This happens when npm is run from Windows (not WSL). Always run npm commands from WSL:

```bash
# DON'T do this from Windows PowerShell
# npm install

# DO this from WSL terminal
cd /mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system/trading-ui
npm install
```

### 8. **"No such file or directory" errors in WSL**

**Solution:**
Make sure you're using correct WSL paths:

```bash
# Correct WSL path
/mnt/c/Users/t_kevinpin/IdeaProjects/spring-boot-trading-system

# NOT Windows path
C:\Users\t_kevinpin\IdeaProjects\spring-boot-trading-system
```

### 9. **CORS error: "No 'Access-Control-Allow-Origin' header"**

**Solution:**
CORS is configured in SecurityConfig.java. Ensure:
- Backend is running on http://localhost:8080
- Frontend is running on http://localhost:3000
- CORS configuration allows localhost:3000

If changing ports, update SecurityConfig.java:

```java
configuration.setAllowedOrigins(List.of("http://localhost:3001")); // if frontend on 3001
```

### 10. **"Tests run: 3, Failures: 0, Errors: 2"**

**Solution:**
Tests may fail due to database issues. Skip tests during build:

```bash
mvn clean package -DskipTests
```

To run tests properly:
```bash
# Make sure database is running
docker-compose up -d

# Then run tests
mvn test
```

### 11. **"Unsupported platform for sockjs-client"**

**Solution:**
This is a Windows vs WSL issue with the sockjs-client module. Use WSL for npm:

```bash
# In WSL
npm install --force
npm run dev
```

### 12. **Cannot POST to /api/market/last-price - Bad Request**

**Solution:**
Ensure parameter is properly passed:

```bash
# Correct
curl 'http://localhost:8080/api/market/last-price?symbol=AAPL'

# Also works
curl 'http://localhost:8080/api/market/last-price' -d 'symbol=AAPL'

# Check backend is receiving:
# Turn on DEBUG logging in application.yml:
logging:
  level:
    root: DEBUG
```

### 13. **"Market Prices is showing empty"**

**Solution:**
No trades have been executed for that symbol. Check demo data:

```bash
# Access database directly
docker exec -it spring-boot-trading-system-postgres-1 psql -U trading -d trading

# In psql shell
SELECT symbol, price FROM trades ORDER BY executed_at DESC LIMIT 10;
```

If no data, check that V2__demo_data.sql migration ran:

```sql
SELECT * FROM trades WHERE symbol = 'AAPL';
```

### 14. **"Connection refused" when connecting to database**

**Solution:**
```bash
# Check Docker container is running
docker ps | grep postgres

# If not, start it
cd /path/to/spring-boot-trading-backend
docker-compose up -d

# Check logs
docker-compose logs postgres
```

### 15. **"Could not resolve placeholder" in application properties**

**Solution:**
Ensure environment variables are set:

```bash
# Set JWT secret
export JWT_SECRET="your-secret-at-least-32-chars"

# Then run
mvn spring-boot:run
```

---

## Quick Diagnostic Commands

```bash
# Check if services are running
docker ps
docker-compose ps

# Check logs
docker-compose logs postgres
docker-compose logs -f

# Check database connectivity
docker exec spring-boot-trading-system-postgres-1 pg_isready -U trading

# Check if backend is responding
curl http://localhost:8080/actuator/health

# Check if frontend is responding
curl http://localhost:3000

# Check port usage
sudo lsof -i :8080
sudo lsof -i :3000

# Test API endpoint
curl -v http://localhost:8080/api/market/last-price?symbol=AAPL

# Check file permissions
ls -la /path/to/file
chmod 755 /path/to/file
```

---

## Performance Optimization

If experiencing slow builds:

```bash
# Skip tests
mvn clean package -DskipTests

# Skip javadoc
mvn clean package -DskipTests -Dmaven.javadoc.skip=true

# Use parallel compilation (requires Maven 3.1.1+)
mvn clean package -DskipTests -T 1C
```

---

## Reset Everything

If something is broken, do a full reset:

```bash
# Stop and remove containers
docker-compose down -v

# Clean Maven
mvn clean

# Remove node_modules
rm -rf trading-ui/node_modules

# Remove build artifacts
rm -rf target/

# Start fresh
docker-compose up -d
mvn clean package -DskipTests
```

