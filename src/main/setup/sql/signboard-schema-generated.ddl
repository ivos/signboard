
    alter table project_member 
        drop constraint project_member__project;

    alter table project_member 
        drop constraint project_member__user;

    alter table project_member_roles 
        drop constraint project_member_roles__project_member;

    alter table task 
        drop constraint task__project;

    alter table task 
        drop constraint task__author;

    alter table user_system_roles 
        drop constraint user_system_roles__user;

    drop table project if exists;

    drop table project_member if exists;

    drop table project_member_roles if exists;

    drop table task if exists;

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

    create table project_member (
        id bigint not null,
        status varchar(32) not null,
        version integer not null,
        project varchar(64) not null,
        user bigint not null,
        primary key (id)
    );

    create table project_member_roles (
        project_member bigint not null,
        role varchar(32) not null,
        primary key (project_member, role)
    );

    create table task (
        id bigint not null,
        description varchar(1024),
        goal varchar(512) not null,
        time_created timestamp not null,
        version integer not null,
        author bigint not null,
        project varchar(64) not null,
        primary key (id)
    );

    create table user (
        id bigint not null,
        email varchar(100) not null unique,
        first_name varchar(100) not null,
        last_login timestamp,
        last_name varchar(100) not null,
        password varchar(100) not null,
        phone varchar(30),
        registered date not null,
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

    alter table project_member 
        add constraint project_member__project 
        foreign key (project) 
        references project;

    alter table project_member 
        add constraint project_member__user 
        foreign key (user) 
        references user;

    alter table project_member_roles 
        add constraint project_member_roles__project_member 
        foreign key (project_member) 
        references project_member;

    alter table task 
        add constraint task__project 
        foreign key (project) 
        references project;

    alter table task 
        add constraint task__author 
        foreign key (author) 
        references user;

    alter table user_system_roles 
        add constraint user_system_roles__user 
        foreign key (user) 
        references user;

    create sequence hibernate_sequence start with 1 increment by 1;
