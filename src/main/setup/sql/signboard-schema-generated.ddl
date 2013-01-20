
    alter table user_system_roles 
        drop constraint user_system_roles__user;

    drop table project if exists;

    drop table user if exists;

    drop table user_system_roles if exists;

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
        status varchar(32) not null,
        version integer not null,
        primary key (id)
    );

    create table user_system_roles (
        user bigint not null,
        system_role varchar(32) not null,
        primary key (user, system_role)
    );

    alter table user_system_roles 
        add constraint user_system_roles__user 
        foreign key (user) 
        references user;

    create sequence hibernate_sequence start with 1 increment by 1;
