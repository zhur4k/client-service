CREATE TABLE t_client (
    id UUID PRIMARY KEY,
    c_name TEXT,
    c_phone TEXT,
    c_created_at TIMESTAMP,
    c_updated_at TIMESTAMP
);

CREATE TYPE task_status AS ENUM ('pending', 'in_progress', 'completed');

CREATE TABLE t_task (
    id UUID PRIMARY KEY,
    c_client_id UUID REFERENCES t_client(id),
    c_title TEXT,
    c_description TEXT,
    c_status task_status,
    c_created_at TIMESTAMP,
    c_updated_at TIMESTAMP
);
