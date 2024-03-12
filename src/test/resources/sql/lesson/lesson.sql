INSERT INTO course (id, course_name, course_description)
values (7, 'Principles of Marketing', 'Learn the basics of marketing');

INSERT INTO groups (id, group_name)
values (1, 'YS-27');

INSERT INTO classroom (id, classroom_name)
VALUES (3, 'Room 103');

INSERT INTO teacher (id, first_name, last_name)
VALUES (6, 'Jessica', 'Brown');

INSERT INTO lesson(id, date, lesson_num, course_id, group_id, classroom_id, teacher_id)
VALUES (2, '2024-03-11', 1, 7, 1, 3, 6);
