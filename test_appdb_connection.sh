#!/bin/bash

# Script to test connection to appdb database
DB_HOST="139.84.210.248.vultrusercontent.com"
DB_PORT="5432"
DB_NAME="appdb"
DB_USER="postgres"
DB_PASSWORD="Pihu@1234"

echo "=========================================="
echo "Testing Connection to appdb Database"
echo "=========================================="
echo "Host: $DB_HOST"
echo "Port: $DB_PORT"
echo "Database: $DB_NAME"
echo "User: $DB_USER"
echo ""

# Test connection
echo "Testing connection..."
PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" <<EOF
\c $DB_NAME
SELECT current_database(), current_user;
\dt
\l
\q
EOF

EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    echo ""
    echo "✅ Connection successful!"
    echo ""
    echo "Checking user permissions..."
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "
        SELECT 
            datname as database,
            datacl as access_privileges
        FROM pg_database 
        WHERE datname = '$DB_NAME';
    "
    
    echo ""
    echo "Checking if user can create tables..."
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "
        SELECT 
            has_database_privilege('$DB_USER', '$DB_NAME', 'CREATE') as can_create,
            has_database_privilege('$DB_USER', '$DB_NAME', 'CONNECT') as can_connect;
    "
else
    echo ""
    echo "❌ Connection failed!"
    echo "Error code: $EXIT_CODE"
    echo ""
    echo "Possible issues:"
    echo "1. User '$DB_USER' doesn't have CONNECT privilege on database '$DB_NAME'"
    echo "2. Password is incorrect"
    echo "3. Network/firewall issue"
    echo ""
    echo "Try granting privileges:"
    echo "   psql -h $DB_HOST -p $DB_PORT -U postgres -d postgres"
    echo "   GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;"
    echo "   \\c $DB_NAME"
    echo "   GRANT ALL ON SCHEMA public TO $DB_USER;"
fi

echo ""
echo "=========================================="
