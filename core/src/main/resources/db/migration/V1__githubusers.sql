create table role (
    id bigint(20) NOT NULL,
    authority varchar(25) NOT NULL,
    primary key(id)
);


create table githubusers(
    id bigint(20) not null,
    email varchar(400) not null,
    avatar_url varchar(255) default null,
    url varchar(255) default null,
    name varchar(200) default null,
    login varchar(255) default null,
    primary key(id),
    unique key(email)
);

create table githubuser_role(
    user_id bigint(20),
    role_id bigint(20),
    constraint fk_user_role_user FOREIGN KEY (user_id) references githubusers(id),
    constraint fk_user_role_role FOREIGN KEY (role_id) REFERENCES role(id),
    unique key(user_id, role_id)
);

insert into role(id, authority) values (1, 'ROLE_ADMIN');
insert into role(id, authority) values (2, 'ROLE_USER');