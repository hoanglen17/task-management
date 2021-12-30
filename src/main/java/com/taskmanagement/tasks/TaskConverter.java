package com.taskmanagement.tasks;
import com.taskmanagement.users.User;
import org.springframework.stereotype.Service;

@Service
public class TaskConverter{
    private ITaskRepo taskRepo;

    public static TaskDto Converter(Task task){
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        if(task.getParent().getId() == task.getId()){
            dto.setTaskType("Main Task");
        }else{
            dto.setTaskType("Sidequests");
        }
        dto.setDescription(task.getDescription());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setStatus(task.getStatus());
        dto.setPoint(task.getPoint());
        dto.setUserId(task.getUser().getId());
        dto.setUserName(task.getUser().getFirstName()+task.getUser().getLastName());
        dto.setParentId(task.getParent().getId());
        dto.setParentName(task.getParent().getDescription());
        return dto;
    }
    public static Task mapper(TaskDto taskDto, User user, Task taskDtoParent){
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setStartDate(taskDto.getStartDate());
        task.setEndDate(taskDto.getEndDate());
        task.setPoint(taskDto.getPoint());
        task.setUser(user);
        task.setParent(taskDtoParent);
        return task;
    }
}
