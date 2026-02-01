# Code Changes Documentation: Add Company ID as Path Parameter to Inventory Controller

## Files Affected:
1. `src/main/java/com/multicar/repository/demo/controller/InventoryController.java` (MODIFIED)
2. `src/main/java/com/multicar/repository/demo/model/AddInventoryRequest.java` (MODIFIED)
3. `src/main/java/com/multicar/repository/demo/model/Partcode.java` (MODIFIED)
4. `src/main/java/com/multicar/repository/demo/entity/PartcodeEntity.java` (MODIFIED)
5. `src/main/java/com/multicar/repository/demo/service/PartcodeService.java` (MODIFIED)
6. `src/main/java/com/multicar/repository/demo/service/InventoryEventService.java` (MODIFIED)
7. `src/main/java/com/multicar/repository/demo/service/InvoiceService.java` (MODIFIED)
8. `src/main/java/com/multicar/repository/demo/repository/PartcodeRepository.java` (MODIFIED)
9. `src/main/java/com/multicar/repository/demo/repository/InventoryEventRepository.java` (MODIFIED)

## New Functions/Components:

### 1. Repository Methods (PartcodeRepository)
- **`findByPartCodeAndCompanyId(String partCode, String companyId)`**: Finds a partcode by both part code and company ID
- **`findByCompanyId(String companyId)`**: Finds all partcodes for a specific company
- **`findLowStockItemsByCompanyId(String companyId)`**: Finds low stock items filtered by company ID

### 2. Repository Methods (InventoryEventRepository)
- **`findByPartCodeAndCompanyId(String partCode, String companyId)`**: Finds inventory events by both part code and company ID

## Modified Functions/Components:

### 1. InventoryController
All endpoints now include `{companyId}` as a path parameter:
- **`POST /api/inventory/{companyId}/add`**: Added `@PathVariable String companyId` parameter
- **`POST /api/inventory/{companyId}/sell`**: Added `@PathVariable String companyId` parameter
- **`GET /api/inventory/{companyId}/partcode/{partCode}`**: Added `@PathVariable String companyId` parameter
- **`GET /api/inventory/{companyId}/partcode`**: Added `@PathVariable String companyId` parameter
- **`GET /api/inventory/{companyId}/events`**: Added `@PathVariable String companyId` parameter
- **`GET /api/inventory/{companyId}/events/{partCode}`**: Added `@PathVariable String companyId` parameter
- **`GET /api/inventory/{companyId}/alerts`**: Added `@PathVariable String companyId` parameter

### 2. AddInventoryRequest Model
- **Added field**: `private String companyId;` - Stores company ID for inventory operations

### 3. Partcode Model
- **Added field**: `private String companyId;` - Represents the company that owns the partcode

### 4. PartcodeEntity
- **Added field**: `@Column(name = "company_id", length = 50, nullable = true) private String companyId;` - Database column for company ID

### 5. PartcodeService
- **`addUnits(String companyId, AddInventoryRequest request)`**: 
  - Now accepts `companyId` as first parameter
  - Uses `findByPartCodeAndCompanyId` instead of `findByPartCode`
  - Sets `companyId` when creating new PartcodeEntity
  - Sets `companyId` in InventoryEventEntity for ADD events
  
- **`sellUnits(String companyId, SellInventoryRequest request)`**: 
  - Now accepts `companyId` as first parameter
  - Uses `findByPartCodeAndCompanyId` instead of `findByPartCode`
  - Sets `companyId` in InventoryEventEntity for SELL events (overrides request companyId to ensure consistency)
  
- **`getPartcodeByPartCode(String companyId, String partCode)`**: 
  - Now accepts `companyId` as first parameter
  - Uses `findByPartCodeAndCompanyId` to filter by company
  
- **`getAllPartcodes(String companyId)`**: 
  - Now accepts `companyId` parameter
  - Uses `findByCompanyId` to filter results by company
  
- **`getLowStockAlerts(String companyId)`**: 
  - Now accepts `companyId` parameter
  - Uses `findLowStockItemsByCompanyId` to filter alerts by company
  
- **`convertToModel(PartcodeEntity entity)`**: 
  - Now includes `companyId` in the converted Partcode model

### 6. InventoryEventService
- **`getAllEvents(String companyId)`**: 
  - Now accepts `companyId` parameter
  - Uses `findByCompanyId` to filter events by company
  
- **`getEventsByPartCode(String companyId, String partCode)`**: 
  - Now accepts `companyId` as first parameter
  - Uses `findByPartCodeAndCompanyId` to filter events by both part code and company

### 7. InvoiceService
- **`createInvoice(CreateInvoiceRequest request)`**: 
  - Updated call to `partcodeService.sellUnits()` to pass `request.getCompanyId()` as first parameter
  - Updated call to `partcodeService.getPartcodeByPartCode()` to pass `request.getCompanyId()` as first parameter
  
- **`updateInvoice(String invoiceId, CreateInvoiceRequest request)`**: 
  - Updated call to `partcodeService.getPartcodeByPartCode()` to pass `request.getCompanyId()` as first parameter

## Removed/Deprecated Code (if any):
None

## Example Usage or Impact:

### Before:
```java
// Endpoint: POST /api/inventory/add
// No company isolation - all companies could see each other's inventory
```

### After:
```java
// Endpoint: POST /api/inventory/{companyId}/add
// Company-specific inventory - each company only sees/manages their own inventory
```

### Example API Calls:

1. **Add Inventory:**
```bash
POST /api/inventory/COMPID123/add
{
  "partCode": "ENG001",
  "units": 10,
  "unitsPrice": 500.00,
  "supplierId": "SUP001"
}
```

2. **Get Partcode:**
```bash
GET /api/inventory/COMPID123/partcode/ENG001
```

3. **Get All Events:**
```bash
GET /api/inventory/COMPID123/events
```

### Database Schema Impact:
- A new column `company_id` of type VARCHAR(50) will be added to the `PARTCODE` table
- The column is nullable to support backward compatibility
- Existing partcodes without companyId will have NULL values
- New partcodes will be associated with a companyId

### Security & Data Isolation:
- All inventory operations are now scoped to a specific company
- Companies can only view and manage their own inventory
- Prevents cross-company data access
- Inventory events are also filtered by companyId

### Breaking Changes:
- **All InventoryController endpoints now require `companyId` as a path parameter**
- **Service method signatures have changed** - all methods now require `companyId` as a parameter
- **InvoiceService has been updated** to pass companyId when calling PartcodeService methods
- **API clients must update their endpoints** to include `{companyId}` in the URL path

### Migration Notes:
- Existing data in the `PARTCODE` table will have `NULL` values for `company_id`
- Consider running a migration script to assign company IDs to existing partcodes
- The `company_id` column is nullable, so the system will continue to work with existing data
