-- ============================================
-- Create PostgreSQL Role Script
-- ============================================
-- This script creates the 'postgres' role/user if it doesn't exist
-- 
-- IMPORTANT: You need to connect to PostgreSQL as a superuser to run this
-- 
-- To run this script:
-- 1. Connect to PostgreSQL as a superuser (usually the default user or 'postgres' superuser)
-- 2. Run: psql -U <superuser> -d postgres -f create_postgres_role.sql
-- 
-- OR connect via psql and run the commands manually:
-- psql -U <superuser> -h 139.84.210.248.vultrusercontent.com -p 5432 -d postgres
-- ============================================

-- Check if postgres role exists, if not create it
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'postgres') THEN
        CREATE ROLE postgres WITH LOGIN PASSWORD 'Pihu@1234' SUPERUSER CREATEDB CREATEROLE;
        RAISE NOTICE 'Role postgres created successfully';
    ELSE
        RAISE NOTICE 'Role postgres already exists';
    END IF;
END
$$;

-- Grant necessary privileges
ALTER ROLE postgres WITH LOGIN PASSWORD 'Pihu@1234';

-- Verify the role was created
SELECT rolname, rolsuper, rolcreatedb, rolcreaterole 
FROM pg_catalog.pg_roles 
WHERE rolname = 'postgres';
