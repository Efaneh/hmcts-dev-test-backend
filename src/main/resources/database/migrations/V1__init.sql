CREATE SCHEMA IF NOT EXISTS core;

--Would usually override or not generate users in a migration.
CREATE USER app_user WITH PASSWORD 'REPLACE_ME_SECURELY';

GRANT USAGE ON SCHEMA core TO app_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA core
GRANT SELECT, DELETE, INSERT, UPDATE ON TABLES TO app_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA core
GRANT USAGE, SELECT ON SEQUENCES TO app_user;

CREATE TABLE core.tasks (
    id SERIAL PRIMARY KEY,
    case_number TEXT NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    status TEXT NOT NULL,
    created_date TIMESTAMP DEFAULT NOW(),
    due_date TIMESTAMP NOT NULL
);
