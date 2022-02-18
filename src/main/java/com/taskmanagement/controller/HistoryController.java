package com.taskmanagement.controller;

import com.taskmanagement.history.History;
import com.taskmanagement.history.IHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/histories")
@AllArgsConstructor
public class HistoryController {
    private final IHistoryService historyService;

    @GetMapping("/get")
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>(historyService.findAllDto(), HttpStatus.OK);
    }
}
