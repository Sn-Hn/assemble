INSERT INTO users(user_name, email, password, phone_number, role, nickname, created_date, modified_date)
VALUES (
           'tester00',
           'test00@gmail.com',
           '$2a$10$c0kMDetZjkkKIBdEeSH4oO60xxkt9A9FObc7CApbyZJ5JEt/ie3L2',
           '01000000000',
           'VIP',
           'test01',
           now(),
           now()); /* password1! */

INSERT INTO users(user_name, email, password, phone_number, role, nickname, created_date, modified_date)
VALUES ('tester01',
        'test01@gmail.com',
        '$2a$10$StGFrD9mIqLFRBaBsvVDuusb54PQMonzjtc/3DbNKJNXAcvY7M.Ey',
        '01000000000',
        'USER',
        'test02',
        now(),
        now()); /* password2! */

INSERT INTO users(user_name, email, password, phone_number, role, nickname, created_date, modified_date)
VALUES ('tester02',
        'test02@gmail.com',
        '$2a$10$8ALIty0h4ebK4HIPVpWDRu7/1cP8CIs9odtGiybmBajNv3qenfid6',
        '01000000000',
        'USER',
        'test03',
        now(),
        now()); /* password3! */

INSERT INTO category(category_name)
VALUES (
           '개발/프로그래밍'
       );

INSERT INTO post(user_id, post_title, post_contents, hits, likes, personnel_number, category_id, expected_period)
VALUES (
           '1',
           '게시글 제목입니다 ~',
           '게시글 내용입니다 ~~~~~~~',
           0,
           0,
           0,
           1,
           0
       );
