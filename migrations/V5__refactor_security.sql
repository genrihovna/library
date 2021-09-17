alter table accounts drop foreign key users_roles_id_fk;

drop index users_roles_id_fk on accounts;

create index users_roles_id_fk
    on accounts (role_id);

alter table accounts drop column role_id;