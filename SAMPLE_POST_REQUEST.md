# Sample POST Request for Creating a Job

## Endpoint
```
POST /api/jobs
Content-Type: application/json
```

## Sample Request 1: Single Job Description

```json
{
  "vehicleNumber": "KA01AB1234",
  "kmReading": 50000,
  "mobileNumber": "+91-9876543210",
  "jobDate": "2024-01-15T10:00:00",
  "status": "PENDING",
  "checkinTime": "2024-01-15T10:00:00",
  "estimatedDelivery": "2024-01-17T18:00:00",
  "companyId": "COMP001",
  "jobDescription": [
    {
      "serviceType": "PERIODIC",
      "description": "Regular service and oil change",
      "assignedMechanicType": "Senior Mechanic",
      "estimatedTime": "2 hours"
    }
  ]
}
```

## Sample Request 2: Multiple Job Descriptions

```json
{
  "vehicleNumber": "MH12XY5678",
  "kmReading": 75000,
  "mobileNumber": "+91-9876543211",
  "jobDate": "2024-01-20T09:30:00",
  "status": "PENDING",
  "checkinTime": "2024-01-20T09:30:00",
  "estimatedDelivery": "2024-01-22T17:00:00",
  "companyId": "COMP001",
  "jobDescription": [
    {
      "serviceType": "PERIODIC",
      "description": "Regular periodic maintenance service",
      "assignedMechanicType": "Senior Mechanic",
      "estimatedTime": "3 hours"
    },
    {
      "serviceType": "AC",
      "description": "AC service and gas refill",
      "assignedMechanicType": "AC Specialist",
      "estimatedTime": "1.5 hours"
    },
    {
      "serviceType": "REPAIR",
      "description": "Brake pad replacement",
      "assignedMechanicType": "Senior Mechanic",
      "estimatedTime": "2 hours"
    }
  ]
}
```

## Sample Request 3: Complete Service Package

```json
{
  "vehicleNumber": "DL08CD9012",
  "kmReading": 30000,
  "mobileNumber": "+91-9876543212",
  "jobDate": "2024-01-25T08:00:00",
  "status": "PENDING",
  "checkinTime": "2024-01-25T08:00:00",
  "estimatedDelivery": "2024-01-26T18:00:00",
  "companyId": "COMP001",
  "jobDescription": [
    {
      "serviceType": "PERIODIC",
      "description": "30K km service - oil change, filter replacement, fluid top-up",
      "assignedMechanicType": "Senior Mechanic",
      "estimatedTime": "4 hours"
    },
    {
      "serviceType": "TIRES",
      "description": "Tire rotation and wheel alignment",
      "assignedMechanicType": "Tire Specialist",
      "estimatedTime": "1 hour"
    },
    {
      "serviceType": "AC",
      "description": "AC cleaning and filter replacement",
      "assignedMechanicType": "AC Specialist",
      "estimatedTime": "1.5 hours"
    }
  ]
}
```

## Sample Request 4: Repair Job

```json
{
  "vehicleNumber": "TN09EF3456",
  "kmReading": 85000,
  "mobileNumber": "+91-9876543213",
  "jobDate": "2024-01-28T11:00:00",
  "status": "PENDING",
  "checkinTime": "2024-01-28T11:00:00",
  "estimatedDelivery": "2024-01-30T16:00:00",
  "companyId": "COMP001",
  "jobDescription": [
    {
      "serviceType": "REPAIR",
      "description": "Engine overhaul and timing belt replacement",
      "assignedMechanicType": "Master Mechanic",
      "estimatedTime": "8 hours"
    },
    {
      "serviceType": "PAINT",
      "description": "Front bumper paint touch-up",
      "assignedMechanicType": "Paint Specialist",
      "estimatedTime": "4 hours"
    }
  ]
}
```

## Field Descriptions

### CreateJobRequest Fields:
- **vehicleNumber** (String, required): Vehicle registration number
- **kmReading** (Integer, optional): Current kilometer reading
- **mobileNumber** (String, optional): Customer mobile number
- **jobDate** (DateTime, required): Job date in ISO format (yyyy-MM-ddTHH:mm:ss)
- **status** (Enum, optional): Job status - PENDING, IN_PROGRESS, QC, READY (defaults to PENDING if not provided)
- **checkinTime** (DateTime, optional): Vehicle check-in time
- **estimatedDelivery** (DateTime, optional): Estimated delivery time
- **companyId** (String, required): Company ID
- **jobDescription** (Array, required): List of job descriptions

### JobDescription Fields:
- **serviceType** (Enum, required): Service type - PERIODIC, REPAIR, AC, TIRES, PAINT
- **description** (String, required): Detailed description of the service
- **assignedMechanicType** (String, optional): Type of mechanic assigned
- **estimatedTime** (String, optional): Estimated time for the service

## cURL Example

```bash
curl -X POST http://localhost:8080/api/jobs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "vehicleNumber": "KA01AB1234",
    "kmReading": 50000,
    "mobileNumber": "+91-9876543210",
    "jobDate": "2024-01-15T10:00:00",
    "status": "PENDING",
    "checkinTime": "2024-01-15T10:00:00",
    "estimatedDelivery": "2024-01-17T18:00:00",
    "companyId": "COMP001",
    "jobDescription": [
      {
        "serviceType": "PERIODIC",
        "description": "Regular service and oil change",
        "assignedMechanicType": "Senior Mechanic",
        "estimatedTime": "2 hours"
      }
    ]
  }'
```

## Postman Collection Format

```json
{
  "name": "Create Job",
  "request": {
    "method": "POST",
    "header": [
      {
        "key": "Content-Type",
        "value": "application/json"
      },
      {
        "key": "Authorization",
        "value": "Bearer {{token}}"
      }
    ],
    "body": {
      "mode": "raw",
      "raw": "{\n  \"vehicleNumber\": \"KA01AB1234\",\n  \"kmReading\": 50000,\n  \"mobileNumber\": \"+91-9876543210\",\n  \"jobDate\": \"2024-01-15T10:00:00\",\n  \"status\": \"PENDING\",\n  \"checkinTime\": \"2024-01-15T10:00:00\",\n  \"estimatedDelivery\": \"2024-01-17T18:00:00\",\n  \"companyId\": \"COMP001\",\n  \"jobDescription\": [\n    {\n      \"serviceType\": \"PERIODIC\",\n      \"description\": \"Regular service and oil change\",\n      \"assignedMechanicType\": \"Senior Mechanic\",\n      \"estimatedTime\": \"2 hours\"\n    }\n  ]\n}"
    },
    "url": {
      "raw": "{{baseUrl}}/api/jobs",
      "host": ["{{baseUrl}}"],
      "path": ["api", "jobs"]
    }
  }
}
```

## Notes

1. **DateTime Format**: Use ISO 8601 format: `yyyy-MM-ddTHH:mm:ss` (e.g., "2024-01-15T10:00:00")
2. **Enum Values**: 
   - **status**: PENDING, IN_PROGRESS, QC, READY
   - **serviceType**: PERIODIC, REPAIR, AC, TIRES, PAINT
3. **jobDescription**: Must be an array. Can contain one or multiple job description objects.
4. **Authentication**: Include JWT token in Authorization header for authenticated requests.
5. **Response**: Returns created JobCard object with generated jobCardId and jobDetailId.






