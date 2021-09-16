create table library_management_system.users
(
    id       BIGINT auto_increment primary key,
    username varchar(255) not null unique,
    password varchar(255) null
);

create table library_management_system.books
(
    id     BIGINT auto_increment primary key,
    author varchar(255) null,
    date   smallint null,
    title  varchar(255) null,
    user BIGINT null,
    constraint books_readers
        foreign key (user) references library_management_system.users (id)
);



