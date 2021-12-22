package com.taskmanagement.controller;
import com.taskmanagement.history.IHistoryService;
import com.taskmanagement.tasks.ITaskService;
import com.taskmanagement.tasks.Task;
import com.taskmanagement.tasks.TaskConverter;
import com.taskmanagement.tasks.TaskDto;
import com.taskmanagement.users.User;
import com.taskmanagement.users.UserService;
import lombok.AllArgsConstructor;
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
    private final IHistoryService historyService;
    private final UserService userService;

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
    public ResponseEntity<TaskDto> createNewTask(@RequestBody TaskDto taskDto) {
        User user = userService.findUser(taskDto.getUserId()).get();
        Task task = TaskConverter.mapper(taskDto, user);
        taskService.save(task);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        Task taskFromDB = taskService.findById(id).get();
        String info = new String();
        if((taskDto.getDescription().equals(taskFromDB.getDescription())) == false ){
            info = "Change " + "Description: " + taskFromDB.getDescription() + " to " + taskDto.getDescription()+ " ";
            taskFromDB.setDescription(taskDto.getDescription());
        }
        if(taskDto.getPoint() != taskFromDB.getPoint()){
            info = info +"Change " + "Point: " + taskFromDB.getPoint() + " to " + taskDto.getPoint()+ " ";
            taskFromDB.setPoint(taskDto.getPoint());
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
        }
        historyService.createHistory(taskFromDB.getId(),info);
        taskService.save(taskFromDB);

        return findByIdDto(taskFromDB.getId());
    }

    @PutMapping("/update-status/{id}")
    public TaskDto updateTaskStatus(@PathVariable Long id, @RequestBody String status) {
        Task taskFromDB = taskService.findById(id).get();
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
            historyService.createHistory(taskFromDB.getId(),info);
        }
        taskService.save(taskFromDB);
        return findByIdDto(taskFromDB.getId());
    }

    @PutMapping("/update-point/{id}")
    public TaskDto updateTaskPoint(@PathVariable Long id, @RequestBody Integer point) {
        Task taskFromDB = taskService.findById(id).get();
        String info = new String() ;
        if(point != taskFromDB.getPoint()){
            info = info + "Change " + "Status: " + taskFromDB.getPoint() + " to " + point + " ";
            taskFromDB.setPoint(point);
            historyService.createHistory(taskFromDB.getId(),info);
        }
        taskService.save(taskFromDB);
        return findByIdDto(taskFromDB.getId());
    }

    @DeleteMapping("/delete/{id}")
    public TaskDto deleteTask(@PathVariable Long id) {
        Optional<Task> taskOptional = taskService.findById(id);
        TaskDto taskDto = TaskConverter.Converter(taskOptional.get());
        taskService.remove(id);
        return taskDto;
    }
}
