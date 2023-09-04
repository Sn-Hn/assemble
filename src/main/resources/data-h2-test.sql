INSERT INTO users(user_name, email, password, phone_number, role, status, nickname, birth_date, created_date, modified_date)
VALUES (
           'tester00',
           'test00@gmail.com',
           '$2a$10$c0kMDetZjkkKIBdEeSH4oO60xxkt9A9FObc7CApbyZJ5JEt/ie3L2',
           '01000000000',
           'VIP',
           'NORMAL',
           'test01',
           '20000101',
           now(),
           now()); /* password1! */

INSERT INTO users(user_name, email, password, phone_number, role, status, nickname, birth_date, created_date, modified_date)
VALUES ('tester01',
        'test01@gmail.com',
        '$2a$10$StGFrD9mIqLFRBaBsvVDuusb54PQMonzjtc/3DbNKJNXAcvY7M.Ey',
        '01000000000',
        'ADMIN',
        'NORMAL',
        'test02',
        '20000101',
        now(),
        now()); /* password2! */

INSERT INTO users(user_name, email, password, phone_number, role, status, nickname, birth_date, created_date, modified_date)
VALUES ('tester02',
        'test02@gmail.com',
        '$2a$10$8ALIty0h4ebK4HIPVpWDRu7/1cP8CIs9odtGiybmBajNv3qenfid6',
        '01000000000',
        'USER',
        'NORMAL',
        'test03',
        '20000101',
        now(),
        now()); /* password3! */

INSERT INTO category(category_name)
VALUES (
           '개발/프로그래밍'
       ),
        (
            '프로젝트'
        );

INSERT INTO meeting(user_id, meeting_name, meeting_description, hits, likes, personnel_number, category_id, expected_period, is_deleted, meeting_status)
VALUES (
           '1',
           '첫 번째 모임',
           '첫 번째 모임 설명',
           0,
           1,
           0,
           1,
           0,
        'N',
        'PROGRESS'
       ),
       (
           '1',
           '두 번째 모임',
           '두 번째 모임 설명',
           0,
           1,
           0,
           1,
           0,
           'N',
           'PROGRESS'
       ),
       (
           '2',
           '세 번째 모임 ~',
           '세 번째 모임 설명',
           0,
           1,
           0,
           1,
           0,
           'N',
           'PROGRESS'
       );
INSERT INTO comment(meeting_id, user_id, contents, is_deleted, created_date, modified_date)
VALUES (
           '1',
           '1',
           '첫 번째 모임의 첫 번째 댓글',
           'N',
           now(),
           now()
       ),
       (
           '1',
           '1',
           '첫 번째 모임의 두 번째 댓글',
           'N',
           now(),
           now()
       );

INSERT INTO likes(meeting_id, user_id)
VALUES (
           '1',
           '2'
       );

INSERT INTO join_request(meeting_id, user_id, status)
VALUES (
        '1',
        '2',
        'REQUEST'
       ),
       (
       '2',
       '1',
       'REQUEST'
       );

INSERT INTO activity(meeting_id, user_id, status)
VALUES (
        '3',
        '1',
        'NORMAL'
       );