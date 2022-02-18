package com.taskmanagement.tasks;

import com.taskmanagement.history.History;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepo extends JpaRepository<Task, Long> {
}
