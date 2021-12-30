package com.taskmanagement.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IHistoryRepo extends JpaRepository<History, Long> {
    @Query("SELECT history FROM History history order by history.dateTime DESC ")
    List<History> findAllOrderByTimeDesc();
}
