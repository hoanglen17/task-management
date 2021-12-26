package com.taskmanagement.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepo extends JpaRepository<Task, Long> {
    @Query("SELECT e FROM Task e order by e.point ASC ")
    List<Task> findAllOrderByPointAsc();

    @Query("SELECT e FROM Task e order by e.point Desc ")
    List<Task> findAllOrderByPointDesc();
}
