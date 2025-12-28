#!/bin/bash

# Script to generate BCrypt hash for SUPER_ADMIN password
# This uses the Spring Boot application to generate the hash

PASSWORD="SuperAdmin@123"

echo "Generating BCrypt hash for password: $PASSWORD"
echo ""
echo "Method 1: Using Spring Boot Test"
echo "---------------------------------"
echo "Create a test file: GenerateHashTest.java"
echo "Run: mvn test-compile exec:java -Dexec.mainClass=GenerateHashTest"
echo ""
echo "Method 2: Using Online Generator"
echo "---------------------------------"
echo "Visit: https://bcrypt-generator.com/"
echo "Enter password: $PASSWORD"
echo "Rounds: 10"
echo ""
echo "Method 3: Using the API (Recommended)"
echo "--------------------------------------"
echo "First, temporarily allow /api/users endpoint (or disable auth)"
echo "Then POST to /api/users with:"
echo '{'
echo '  "emailId": "superadmin@multicar.com",'
echo '  "password": "'$PASSWORD'",'
echo '  "userType": "SUPER_ADMIN"'
echo '}'
echo ""
echo "Then copy the password hash from the database."

