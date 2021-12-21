package com.restservice.history;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryDto {
    private Long idTask;
    private String info;
    private LocalDateTime time;
}
