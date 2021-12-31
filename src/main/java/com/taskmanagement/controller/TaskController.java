package com.taskmanagement.controller;
import com.taskmanagement.history.IHistoryService;
import com.taskmanagement.tasks.ITaskService;
import com.taskmanagement.tasks.Task;
import com.taskmanagement.tasks.TaskConverter;
import com.taskmanagement.tasks.TaskDto;
import com.taskmanagement.users.User;
import com.taskmanagement.users.UserDto;
import com.taskmanagement.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final ITaskService taskService;

    @GetMapping("/get")
    public ResponseEntity<Object> getAllTask() {
        return new ResponseEntity<>(taskService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<TaskDto>> findByIdDto(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.findByIdDto(id),HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchTask (TaskDto taskDto) {
        return new ResponseEntity<>(taskService.searchTask(taskDto),HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createNewTask(@RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.createTask(taskDto),HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.updateTask(id, taskDto), HttpStatus.OK);
    }
    @PutMapping("/re-assign/{id}")
    public ResponseEntity<Object> reAssignTask(@PathVariable Long id, @RequestBody Long userId) {
        return new ResponseEntity<>(taskService.reAssignTask(id, userId), HttpStatus.OK);
    }
}
