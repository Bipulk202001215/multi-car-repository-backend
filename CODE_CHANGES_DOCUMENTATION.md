# Code Changes Documentation (CCD)

## Implementation: AdditionalInvoiceDetails for Invoice System

### Files Affected:
1. `src/main/java/com/multicar/repository/demo/model/AdditionalInvoiceDetails.java` (NEW)
2. `src/main/java/com/multicar/repository/demo/converter/AdditionalInvoiceDetailsConverter.java` (NEW)
3. `src/main/java/com/multicar/repository/demo/entity/InvoiceEntity.java` (MODIFIED)
4. `src/main/java/com/multicar/repository/demo/model/Invoice.java` (MODIFIED)
5. `src/main/java/com/multicar/repository/demo/service/InvoiceService.java` (MODIFIED)
6. `src/main/java/com/multicar/repository/demo/model/CreateInvoiceRequest.java` (MODIFIED - import cleanup)

### New Functions/Components:

#### 1. AdditionalInvoiceDetails Model Class
- **Location**: `src/main/java/com/multicar/repository/demo/model/AdditionalInvoiceDetails.java`
- **Purpose**: Model class to hold optional additional invoice details
- **Fields**:
  - `name` (String): Customer name
  - `customerGstin` (String): Customer GSTIN number
- **Annotations**: Uses Lombok annotations for builder pattern, getters, setters, and Jackson annotations for JSON serialization

#### 2. AdditionalInvoiceDetailsConverter
- **Location**: `src/main/java/com/multicar/repository/demo/converter/AdditionalInvoiceDetailsConverter.java`
- **Purpose**: JPA AttributeConverter to serialize/deserialize AdditionalInvoiceDetails to/from JSON string for database storage
- **Methods**:
  - `convertToDatabaseColumn()`: Converts AdditionalInvoiceDetails object to JSON string
  - `convertToEntityAttribute()`: Converts JSON string back to AdditionalInvoiceDetails object
- **Storage**: Uses PostgreSQL JSONB column type for efficient JSON storage and querying

### Modified Functions/Components:

#### 1. InvoiceEntity
- **Added Field**: `additionalDetails` (AdditionalInvoiceDetails)
- **Annotations Added**:
  - `@Convert(converter = AdditionalInvoiceDetailsConverter.class)`: Specifies the converter
  - `@JdbcTypeCode(SqlTypes.JSON)`: Marks as JSON type
  - `@Column(name = "additional_details", columnDefinition = "jsonb", nullable = true)`: Database column mapping
- **Impact**: The field is stored as JSONB in PostgreSQL, allowing for flexible schema and efficient querying

#### 2. Invoice Model
- **Added Field**: `additionalDetails` (AdditionalInvoiceDetails)
- **Impact**: The field is now included in all Invoice responses, allowing clients to access additional invoice details

#### 3. InvoiceService
- **Modified Methods**:
  - `createInvoice()`: Now saves `additionalDetails` from request to entity
  - `updateInvoice()`: Now updates `additionalDetails` if provided in request
  - `convertToModelWithoutItems()`: Now includes `additionalDetails` in response
  - `convertToModel()`: Now includes `additionalDetails` in response
  - `convertToFullModel()`: Now includes `additionalDetails` in full invoice response
- **Impact**: Additional details are persisted when creating/updating invoices and returned in all invoice fetch operations

#### 4. CreateInvoiceRequest
- **Field**: `additionalDetails` was already present (as per user requirement)
- **Cleanup**: Removed unused `BigDecimal` import

### Removed/Deprecated Code:
- None

### Example Usage or Impact:

#### Creating Invoice with Additional Details:
```json
POST /api/invoices
{
  "jobId": "JOBID20241225143052123",
  "companyId": "COMPID20251228141634174",
  "paymentStatus": "PENDING",
  "paymentMode": "UPI",
  "items": [
    {
      "partCode": "ENG001",
      "units": 2
    }
  ],
  "additionalDetails": {
    "name": "John Doe",
    "customerGstin": "29ABCDE1234F1Z5"
  }
}
```

#### Fetching Invoice (Full Details):
```json
GET /api/invoices/getFullInvoice/{invoiceId}

Response:
{
  "invoiceId": "INVID20241225143052123",
  "jobId": "JOBID20241225143052123",
  "companyId": "COMPID20251228141634174",
  "subtotal": 5000.00,
  "cgst": 450.00,
  "sgst": 450.00,
  "total": 5900.00,
  "paymentStatus": "PENDING",
  "paymentMode": "UPI",
  "items": [...],
  "additionalDetails": {
    "name": "John Doe",
    "customerGstin": "29ABCDE1234F1Z5"
  },
  "createdOn": "2024-12-25T14:30:52",
  "updatedOn": "2024-12-25T14:30:52"
}
```

### Database Schema Impact:
- A new column `additional_details` of type JSONB will be added to the `INVOICES` table
- The column is nullable, so existing invoices without additional details will have NULL values
- No migration script is provided - database schema update should be handled separately

### Notes:
- The `additionalDetails` field is optional - invoices can be created with or without it
- The field is stored as JSONB in PostgreSQL, providing efficient storage and querying capabilities
- All invoice fetch endpoints (`getInvoiceById`, `getFullInvoiceById`, `getAllInvoices`) will include the `additionalDetails` field when present
- The implementation follows the same pattern used for `JobDescription` in `JobDetailEntity`, ensuring consistency across the codebase

---

## Fix: Ensure PartcodeEntity is Saved When Created

### Files Affected:
1. `src/main/java/com/multicar/repository/demo/service/PartcodeService.java` (MODIFIED)

### Modified Functions/Components:

#### 1. PartcodeService.addUnits()
- **Location**: `src/main/java/com/multicar/repository/demo/service/PartcodeService.java` (lines 53-67)
- **Change**: Modified the `orElseGet()` lambda to explicitly save the newly created `PartcodeEntity` to the repository
- **Before**: The new `PartcodeEntity` was built but not immediately saved (relied on later save operation)
- **After**: The new `PartcodeEntity` is now saved immediately using `partcodeRepository.save(newPartcode)` within the `orElseGet()` block
- **Impact**: 
  - Ensures that when a Partcode doesn't exist, it's created and persisted to the database immediately
  - Makes the code intent clearer and more explicit
  - Prevents potential issues if the entity is used before the later save operation

### Removed/Deprecated Code:
- None

### Example Usage or Impact:

#### Before (Implicit Save):
```java
PartcodeEntity partcode = partcodeRepository.findByPartCode(request.getPartCode())
    .orElseGet(() -> {
        return PartcodeEntity.builder()
            .partCode(request.getPartCode())
            // ... other fields ...
            .build();
    });
// Entity saved later at line 87
partcodeRepository.save(partcode);
```

#### After (Explicit Save):
```java
PartcodeEntity partcode = partcodeRepository.findByPartCode(request.getPartCode())
    .orElseGet(() -> {
        PartcodeEntity newPartcode = PartcodeEntity.builder()
            .partCode(request.getPartCode())
            // ... other fields ...
            .build();
        // Save the new partcode to repository
        return partcodeRepository.save(newPartcode);
    });
// Existing partcode updated later at line 87
partcodeRepository.save(partcode);
```

### Notes:
- The change ensures that new `PartcodeEntity` instances are persisted immediately when created
- This is particularly important for ensuring data consistency and avoiding potential issues with transient entities
- The existing save operation at line 87 will now update existing partcodes or handle any additional updates needed
- No database schema changes required
