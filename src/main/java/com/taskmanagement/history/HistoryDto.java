package com.taskmanagement.history;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryDto {
    private Long idTask;
    private String descriptionTask;
    private String info;
    private LocalDateTime time;
}
