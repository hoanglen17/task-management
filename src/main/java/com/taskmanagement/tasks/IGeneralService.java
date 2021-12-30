package com.taskmanagement.tasks;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<Tasks> {
    List<TaskDto> getByStatusAscending ();
    List<TaskDto> getByStatusDescending ();
    List<TaskDto> getByPointAscending();
    List<TaskDto> getByPointDescending();
    List<TaskDto> findAll();
    Optional<Tasks> findById(Long id);
    List<TaskDto> findByIdDto(Long id);
    List<TaskDto> findByPoint(Integer point);
    List<TaskDto> findByStatus(String status);
    List<TaskDto> findByDescription (String description);
    List<TaskDto> findByUser (Long id);
    List<TaskDto> findByParent(Long id);
    List<TaskDto> findByTaskType(String taskType);
    Object updateTask(Long id,TaskDto task);
    Object reAssignTask(Long id, Long userId);
    Object createTask(TaskDto task);
    Task save(Tasks task);
}