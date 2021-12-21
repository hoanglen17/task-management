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
            TaskDto dto = new TaskDto();
            dto.setId(task.getId());
            dto.setDescription(task.getDescription());
            dto.setStatus(task.getStatus());
            dto.setPoint(task.getPoint());
            dto.setIdUser(task.getUser().getId());
            dto.setIdParent_id(task.getId());
            taskDtos.add(dto);
        }
        return taskDtos;
    }
    @Override
    public List<TaskDto> findByPoint(Integer point) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getPoint() == point){
            TaskDto dto = new TaskDto();
            dto.setId(task.getId());
            dto.setDescription(task.getDescription());
            dto.setStatus(task.getStatus());
            dto.setPoint(task.getPoint());
            dto.setIdUser(task.getUser().getId());
            dto.setIdParent_id(task.getId());
            taskDtos.add(dto);}
        }
        return taskDtos;
    }
    @Override
    public List<TaskDto> findByStatus(String status) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals((status))){
                TaskDto dto = new TaskDto();
                dto.setId(task.getId());
                dto.setDescription(task.getDescription());
                dto.setStatus(task.getStatus());
                dto.setPoint(task.getPoint());
                dto.setIdUser(task.getUser().getId());
                dto.setIdParent_id(task.getId());
                taskDtos.add(dto);}
        }
        return taskDtos;
    }
    @Override
    public List<TaskDto> getByStatusAscending ( ) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals("TODO")){
                TaskDto dto = new TaskDto();
                dto.setId(task.getId());
                dto.setDescription(task.getDescription());
                dto.setStatus(task.getStatus());
                dto.setPoint(task.getPoint());
                dto.setIdUser(task.getUser().getId());
                dto.setIdParent_id(task.getId());
                taskDtos.add(dto);}
        }
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals("IN_PROGRESS")){
                TaskDto dto = new TaskDto();
                dto.setId(task.getId());
                dto.setDescription(task.getDescription());
                dto.setStatus(task.getStatus());
                dto.setPoint(task.getPoint());
                dto.setIdUser(task.getUser().getId());
                dto.setIdParent_id(task.getId());
                taskDtos.add(dto);}
        }
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals("DONE")){
                TaskDto dto = new TaskDto();
                dto.setId(task.getId());
                dto.setDescription(task.getDescription());
                dto.setStatus(task.getStatus());
                dto.setPoint(task.getPoint());
                dto.setIdUser(task.getUser().getId());
                dto.setIdParent_id(task.getId());
                taskDtos.add(dto);}
        }
        return taskDtos;
    }

    @Override
    public List<TaskDto> getByStatusDescending ( ) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals("DONE")){
                TaskDto dto = new TaskDto();
                dto.setId(task.getId());
                dto.setDescription(task.getDescription());
                dto.setStatus(task.getStatus());
                dto.setPoint(task.getPoint());
                dto.setIdUser(task.getUser().getId());
                dto.setIdParent_id(task.getId());
                taskDtos.add(dto);}
        }
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals("IN_PROGRESS")){
                TaskDto dto = new TaskDto();
                dto.setId(task.getId());
                dto.setDescription(task.getDescription());
                dto.setStatus(task.getStatus());
                dto.setPoint(task.getPoint());
                dto.setIdUser(task.getUser().getId());
                dto.setIdParent_id(task.getId());
                taskDtos.add(dto);}
        }
        for (Task task : taskRepo.findAll()) {
            if(task.getStatus().equals("TODO")){
                TaskDto dto = new TaskDto();
                dto.setId(task.getId());
                dto.setDescription(task.getDescription());
                dto.setStatus(task.getStatus());
                dto.setPoint(task.getPoint());
                dto.setIdUser(task.getUser().getId());
                dto.setIdParent_id(task.getId());
                taskDtos.add(dto);}
        }
        return taskDtos;
    }

    @Override
    public List<TaskDto> getByPointAscending(){
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAllOrderByPointAsc()) {
                TaskDto dto = new TaskDto();
                dto.setId(task.getId());
                dto.setDescription(task.getDescription());
                dto.setStatus(task.getStatus());
                dto.setPoint(task.getPoint());
                dto.setIdUser(task.getUser().getId());
                dto.setIdParent_id(task.getId());
                taskDtos.add(dto);
        }
        return taskDtos;
    }

    @Override
    public List<TaskDto> getByPointDescending(){
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAllOrderByPointDesc()) {
            TaskDto dto = new TaskDto();
            dto.setId(task.getId());
            dto.setDescription(task.getDescription());
            dto.setStatus(task.getStatus());
            dto.setPoint(task.getPoint());
            dto.setIdUser(task.getUser().getId());
            dto.setIdParent_id(task.getId());
            taskDtos.add(dto);
        }
        return taskDtos;
    }
    @Override
    public List<TaskDto> findByDescription(String description) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getDescription().equals((description))){
                TaskDto dto = new TaskDto();
                dto.setId(task.getId());
                dto.setDescription(task.getDescription());
                dto.setStatus(task.getStatus());
                dto.setPoint(task.getPoint());
                dto.setIdUser(task.getUser().getId());
                dto.setIdParent_id(task.getId());
                taskDtos.add(dto);}
        }
        return taskDtos;
    }
    @Override
    public List<TaskDto> findByUser(Long id) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getUser().getId() == id){
                TaskDto dto = new TaskDto();
                dto.setId(task.getId());
                dto.setDescription(task.getDescription());
                dto.setStatus(task.getStatus());
                dto.setPoint(task.getPoint());
                dto.setIdUser(task.getUser().getId());
                dto.setIdParent_id(task.getId());
                taskDtos.add(dto);}
        }
        return taskDtos;
    }
    @Override
    public List<TaskDto> findByParent(Long id) {
        List<TaskDto> taskDtos = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if(task.getParentId() == id){
                TaskDto dto = new TaskDto();
                dto.setId(task.getId());
                dto.setDescription(task.getDescription());
                dto.setStatus(task.getStatus());
                dto.setPoint(task.getPoint());
                dto.setIdUser(task.getUser().getId());
                dto.setIdParent_id(task.getId());
                taskDtos.add(dto);}
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
        TaskDto taskDto = new TaskDto();
        taskDto.setId(taskOptional.get().getId());
        taskDto.setDescription(taskOptional.get().getDescription());
        taskDto.setStatus(taskOptional.get().getStatus());
        taskDto.setPoint(taskOptional.get().getPoint());
        taskDto.setIdUser(taskOptional.get().getUser().getId());
        taskDto.setIdParent_id(taskOptional.get().getId());
        return taskDto;
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
