package com.taskmanagement.tasks;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<Tasks> {
    List<TaskDto> findAll();
    Optional<Tasks> findById(Long id);
    List<TaskDto> findByIdDto(Long id);
    List<TaskDto> searchTask(TaskDto taskDto);
    Object updateTask(Long id,TaskDto task);
    Object reAssignTask(Long id, Long userId);
    Object createTask(TaskDto task);
    Task save(Tasks task);
}