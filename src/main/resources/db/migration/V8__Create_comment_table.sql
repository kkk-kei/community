create table comment
(
    id BIGINT auto_increment,
    parent_id BIGINT not null,
    type int not null,
    creator int not null,
    gmt_create BIGINT not null,
    gmt_modify BIGINT not null,
    like_count BIGINT default 0,
    constraint COMMENT_PK
    primary key (id)
);