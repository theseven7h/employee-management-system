CREATE TABLE departments (
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR(100) NOT NULL UNIQUE,
     description VARCHAR(500),
     manager_id BIGINT,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE employees (
       employee_id BIGSERIAL PRIMARY KEY,
       first_name VARCHAR(100) NOT NULL,
       last_name VARCHAR(100) NOT NULL,
       email VARCHAR(255) NOT NULL UNIQUE,
       department_id BIGINT,
       status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL,
       CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'ON_LEAVE', 'TERMINATED'))
);

CREATE INDEX idx_employees_email ON employees(email);
CREATE INDEX idx_employees_department_id ON employees(department_id);
CREATE INDEX idx_employees_status ON employees(status);
CREATE INDEX idx_departments_name ON departments(name);
CREATE INDEX idx_departments_manager_id ON departments(manager_id);

INSERT INTO departments (name, description, manager_id) VALUES
    ('Engineering', 'Software development and engineering', NULL),
    ('Human Resources', 'HR and recruitment', NULL),
    ('Finance', 'Financial planning and accounting', NULL),
    ('Marketing', 'Marketing and communications', NULL),
    ('Operations', 'Business operations', NULL);

INSERT INTO employees (first_name, last_name, email, department_id, status) VALUES
    ('Alice', 'Johnson', 'alice.johnson@company.com', 1, 'ACTIVE'),
    ('Bob', 'Williams', 'bob.williams@company.com', 1, 'ACTIVE'),
    ('Carol', 'Brown', 'carol.brown@company.com', 2, 'ACTIVE'),
    ('David', 'Miller', 'david.miller@company.com', 3, 'ACTIVE'),
    ('Eve', 'Davis', 'eve.davis@company.com', 4, 'ACTIVE');

UPDATE departments SET manager_id = 1 WHERE name = 'Engineering';
UPDATE departments SET manager_id = 3 WHERE name = 'Human Resources';
UPDATE departments SET manager_id = 4 WHERE name = 'Finance';