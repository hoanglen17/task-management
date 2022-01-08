package com.taskmanagement.tasks;

import com.taskmanagement.history.HistoryService;
import com.taskmanagement.users.User;
import com.taskmanagement.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    @PersistenceContext
    EntityManager em;

    @Override
    public List<TaskDto> findAll() {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            listTaskDto.add(TaskConverter.Converter(task));
        }
        return listTaskDto;
    }

    @Override
    public List<TaskDto> searchTask(SearchTaskDto searchTaskDto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);

        Root<Task> task = cq.from(Task.class);
        List<Predicate> predicates = new ArrayList<>();

        if (searchTaskDto.getId() != null) {
            predicates.add(cb.equal(task.get("id"), searchTaskDto.getId()));
        }
        if (searchTaskDto.getDescription() != null) {
            predicates.add(cb.like(task.get("description"), "%" + searchTaskDto.getDescription() + "%"));
        }
        if (searchTaskDto.getPoint() != null) {
            predicates.add(cb.equal(task.get("point"), searchTaskDto.getPoint()));
        }
        if (searchTaskDto.getPointMin() != null) {
            predicates.add(cb.between(task.get("point"), searchTaskDto.getPointMin(), searchTaskDto.getPointMax()));
        }
        if (searchTaskDto.getPointMax() != null) {
            predicates.add(cb.between(task.get("point"), searchTaskDto.getPointMin(), searchTaskDto.getPointMax()));
        }
        if (searchTaskDto.getStatus() != null) {
            predicates.add(cb.like(task.get("progress"), "%" + searchTaskDto.getStatus() + "%"));
        }
        if (searchTaskDto.getUserId() != null) {
            predicates.add(cb.equal(task.get("user").get("id"), searchTaskDto.getUserId()));
        }
        if (searchTaskDto.getUserFirstName() != null) {
            predicates.add(cb.like(task.get("user").get("firstName"), "%" + searchTaskDto.getUserFirstName() + "%"));
        }
        if (searchTaskDto.getUserLastName() != null) {
            predicates.add(cb.like(task.get("user").get("lastName"), "%" + searchTaskDto.getUserLastName() + "%"));
        }

        if (searchTaskDto.getSortingPoint() != null) {
            if (searchTaskDto.getSortingPoint().equals("ASC")) {
                cq.orderBy(cb.asc(task.get("point")));
            } else if (searchTaskDto.getSortingPoint().equals("DESC")) {
                cq.orderBy(cb.desc(task.get("point")));
            }
        }
        if (searchTaskDto.getSortingStatus() != null) {
            if (searchTaskDto.getSortingStatus().equals("ASC")) {
                cq.orderBy(cb.asc(task.get("status")));
            } else if (searchTaskDto.getSortingStatus().equals("DESC")) {
                cq.orderBy(cb.desc(task.get("status")));
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        List<TaskDto> taskList = new ArrayList<>();
        if (searchTaskDto != null) {
            for (Task taskFind : em.createQuery(cq).getResultList()) {
                TaskDto taskDto = TaskConverter.Converter(taskFind);
                taskList.add(taskDto);
            }
        }
        return taskList;
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
        if (taskDto != null) {
            String taskHistoryInfo = "Update Task: ";
            if ((taskFromDB.getUser().getId()) != taskDto.getUserId() && taskDto.getUserId() != null) {
                User user = userService.findUser(taskDto.getUserId()).get();
                taskFromDB.setUser(user);
                taskHistoryInfo = taskHistoryInfo + "Assign: " + taskFromDB.getUser().getFirstName() + " " + taskFromDB.getUser().getLastName() + " to " + user.getFirstName() + " " + user.getLastName() + " ";
                save(taskFromDB);
            }
            if (((taskFromDB.getDescription().equals(taskDto.getDescription())) == false) && taskDto.getDescription() != null) {
                taskHistoryInfo = taskHistoryInfo + "Description: " + taskFromDB.getDescription() + " to " + taskDto.getDescription() + " ";
                taskFromDB.setDescription(taskDto.getDescription());
                save(taskFromDB);
            }
            if (taskFromDB.getPoint() != taskDto.getPoint() && taskDto.getPoint() != null) {
                if (taskDto.getPoint() >= 0 && taskDto.getPoint() <= 5) {
                    taskHistoryInfo = taskHistoryInfo + "Point: " + taskFromDB.getPoint() + " to " + taskDto.getPoint() + " ";
                    taskFromDB.setPoint(taskDto.getPoint());
                    save(taskFromDB);
                } else {
                    return HttpStatus.BAD_REQUEST;
                }
            }
            if (((taskFromDB.getProgress().equals(taskDto.getProgress())) == false) && taskDto.getDescription() != null) {
                taskHistoryInfo = taskHistoryInfo + "Status: " + taskFromDB.getProgress() + " to " + taskDto.getProgress() + " ";
                if (taskDto.getProgress().equals("IN_PROGRESS")) {
                    taskFromDB.setStartDate(LocalDate.now());
                    taskFromDB.setProgress("IN_PROGRESS");
                } else if (taskDto.getProgress().equals("DONE")) {
                    taskFromDB.setEndDate(LocalDate.now());
                    taskFromDB.setProgress("DONE");
                }
                save(taskFromDB);
            }
            historyService.createHistory(taskFromDB.getId(), taskFromDB.getDescription(), taskHistoryInfo);
        }
        return TaskConverter.Converter(taskFromDB);
    }

    @Override
    public Object reAssignTask(Long id, Long userId) {
        User user = userService.findUser(userId).get();
        Task taskFromDB = findById(id).get();
        String taskHistoryInfo = new String();
        if (taskFromDB.getUser().getId() != userId) {
            taskHistoryInfo = "ReAssign Task: ";
            taskHistoryInfo = taskHistoryInfo + "UserId: " + taskFromDB.getUser().getId() + " to " + userId + " ";
            taskHistoryInfo = taskHistoryInfo + "Change " + "UserName: " + taskFromDB.getUser().getLastName() + " to " + user.getLastName() + " ";
            taskFromDB.setUser(user);
            save(taskFromDB);
            historyService.createHistory(taskFromDB.getId(), taskFromDB.getDescription(), taskHistoryInfo);
        }
        return TaskConverter.Converter(taskFromDB);
    }

    @Override
    public Object createTask(TaskDto taskDto) {

        User user = userService.findUser(taskDto.getUserId()).get();
        Task taskParent = new Task();

        if(taskDto.getParentId() != null){
            taskParent  = findById(taskDto.getParentId()).get();
        }else{
            taskParent = null;
        }

        Task task = TaskConverter.mapper(taskDto, user, taskParent);

        String taskHistoryInfo = new String();
        taskHistoryInfo = "Create: ";
        taskHistoryInfo = taskHistoryInfo + "Description: " + taskDto.getDescription() + " ";
        taskHistoryInfo = taskHistoryInfo + "Point: " + task.getPoint() + " ";
        taskHistoryInfo = taskHistoryInfo + "ParentId" + task.getParent().getId();
        taskHistoryInfo = taskHistoryInfo + "UserID: " + task.getUser().getId() + " ";
        taskHistoryInfo = taskHistoryInfo + "Assign: " + task.getUser().getFirstName() + " " + task.getUser().getLastName() + " ";
        task.setProgress("TODO");

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
