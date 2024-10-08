create table "role"
(
    "id"         bigint generated by default as identity,
    "active"     boolean                     not null,
    "identifier" varchar(30) unique          not null,
    "name"       varchar(100)                not null,
    "created_at" timestamp(6) with time zone not null,
    "updated_at" timestamp(6) with time zone not null,
    primary key ("id")
);

create table "permission"
(
    "id"          bigint generated by default as identity,
    "identifier"  varchar(30) unique          not null,
    "description" varchar(100)                not null,
    "created_at"  timestamp(6) with time zone not null,
    "updated_at"  timestamp(6) with time zone not null,
    primary key ("id")
);

alter table "sys_user"
    drop column "username";

alter table "sys_user"
    add column "email" varchar(100)        not null;

alter table "sys_user"
    add column "active" boolean            not null;

alter table "sys_user"
    add column "password" varchar(255)     not null;

alter table "sys_user"
    add column "salt" varchar(255)         not null;

ALTER TABLE "sys_user"
    ADD CONSTRAINT "unique_user_email" UNIQUE ("email");

create table "user_role"
(
    "user_id" bigint not null,
    "role_id" bigint not null,
    primary key ("user_id", "role_id")
);

create table "role_permission"
(
    "id" bigint generated by default as identity,
    "role_id"       bigint not null,
    "permission_id" bigint not null,
    "action"        varchar(10) not null,
    "created_at"    timestamp(6) with time zone not null,
    "updated_at"    timestamp(6) with time zone not null,
    primary key ("id")
);

ALTER TABLE "role_permission"
    ADD CONSTRAINT "unique_role_permission" UNIQUE ("role_id", "permission_id");

alter table "user_role"
    add constraint "fk_user_role_user_id"
    foreign key ("user_id")
    references "sys_user";

alter table "user_role"
    add constraint "fk_user_role_role_id"
    foreign key ("role_id")
    references "role";

alter table "role_permission"
    add constraint "fk_role_permission_role_id"
    foreign key ("role_id")
    references "role";

alter table "role_permission"
    add constraint "fk_role_permission_permission_id"
    foreign key ("permission_id")
    references "permission";
