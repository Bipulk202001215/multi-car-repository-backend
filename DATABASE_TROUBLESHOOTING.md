# Database Connection Troubleshooting Guide

## Error: "role 'postgres' does not exist"

This error occurs when PostgreSQL doesn't have a user/role named "postgres" that your application is trying to connect with.

## Solutions

### Solution 1: Create the postgres role (Recommended)

If you have superuser access to your PostgreSQL database:

1. **Connect to PostgreSQL as a superuser:**
   ```bash
   psql -U <superuser> -h 139.84.210.248.vultrusercontent.com -p 5432 -d postgres
   ```
   (Replace `<superuser>` with an existing superuser account)

2. **Run the create_postgres_role.sql script:**
   ```bash
   psql -U <superuser> -h 139.84.210.248.vultrusercontent.com -p 5432 -d postgres -f create_postgres_role.sql
   ```

   OR manually run the SQL commands from `create_postgres_role.sql` in your psql session.

### Solution 2: Use an existing database user

If you don't have superuser access or prefer to use an existing user:

1. **First, check what users exist:**
   ```bash
   psql -U <any_existing_user> -h 139.84.210.248.vultrusercontent.com -p 5432 -d postgres -f check_database_roles.sql
   ```

2. **Update application.properties:**
   Edit `src/main/resources/application.properties` and change:
   ```properties
   spring.datasource.username=postgres
   ```
   to an existing username, for example:
   ```properties
   spring.datasource.username=your_existing_user
   ```

### Solution 3: Use local database (for development)

If you want to use a local PostgreSQL database instead:

1. **Make sure PostgreSQL is running locally:**
   ```bash
   # macOS
   brew services start postgresql
   
   # Linux
   sudo systemctl start postgresql
   ```

2. **Create the database and user:**
   ```bash
   psql -U postgres
   CREATE DATABASE websocket_demo;
   CREATE USER postgres WITH PASSWORD '12345';
   GRANT ALL PRIVILEGES ON DATABASE websocket_demo TO postgres;
   ```

3. **Update application.properties:**
   Comment out the cloud configuration and uncomment the local configuration:
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

## Verification

After applying any solution, verify the connection:

1. **Test the connection:**
   ```bash
   psql -U postgres -h 139.84.210.248.vultrusercontent.com -p 5432 -d appdb
   ```

2. **Run your Spring Boot application:**
   ```bash
   ./mvnw spring-boot:run
   ```

## Common Issues

- **"password authentication failed"**: Check that the password in `application.properties` matches the database user's password.
- **"database does not exist"**: Make sure the database `appdb` exists. Create it with: `CREATE DATABASE appdb;`
- **Connection timeout**: Check firewall settings and ensure the database server is accessible from your network.
