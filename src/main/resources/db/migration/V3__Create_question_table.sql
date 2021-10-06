create table question
(
    id int auto_increment,
    title VARCHAR(50),
    description text,
    gmt_create BIGINT,
    gmt_modify BIGINT,
    creator int,
    comment_count int default 0,
    view_count int default 0,
    like_count int default 0,
    tag VARCHAR(256),
    constraint QUESTION_PK
        primary key (id)
);