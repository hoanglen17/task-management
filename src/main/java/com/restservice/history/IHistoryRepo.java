package com.restservice.history;

import com.restservice.tasks.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IHistoryRepo extends JpaRepository<History, Long> {
    @Query("SELECT e FROM History e order by e.dateTime DESC ")
    List<History> findAllOrderByTimeDesc();

}
