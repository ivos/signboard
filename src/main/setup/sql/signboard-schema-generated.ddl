
    drop table project if exists;

    drop table user if exists;

    drop sequence hibernate_sequence;

    create table project (
        code varchar(64) not null,
        date_created date not null,
        description varchar(1024),
        name varchar(128) not null,
        version integer not null,
        primary key (code)
    );

    create table user (
        id bigint not null,
        email varchar(100) not null unique,
        first_name varchar(100) not null,
        last_name varchar(100) not null,
        password varchar(100) not null,
        phone varchar(30),
        skype varchar(100),
        version integer not null,
        primary key (id)
    );

    create sequence hibernate_sequence start with 1 increment by 1;
