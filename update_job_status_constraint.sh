#!/bin/bash

# ============================================
# Update Job Status Constraint Script
# ============================================
# This script updates the job_cards_status_check constraint 
# to include COMPLETED status
# ============================================

# Database connection parameters
DB_HOST="139.84.210.248"
DB_PORT="5432"
DB_NAME="appdb"
DB_USER="postgres"
DB_PASSWORD="Pihu@1234"
# Note: You can override DB_PASSWORD by setting it as environment variable

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}============================================"
echo "Update Job Status Constraint"
echo "============================================${NC}"

# Use environment variable if set, otherwise use default
if [ -z "$DB_PASSWORD" ]; then
    DB_PASSWORD="Pihu@1234"
fi

export PGPASSWORD="$DB_PASSWORD"

# Run the SQL script
echo -e "${YELLOW}Running SQL script to update constraint...${NC}"

psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f update_job_status_constraint.sql

EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}✓ Constraint updated successfully!${NC}"
    echo -e "${GREEN}The job_cards table now accepts COMPLETED status.${NC}"
else
    echo -e "${RED}✗ Failed to update constraint. Please check the error above.${NC}"
    exit $EXIT_CODE
fi

# Clean up password from environment (optional, for security)
# unset PGPASSWORD
