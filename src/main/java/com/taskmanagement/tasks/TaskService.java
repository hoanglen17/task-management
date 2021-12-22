package com.taskmanagement.tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {
    @Autowired
    private final ITaskRepo taskRepo;

    public TaskService(ITaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

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
    public Task save(Task task) {
        return taskRepo.save(task);
    }

    @Override
    public void remove(Long id) {
        taskRepo.deleteById(id);
    }
}
