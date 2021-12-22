package com.taskmanagement.tasks;
import com.taskmanagement.users.User;
import org.springframework.stereotype.Service;

@Service
public class TaskConverter{

    public static TaskDto Converter(Task task){
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setDescription(task.getDescription());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getStartDate());
        dto.setStatus(task.getStatus());
        dto.setPoint(task.getPoint());
        dto.setUserId(task.getUser().getId());
        dto.setParentId(task.getId());
        return dto;
    }
    public static Task mapper(TaskDto taskDto, User user){
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setStartDate(taskDto.getStartDate());
        task.setEndDate(taskDto.getEndDate());
        task.setPoint(taskDto.getPoint());
        task.setUser(user);
        task.setParentId(taskDto.getParentId());
        return task;
    }
}
