-- ============================================
-- Create appdb Database Script
-- ============================================
-- This script creates the 'appdb' database if it doesn't exist
-- 
-- IMPORTANT: You need to connect to PostgreSQL as a superuser to run this
-- 
-- To run this script:
-- psql -U postgres -h 139.84.210.248.vultrusercontent.com -p 5432 -d postgres -f create_appdb_database.sql
-- ============================================

-- Check if appdb database exists, if not create it
SELECT 'CREATE DATABASE appdb'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'appdb')\gexec

-- If the above doesn't work, use this alternative:
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'appdb') THEN
        PERFORM dblink_exec('dbname=postgres', 'CREATE DATABASE appdb');
    END IF;
END
$$;

-- Alternative simpler approach (run this if above doesn't work):
-- CREATE DATABASE appdb;

-- Grant privileges to postgres user
GRANT ALL PRIVILEGES ON DATABASE appdb TO postgres;

-- Verify the database was created
SELECT datname, datcollate, datctype 
FROM pg_database 
WHERE datname = 'appdb';
