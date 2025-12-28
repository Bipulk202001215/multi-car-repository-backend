-- ============================================
-- Create SUPER_ADMIN User
-- ============================================
-- This script creates a SUPER_ADMIN user
-- 
-- Credentials:
-- Email: superadmin@multicar.com
-- Password: SuperAdmin@123
-- 
-- IMPORTANT: Replace the password hash below with a valid BCrypt hash.
-- To generate the hash, use one of these methods:
-- 
-- Method 1 (Recommended): Use the API endpoint POST /api/users first
-- Method 2: Use online BCrypt generator: https://bcrypt-generator.com/ (10 rounds)
-- Method 3: Run the generate_hash.sh script in this directory
-- ============================================

-- Insert SUPER_ADMIN user
-- NOTE: You must replace the password hash with a valid BCrypt hash
-- The hash below is a placeholder and will NOT work for authentication
INSERT INTO "UserEntity" (
    "USID",
    "email_id",
    "password",
    "company_id",
    "user_type",
    "created_on",
    "updated_on"
) VALUES (
    'USID' || TO_CHAR(CURRENT_TIMESTAMP, 'YYMMDDHH24MISS'),
    'superadmin@multicar.com',
    '$2a$10$REPLACE_WITH_BCRYPT_HASH_FOR_PASSWORD_SuperAdmin@123', -- TODO: Replace this with actual BCrypt hash
    NULL, -- SUPER_ADMIN does not belong to any company
    'SUPER_ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT ("email_id") DO UPDATE
SET
    "password" = EXCLUDED."password",
    "user_type" = EXCLUDED."user_type",
    "company_id" = NULL,
    "updated_on" = CURRENT_TIMESTAMP;

-- Verify the user was created
SELECT 
    "USID" as user_id,
    "email_id" as email,
    "user_type",
    "company_id",
    "created_on"
FROM "UserEntity"
WHERE "email_id" = 'superadmin@multicar.com';
