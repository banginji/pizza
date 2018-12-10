drop table if exists user;

create table users(
    username varchar(25) not null primary key,
    password varchar(75) not null,
    enabled boolean not null
);

drop table if exists authorities;

create table authorities(
    username varchar(25) not null,
    authority varchar(25) not null,
    constraint fk_authorities_users foreign key (username) references users(username)
);