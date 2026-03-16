-- ============================================================
-- V1__init.sql  — TaskFlow initial schema
-- ============================================================

-- ---- Projects -----------------------------------------------
CREATE TABLE projects (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    description TEXT,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- ---- Enums --------------------------------------------------
CREATE TYPE task_status AS ENUM ('TODO', 'IN_PROGRESS', 'DONE');
CREATE TYPE task_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH');

-- ---- Tasks --------------------------------------------------
CREATE TABLE tasks (
    id          BIGSERIAL     PRIMARY KEY,
    title       VARCHAR(300)  NOT NULL,
    description TEXT,
    status      task_status   NOT NULL DEFAULT 'TODO',
    priority    task_priority NOT NULL DEFAULT 'MEDIUM',
    project_id  BIGINT        NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

-- ---- Indexes ------------------------------------------------
CREATE INDEX idx_tasks_project_id ON tasks(project_id);
CREATE INDEX idx_tasks_status     ON tasks(status);
CREATE INDEX idx_tasks_priority   ON tasks(priority);
