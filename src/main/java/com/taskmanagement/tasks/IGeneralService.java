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
    TaskDto findByIdDto(Long id);
    List<TaskDto> findByPoint(Integer point);
    List<TaskDto> findByStatus(String status);
    List<TaskDto> findByDescription (String description);
    List<TaskDto> findByUser (Long id);
    List<TaskDto> findByParent(Long id);
    List<TaskDto> findByTaskType(String taskType);
    TaskDto updateStatusTask(Long id, String status);
    TaskDto updateTask(Long id,TaskDto task);
    TaskDto updatePointTask(Long id, Integer point);
    Task save(Tasks task);
}