create table if not exists "_keys" (
    id            UUID,
    version_id    UUID,
    secret        varchar(10000) not null,
    created_at    timestamp    not null default now(),
    primary key (id)
);

create table if not exists "_certificates" (
    id            UUID,
    version_id    UUID,
    secret        varchar(10000) not null,
    created_at    timestamp    not null default now(),
    primary key (id)
);