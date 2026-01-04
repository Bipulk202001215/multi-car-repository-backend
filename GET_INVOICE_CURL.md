# Get Invoice API - cURL Examples

## Endpoints

### 1. Get Invoice by ID
`GET /api/invoices/{invoiceId}`

### 2. Get All Invoices
`GET /api/invoices`

### 3. Get Invoices by Job ID
`GET /api/invoices/job/{jobId}`

### 4. Get Invoices by Payment Status
`GET /api/invoices/status/{paymentStatus}`

## cURL Command Examples

### 1. Get Invoice by ID

#### Basic Example
```bash
curl -X GET http://localhost:8080/api/invoices/INVID20241225143052123 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### With Pretty Print (jq)
```bash
curl -X GET http://localhost:8080/api/invoices/INVID20241225143052123 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" | jq '.'
```

#### With Environment Variables
```bash
curl -X GET ${API_BASE}/invoices/${invoiceId} \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Verbose Output
```bash
curl -v -X GET http://localhost:8080/api/invoices/INVID20241225143052123 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Success Response (200 OK):**
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
  "items": [
    {
      "partCode": "ENG001",
      "units": 2
    },
    {
      "partCode": "BRAKE001",
      "units": 1
    }
  ],
  "createdOn": "2024-12-25T14:30:52.123",
  "updatedOn": "2024-12-25T14:30:52.123"
}
```

**Error Response (404 Not Found):**
```json
{
  "errorcode": "INVOICE_NOT_FOUND",
  "errormessage": "Invoice not found with id: INVID20241225143052123",
  "status": 404
}
```

---

### 2. Get All Invoices

#### Basic Example
```bash
curl -X GET http://localhost:8080/api/invoices \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### With Pretty Print (jq)
```bash
curl -X GET http://localhost:8080/api/invoices \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" | jq '.'
```

#### With Environment Variables
```bash
curl -X GET ${API_BASE}/invoices \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Verbose Output
```bash
curl -v -X GET http://localhost:8080/api/invoices \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Success Response (200 OK):**
```json
[
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
    "items": [
      {
        "partCode": "ENG001",
        "units": 2
      }
    ],
    "createdOn": "2024-12-25T14:30:52.123",
    "updatedOn": "2024-12-25T14:30:52.123"
  },
  {
    "invoiceId": "INVID20241225144530145",
    "jobId": "JOBID20241225144530145",
    "companyId": "COMPID20251228141634174",
    "subtotal": 3000.00,
    "cgst": 270.00,
    "sgst": 270.00,
    "igst": 0.00,
    "total": 3540.00,
    "paymentStatus": "COMPLETED",
    "paymentMode": "CASH",
    "items": [
      {
        "partCode": "BRAKE001",
        "units": 1
      }
    ],
    "createdOn": "2024-12-25T14:45:30.145",
    "updatedOn": "2024-12-25T14:45:30.145"
  }
]
```

---

### 3. Get Invoices by Job ID

#### Basic Example
```bash
curl -X GET http://localhost:8080/api/invoices/job/JOBID20241225143052123 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### With Pretty Print (jq)
```bash
curl -X GET http://localhost:8080/api/invoices/job/JOBID20241225143052123 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" | jq '.'
```

#### With Environment Variables
```bash
curl -X GET ${API_BASE}/invoices/job/${jobCardId} \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Verbose Output
```bash
curl -v -X GET http://localhost:8080/api/invoices/job/JOBID20241225143052123 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Success Response (200 OK):**
```json
[
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
    "items": [
      {
        "partCode": "ENG001",
        "units": 2
      }
    ],
    "createdOn": "2024-12-25T14:30:52.123",
    "updatedOn": "2024-12-25T14:30:52.123"
  }
]
```

**Empty Response (200 OK - No invoices found):**
```json
[]
```

---

### 4. Get Invoices by Payment Status

#### Get PENDING Invoices
```bash
curl -X GET http://localhost:8080/api/invoices/status/PENDING \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Get COMPLETED Invoices
```bash
curl -X GET http://localhost:8080/api/invoices/status/COMPLETED \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### With Pretty Print (jq)
```bash
curl -X GET http://localhost:8080/api/invoices/status/PENDING \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" | jq '.'
```

#### With Environment Variables
```bash
curl -X GET ${API_BASE}/invoices/status/PENDING \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

#### Verbose Output
```bash
curl -v -X GET http://localhost:8080/api/invoices/status/PENDING \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Success Response (200 OK):**
```json
[
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
    "items": [
      {
        "partCode": "ENG001",
        "units": 2
      }
    ],
    "createdOn": "2024-12-25T14:30:52.123",
    "updatedOn": "2024-12-25T14:30:52.123"
  }
]
```

**Empty Response (200 OK - No invoices found):**
```json
[]
```

**Error Response (400 Bad Request - Invalid payment status):**
```json
{
  "errorcode": "BAD_REQUEST",
  "errormessage": "Invalid payment status",
  "status": 400
}
```

---

## Payment Status Values

Valid values for payment status:
- `PENDING` - Payment is pending
- `COMPLETED` - Payment has been completed

---

## Error Responses

### Unauthorized (401 Unauthorized)
```json
{
  "errorcode": "UNAUTHORIZED",
  "errormessage": "Unauthorized",
  "status": 401
}
```

### Invoice Not Found (404 Not Found)
```json
{
  "errorcode": "INVOICE_NOT_FOUND",
  "errormessage": "Invoice not found with id: INVID20241225143052123",
  "status": 404
}
```

---

## Notes

- All GET endpoints require Authorization header with Bearer token
- All endpoints return 200 OK even if no invoices are found (returns empty array `[]`)
- Payment status values are case-sensitive and must match enum values exactly (PENDING, COMPLETED)
- The invoice ID format follows the pattern: `INVID` + timestamp
- All responses include full invoice details including companyId and items
- The `items` array contains the list of partCode and units from SELL events for that jobId
- Items are fetched from INVENTORY_EVENT table, filtered to only include SELL events
- If no SELL events exist for the jobId, the items array will be empty `[]`

