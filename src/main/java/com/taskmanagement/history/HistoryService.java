package com.taskmanagement.history;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryService implements IHistoryService{
    @Autowired
    private IHistoryRepo historyRepo;

    @Override
    public Iterable<History> findAll() {
        return historyRepo.findAll();
    }
    @Override
    public List<HistoryDto> findAllDto() {
        List<HistoryDto> historyDtos = new ArrayList<>();
        for (History history : historyRepo.findAllOrderByTimeDesc()) {
            HistoryDto dto = new HistoryDto();
            dto.setIdTask(history.getIdTask());
            dto.setInfo(history.getInfo());
            dto.setTime(history.getDateTime());
            historyDtos.add(dto);
        }
        return historyDtos;
    }
    @Override
    public Optional<History> findById(Long id) {
        return historyRepo.findById(id);
    }

    @Override
    public History save(History category) {
        return historyRepo.save(category);
    }

    @Override
    public void remove(Long id) {
        historyRepo.deleteById(id);
    }
}
