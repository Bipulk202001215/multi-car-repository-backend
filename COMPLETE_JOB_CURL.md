# Complete Job API - cURL Examples

## Endpoint
`GET /api/jobs/{jobCardId}/complete`

## Description
Marks a job card as COMPLETED by updating its status to `COMPLETED`.

## cURL Command Examples

### Basic Example
```bash
curl -X GET http://localhost:8080/api/jobs/JBCD001/complete \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### With Pretty Print (jq)
```bash
curl -X GET http://localhost:8080/api/jobs/JBCD001/complete \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" | jq '.'
```

### With Environment Variables
```bash
curl -X GET ${API_BASE}/jobs/${jobCardId}/complete \
  -H "Authorization: Bearer ${JWT_TOKEN}"
```

### Verbose Output
```bash
curl -v -X GET http://localhost:8080/api/jobs/JBCD001/complete \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### One-liner (for quick testing)
```bash
curl -X GET http://localhost:8080/api/jobs/JBCD001/complete -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Success Response (200 OK)

```json
{
  "jobCardId": "JBCD001",
  "vehicleNumber": "KA01AB1234",
  "vehicleModel": "Honda City",
  "kmReading": 50000,
  "jobDate": "2024-01-15T10:00:00",
  "status": "COMPLETED",
  "checkinTime": "2024-01-15T10:00:00",
  "estimatedDelivery": "2024-01-17T18:00:00",
  "mobileNumber": "9876543210",
  "companyId": "COMP001",
  "invoiceId": null,
  "createdOn": "2024-01-15T10:00:00",
  "updatedOn": "2024-01-17T18:00:00",
  "jobDetailId": {
    "jobDetailId": "JBID001",
    "jobDescription": [
      {
        "serviceType": "PERIODIC",
        "description": "Regular service and oil change",
        "assignedMechanicType": "Senior Mechanic",
        "estimatedTime": "2.5 hours"
      }
    ],
    "createdOn": "2024-01-15T10:00:00",
    "updatedOn": "2024-01-15T10:00:00"
  }
}
```

---

## Error Responses

### Job Not Found (404 Not Found)
```json
{
  "errorcode": "JOB_NOT_FOUND",
  "errormessage": "Job card not found with id: JBCD999",
  "status": 404
}
```

**Example cURL that triggers 404:**
```bash
curl -X GET http://localhost:8080/api/jobs/JBCD999/complete \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Unauthorized (401 Unauthorized)
```json
{
  "errorcode": "UNAUTHORIZED",
  "errormessage": "Unauthorized",
  "status": 401
}
```

**Example cURL without token:**
```bash
curl -X GET http://localhost:8080/api/jobs/JBCD001/complete
```

---

## Request Parameters

| Parameter | Type | Location | Required | Description |
|-----------|------|----------|----------|-------------|
| jobCardId | String | Path | Yes | The unique identifier of the job card to complete |

---

## Response Fields

| Field | Type | Description |
|-------|------|-------------|
| jobCardId | String | Unique identifier of the job card |
| vehicleNumber | String | Vehicle registration number |
| vehicleModel | String | Model of the vehicle |
| kmReading | Integer | Kilometer reading of the vehicle |
| jobDate | DateTime | Date and time when the job was created |
| status | Enum | Job status (will be "COMPLETED" after this call) |
| checkinTime | DateTime | Time when vehicle was checked in |
| estimatedDelivery | DateTime | Estimated delivery time |
| mobileNumber | String | Contact mobile number |
| companyId | String | Company identifier |
| invoiceId | String | Associated invoice ID (if any) |
| createdOn | DateTime | Job card creation timestamp |
| updatedOn | DateTime | Last update timestamp |
| jobDetailId | Object | Job detail information |
| jobDetailId.jobDetailId | String | Unique identifier of job detail |
| jobDetailId.jobDescription | Array | List of job descriptions |
| jobDetailId.createdOn | DateTime | Job detail creation timestamp |
| jobDetailId.updatedOn | DateTime | Job detail update timestamp |

---

## Job Status Values

Valid job status values:
- `PENDING` - Job is pending and not yet started
- `IN_PROGRESS` - Job is currently in progress
- `QC` - Job is in quality check phase
- `READY` - Job is completed and ready for delivery
- `COMPLETED` - Job is completed (set by this endpoint)

---

## Notes

- The endpoint uses **GET** method to mark a job as completed
- Requires Authorization header with Bearer token
- The job status will be updated to `COMPLETED` in the database
- The `updatedOn` timestamp will be automatically updated
- If the job card doesn't exist, a 404 error will be returned
- The endpoint is idempotent - calling it multiple times on the same job will keep the status as `COMPLETED`
- All other job card fields remain unchanged

---

## Example Usage Scenarios

### Scenario 1: Complete a job after finishing all work
```bash
# First, get the job to see current status
curl -X GET http://localhost:8080/api/jobs/JBCD001 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Then complete the job
curl -X GET http://localhost:8080/api/jobs/JBCD001/complete \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Scenario 2: Using in a script with variables
```bash
#!/bin/bash
JOB_CARD_ID="JBCD001"
JWT_TOKEN="your_jwt_token_here"
API_BASE="http://localhost:8080/api"

curl -X GET "${API_BASE}/jobs/${JOB_CARD_ID}/complete" \
  -H "Authorization: Bearer ${JWT_TOKEN}" \
  -w "\nHTTP Status: %{http_code}\n"
```

### Scenario 3: Save response to file
```bash
curl -X GET http://localhost:8080/api/jobs/JBCD001/complete \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -o completed_job_response.json
```
