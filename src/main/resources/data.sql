 insert into t_user (id, first_name, last_name) values (1, 'Tran Hoang', 'Len');
 insert into t_user (id, first_name, last_name) values (2, 'Nguyen Thi Hoang', 'Huyen');


 insert into task (id, description, parent_id, point,point_status, status,user_id)
 values(1, 'description1', 1,4,1,'TODO',1);

 insert into task (id, description, parent_id, point, point_status,status,start_date,user_id)
 values(2, 'description2', 2,3,2,'IN_PROGRESS','2021-10-17',2);

 insert into task (id, description, parent_id, point, point_status,status,start_date,end_date,user_id)
 values(3, 'description3', 1,2,3,'DONE','2021-10-17','2021-10-27',1);

 insert into task (id, description, parent_id, point, point_status,status,user_id)
 values(4, 'description4', 2,1,1,'TODO',2);


insert into history (id, id_task,description_task,date_time,info)
values (1,1,'description1','2021-10-12','Create: Description: description1 Point: 4 UserID: 1 Assign: Tran Hoang Len');

 insert into history (id, id_task,description_task,date_time,info)
values (2,2,'description2','2021-10-12','Create: Description: description2 Point: 3 UserID: 2 Assign: Nguyen Thi Hoang Huyen');

 insert into history (id, id_task,description_task,date_time,info)
values (3,3,'description3','2021-10-12','Create: Description: description3 Point: 2 UserID: 1 Assign: Tran Hoang Len');

 insert into history (id, id_task,description_task,date_time,info)
values (4,4,'description4','2021-10-12','Create: Description: description4 Point: 1 UserID: 2 Assign: Nguyen Thi Hoang Huyen');
