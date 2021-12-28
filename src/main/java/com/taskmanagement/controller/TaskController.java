package com.taskmanagement.controller;
import com.taskmanagement.history.IHistoryService;
import com.taskmanagement.tasks.ITaskService;
import com.taskmanagement.tasks.Task;
import com.taskmanagement.tasks.TaskConverter;
import com.taskmanagement.tasks.TaskDto;
import com.taskmanagement.users.User;
import com.taskmanagement.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor

public class TaskController {

    private final ITaskService taskService;
    private final  IHistoryService historyService;
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<Object> getAllTask() {
        return new ResponseEntity<>(taskService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TaskDto> findByIdDto(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.findByIdDto(id),HttpStatus.OK);
    }

    @GetMapping("/get-by-status/ascending")
    public ResponseEntity<Object> getTaskByStatusAscending () {
        return new ResponseEntity<>(taskService.getByStatusAscending(),HttpStatus.OK);
    }

    @GetMapping("/get-by-status/descending")
    public ResponseEntity<Object> getTaskByStatusDescending () {
        return new ResponseEntity<>(taskService.getByStatusDescending(),HttpStatus.OK);
    }

    @GetMapping("/get-by-point/ascending")
    public ResponseEntity<Object> getTaskByPointAscending () {
        return new ResponseEntity<>(taskService.getByPointAscending(),HttpStatus.OK);
    }

    @GetMapping("/get-by-point/descending")
    public ResponseEntity<Object> getByPointDescending () {
        return new ResponseEntity<>(taskService.getByPointDescending(),HttpStatus.OK);
    }

    @GetMapping("/find-by-point")
    public ResponseEntity<Object> findByPoint(@RequestBody Integer point) {
        return new ResponseEntity<>(taskService.findByPoint(point),HttpStatus.OK);
    }

    @GetMapping("/find-by-description")
    public ResponseEntity<Object> findByDescription(@RequestBody String description) {
        return new ResponseEntity<>(taskService.findByDescription(description),HttpStatus.OK);
    }

    @GetMapping("/find-by-user")
    public ResponseEntity<Object> findByUser(@RequestBody Long idUser) {
        return new ResponseEntity<>(taskService.findByUser(idUser),HttpStatus.OK);
    }

    @GetMapping("/find-by-parent")
    public ResponseEntity<Object> findByParent(@RequestBody Long idParent) {
        return new ResponseEntity<>(taskService.findByParent(idParent),HttpStatus.OK);
    }

    @GetMapping("/find-by-status")
    public ResponseEntity<Object> findTaskByStatus(@RequestBody String status) {
        return new ResponseEntity<>(taskService.findByStatus(status),HttpStatus.OK);
    }

    @GetMapping("/find-by-tasktype")
    public ResponseEntity<Object> findTaskByTaskType(@RequestBody String status) {
        return new ResponseEntity<>(taskService.findByTaskType(status),HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createNewTask(@RequestBody TaskDto taskDto) {
        User user = userService.findUser(taskDto.getUserId()).get();
        Task task = TaskConverter.mapper(taskDto, user);
        if(task.getPoint() >= 0 && task.getPoint() <= 5){
            taskService.save(task);
            return new ResponseEntity<>(taskService.findByIdDto(task.getId()), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.updateTask(id, taskDto), HttpStatus.OK);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<Object> updateTaskStatus(@PathVariable Long id, @RequestBody String status) {
        return new ResponseEntity<>(taskService.updateStatusTask(id, status), HttpStatus.OK);
    }

    @PutMapping("/update-point/{id}")
    public ResponseEntity<Object> updateTaskPoint(@PathVariable Long id, @RequestBody Integer point) {
        return new ResponseEntity<>(taskService.updatePointTask(id, point), HttpStatus.OK);
    }
}
