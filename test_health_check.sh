#!/bin/bash

# Health Check API Test Script
# Default port is 8080 (Spring Boot default)

BASE_URL="${1:-http://localhost:8080}"
ENDPOINT="/api/health"

echo "Testing Health Check API..."
echo "URL: ${BASE_URL}${ENDPOINT}"
echo ""

# Test the health check endpoint
curl -X GET "${BASE_URL}${ENDPOINT}" \
  -H "Content-Type: application/json" \
  -w "\n\nHTTP Status: %{http_code}\n" \
  -s | jq '.' 2>/dev/null || curl -X GET "${BASE_URL}${ENDPOINT}" \
  -H "Content-Type: application/json" \
  -w "\n\nHTTP Status: %{http_code}\n"

echo ""
echo "Done!"
