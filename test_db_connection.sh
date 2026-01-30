#!/bin/bash

# Database connection test script
# This script helps identify the correct database username

DB_HOST="139.84.210.248.vultrusercontent.com"
DB_PORT="5432"
DB_NAME="appdb"
DB_PASSWORD="Pihu@1234"

echo "=========================================="
echo "Testing PostgreSQL Connection"
echo "=========================================="
echo "Host: $DB_HOST"
echo "Port: $DB_PORT"
echo "Database: $DB_NAME"
echo ""

# Common PostgreSQL usernames to try
USERS=("postgres" "admin" "appdb" "appuser" "multicar" "root" "administrator")

for USER in "${USERS[@]}"; do
    echo "Testing username: $USER"
    OUTPUT=$(PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$USER" -d "$DB_NAME" -c "SELECT current_user;" 2>&1)
    EXIT_CODE=$?
    
    if [ $EXIT_CODE -eq 0 ] && ! echo "$OUTPUT" | grep -q "FATAL\|error\|does not exist"; then
        echo "$OUTPUT" | head -3
        echo "✅ SUCCESS! Username '$USER' works!"
        echo ""
        echo "Update application.properties with:"
        echo "spring.datasource.username=$USER"
        exit 0
    else
        echo "$OUTPUT" | grep -E "FATAL|error|does not exist" | head -1
        echo "❌ Failed with username: $USER"
        echo ""
    fi
done

echo "=========================================="
echo "None of the common usernames worked."
echo "Please check with your database administrator"
echo "or try connecting manually:"
echo ""
echo "psql -h $DB_HOST -p $DB_PORT -U <your_username> -d $DB_NAME"
echo "=========================================="
