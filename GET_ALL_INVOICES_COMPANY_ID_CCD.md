# Code Changes Documentation: Add Company ID Path Parameter to GetAllInvoices API

## Files Affected:
1. `src/main/java/com/multicar/repository/demo/controller/InvoiceController.java` (MODIFIED)
2. `src/main/java/com/multicar/repository/demo/service/InvoiceService.java` (MODIFIED)
3. `src/main/java/com/multicar/repository/demo/repository/InvoiceRepository.java` (MODIFIED)

## New Functions/Components:

### 1. Repository Method (InvoiceRepository)
- **`findByCompanyId(String companyId)`**: Finds all invoices for a specific company ID
  - Returns: `List<InvoiceEntity>`
  - Purpose: Filter invoices by company to ensure data isolation

## Modified Functions/Components:

### 1. InvoiceController
- **`getAllInvoices()`** → **`getAllInvoices(@PathVariable String companyId)`**
  - **Endpoint Changed**: 
    - **Before**: `GET /api/invoices`
    - **After**: `GET /api/invoices/{companyId}/company`
  - **Change**: Added `@PathVariable String companyId` parameter
  - **Annotation Updated**: Changed from `@GetMapping` to `@GetMapping("/{companyId}/company")`
  - **Impact**: The endpoint now requires companyId in the URL path and filters results by company

### 2. InvoiceService
- **`getAllInvoices()`** → **`getAllInvoices(String companyId)`**
  - **Change**: Added `companyId` parameter
  - **Implementation Updated**: 
    - **Before**: Used `invoiceRepository.findAll()` to get all invoices
    - **After**: Uses `invoiceRepository.findByCompanyId(companyId)` to filter by company
  - **Impact**: Only returns invoices belonging to the specified company

### 3. InvoiceRepository
- **Added Method**: `List<InvoiceEntity> findByCompanyId(String companyId);`
  - **Purpose**: Spring Data JPA will automatically generate a query to find all invoices where `companyId` matches the provided value
  - **Return Type**: List of InvoiceEntity objects

## Removed/Deprecated Code (if any):
None

## Example Usage or Impact:

### Before:
```java
// Endpoint: GET /api/invoices
// Returns all invoices across all companies (no filtering)
@GetMapping
public ResponseEntity<List<Invoice>> getAllInvoices() {
    List<Invoice> invoices = invoiceService.getAllInvoices();
    return ResponseEntity.ok(invoices);
}
```

### After:
```java
// Endpoint: GET /api/invoices/{companyId}/company
// Returns only invoices for the specified company
@GetMapping("/{companyId}/company")
public ResponseEntity<List<Invoice>> getAllInvoices(@PathVariable String companyId) {
    List<Invoice> invoices = invoiceService.getAllInvoices(companyId);
    return ResponseEntity.ok(invoices);
}
```

### Example API Calls:

**Before:**
```bash
curl -X GET http://localhost:8080/api/invoices \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**After:**
```bash
curl -X GET http://localhost:8080/api/invoices/COMPID20251228141634174/company \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Response Example:
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
    "items": null,
    "createdOn": "2024-12-25T14:30:52.123",
    "updatedOn": "2024-12-25T14:30:52.123"
  }
]
```

### Security & Data Isolation:
- **Company-specific data access**: Invoices are now filtered by companyId
- **Prevents cross-company data access**: Users can only see invoices for their company
- **Consistent with inventory API pattern**: Follows the same pattern as the inventory endpoints

### Breaking Changes:
- **API endpoint URL has changed**: 
  - Old: `GET /api/invoices`
  - New: `GET /api/invoices/{companyId}/company`
- **API clients must update**: All clients calling this endpoint must include `companyId` in the URL path
- **Service method signature changed**: Any code directly calling `InvoiceService.getAllInvoices()` must now pass `companyId` parameter

### Migration Notes:
- Existing API clients need to update their endpoint URLs
- The `companyId` path parameter is mandatory - requests without it will result in 404 Not Found
- All invoices in the database should already have a `companyId` field (from InvoiceEntity)
- No database schema changes required - the `company_id` column already exists in the `INVOICES` table
