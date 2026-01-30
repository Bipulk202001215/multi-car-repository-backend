# Quick Fix for "role postgres does not exist" Error

## Immediate Solutions

### Option 1: Find the Correct Database Username (Recommended)

Run the test script to find which username works:

```bash
chmod +x test_db_connection.sh
./test_db_connection.sh
```

This will test common PostgreSQL usernames and tell you which one works.

### Option 2: Check Database Users Manually

If you have access to the database server or a database client:

```bash
# Try connecting with different common usernames
psql -h 139.84.210.248.vultrusercontent.com -p 5432 -U postgres -d appdb
psql -h 139.84.210.248.vultrusercontent.com -p 5432 -U admin -d appdb
psql -h 139.84.210.248.vultrusercontent.com -p 5432 -U appdb -d appdb
```

Once you find the correct username, update `application.properties`:

```properties
spring.datasource.username=<correct_username>
```

### Option 3: Use Local Database (For Development)

If you want to develop locally, switch to local database:

1. **Start local PostgreSQL** (if not running):
   ```bash
   # macOS
   brew services start postgresql
   
   # Or using Docker
   docker run -d --name postgres-local -e POSTGRES_PASSWORD=12345 -p 5432:5432 postgres:13
   ```

2. **Create database and user**:
   ```bash
   psql -U postgres
   ```
   Then run:
   ```sql
   CREATE DATABASE websocket_demo;
   CREATE USER postgres WITH PASSWORD '12345';
   GRANT ALL PRIVILEGES ON DATABASE websocket_demo TO postgres;
   \q
   ```

3. **Update application.properties** - Comment out cloud config and uncomment local:
   ```properties
   # Database Configuration cloud
   #spring.datasource.url=jdbc:postgresql://139.84.210.248.vultrusercontent.com:5432/appdb
   #spring.datasource.username=postgres
   #spring.datasource.password=Pihu@1234
   #spring.datasource.driver-class-name=org.postgresql.Driver

   # Database Configuration local
   spring.datasource.url=jdbc:postgresql://localhost:5432/websocket_demo
   spring.datasource.username=postgres
   spring.datasource.password=12345
   spring.datasource.driver-class-name=org.postgresql.Driver
   ```

### Option 4: Create Postgres Role on Remote Database

If you have superuser access to the remote database:

1. Connect as a superuser (you'll need to know an existing superuser):
   ```bash
   psql -h 139.84.210.248.vultrusercontent.com -p 5432 -U <superuser> -d postgres
   ```

2. Run the create_postgres_role.sql script:
   ```bash
   psql -h 139.84.210.248.vultrusercontent.com -p 5432 -U <superuser> -d postgres -f create_postgres_role.sql
   ```

## After Fixing

Restart your Spring Boot application. The error should be resolved.
