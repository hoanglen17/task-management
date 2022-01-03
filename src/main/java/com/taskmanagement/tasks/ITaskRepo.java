package com.taskmanagement.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepo extends JpaRepository<Task, Long> {
    @Query("SELECT task FROM Task task " +
            "where (:description = NULL or task.description like %:description%) " +
            "and (:userId = NULL or task.user.id = :userId) " +
            "and (:userName = NULL or task.user.firstName like %:userName% or task.user.lastName like %:userName% ) " +
            "and (:point = Null or task.point =:point)" +
            "and (:pointMin = Null or task.point between :pointMin and :pointMax)" +
            "and (:pointMax = Null or task.point between :pointMin and :pointMax)" +
            "and (:status = Null or task.status like %:status%)"+
            "order by :description ASC, :userId ASC, :userName ASC, task.point ASC, :status ASC")
    List<Task> searchTaskASC(@Param("description") String description, @Param("userId") Long id,@Param("userName") String userName,@Param("point") Integer point, @Param("pointMin") Integer pointMin,@Param("pointMax") Integer pointMax, @Param("status") String Status);

    @Query("SELECT task FROM Task task " +
            "where (:description = NULL or task.description like %:description%) " +
            "and (:userId = NULL or task.user.id = :userId) " +
            "and (:userName = NULL or task.user.firstName like %:userName% or task.user.lastName like %:userName% ) " +
            "and (:point = Null or task.point =:point)" +
            "and (:pointMin = Null or task.point between :pointMin and :pointMax)" +
            "and (:pointMax = Null or task.point between :pointMin and :pointMax)" +
            "and (:status = Null or task.status like %:status%)"+
            "order by :description DESC , :userName DESC, task.point ASC, :status DESC")
    List<Task> searchTaskDESC(@Param("description") String description, @Param("userId") Long id,@Param("userName") String userName,@Param("point") Integer point, @Param("pointMin") Integer pointMin,@Param("pointMax") Integer pointMax, @Param("status") String Status);
}
