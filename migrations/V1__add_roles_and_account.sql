CREATE TABLE library_management_system.roles
(
    id          BIGINT AUTO_INCREMENT,
    name        VARCHAR(250) NOT NULL,
    description INT          NULL,
    CONSTRAINT roles_pk
        PRIMARY KEY (id)
);

CREATE UNIQUE INDEX roles_name_uindex
    ON library_management_system.roles (name);

CREATE TABLE library_management_system.accounts
(
    id       BIGINT AUTO_INCREMENT,
    username VARCHAR(250) NOT NULL,
    password VARCHAR(512) NOT NULL,
    CONSTRAINT account_pk
        PRIMARY KEY (id)
);

CREATE UNIQUE INDEX accounts_username_uindex
    ON library_management_system.accounts (username);

ALTER TABLE library_management_system.users
    DROP COLUMN password;

DROP INDEX username ON library_management_system.users;

ALTER TABLE library_management_system.users
    DROP COLUMN username;

RENAME TABLE library_management_system.users TO readers;

ALTER TABLE library_management_system.readers
    ADD name VARCHAR(250) NOT NULL;

CREATE UNIQUE INDEX username
    ON library_management_system.readers (name);

ALTER TABLE library_management_system.readers
    ADD CONSTRAINT name
        UNIQUE (name);

ALTER TABLE library_management_system.accounts
    ADD role_id BIGINT NOT NULL;

ALTER TABLE library_management_system.accounts
    ADD CONSTRAINT users_roles_id_fk
        FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE readers
    ADD account_id BIGINT NOT NULL;

CREATE UNIQUE INDEX readers_account_id_uindex
    ON readers (account_id);

ALTER TABLE readers
    ADD CONSTRAINT readers_accounts_id_fk
        FOREIGN KEY (account_id) REFERENCES accounts (id);
