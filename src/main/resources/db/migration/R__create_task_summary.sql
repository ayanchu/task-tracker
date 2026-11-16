-- ${build_timestamp}
-- Repeatable migration: create a view for tasks

CREATE OR REPLACE VIEW v_task_summary AS
SELECT
    id,
    title,
    description,
    done,
    created_date
FROM task;
