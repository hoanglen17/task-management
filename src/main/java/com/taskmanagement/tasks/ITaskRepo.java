package com.taskmanagement.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepo extends JpaRepository<Task, Long> {
    
    @Query(value = "SELECT * FROM Task task " +
            "inner join t_user user on task.user_id = user.id " +
            "where (isnull(:description) or task.description like %:description%) " +
            "and (isnull(:userId) or task.user_id = :userId) "+
            "and (isnull(:firstName) or user.first_name like %:firstName%) " +
            "and (isnull(:lastName) or  user.last_name like :lastName) " +
            "and (isnull(:point) or task.point =:point)" +
            "and (isnull(:pointMin) or task.point between :pointMin and :pointMax)" +
            "and (isnull(:pointMax) or task.point between :pointMin and :pointMax)" +
            "and (isnull(:status) or task.status like %:status%)"+
            "order by task.description ASC,  task.user_id ASC, user.last_name ASC, task.point ASC, task.status ASC", nativeQuery = true)
    List<Task> searchTaskASC(@Param("description") String description, @Param("userId") Long id,@Param("firstName") String firstName,@Param("lastName") String lastName,@Param("point") Integer point, @Param("pointMin") Integer pointMin,@Param("pointMax") Integer pointMax, @Param("status") String Status);

    @Query(value = ":id",nativeQuery = true)
    List<Task> selectAll(@Param("id") String id);

}
