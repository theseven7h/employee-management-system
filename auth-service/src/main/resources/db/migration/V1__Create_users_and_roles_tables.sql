CREATE TABLE roles (
   id BIGSERIAL PRIMARY KEY,
   name VARCHAR(50) NOT NULL UNIQUE,
   description VARCHAR(255)
);

CREATE TABLE users (
   id BIGSERIAL PRIMARY KEY,
   email VARCHAR(255) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   first_name VARCHAR(100) NOT NULL,
   last_name VARCHAR(100) NOT NULL,
   enabled BOOLEAN NOT NULL DEFAULT TRUE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles(role_id);

INSERT INTO roles (name, description) VALUES
  ('ROLE_ADMIN', 'Administrator with full system access'),
  ('ROLE_MANAGER', 'Manager with departmental access'),
  ('ROLE_EMPLOYEE', 'Regular employee with limited access');

INSERT INTO users (email, password, first_name, last_name, enabled) VALUES
    ('admin@darumng.com', '$2a$10$xVYBqFZzXyD3pD3X7mI7W.Z8YqXl2pG5yEfZhVqHqJYcXmNLQzP/e', 'System', 'Admin', TRUE);

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);