package com.taskmanagement.tasks;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDto {
    private Long id;
    private String taskType;
    private String description;
    private Long userId;
    private String userName;
    private Long parentId;
    private String parentName;
    private Integer point;
    private String progress;
    private LocalDate startDate;
    private LocalDate endDate;
}
