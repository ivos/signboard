
    drop table user if exists;

    drop sequence hibernate_sequence;

    create table user (
        id bigint not null,
        email varchar(100) not null unique,
        first_name varchar(100),
        last_name varchar(100),
        password varchar(100) not null,
        phone varchar(30),
        skype varchar(100),
        version integer not null,
        primary key (id)
    );

    create sequence hibernate_sequence start with 1 increment by 1;
