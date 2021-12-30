 insert into t_user (id, first_name, last_name) values (1, 'Tran Hoang', 'Len');
 insert into t_user (id, first_name, last_name) values (2, 'Nguyen Thi Hoang', 'Huyen');


 insert into task (id, description, parent_id, point, status,user_id)
 values(1, 'description1', 1,4,'TODO',1);

 insert into task (id, description, parent_id, point, status,user_id)
 values(2, 'description2', 2,3,'TODO',2);

 insert into task (id, description, parent_id, point, status,user_id)
 values(3, 'description3', 1,2,'TODO',1);

 insert into task (id, description, parent_id, point, status,user_id)
 values(4, 'description4', 2,1,'TODO',2);
