package com.taskmanagement.tasks;

import lombok.Data;

@Data
public class SearchTaskDto {
    private Long id;
    private String description;
    private String status;
    private Integer point;
    private Integer pointMin;
    private Integer pointMax;
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String sorting;
    private String sortingPoint;
    private String sortingStatus;
}
