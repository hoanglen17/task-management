package com.taskmanagement.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepo extends JpaRepository<Task, Long> {
    @Query("SELECT task FROM Task task order by task.point ASC ")
    List<Task> findAllOrderByPointAsc();

    @Query("SELECT task FROM Task task order by task.point Desc ")
    List<Task> findAllOrderByPointDesc();
}
