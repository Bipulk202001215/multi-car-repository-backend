# Fix for "database appdb does not exist" Error

## Issue
Spring Boot application is throwing error: `FATAL: database "appdb" does not exist` even though the database exists on the remote server.

## Root Causes
1. **DNS Resolution Issue**: The hostname `139.84.210.248.vultrusercontent.com` might be resolving to `127.0.0.1` (localhost) instead of the actual server IP
2. **Cached Build**: Old compiled classes might have cached configuration
3. **Connection Pool Issues**: Missing connection pool configuration
4. **Permissions**: User might not have proper permissions on the database

## Solutions Applied

### 1. Added Connection Pool Configuration
Added HikariCP connection pool settings to `application.properties` to improve connection handling:
- Connection timeout: 30 seconds
- Maximum pool size: 10
- Minimum idle connections: 5
- Connection leak detection enabled

### 2. Cleaned Maven Build
Removed cached build artifacts that might contain old configuration.

## Next Steps to Fix

### Option 1: Verify Hostname Resolution
Check if the hostname resolves correctly:

```bash
nslookup 139.84.210.248.vultrusercontent.com
# or
ping 139.84.210.248.vultrusercontent.com
```

If it resolves to `127.0.0.1`, you have two options:
- **Use IP address directly** in application.properties (if you know the actual IP)
- **Fix DNS/hosts file** if this is a local development issue

### Option 2: Grant Database Permissions
If you can connect to the database server, ensure the `postgres` user has all necessary permissions:

```bash
# Connect to PostgreSQL
psql -h 139.84.210.248.vultrusercontent.com -p 5432 -U postgres -d postgres

# Then run:
\c appdb
GRANT ALL PRIVILEGES ON DATABASE appdb TO postgres;
GRANT ALL ON SCHEMA public TO postgres;
ALTER SCHEMA public OWNER TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO postgres;
```

Or use the provided script:
```bash
psql -h 139.84.210.248.vultrusercontent.com -p 5432 -U postgres -d postgres -f grant_appdb_permissions.sql
```

### Option 3: Rebuild and Run
After making sure permissions are correct:

```bash
# Clean and rebuild
./mvnw clean compile

# Run the application
./mvnw spring-boot:run
```

### Option 4: Test Connection Manually
Test if you can connect using the exact same credentials:

```bash
PGPASSWORD="Pihu@1234" psql -h 139.84.210.248.vultrusercontent.com -p 5432 -U postgres -d appdb -c "SELECT current_database();"
```

If this works but Spring Boot doesn't, the issue is likely:
- Cached configuration (fixed by clean build)
- Connection pool settings (already added)
- Application needs restart

## Current Configuration
Your `application.properties` now has:
```properties
spring.datasource.url=jdbc:postgresql://139.84.210.248.vultrusercontent.com:5432/appdb
spring.datasource.username=postgres
spring.datasource.password=Pihu@1234
spring.datasource.driver-class-name=org.postgresql.Driver

# Connection Pool Configuration (HikariCP)
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.leak-detection-threshold=60000
```

## Verification
After applying fixes, the application should start successfully. Check the logs for:
- ✅ "HikariPool-1 - Starting..."
- ✅ "HikariPool-1 - Start completed"
- ✅ No "database does not exist" errors
