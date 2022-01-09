package com.taskmanagement.tasks;

import com.taskmanagement.history.History;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepo extends JpaRepository<Task, Long> {
    @Query(value = "select case progress " +
            "           when 'TODO' then 1" +
            "           when 'IN_PROGRESS' then 2" +
            "           when 'DONE' then 3" +
            "           end  progress, description , id" +
            "from task " +
            "order by progress asc",nativeQuery = true)
    List<History> sortingProgressASC();
}
