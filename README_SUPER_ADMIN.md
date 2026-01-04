# Creating SUPER_ADMIN User

## Recommended Method: Using the API

The easiest way to create a SUPER_ADMIN user is through the API endpoint, which automatically handles password encryption:

### Using Postman Collection

1. Import the Postman collection if you haven't already
2. First, make sure the application is running
3. Go to **Users > Create User**
4. Use the following request body:

```json
{
  "emailId": "superadmin@multicar.com",
  "password": "SuperAdmin@123",
  "userType": "SUPER_ADMIN"
}
```

**Note:** Since this is before login, you may need to temporarily allow this endpoint or use a different authentication method.

### Using cURL

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "emailId": "superadmin@multicar.com",
    "password": "SuperAdmin@123",
    "userType": "SUPER_ADMIN"
  }'
```

## Alternative Method: Using SQL Script

If you prefer to use SQL directly:

1. **First, generate a BCrypt hash** for the password "SuperAdmin@123":
   - Use online generator: https://bcrypt-generator.com/ (set rounds to 10)
   - Or use the Spring Boot application's BCryptPasswordEncoder

2. **Edit `create_super_admin.sql`** and uncomment the INSERT statement

3. **Replace the password hash** with the generated BCrypt hash

4. **Run the SQL script** against your database

## After Creating the User

Once the user is created, you can login using:

- **Email**: `superadmin@multicar.com`
- **Password**: `SuperAdmin@123`

Use the **Authentication > Login** endpoint in Postman or:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "emailId": "superadmin@multicar.com",
    "password": "SuperAdmin@123"
  }'
```

The response will include a JWT token that you can use for authenticated requests.

## Important Notes

- SUPER_ADMIN users do not belong to any company (company_id is NULL)
- The password is encrypted using BCrypt with 10 rounds
- The user ID is auto-generated in the format: USID + YYMMDDHHMMSS
- Make sure to change the default password in production!





