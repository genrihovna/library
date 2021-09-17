CREATE TABLE library_management_system.users
(
    id       BIGINT auto_increment PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NULL
);

CREATE TABLE library_management_system.books
(
    id     BIGINT auto_increment PRIMARY KEY,
    author VARCHAR(255) NULL,
    date   SMALLINT NULL,
    title  VARCHAR(255) NULL,
    user   BIGINT NULL,
    CONSTRAINT books_readers
        FOREIGN KEY (user) REFERENCES library_management_system.users (id)
);



