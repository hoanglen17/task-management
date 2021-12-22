package com.taskmanagement.history;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<History>{
    Iterable<History> findAll();
    List<HistoryDto> findAllDto();
    Optional<History> findById(Long id);
    History save(History history);
    History createHistory(Long taskId, String info);
    void remove(Long id);
}
