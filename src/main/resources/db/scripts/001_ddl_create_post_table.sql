create table IF NOT EXISTS post(
    id bigint,
    title text,
    link text unique,
    description text,
    created timestamp
);
