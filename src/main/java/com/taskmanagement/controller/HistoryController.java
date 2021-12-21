package com.taskmanagement.controller;

import com.taskmanagement.history.History;
import com.taskmanagement.history.HistoryDto;
import com.taskmanagement.history.IHistoryService;
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
        //add bug code
        return historyService.findAllDto();
    }

    @PostMapping("/create")
    public ResponseEntity<History> createNewTask(@RequestBody History history) {
        return new ResponseEntity<>(historyService.save(history), HttpStatus.OK);
    }
}
