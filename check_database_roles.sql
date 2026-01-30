-- ============================================
-- Check Existing Database Roles
-- ============================================
-- This script lists all existing roles/users in PostgreSQL
-- 
-- To run this script:
-- psql -U <any_existing_user> -h 139.84.210.248.vultrusercontent.com -p 5432 -d postgres -f check_database_roles.sql
-- ============================================

-- List all roles
SELECT 
    rolname as role_name,
    rolsuper as is_superuser,
    rolcreatedb as can_create_db,
    rolcreaterole as can_create_role,
    rolcanlogin as can_login
FROM pg_catalog.pg_roles
ORDER BY rolname;

-- Check current user
SELECT current_user as current_connected_user;
