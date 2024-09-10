insert into "sys_user" ("id", "email", "name", "active", "password", "salt", "created_at", "updated_at") values
(0, 'system@gra.com', 'Internal System', false, '$2a$12$yeyymGiEu2/TS.d6sf0aeujdx9OKk5.UpRw.JE9TxPmYJAsOhkuvy', '52345hjhdfhak', now(), now()),
(1, 'admin@gra.com', 'System Administrator', true, '$2a$12$Py36ID19LjeeNM.54pBIPeYE8sKYfmR5WVUV0.OfwYgP6u1XAKQN6', 'h14khHJ3b', now(), now()),
(2, 'user@gra.com', 'Any user', true, '$2a$12$Py36ID19LjeeNM.54pBIPeYE8sKYfmR5WVUV0.OfwYgP6u1XAKQN6', 'h14khHJ3b', now(), now());

insert into "permission" ("id", "identifier", "description", "created_at", "updated_at") values
(1, 'USR_VIEW', 'View users', now(), now()),
(2, 'RLE_VIEW', 'View roles', now(), now()),
(3, 'PERM_VIEW', 'View permissions', now(), now()),
(4, 'PRC_INS', 'Insert producer', now(), now()),
(5, 'PRC_EDT', 'Update producer', now(), now()),
(6, 'PRC_DEL', 'Delete producer', now(), now()),
(7, 'PRC_VIEW', 'View producer', now(), now()),
(8, 'STD_INS', 'Insert studio', now(), now()),
(9, 'STD_EDT', 'Update studio', now(), now()),
(10, 'STD_DEL', 'Delete studio', now(), now()),
(11, 'STD_VIEW', 'View studio', now(), now()),
(12, 'MOV_INS', 'Insert movie', now(), now()),
(13, 'MOV_EDT', 'Update movie', now(), now()),
(14, 'MOV_DEL', 'Delete movie', now(), now()),
(15, 'MOV_VIEW', 'View movie', now(), now()),
(16, 'AWD_INS', 'Insert award', now(), now()),
(17, 'AWD_EDT', 'Update award', now(), now()),
(18, 'AWD_DEL', 'Delete award', now(), now()),
(19, 'AWD_VIEW', 'View award', now(), now()),
(20, 'LOG_VIEW', 'View log', now(), now());

insert into "role" ("id", "active", "identifier", "name", "created_at", "updated_at") values
(1, true, 'ADMIN', 'Administrators', now(), now()),
(2, true, 'USER', 'Users', now(), now());

insert into "user_role" ("user_id", "role_id") values
(1, 1),
(2, 2);

insert into "role_permission" ("role_id", "permission_id", "action", "created_at", "updated_at") values
(2, 1, 'ALLOW', now(), now() ),
(2, 2, 'ALLOW', now(), now() ),
(2, 3, 'ALLOW', now(), now() ),
(2, 7, 'ALLOW', now(), now() ),
(2, 11, 'ALLOW', now(), now() ),
(2, 15, 'ALLOW', now(), now() ),
(2, 19, 'ALLOW', now(), now() );
