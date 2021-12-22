package com.taskmanagement.history;

public class HistoryConverter {
    public static HistoryDto Converter(History history){
        HistoryDto dto = new HistoryDto();
        dto.setIdTask(history.getIdTask());
        dto.setInfo(history.getInfo());
        dto.setTime(history.getDateTime());
        return dto;
    }
}
