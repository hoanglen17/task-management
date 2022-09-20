package com.taskmanagement.history;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HistoryService implements IHistoryService{

    private final IHistoryRepo historyRepo;

    @Override
    public List<HistoryDto> findAllDto() {
        List<HistoryDto> listHistoryDto = new ArrayList<>();
        for (History history : historyRepo.findAllOrderByTimeDesc()) {
            listHistoryDto.add(HistoryConverter.Converter(history));
        }
        return listHistoryDto;
    }
    @Override
    public History createHistory(Long taskId,String descriptionTask, String info){
        History history = new History();
        history.setIdTask(taskId);
        LocalDateTime time = LocalDateTime.now();
        history.setDateTime(time);
        history.setInfo(info);
        history.setDescriptionTask(descriptionTask);
        return historyRepo.save(history);
    }
}
