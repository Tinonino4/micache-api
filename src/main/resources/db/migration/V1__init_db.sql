CREATE TABLE users (
  id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  is_enabled BOOLEAN NOT NULL,
  account_no_expired BOOLEAN NOT NULL,
  account_no_locked BOOLEAN NOT NULL,
  credential_no_expired BOOLEAN NOT NULL
);

CREATE TABLE users_profiles (
  id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  user_id BIGINT UNIQUE NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  city VARCHAR(255),
  birthday VARCHAR(255),
  zipcode VARCHAR(255),
  phone_number VARCHAR(255),
  jobTitle VARCHAR(255),
  education VARCHAR(255),
  CONSTRAINT fk_user_profile_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE roles (
  id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  role_name VARCHAR(255) NOT NULL UNIQUE,
  CONSTRAINT fk_role_enum CHECK (role_name IN ('ADMIN', 'USER', 'COMPANY'))
);

CREATE TABLE permissions (
  id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  name VARCHAR(255) UNIQUE NOT NULL CHECK (name <> ''), -- Enforces non-empty name
  CONSTRAINT unique_permission_name_combo UNIQUE (name) -- Additional unique constraint (optional)
);
INSERT INTO permissions (name) VALUES ('CREATE');
INSERT INTO permissions (name) VALUES ('READ');
INSERT INTO permissions (name) VALUES ('UPDATE');
INSERT INTO permissions (name) VALUES ('DELETE');

INSERT INTO roles (role_name) VALUES ('ADMIN'), ('USER'), ('COMPANY');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
INNER JOIN permissions p ON p.name = 'CREATE'
WHERE r.role_name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
INNER JOIN permissions p ON p.name = 'READ'
WHERE r.role_name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
INNER JOIN permissions p ON p.name = 'UPDATE'
WHERE r.role_name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
INNER JOIN permissions p ON p.name = 'DELETE'
WHERE r.role_name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
INNER JOIN permissions p ON (p.name IN ('CREATE', 'UPDATE', 'DELETE'))
WHERE r.role_name = 'USER';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
INNER JOIN permissions p ON p.name = 'CREATE'
WHERE r.role_name = 'COMPANY';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
INNER JOIN permissions p ON p.name = 'READ'
WHERE r.role_name = 'COMPANY';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
INNER JOIN permissions p ON p.name = 'UPDATE'
WHERE r.role_name = 'COMPANY';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
INNER JOIN permissions p ON p.name = 'DELETE'
WHERE r.role_name = 'COMPANY';

