package com.taskmanagement.tasks;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDto {
    private Long id;
    private String taskType;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Integer point;
    private Long userId;
    private Long parentId;
}
