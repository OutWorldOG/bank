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

create table IF NOT EXISTS transaction_history
(
    history_id uuid,
    account_id uuid,
    date       TIMESTAMP not null,
    operation_type  varchar2(36) not null,
    amount bigint not null,
    constraint TRANSACTION_HISTORY_PK
        primary key (history_id),
    constraint TRANSACTION_HISTORY_USER_ACCOUNT_ACCOUNT_ID_FK
        foreign key (account_id) references user_account (account_id)
);

create unique index ACCOUNTS_USER_ID_UINDEX
    on user_account (user_id);

alter table user_account
    add constraint user_foreign
        foreign key (USER_ID) references bank_user;

