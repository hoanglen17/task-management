package com.taskmanagement.tasks;
import com.taskmanagement.history.History;
import com.taskmanagement.history.HistoryService;
import com.taskmanagement.history.IHistoryRepo;
import com.taskmanagement.users.User;
import com.taskmanagement.users.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final ITaskRepo taskRepo;
    private final HistoryService historyService;

    @Override
    public List<TaskDto> findAll() {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            taskDtos.add(TaskConverter.Converter(task));
        }
        return taskDtos;
    }

    @Override
    public List<TaskDto> findByPoint(Integer point) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getPoint() == point){
            taskDtos.add(TaskConverter.Converter(task));}
        }
        return taskDtos;
    }

    @Override
    public List<TaskDto> findByStatus(String status) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals((status))){
                taskDtos.add(TaskConverter.Converter(task));}
        }
        return taskDtos;
    }

    @Override
    public List<TaskDto> getByStatusAscending ( ) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals("TODO")){
                taskDtos.add(TaskConverter.Converter(task));
            }
        }
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals("IN_PROGRESS")){
                taskDtos.add(TaskConverter.Converter(task));}
        }
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals("DONE")){
                taskDtos.add(TaskConverter.Converter(task));}
        }
        return taskDtos;
    }

    @Override
    public List<TaskDto> getByStatusDescending ( ) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals("DONE")){
                taskDtos.add(TaskConverter.Converter(task));}
        }
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals("IN_PROGRESS")){
                taskDtos.add(TaskConverter.Converter(task));}
        }
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals("TODO")){
                taskDtos.add(TaskConverter.Converter(task));}
        }
        return taskDtos;
    }

    @Override
    public List<TaskDto> getByPointAscending(){
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAllOrderByPointAsc()) {
                taskDtos.add(TaskConverter.Converter(task));
        }
        return taskDtos;
    }

    @Override
    public List<TaskDto> getByPointDescending(){
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAllOrderByPointDesc()) {
            taskDtos.add(TaskConverter.Converter(task));
        }
        return taskDtos;
    }
    @Override
    public List<TaskDto> findByDescription(String description) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getDescription().equals((description))){
                taskDtos.add(TaskConverter.Converter(task));}
        }
        return taskDtos;
    }
    @Override
    public List<TaskDto> findByUser(Long id) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getUser().getId() == id){
                taskDtos.add(TaskConverter.Converter(task));
            }
        }
        return taskDtos;
    }

    @Override
    public List<TaskDto> findByParent(Long id) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getParentId() == id){
                taskDtos.add(TaskConverter.Converter(task));
            }
        }
        return taskDtos;
    }

    @Override
    public List<TaskDto> findByTaskType(String taskType) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            TaskDto taskDto = TaskConverter.Converter(task);
            if(taskDto.getTaskType().equals(taskType)){
                taskDtos.add(TaskConverter.Converter(task));
            }
        }
        return taskDtos;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepo.findById(id);
    }

    @Override
    public TaskDto findByIdDto(Long id) {
        Optional<Task> taskOptional = taskRepo.findById(id);
        return TaskConverter.Converter(taskOptional.get());
    }

    @Override
    public TaskDto updateStatusTask(Long id, String status){
        Task taskFromDB = findById(id).get();
        String taskHistoryInfo = new String() ;
        if((status.equals(taskFromDB.getStatus())) == false ){
            taskHistoryInfo = taskHistoryInfo + "Change " + "Status: " + taskFromDB.getStatus() + " to " + status + " ";
            if(status.equals("IN_PROGRESS")){
                taskFromDB.setStartDate(LocalDate.now()) ;
                taskFromDB.setStatus("IN_PROGRESS");
            }else if (status.equals("DONE")){
                taskFromDB.setEndDate(LocalDate.now());
                taskFromDB.setStatus("DONE");
            }else if(status.equals("TODO")){
                taskFromDB.setStatus("TODO");
                taskFromDB.setStartDate(null) ;
                taskFromDB.setEndDate(null);
            }
            historyService.createHistory(taskFromDB.getId(),taskHistoryInfo);
        }
        return TaskConverter.Converter(taskFromDB);
    }
    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto){
        Task taskFromDB = findById(id).get();
        String taskHistoryInfo = new String();
        if((taskDto.getDescription().equals(taskFromDB.getDescription())) == false ){
            taskHistoryInfo = "Change " + "Description: " + taskFromDB.getDescription() + " to " + taskDto.getDescription()+ " ";
            taskFromDB.setDescription(taskDto.getDescription());
        }
        if(taskDto.getPoint() != taskFromDB.getPoint()){
            taskHistoryInfo = taskHistoryInfo +"Change " + "Point: " + taskFromDB.getPoint() + " to " + taskDto.getPoint()+ " ";
            taskFromDB.setPoint(taskDto.getPoint());
        }
        if((taskDto.getStatus().equals(taskFromDB.getStatus())) == false ){
            taskHistoryInfo = taskHistoryInfo + "Change " + "Status: " + taskFromDB.getStatus() + " to " + taskDto.getStatus()+ " ";
            if(taskDto.getStatus().equals("IN_PROGRESS")){
                taskFromDB.setStartDate(LocalDate.now()) ;
                taskFromDB.setStatus("IN_PROGRESS");
            }else if (taskDto.getStatus().equals("DONE")){
                taskFromDB.setEndDate(LocalDate.now());
                taskFromDB.setStatus("DONE");
            }else if(taskDto.getStatus().equals("TODO")){
                taskFromDB.setStatus("TODO");
                taskFromDB.setStartDate(null) ;
                taskFromDB.setEndDate(null);
            }
        }
        historyService.createHistory(taskFromDB.getId(),taskHistoryInfo);
        return TaskConverter.Converter(taskFromDB);
    }

    @Override
    public TaskDto updatePointTask(Long id, Integer point){
        Task taskFromDB = findById(id).get();
        String taskHistoryInfo = new String() ;
        if(point != taskFromDB.getPoint()){
            if(taskFromDB.getPoint() >= 0 && taskFromDB.getPoint() <= 5){
            taskHistoryInfo = taskHistoryInfo + "Change " + "Status: " + taskFromDB.getPoint() + " to " + point + " ";
            taskFromDB.setPoint(point);
            historyService.createHistory(taskFromDB.getId(),taskHistoryInfo);
            save(taskFromDB);
            }
        }
        return TaskConverter.Converter(taskFromDB);
    }

    @Override
    public Task save(Task task) {
        return taskRepo.save(task);
    }
}
