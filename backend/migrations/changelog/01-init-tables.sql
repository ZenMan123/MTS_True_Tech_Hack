--changeset RuslanHkmoff:create-users-table
create table if not exists users
(
    user_id           bigint generated always as identity,
    name         varchar(100) NOT NULL,
    surname      varchar(100) NOT NULL,
    phone_number varchar(12)  NOT NULL,
    balance      numeric,
    primary key (user_id),
    unique (phone_number)
)