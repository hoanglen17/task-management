package com.taskmanagement.tasks;
import com.taskmanagement.users.User;
import org.springframework.stereotype.Service;

@Service
public class TaskConverter{
    private ITaskRepo taskRepo;

    public static TaskDto Converter(Task task){
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        if(task.getParentId() == task.getId()){
            dto.setTaskType("Main Task");
        }else{
            dto.setTaskType("Side Quests");
        }
        dto.setDescription(task.getDescription());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setProgress(task.getProgress());
        dto.setPoint(task.getPoint());
        dto.setUserId(task.getUser().getId());
        dto.setUserName(task.getUser().getFirstName()+" "+ task.getUser().getLastName());
        dto.setParentId(task.getParentId());
        dto.setParentDescription(task.getParentDescription());
        return dto;
    }

    public static Task mapper(TaskDto taskDto, User user){
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setDescription(taskDto.getDescription());
        task.setProgress(taskDto.getProgress());
        task.setStartDate(taskDto.getStartDate());
        task.setEndDate(taskDto.getEndDate());
        task.setPoint(taskDto.getPoint());
        task.setUser(user);
        task.setProgress(taskDto.getParentDescription());
        task.setParentId(taskDto.getParentId());
        task.setParentDescription(taskDto.getParentDescription());
        return task;
    }
}
