package com.taskmanagement.tasks;

import com.taskmanagement.history.History;
import com.taskmanagement.history.HistoryDto;
import com.taskmanagement.history.HistoryService;
import com.taskmanagement.users.User;
import com.taskmanagement.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final ITaskRepo taskRepo;
    private final HistoryService historyService;
    private final UserService userService;


    @Override
    public List<TaskDto> findAll() {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            listTaskDto.add(TaskConverter.Converter(task));
        }
        return listTaskDto;
    }

    @Override
    public List<TaskDto> findByPoint(Integer point) {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if (task.getPoint() == point) {
                listTaskDto.add(TaskConverter.Converter(task));
            }
        }
        return listTaskDto;
    }

    @Override
    public List<TaskDto> findByStatus(String status) {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if (task.getStatus().equals((status))) {
                listTaskDto.add(TaskConverter.Converter(task));
            }
        }
        return listTaskDto;
    }

    @Override
    public List<TaskDto> getByStatusAscending() {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if (task.getStatus().equals("TODO")) {
                listTaskDto.add(TaskConverter.Converter(task));
            }
        }
        for (Task task : taskRepo.findAll()) {
            if (task.getStatus().equals("IN_PROGRESS")) {
                listTaskDto.add(TaskConverter.Converter(task));
            }
        }
        for (Task task : taskRepo.findAll()) {
            if (task.getStatus().equals("DONE")) {
                listTaskDto.add(TaskConverter.Converter(task));
            }
        }
        return listTaskDto;
    }

    @Override
    public List<TaskDto> getByStatusDescending() {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if (task.getStatus().equals("DONE")) {
                listTaskDto.add(TaskConverter.Converter(task));
            }
        }
        for (Task task : taskRepo.findAll()) {
            if (task.getStatus().equals("IN_PROGRESS")) {
                listTaskDto.add(TaskConverter.Converter(task));
            }
        }
        for (Task task : taskRepo.findAll()) {
            if (task.getStatus().equals("TODO")) {
                listTaskDto.add(TaskConverter.Converter(task));
            }
        }
        return listTaskDto;
    }

    @Override
    public List<TaskDto> getByPointAscending() {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAllOrderByPointAsc()) {
            listTaskDto.add(TaskConverter.Converter(task));
        }
        return listTaskDto;
    }

    @Override
    public List<TaskDto> getByPointDescending() {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAllOrderByPointDesc()) {
            listTaskDto.add(TaskConverter.Converter(task));
        }
        return listTaskDto;
    }

    @Override
    public List<TaskDto> findByDescription(String description) {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if (task.getDescription().equals((description))) {
                listTaskDto.add(TaskConverter.Converter(task));
            }
        }
        return listTaskDto;
    }

    @Override
    public List<TaskDto> findByUser(Long id) {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if (task.getUser().getId() == id) {
                listTaskDto.add(TaskConverter.Converter(task));
            }
        }
        return listTaskDto;
    }

    @Override
    public List<TaskDto> findByParent(Long id) {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            if (task.getParent().getId() == id) {
                listTaskDto.add(TaskConverter.Converter(task));
            }
        }
        return listTaskDto;
    }

    @Override
    public List<TaskDto> findByTaskType(String taskType) {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            TaskDto taskDto = TaskConverter.Converter(task);
            if (taskDto.getTaskType().equals(taskType)) {
                listTaskDto.add(TaskConverter.Converter(task));
            }
        }
        return listTaskDto;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepo.findById(id);
    }

    @Override
    public List<TaskDto> findByIdDto(Long id) {
        List<TaskDto> taskList = new ArrayList<>();
        for (Task taskFind : taskRepo.findAll()) {
            TaskDto taskDto = TaskConverter.Converter(taskFind);
            if (id == taskDto.getId()) {
                taskList.add(taskDto);
                continue;
            }
            if (id == taskDto.getParentId()) {
                taskList.add(taskDto);
            }
        }
        return taskList;
    }

    @Override
    public Object updateTask(Long id, TaskDto taskDto) {
        Task taskFromDB = findById(id).get();
        String taskHistoryInfo = new String();
        if ((taskDto.getDescription().equals(taskFromDB.getDescription())) == false) {
            taskHistoryInfo = "Description: " + taskFromDB.getDescription() + " to " + taskDto.getDescription() + " ";
            taskFromDB.setDescription(taskDto.getDescription());
            save(taskFromDB);
        }
        if (taskDto.getPoint() != taskFromDB.getPoint()) {
            if (taskDto.getPoint() >= 0 && taskDto.getPoint() <= 5) {
                taskHistoryInfo = taskHistoryInfo + "Point: " + taskFromDB.getPoint() + " to " + taskDto.getPoint() + " ";
                taskFromDB.setPoint(taskDto.getPoint());
                save(taskFromDB);
            } else {
                return HttpStatus.BAD_REQUEST;
            }
        }
        if ((taskDto.getStatus().equals(taskFromDB.getStatus())) == false) {
            taskHistoryInfo = taskHistoryInfo + "Status: " + taskFromDB.getStatus() + " to " + taskDto.getStatus() + " ";
            if (taskDto.getStatus().equals("IN_PROGRESS")) {
                taskFromDB.setStartDate(LocalDate.now());
                taskFromDB.setStatus("IN_PROGRESS");
            } else if (taskDto.getStatus().equals("DONE")) {
                taskFromDB.setEndDate(LocalDate.now());
                taskFromDB.setStatus("DONE");
            }
            save(taskFromDB);
        }
        historyService.createHistory(taskFromDB.getId(), taskFromDB.getDescription(), taskHistoryInfo);
        return TaskConverter.Converter(taskFromDB);
    }

    @Override
    public Object reAssignTask(Long id, Long userId) {
        User user = userService.findUser(userId).get();
        Task taskFromDB = findById(id).get();
        String taskHistoryInfo = new String();
        if (taskFromDB.getUser().getId() != userId) {
            taskHistoryInfo = "UserId: " + taskFromDB.getUser().getId() + " to " + userId + " ";
            taskHistoryInfo = taskHistoryInfo + "Change " + "UserName: " + taskFromDB.getUser().getLastName() + " to " + user.getLastName() + " ";
            taskFromDB.setUser(user);
            save(taskFromDB);
        }
        historyService.createHistory(taskFromDB.getId(), taskFromDB.getDescription(), taskHistoryInfo);
        return TaskConverter.Converter(taskFromDB);
    }

    @Override
    public Object createTask(TaskDto taskDto) {
        User user = userService.findUser(taskDto.getUserId()).get();
        Task taskParent = findById(taskDto.getParentId()).get();
        Task task = TaskConverter.mapper(taskDto, user, taskParent);

        String taskHistoryInfo = new String();
        taskHistoryInfo = "Description: " + taskDto.getDescription() + " ";
        taskHistoryInfo = taskHistoryInfo + "Point: " + task.getPoint() + " ";
        taskHistoryInfo = taskHistoryInfo + "UserID: " + task.getUser().getId() + " ";
        taskHistoryInfo = taskHistoryInfo + "Assign: " + task.getUser().getFirstName() + " " + task.getUser().getLastName() + " ";
        task.setStatus("TODO");
        if (taskDto.getPoint() >= 0 && taskDto.getPoint() <= 5) {
            save(task);
            historyService.createHistory(task.getId(), task.getDescription(), taskHistoryInfo);
            return TaskConverter.Converter(task);
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public Task save(Task task) {
        return taskRepo.save(task);
    }
}
