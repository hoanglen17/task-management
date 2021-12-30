package com.taskmanagement.history;
import com.taskmanagement.tasks.Task;
import com.taskmanagement.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Iterable<History> findAll() {
        return historyRepo.findAll();
    }

    @Override
    public History findByTaskId(Long id){
        History history = new History();
        for (History historyFind : historyRepo.findAllOrderByTimeDesc()) {
            if(history.getIdTask() == id){
                history = historyFind;
            }
        }
        return history;
    }
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
    @Override
    public Optional<History> findById(Long id) {
        return historyRepo.findById(id);
    }

    @Override
    public History save(History history) {
        return historyRepo.save(history);
    }

    @Override
    public void remove(Long id) {
        historyRepo.deleteById(id);
    }
}
