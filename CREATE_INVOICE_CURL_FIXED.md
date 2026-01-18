# Create Invoice API - Corrected cURL

## Corrected cURL Command

The issue was missing quotes around string values in JSON. Here's the corrected version:

```bash
curl --location 'http://localhost:8080/api/invoices' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJjb21wYW55SWQiOiJDT01QSUQyMDI1MTIyODE0MTYzNDE3NCIsImVtYWlsSWQiOiJiaXB1bGszMzM1QGdtYWlsLmNvbSIsInVzZXJUeXBlIjoiQ09NUEFOWV9VU0VSIiwidXNlcklkIjoiVVNJRDI1MTIyODE0MTczNyIsInN1YiI6ImJpcHVsazMzMzVAZ21haWwuY29tIiwiaWF0IjoxNzY2OTEyMzY0LCJleHAiOjE3NjY5OTg3NjR9.QQ5WxBOroo5WGfggCxymCYEiAkZq6e3TiX_1KPlCHL997RImlD2RyMOjuoys4HGy' \
--data '{
    "jobId": "JBCDId20251228223539957",
    "companyId": "COMPID20251228141634174",
    "subtotal": 10000.00,
    "cgst": 9.00,
    "sgst": 9.00,
    "igst": 9.00,
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
    ]
}'
```

## Key Fixes

1. **jobId**: Changed from `JBCDId20251228223539957` to `"JBCDId20251228223539957"` (added quotes)
2. **companyId**: Changed from `COMPID20251228141634174` to `"COMPID20251228141634174"` (added quotes)

## Alternative Format (Single Line)

```bash
curl --location 'http://localhost:8080/api/invoices' --header 'Content-Type: application/json' --header 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJjb21wYW55SWQiOiJDT01QSUQyMDI1MTIyODE0MTYzNDE3NCIsImVtYWlsSWQiOiJiaXB1bGszMzM1QGdtYWlsLmNvbSIsInVzZXJUeXBlIjoiQ09NUEFOWV9VU0VSIiwidXNlcklkIjoiVVNJRDI1MTIyODE0MTczNyIsInN1YiI6ImJpcHVsazMzMzVAZ21haWwuY29tIiwiaWF0IjoxNzY2OTEyMzY0LCJleHAiOjE3NjY5OTg3NjR9.QQ5WxBOroo5WGfggCxymCYEiAkZq6e3TiX_1KPlCHL997RImlD2RyMOjuoys4HGy' --data '{"jobId":"JBCDId20251228223539957","companyId":"COMPID20251228141634174","subtotal":10000.00,"cgst":9.00,"sgst":9.00,"igst":9.00,"paymentStatus":"PENDING","paymentMode":"UPI","items":[{"partCode":"ENG001","units":2},{"partCode":"BRAKE001","units":1}]}'
```

## JSON Structure Rules

In JSON, all string values must be enclosed in double quotes:
- ✅ Correct: `"jobId": "JBCDId20251228223539957"`
- ❌ Wrong: `"jobId": JBCDId20251228223539957`

- ✅ Correct: `"companyId": "COMPID20251228141634174"`
- ❌ Wrong: `"companyId": COMPID20251228141634174`

## Expected Success Response (201 Created)

```json
{
  "invoiceId": "INVID20241225143052123",
  "jobId": "JBCDId20251228223539957",
  "companyId": "COMPID20251228141634174",
  "subtotal": 10000.00,
  "cgst": 9.00,
  "sgst": 9.00,
  "igst": 9.00,
  "total": 10027.00,
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
  "createdOn": "2026-01-04T22:48:07.209",
  "updatedOn": "2026-01-04T22:48:07.209"
}
```



