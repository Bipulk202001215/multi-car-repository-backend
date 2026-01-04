# Create Invoice API - cURL Examples

## Endpoint
`POST /api/invoices`

## Description
Creates an invoice and automatically creates SELL events for all items in the list. Each item will create a SELL event in the inventory system, reducing the available units in the partcode table.

## Required Fields
- `jobId` (String) - Job card identifier
- `companyId` (String) - Company identifier
- `subtotal` (BigDecimal) - Subtotal amount
- `items` (List<InvoiceItem>) - List of items with partCode and units

## Optional Fields
- `cgst` (BigDecimal) - Central GST
- `sgst` (BigDecimal) - State GST
- `igst` (BigDecimal) - Integrated GST
- `paymentStatus` (PaymentStatus) - Defaults to PENDING if not provided
- `paymentMode` (PaymentMode) - Payment mode (e.g., UPI, CASH, CARD)

## InvoiceItem Structure
Each item in the `items` array should have:
- `partCode` (String) - Part code identifier
- `units` (Integer) - Number of units to sell

## cURL Command Examples

### Basic Example
```bash
curl -X POST http://localhost:8080/api/invoices \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "jobId": "JOBID20241225143052123",
    "companyId": "COMPID20251228141634174",
    "subtotal": 5000.00,
    "cgst": 450.00,
    "sgst": 450.00,
    "igst": 0.00,
    "paymentStatus": "PENDING",
    "paymentMode": "UPI",
    "items": [
      {
        "partCode": "ENG001",
        "units": 2
      }
    ]
  }'
```

### Example with Multiple Items
```bash
curl -X POST http://localhost:8080/api/invoices \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "jobId": "JOBID20241225143052123",
    "companyId": "COMPID20251228141634174",
    "subtotal": 10000.00,
    "cgst": 900.00,
    "sgst": 900.00,
    "igst": 0.00,
    "paymentStatus": "PENDING",
    "paymentMode": "UPI",
    "items": [
      {
        "partCode": "ENG001",
        "units": 2
      },
      {
        "partCode": "BRAKE001",
        "units": 1
      },
      {
        "partCode": "FILTER001",
        "units": 3
      }
    ]
  }'
```

### Example with Minimal Fields (Defaults paymentStatus to PENDING)
```bash
curl -X POST http://localhost:8080/api/invoices \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "jobId": "JOBID20241225143052123",
    "companyId": "COMPID20251228141634174",
    "subtotal": 5000.00,
    "items": [
      {
        "partCode": "ENG001",
        "units": 2
      }
    ]
  }'
```

### Example with Environment Variables
```bash
curl -X POST ${API_BASE}/invoices \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -d '{
    "jobId": "${jobCardId}",
    "companyId": "${companyId}",
    "subtotal": 5000.00,
    "cgst": 450.00,
    "sgst": 450.00,
    "igst": 0.00,
    "paymentStatus": "PENDING",
    "paymentMode": "UPI",
    "items": [
      {
        "partCode": "ENG001",
        "units": 2
      }
    ]
  }'
```

### Example with Different Payment Status
```bash
curl -X POST http://localhost:8080/api/invoices \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "jobId": "JOBID20241225143052123",
    "companyId": "COMPID20251228141634174",
    "subtotal": 5000.00,
    "cgst": 450.00,
    "sgst": 450.00,
    "igst": 0.00,
    "paymentStatus": "PAID",
    "paymentMode": "CASH",
    "items": [
      {
        "partCode": "ENG001",
        "units": 2
      }
    ]
  }'
```

### Pretty Print Response (with jq)
```bash
curl -X POST http://localhost:8080/api/invoices \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "jobId": "JOBID20241225143052123",
    "companyId": "COMPID20251228141634174",
    "subtotal": 5000.00,
    "cgst": 450.00,
    "sgst": 450.00,
    "igst": 0.00,
    "paymentStatus": "PENDING",
    "paymentMode": "UPI",
    "items": [
      {
        "partCode": "ENG001",
        "units": 2
      }
    ]
  }' | jq '.'
```

### Verbose Output (for debugging)
```bash
curl -v -X POST http://localhost:8080/api/invoices \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "jobId": "JOBID20241225143052123",
    "companyId": "COMPID20251228141634174",
    "subtotal": 5000.00,
    "cgst": 450.00,
    "sgst": 450.00,
    "igst": 0.00,
    "paymentStatus": "PENDING",
    "paymentMode": "UPI",
    "items": [
      {
        "partCode": "ENG001",
        "units": 2
      }
    ]
  }'
```

## Request Body Structure
```json
{
  "jobId": "JOBID20241225143052123",
  "companyId": "COMPID20251228141634174",
  "subtotal": 5000.00,
  "cgst": 450.00,
  "sgst": 450.00,
  "igst": 0.00,
  "paymentStatus": "PENDING",
  "paymentMode": "UPI",
  "items": [
    {
      "partCode": "ENG001",
      "units": 2
    }
  ]
}
```

## Success Response (201 Created)
```json
{
  "invoiceId": "INVID20241225143052123",
  "jobId": "JOBID20241225143052123",
  "companyId": "COMPID20251228141634174",
  "subtotal": 5000.00,
  "cgst": 450.00,
  "sgst": 450.00,
  "igst": 0.00,
  "total": 5900.00,
  "paymentStatus": "PENDING",
  "paymentMode": "UPI",
  "createdOn": "2024-12-25T14:30:52.123",
  "updatedOn": "2024-12-25T14:30:52.123"
}
```

## Error Responses

### Insufficient Stock (400 Bad Request)
```json
{
  "errorcode": "INSUFFICIENT_STOCK",
  "errormessage": "Insufficient stock for part code: ENG001. Requested: 10, Available: 5",
  "status": 400
}
```

### Partcode Not Found (404 Not Found)
```json
{
  "errorcode": "PARTCODE_NOT_FOUND",
  "errormessage": "Partcode not found with part code: ENG001",
  "status": 404
}
```

### Missing Units (400 Bad Request)
```json
{
  "errorcode": "UNITS_REQUIRED",
  "errormessage": "Units field is required",
  "status": 400
}
```

### Missing Part Code (400 Bad Request)
```json
{
  "errorcode": "PARTCODE_REQUIRED",
  "errormessage": "Part code field is required",
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

## PaymentStatus Enum Values
- `PENDING` - Payment is pending (default)
- `PAID` - Payment has been completed
- `PARTIAL` - Partial payment received

## PaymentMode Enum Values
- `CASH` - Cash payment
- `UPI` - UPI payment
- `CARD` - Card payment
- `NET_BANKING` - Net banking
- `CHEQUE` - Cheque payment

## Notes
- When an invoice is created, SELL events are automatically created for each item in the `items` list
- Each SELL event will:
  - Validate sufficient stock is available
  - Create an event record in the INVENTORY_EVENT table
  - Decrease the units in the PARTCODE table
  - Use the units_price from the partcode for the SELL event
- The `total` is automatically calculated as: subtotal + cgst + sgst + igst
- If `paymentStatus` is not provided, it defaults to `PENDING`
- The invoice ID is automatically linked to the job card
- All inventory operations (SELL events) happen within a transaction, so if any item fails, the entire invoice creation is rolled back

