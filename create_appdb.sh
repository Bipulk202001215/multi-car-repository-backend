#!/bin/bash

# Script to create appdb database on remote PostgreSQL server
# This script will create the database if it doesn't exist

DB_HOST="139.84.210.248.vultrusercontent.com"
DB_PORT="5432"
DB_NAME="appdb"
DB_USER="postgres"
DB_PASSWORD="Pihu@1234"

echo "=========================================="
echo "Creating appdb Database"
echo "=========================================="
echo "Host: $DB_HOST"
echo "Port: $DB_PORT"
echo "Database: $DB_NAME"
echo "User: $DB_USER"
echo ""

# First, try to connect to postgres database and create appdb
echo "Attempting to create database '$DB_NAME'..."

# Method 1: Using psql with command
PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres <<EOF
-- Check if database exists, if not create it
SELECT 'CREATE DATABASE $DB_NAME'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$DB_NAME')\gexec

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;

-- Verify
\c $DB_NAME
SELECT current_database();
\q
EOF

EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    echo ""
    echo "✅ Database '$DB_NAME' created successfully!"
    echo ""
    echo "Testing connection to the new database..."
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "SELECT current_database(), current_user;" 2>&1
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "✅ Connection test successful!"
        echo ""
        echo "You can now start your Spring Boot application."
    else
        echo ""
        echo "⚠️  Database created but connection test failed. Please verify manually."
    fi
else
    echo ""
    echo "❌ Failed to create database. Trying alternative method..."
    echo ""
    
    # Method 2: Direct CREATE DATABASE command
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -c "CREATE DATABASE $DB_NAME;" 2>&1
    
    if [ $? -eq 0 ]; then
        echo "✅ Database created using alternative method!"
        PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -c "GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;"
    else
        echo "❌ Both methods failed. Please check:"
        echo "   1. PostgreSQL server is running and accessible"
        echo "   2. User '$DB_USER' has CREATE DATABASE privileges"
        echo "   3. Network connectivity to $DB_HOST:$DB_PORT"
        echo ""
        echo "You can try manually:"
        echo "   psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d postgres"
        echo "   Then run: CREATE DATABASE $DB_NAME;"
    fi
fi

echo ""
echo "=========================================="
