package com.restservice.controller;

import com.restservice.history.History;
import com.restservice.history.HistoryDto;
import com.restservice.history.IHistoryService;
import com.restservice.tasks.Task;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historys")
@AllArgsConstructor
public class HistoryController {
    @Autowired
    private final IHistoryService historyService;

    @GetMapping("/get")
    public List<HistoryDto> getAll() {
        return historyService.findAllDto();
    }

    @PostMapping("/create")
    public ResponseEntity<History> createNewTask(@RequestBody History history) {
        return new ResponseEntity<>(historyService.save(history), HttpStatus.OK);
    }
}
