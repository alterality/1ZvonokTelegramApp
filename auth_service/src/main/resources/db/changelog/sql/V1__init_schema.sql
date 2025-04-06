
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(30) NOT NULL UNIQUE,
                       password VARCHAR(80) NOT NULL,
                       email VARCHAR(50) UNIQUE
);

CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL
);

CREATE TABLE users_roles (
                             user_id BIGINT NOT NULL,
                             role_id INT NOT NULL,
                             PRIMARY KEY (user_id, role_id),
                             FOREIGN KEY (user_id) REFERENCES users(id),
                             FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE refresh_tokens (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL,
                                token TEXT NOT NULL,
                                FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO roles (name) VALUES
                             ('ROLE_USER'),
                             ('ROLE_ADMIN');

INSERT INTO users (username, password, email) VALUES
                                                  ('user', '$2a$12$MTbrzkxTGAnaeKoL.1QSPO.LsJm7NqnK.GhDjqD8dfPqrQCVxEzjy', 'user@exaple.com'),
                                                  ('admin', '$2a$12$iTcsff1KeAnxWXuPjefetOLdrbJ8nttIJ16FS0Avg1cIiTcS2Phpe', 'admin@example.com');

INSERT INTO users_roles (user_id, role_id) VALUES
                                               (1, 1),
                                               (2, 2);
