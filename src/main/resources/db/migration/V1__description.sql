CREATE SEQUENCE if not exists userInfo_seq minvalue 10;
CREATE SEQUENCE if not exists user_seq  minvalue 10;
CREATE SEQUENCE if not exists category_seq minvalue 10;
CREATE SEQUENCE if not exists charity_seq minvalue 10;
CREATE SEQUENCE if not exists complaint_seq minvalue 10;
CREATE SEQUENCE if not exists gift_seq minvalue 10;
CREATE SEQUENCE if not exists holiday_seq minvalue 10;
CREATE SEQUENCE if not exists mailingList_seq minvalue 10;
CREATE SEQUENCE if not exists notification_seq minvalue 10;
CREATE SEQUENCE if not exists subCategory_seq minvalue 10;
CREATE SEQUENCE if not exists wish_seq minvalue 10;


create table user_info
(
    id            bigserial
        primary key,
    clothing_size varchar(255),
    country       varchar(255),
    date_of_birth date,
    hobby         varchar(255),
    important     varchar(255),
    phone_number  varchar(255),
    photo         varchar(255),
    shoe_size     integer
);

create table users
(
    id           bigserial
        primary key,
    email        varchar(255)
        constraint akjghfuewhb
            unique,
    first_name   varchar(255),
    is_block     boolean,
    last_name    varchar(255),
    password     varchar(255),
    image        varchar(255),
    role         varchar(255),
    user_info_id bigint
        constraint kjahfhnds
            references user_info
);

create table categories
(
    id   bigserial not null
        primary key,
    name varchar(255)
);

create table sub_category
(
    id          bigserial not null
        primary key,
    name        varchar(255),
    category_id bigint
        constraint fk76nk9rvg73ac82lf80hcy17r8
            references categories
);

create table charity
(
    id              bigserial not null
        primary key,
    charity_status  varchar(255),
    condition       varchar(255),
    created_date    date,
    description     varchar(10000),
    image           varchar(10000),
    name            varchar(255),
    category_id     bigint
        constraint fkponabnxqllr11bxe462sa9io2
            references categories,
    reservoir_id    bigint
        constraint fks7wkpbk5xb0gbnygbgvmaoumd
            references users,
    sub_category_id bigint
        constraint fk5kq4r60352olku7duj07rv36d
            references sub_category,
    user_id         bigint
        constraint fkck5bwpe6sa5d5yt1cq6w1lcg1
            references users
);

create table holidays
(
    id              bigserial not null
        primary key,
    date_of_holiday date,
    image           varchar(10000),
    name            varchar(255),
    user_id         bigint
        constraint fk4e2a1pjumo9ugc2dhwb4d8ec4
            references users
);

create table users_friends
(
    user_id    bigserial not null
        constraint fkry5pun2eg852sbl2l50p236bo
            references users,
    friends_id bigint    not null
        constraint fko51ktjiheso8mkdd5n4pdf9f3
            references users
);

create table users_requests
(
    user_id     bigserial not null
        constraint fka7n7kbuu8vjwbxu847t5e6vqo
            references users,
    requests_id bigint    not null
        constraint fkkonj41a37puqia0uw28yoepc7
            references users
);

create table wishes
(
    id              bigserial not null
        primary key,
    date_of_holiday date,
    description     varchar(10000),
    image           varchar(10000),
    is_block        boolean,
    link_to_gift    varchar(255),
    wish_name       varchar(255),
    wish_status     varchar(255),
    holiday_id      bigint
        constraint fki01jsv3i2mnuiat2yyv3wvg06
            references holidays,
    reservoir_id    bigint
        constraint fkgixwvlvci2fkobydfeavq26le
            references users,
    user_id         bigint
        constraint fkh4fwumji30i8f8lt9gnhqxjy7
            references users
);


create table gift
(
    id      bigint not null
        primary key,
    user_id bigint
        constraint fkrv0c4mo0tryp2o8skng8k5qgt
            references users,
    wish_id bigint
        constraint fkjbg1jmvmb3hk35pk02p62fbk4
            references wishes
);
create table complaints
(
    id            bigserial not null
        primary key,
    created_at    date,
    is_seen       boolean,
    reason_text   varchar(10000),
    complainer_id bigint
        constraint fkekg8b18ywunyibblqamrmeltw
            references users,
    wish_id       bigint
        constraint fk3cg48rba8aw9jorg8fnsf8o68
            references wishes
);

create table notification
(
    id                bigserial not null
        primary key,
    created_date      date,
    is_seen           boolean,
    notification_type varchar(255),
    from_user_id      bigint
        constraint fk32miscyb8msuwy1yn4a1pqpuf
            references users,
    gift_id           bigint
        constraint fktcr6ygd4i1sboeklnoybkhyav
            references gift,
    user_id           bigint
        constraint fknk4ftb5am9ubmkv1661h15ds9
            references users,
    wish_id           bigint
        constraint fkhou2srdvoxuspgjv7iyen8r1i
            references wishes
);

create table notification_complaints
(
    notification_id bigserial not null
        constraint fkpv9u5dqqcwj9cwmv0v0ex2qvg
            references notification,
    complaints_id   bigint    not null
        constraint uk_6djw265nyjgdq3jj9ef4cfao4
            unique
        constraint fkb79xv568l6tgnc5mnt3vy00mc
            references complaints
);

create table mailing_list
(
    id         bigserial not null
        primary key,
    created_at timestamp,
    image      varchar(255),
    name       varchar(255),
    text       varchar(10000)
);