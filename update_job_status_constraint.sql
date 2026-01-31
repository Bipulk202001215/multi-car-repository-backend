-- ============================================
-- Update Job Status Constraint Script
-- ============================================
-- This script updates the job_cards_status_check constraint to include COMPLETED status
-- 
-- To run this script:
-- psql -U postgres -h 139.84.210.248 -p 5432 -d appdb -f update_job_status_constraint.sql
-- ============================================

-- Step 1: Drop the existing constraint
ALTER TABLE public.job_cards DROP CONSTRAINT IF EXISTS job_cards_status_check;

-- Step 2: Recreate the constraint with all valid status values including COMPLETED
-- Using the same syntax format as the original constraint
ALTER TABLE public.job_cards 
ADD CONSTRAINT job_cards_status_check 
CHECK (((status)::text = ANY (ARRAY[
    ('PENDING'::character varying)::text, 
    ('IN_PROGRESS'::character varying)::text, 
    ('QC'::character varying)::text, 
    ('READY'::character varying)::text,
    ('COMPLETED'::character varying)::text
])));

-- Verify the constraint was created successfully
SELECT 
    conname AS constraint_name,
    pg_get_constraintdef(oid) AS constraint_definition
FROM pg_constraint
WHERE conname = 'job_cards_status_check'
AND conrelid = 'public.job_cards'::regclass;
