create table IF NOT EXISTS bank_user
(
    user_id   uuid,
    name varchar2 not null,
    constraint bank_user_pk
        primary key (user_id)
);

create table IF NOT EXISTS user_account
(
    account_id      uuid,
    pin     varchar2(4) not null,
    user_id uuid,
    account_number varchar(16),
    money bigint default 0 not null,
    constraint ACCOUNT_PK
        primary key (account_id)
);

create unique index ACCOUNTS_USER_ID_UINDEX
    on user_account (user_id);

alter table user_account
    add constraint user_foreign
        foreign key (USER_ID) references bank_user;

