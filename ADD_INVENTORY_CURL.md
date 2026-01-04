# Add Inventory API - cURL Examples

## Endpoint
`POST /api/inventory/add`

## Description
Adds units to inventory. Creates an ADD event and updates the partcode table.

## Required Fields
- `units` (Integer) - **MANDATORY** - Number of units to add
- `unitsPrice` (BigDecimal) - **MANDATORY** - Price per unit

## Optional Fields
- `partCode` (String) - Part code identifier

## cURL Command Examples

### Basic Example
```bash
curl -X POST http://localhost:8080/api/inventory/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "partCode": "ENG001",
    "units": 10,
    "unitsPrice": 500.00
  }'
```

### Example with Different Part Code
```bash
curl -X POST http://localhost:8080/api/inventory/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "partCode": "BRAKE001",
    "units": 5,
    "unitsPrice": 1200.50
  }'
```

### Example with Environment Variables
```bash
curl -X POST ${API_BASE}/inventory/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "partCode": "ENG001",
    "units": 10,
    "unitsPrice": 500.00
  }'
```

### Pretty Print Response (with jq)
```bash
curl -X POST http://localhost:8080/api/inventory/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "partCode": "ENG001",
    "units": 10,
    "unitsPrice": 500.00
  }' | jq '.'
```

### Verbose Output (for debugging)
```bash
curl -v -X POST http://localhost:8080/api/inventory/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "partCode": "ENG001",
    "units": 10,
    "unitsPrice": 500.00
  }'
```

## Request Body Structure
```json
{
  "partCode": "ENG001",
  "units": 10,
  "unitsPrice": 500.00
}
```

## Success Response (201 Created)
```json
{
  "eventId": "EVTID20241225143052123",
  "partCode": "ENG001",
  "event": "ADD",
  "units": 10,
  "price": 5000.00,
  "unitsPrice": 500.00,
  "companyId": null,
  "jobId": null,
  "createdOn": "2024-12-25T14:30:52.123"
}
```

## Error Responses

### Missing Units (400 Bad Request)
```json
{
  "errorcode": "UNITS_REQUIRED",
  "errormessage": "Units field is required",
  "status": 400
}
```

### Missing Units Price (400 Bad Request)
```json
{
  "errorcode": "UNITS_PRICE_REQUIRED",
  "errormessage": "Units price field is required",
  "status": 400
}
```

### Invalid Units (400 Bad Request)
```json
{
  "errorcode": "VALIDATION_ERROR",
  "errormessage": "Units must be greater than 0",
  "status": 400
}
```

### Invalid Units Price (400 Bad Request)
```json
{
  "errorcode": "VALIDATION_ERROR",
  "errormessage": "Units price must be greater than 0",
  "status": 400
}
```

### Unauthorized (401 Unauthorized)
```json
{
  "errorcode": "UNAUTHORIZED",
  "errormessage": "Unauthorized",
  "status": 401
}
```

## Notes
- If the partcode doesn't exist, it will be created automatically
- The `unitsPrice` from the event will be stored in the partcode table
- The `units` in the partcode will be increased by the amount specified
- `minStockAlert` defaults to 2 if not specified



