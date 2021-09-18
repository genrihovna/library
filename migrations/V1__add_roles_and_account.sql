CREATE TABLE best_library.roles
(
    id          BIGINT AUTO_INCREMENT,
    name        VARCHAR(250) NOT NULL,
    description INT          NULL,
    CONSTRAINT roles_pk
        PRIMARY KEY (id)
);

CREATE UNIQUE INDEX roles_name_uindex
    ON best_library.roles (name);

CREATE TABLE best_library.accounts
(
    id       BIGINT AUTO_INCREMENT,
    username VARCHAR(250) NOT NULL,
    password VARCHAR(512) NOT NULL,
    CONSTRAINT account_pk
        PRIMARY KEY (id)
);

CREATE UNIQUE INDEX accounts_username_uindex
    ON best_library.accounts (username);

ALTER TABLE best_library.users
    DROP COLUMN password;

DROP INDEX username ON best_library.users;

ALTER TABLE best_library.users
    DROP COLUMN username;

RENAME TABLE best_library.users TO readers;

ALTER TABLE best_library.readers
    ADD name VARCHAR(250) NOT NULL;

CREATE UNIQUE INDEX username
    ON best_library.readers (name);

ALTER TABLE best_library.readers
    ADD CONSTRAINT name
        UNIQUE (name);

ALTER TABLE best_library.accounts
    ADD role_id BIGINT NOT NULL;

ALTER TABLE best_library.accounts
    ADD CONSTRAINT users_roles_id_fk
        FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE readers
    ADD account_id BIGINT NOT NULL;

CREATE UNIQUE INDEX readers_account_id_uindex
    ON readers (account_id);

ALTER TABLE readers
    ADD CONSTRAINT readers_accounts_id_fk
        FOREIGN KEY (account_id) REFERENCES accounts (id);
