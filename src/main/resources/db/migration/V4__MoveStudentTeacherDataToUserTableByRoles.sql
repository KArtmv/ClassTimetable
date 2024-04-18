begin;

insert into users (role, first_name, last_name, group_id)
select 'student', first_name, last_name, group_id
from student;

insert into users (role, first_name, last_name)
select 'teacher', first_name, last_name
from teacher;

commit;