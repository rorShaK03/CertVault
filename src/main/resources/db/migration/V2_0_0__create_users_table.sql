create table if not exists "_users" (
    id               UUID,
    first_name       varchar(255),
    last_name        varchar(255),
    login            varchar(255) not null,
    password         varchar(1000) not null,
    role             varchar(255) not null,
    created_at       timestamp  not null default now(),
    primary key (id)
);

create table if not exists "_rights" (
    id               UUID,
    user_id          UUID not null,
    secret_id        UUID not null,
    role             varchar(255) not null,
    created_at       timestamp not null default now(),
    primary key (id)
);