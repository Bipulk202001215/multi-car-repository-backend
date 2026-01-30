-- Grant all necessary permissions to postgres user on appdb database
-- Run this as a superuser connected to the postgres database

-- Connect to appdb and grant schema permissions
\c appdb

-- Grant all privileges on database
GRANT ALL PRIVILEGES ON DATABASE appdb TO postgres;

-- Grant all privileges on schema
GRANT ALL ON SCHEMA public TO postgres;
ALTER SCHEMA public OWNER TO postgres;

-- Grant privileges on all existing tables
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;

-- Grant privileges on future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO postgres;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO postgres;

-- Verify permissions
SELECT 
    datname,
    datacl
FROM pg_database 
WHERE datname = 'appdb';

SELECT 
    schema_name,
    schema_owner
FROM information_schema.schemata
WHERE schema_name = 'public';
