# SSH Tunnel Setup for Database Connection

## Problem
The PostgreSQL database is only accessible through an SSH tunnel (as configured in DBeaver), but Spring Boot is trying to connect directly to the hostname, which fails.

## Solution: Create SSH Tunnel

### Step 1: Update SSH Tunnel Script

Edit `create_ssh_tunnel.sh` and update the SSH credentials:

```bash
SSH_HOST="139.84.210.248.vultrusercontent.com"  # Your SSH server
SSH_USER="your_ssh_username"                     # Your SSH username (e.g., root, ubuntu, etc.)
SSH_PORT="22"                                    # SSH port (usually 22)
SSH_KEY_PATH="/path/to/your/private/key"         # Optional: path to SSH private key
```

**Important**: If you don't have an SSH key, leave `SSH_KEY_PATH` empty and you'll be prompted for a password.

### Step 2: Start SSH Tunnel

```bash
# Make script executable (if not already)
chmod +x create_ssh_tunnel.sh

# Start the tunnel
./create_ssh_tunnel.sh
```

The tunnel will forward:
- **Local port 5433** → **Remote database (localhost:5432 on server)**

**Keep this terminal window open** while running your application!

### Step 3: Update Application Configuration

You have two options:

#### Option A: Use SSH Tunnel Profile (Recommended)

1. The tunnel is already running (from Step 2)
2. Update `application.properties` to use localhost:5433:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/appdb
```

Or use the profile:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=ssh-tunnel
```

#### Option B: Temporarily Modify application.properties

Update the database URL in `src/main/resources/application.properties`:

```properties
# Change from:
spring.datasource.url=jdbc:postgresql://139.84.210.248.vultrusercontent.com:5432/appdb

# To:
spring.datasource.url=jdbc:postgresql://localhost:5433/appdb
```

### Step 4: Run Your Application

```bash
./mvnw spring-boot:run
```

## Complete Workflow

1. **Terminal 1**: Start SSH tunnel
   ```bash
   ./create_ssh_tunnel.sh
   ```

2. **Terminal 2**: Run Spring Boot application
   ```bash
   ./mvnw spring-boot:run
   ```

3. When done, press `Ctrl+C` in Terminal 1 to close the tunnel.

## Verify SSH Tunnel is Working

Test the tunnel connection:

```bash
# Test connection through tunnel
PGPASSWORD="Pihu@1234" psql -h localhost -p 5433 -U postgres -d appdb -c "SELECT current_database();"
```

If this works, your Spring Boot application will also work!

## Troubleshooting

### Port Already in Use
If port 5433 is already in use:
1. Change `LOCAL_PORT` in `create_ssh_tunnel.sh` to a different port (e.g., 5434)
2. Update `application.properties` to use the new port

### SSH Connection Fails
- Verify SSH credentials (username, host, port)
- Check if SSH key has correct permissions: `chmod 600 /path/to/key`
- Try password authentication by leaving `SSH_KEY_PATH` empty

### Database Connection Still Fails
- Make sure SSH tunnel is running (check with `lsof -i :5433`)
- Verify database credentials in `application.properties`
- Check that database `appdb` exists on the remote server

## Alternative: Programmatic SSH Tunnel (Advanced)

If you want Spring Boot to automatically create the SSH tunnel, you would need to:
1. Add JSch dependency to `pom.xml`
2. Create a `@PostConstruct` bean that establishes the tunnel
3. This is more complex but doesn't require a separate terminal

For now, the script-based approach is simpler and more reliable.
