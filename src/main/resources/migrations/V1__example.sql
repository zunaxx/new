drop sequence if exists category_seq cascade;

drop sequence if exists cheque_seq cascade;

drop sequence if exists menu_seq cascade;

drop sequence if exists restaurant_seq cascade;

drop sequence if exists stop_list_seq cascade;

drop sequence if exists subcategory_seq cascade;

drop sequence if exists answer_seq cascade;

drop sequence if exists option_seq cascade;

drop sequence if exists question_seq cascade;

drop sequence if exists result_seq cascade;

drop sequence if exists test_seq cascade;

drop sequence if exists user_seq cascade;

drop table if exists options_users cascade;

drop table if exists options cascade;

drop table if exists results cascade;

drop table if exists answers cascade;

drop table if exists questions cascade;

drop table if exists users_tests cascade;

drop table if exists tests cascade;

drop table if exists users cascade;

create sequence if not exists answer_seq
    start with 10;

alter sequence answer_seq owner to postgres;

create sequence if not exists option_seq
    start with 19;

alter sequence option_seq owner to postgres;

create sequence if not exists question_seq
    start with 10;

alter sequence question_seq owner to postgres;

create sequence if not exists result_seq
    start with 5;

alter sequence result_seq owner to postgres;

create sequence if not exists test_seq
    start with 4;

alter sequence test_seq owner to postgres;

create sequence if not exists user_seq
    start with 6;

alter sequence user_seq owner to postgres;


create table if not exists tests
(
    id          bigint not null
        primary key,
    description varchar(255),
    duration    integer,
    enable      boolean,
    title       varchar(255),
    updated_at  timestamp(6) with time zone
);

alter table tests
    owner to postgres;

create table  if not exists questions
(
    id             bigint not null
        primary key,
    attempts       integer,
    correct_answer varchar(255),
    duration       integer,
    enable         boolean,
    file_url       varchar(255),
    passage        varchar(15000),
    question_type  varchar(255)
        constraint questions_question_type_check
            check ((question_type)::text = ANY
                   ((ARRAY ['SELECT_REAL_ENGLISH_WORD'::character varying, 'LISTEN_AND_SELECT_ENGLISH_WORDS'::character varying, 'TYPE_WHAT_YOU_HEAR'::character varying, 'DESCRIBE_IMAGE'::character varying, 'RECORD_SAYING_STATEMENT'::character varying, 'RESPOND_AT_LEAST_N_WORDS'::character varying, 'HIGHLIGHT_THE_ANSWER'::character varying, 'SELECT_THE_MAIN_IDEA'::character varying, 'SELECT_THE_BEST_TITLE'::character varying])::text[])),
    statement      varchar(2000),
    title          varchar(255),
    updated_at     timestamp(6) with time zone,
    test_id        bigint
        constraint fkoc6xkgj16nhyyes4ath9dyxxw
            references tests
);

alter table questions
    owner to postgres;

create table  if not exists options
(
    id          bigint not null
        primary key,
    audio_url   varchar(255),
    is_true     boolean,
    title       varchar(255),
    question_id bigint
        constraint fk5bmv46so2y5igt9o9n9w4fh6y
            references questions
);

alter table options
    owner to postgres;

create table  if not exists users
(
    id         bigint not null
        primary key,
    email      varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    role       varchar(255)
        constraint users_role_check
            check ((role)::text = ANY ((ARRAY ['ADMIN'::character varying, 'USER'::character varying])::text[]))
);

alter table users
    owner to postgres;

create table if not exists answers
(
    id                 bigint not null
        primary key,
    audio_file         varchar(255),
    date_of_submission timestamp(6) with time zone,
    question_type      varchar(255)
        constraint answers_question_type_check
            check ((question_type)::text = ANY
                   ((ARRAY ['SELECT_REAL_ENGLISH_WORD'::character varying, 'LISTEN_AND_SELECT_ENGLISH_WORDS'::character varying, 'TYPE_WHAT_YOU_HEAR'::character varying, 'DESCRIBE_IMAGE'::character varying, 'RECORD_SAYING_STATEMENT'::character varying, 'RESPOND_AT_LEAST_N_WORDS'::character varying, 'HIGHLIGHT_THE_ANSWER'::character varying, 'SELECT_THE_MAIN_IDEA'::character varying, 'SELECT_THE_BEST_TITLE'::character varying])::text[])),
    statement          varchar(15000),
    question_id        bigint
        constraint fk3erw1a3t0r78st8ty27x6v3g1
            references questions,
    user_id            bigint
        constraint fk5bp3d5loftq2vjn683ephn75a
            references users
);

alter table answers
    owner to postgres;

create table if not exists options_users
(
    options_id bigint not null
        constraint fk68u1scvcjxda17boyvkaegavi
            references options,
    users_id   bigint not null
        constraint fk1cgrf3tm73r1sat3odjdyr0ut
            references users
);

alter table options_users
    owner to postgres;

create table  if not exists results
(
    id                 bigint not null
        primary key,
    checked            boolean,
    date_of_submission timestamp(6) with time zone,
    final_score        varchar(255),
    score              varchar(255),
    answer_id          bigint
        constraint uk_8vov9xpkdoauvsg0xcdafjos
            unique
        constraint fkktiemhrq6jeoscavpe3bhc6mx
            references answers
);

alter table results
    owner to postgres;

create table if not exists users_tests
(
    users_id bigint not null
        constraint fksx9katadvft4pjg8n976bym7s
            references users,
    tests_id bigint not null
        constraint fkrck1ugtbi0mgk4ym4gwp37uam
            references tests
);

alter table users_tests
    owner to postgres;

