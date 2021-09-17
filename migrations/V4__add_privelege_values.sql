CREATE TABLE privileges
(
    id   BIGINT       NOT NULL,
    name VARCHAR(512) NOT NULL,
    CONSTRAINT privilege_pk
        PRIMARY KEY (id)
);

ALTER TABLE privileges
    MODIFY id BIGINT AUTO_INCREMENT;

CREATE UNIQUE INDEX privilege_name_uindex
    ON privileges (name);

CREATE TABLE accounts_roles
(
    account_id BIGINT NOT NULL,
    role_id    BIGINT NOT NULL,
    CONSTRAINT accounts_roles_pk
        PRIMARY KEY (role_id, account_id),
    CONSTRAINT accounts_roles_accounts_id_fk
        FOREIGN KEY (account_id) REFERENCES accounts (id),
    CONSTRAINT accounts_roles_roles_id_fk
        FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE roles_priveleges
(
    role_id      BIGINT NOT NULL,
    privilege_id BIGINT NOT NULL,
    CONSTRAINT roles_priveleges_pk
        PRIMARY KEY (role_id, privilege_id),
    CONSTRAINT roles_priveleges_privilege_id_fk
        FOREIGN KEY (privilege_id) REFERENCES privileges (id),
    CONSTRAINT roles_priveleges_role_id_fk
        FOREIGN KEY (role_id) REFERENCES roles (id)
);