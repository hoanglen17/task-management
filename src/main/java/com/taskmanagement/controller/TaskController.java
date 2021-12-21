package com.taskmanagement.controller;

import com.taskmanagement.history.History;
import com.taskmanagement.history.IHistoryService;
import com.taskmanagement.tasks.ITaskService;
import com.taskmanagement.tasks.Task;
import com.taskmanagement.tasks.TaskDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    @Autowired
    private final ITaskService taskService;
    private final IHistoryService historyService;

    @GetMapping("/get")
    public List<TaskDto> getAllTask() {
        return taskService.findAll();
    }

    @GetMapping("/get/{id}")
    public TaskDto findByIdDto(@PathVariable Long id) {
       return taskService.findByIdDto(id);
    }

    @GetMapping("/get-by-status/ascending")
    public List<TaskDto> getTaskByStatusAscending () {
        return  taskService.getByStatusAscending();
    }
    @GetMapping("/get-by-status/descending")
    public List<TaskDto> getTaskByStatusDescending () {
        return  taskService.getByStatusDescending();
    }


    @GetMapping("/get-by-point/ascending")
    public List<TaskDto> getTaskByPointAscending () {
        return  taskService.getByPointAscending();
    }
    @GetMapping("/get-by-point/descending")

    public List<TaskDto> getByPointDescending () {
        return  taskService.getByPointDescending();
    }
    @GetMapping("/find-by-point")
    public List<TaskDto> findByPoint(@RequestBody Integer point) {
        return  taskService.findByPoint(point);
    }

    @GetMapping("/find-by-description")
    public List<TaskDto> findByDescription(@RequestBody String description) {
        return  taskService.findByDescription(description);
    }
    @GetMapping("/find-by-user")
    public List<TaskDto> findByUser(@RequestBody Long idUser) {
        return  taskService.findByUser(idUser);
    }
    @GetMapping("/find-by-parent")
    public List<TaskDto> findByParent(@RequestBody Long idParent) {
        return  taskService.findByParent(idParent);
    }
    @GetMapping("/find-by-status")
    public List<TaskDto> getTaskByStatus(@RequestBody String status) {
        return  taskService.findByStatus(status);
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createNewTask(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.save(task), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        Task taskFromDB = taskService.findById(id).get();
        History history = new History();
        String info = new String();

        if((taskDto.getDescription().equals(taskFromDB.getDescription())) == false ){
            info = "Change " + "Description: " + taskFromDB.getDescription() + " to " + taskDto.getDescription()+ " ";
            history.setIdTask(taskFromDB.getId());
            LocalDateTime time = LocalDateTime.now();
            history.setDateTime(time);
            history.setInfo(info);
            historyService.save(history);
            taskFromDB.setDescription(taskDto.getDescription());
        }

        if(taskDto.getPoint() != taskFromDB.getPoint()){
            info = info +"Change " + "Point: " + taskFromDB.getPoint() + " to " + taskDto.getPoint()+ " ";
            history.setIdTask(taskFromDB.getId());
            LocalDateTime time = LocalDateTime.now();
            history.setDateTime(time);
            history.setInfo(info);
            historyService.save(history);
        }

        if((taskDto.getStatus().equals(taskFromDB.getStatus())) == false ){
            info = info + "Change " + "Status: " + taskFromDB.getStatus() + " to " + taskDto.getStatus()+ " ";
            if(taskDto.getStatus().equals("IN_PROGRESS")){
                taskFromDB.setStartDate(LocalDate.now()) ;
                taskFromDB.setStatus("IN_PROGRESS");
            }else if (taskDto.getStatus().equals("DONE")){
                taskFromDB.setEndDate(LocalDate.now());
                taskFromDB.setStatus("DONE");
            }else if(taskDto.getStatus().equals("TODO")){
                taskFromDB.setStatus("TODO");
                taskFromDB.setStartDate(null) ;
                taskFromDB.setEndDate(null);
            }

            history.setIdTask(taskFromDB.getId());
            LocalDateTime time = LocalDateTime.now();
            history.setDateTime(time);
            history.setInfo(info);
            historyService.save(history);
        }

        taskService.save(taskFromDB);

        return findByIdDto(taskFromDB.getId());
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody String status) {
        Task taskFromDB = taskService.findById(id).get();
        History history = new History();
        String info = new String() ;

        if((status.equals(taskFromDB.getStatus())) == false ){
            info = info + "Change " + "Status: " + taskFromDB.getStatus() + " to " + status + " ";
            if(status.equals("IN_PROGRESS")){
                taskFromDB.setStartDate(LocalDate.now()) ;
                taskFromDB.setStatus("IN_PROGRESS");
            }else if (status.equals("DONE")){
                taskFromDB.setEndDate(LocalDate.now());
                taskFromDB.setStatus("DONE");
            }else if(status.equals("TODO")){
                taskFromDB.setStatus("TODO");
                taskFromDB.setStartDate(null) ;
                taskFromDB.setEndDate(null);
            }
            history.setIdTask(taskFromDB.getId());
            LocalDateTime time = LocalDateTime.now();
            history.setDateTime(time);
            history.setInfo(info);
            historyService.save(history);
        }

        return new ResponseEntity<>(taskService.save(taskFromDB), HttpStatus.OK);
    }

    @PutMapping("/update-point/{id}")
    public ResponseEntity<Task> updateTaskPoint(@PathVariable Long id, @RequestBody Integer point) {
        Task taskFromDB = taskService.findById(id).get();
        History history = new History();
        String info = new String() ;
        if(point != taskFromDB.getPoint()){
            info = info + "Change " + "Status: " + taskFromDB.getPoint() + " to " + point + " ";
            taskFromDB.setPoint(point);
            history.setIdTask(taskFromDB.getId());
            LocalDateTime time = LocalDateTime.now();
            history.setDateTime(time);
            history.setInfo(info);
            historyService.save(history);
        }
        return new ResponseEntity<>(taskService.save(taskFromDB), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Task> deleteCategory(@RequestBody Long id) {
        Optional<Task> taskOptional = taskService.findById(id);
        return taskOptional.map(task -> {
            taskService.remove(id);
            return new ResponseEntity<>(task, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
