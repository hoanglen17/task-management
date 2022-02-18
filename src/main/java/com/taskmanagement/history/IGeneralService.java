package com.taskmanagement.history;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<History>{
    List<HistoryDto> findAllDto();
    History createHistory(Long taskId,String descriptionTask, String info);
}
