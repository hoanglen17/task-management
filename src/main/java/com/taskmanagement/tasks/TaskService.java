package com.taskmanagement.tasks;

import com.taskmanagement.history.HistoryService;
import com.taskmanagement.users.User;
import com.taskmanagement.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
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
        if (searchTaskDto.getProgress() != null) {
            predicates.add(cb.like(task.get("progress"), "%" + searchTaskDto.getProgress() + "%"));
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
        if (searchTaskDto.getSortingLastName() != null) {
            if (searchTaskDto.getSortingLastName().equals("ASC")) {
                cq.orderBy(cb.asc(task.get("user").get("lastName")));
            } else if (searchTaskDto.getSortingLastName().equals("DESC")) {
                cq.orderBy(cb.desc(task.get("user").get("lastName")));
            }
        }
        if (searchTaskDto.getSortingPoint() != null) {
            if (searchTaskDto.getSortingPoint().equals("ASC")) {
                cq.orderBy(cb.asc(task.get("point")));
            } else if (searchTaskDto.getSortingPoint().equals("DESC")) {
                cq.orderBy(cb.desc(task.get("point")));
            }
        }
        if (searchTaskDto.getSortingProgress() != null) {
            Expression<Object> caseExpression = cb.selectCase()
                    .when(cb.equal(task.get("progress"), cb.literal("TODO")), 1)
                    .when(cb.equal(task.get("progress"), cb.literal("IN_PROGRESS")), 2)
                    .when(cb.equal(task.get("progress"), cb.literal("DONE")), 3)
                    .otherwise(0);
            if (searchTaskDto.getSortingProgress().equals("ASC")) {
                Order temp2 = cb.asc(caseExpression);
                cq = cq.orderBy(temp2);
            } else if (searchTaskDto.getSortingProgress().equals("DESC")) {
                Order temp2 = cb.desc(caseExpression);
                cq = cq.orderBy(temp2);
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
        if (id == null) {
            throw new IllegalArgumentException("ID NULL!");
        }
        return taskRepo.findById(id);
    }

    @Override
    public List<TaskDto> findByIdDto(Long id) {
        Task task = taskRepo.findById(id).get();
        List<TaskDto> taskList = new ArrayList<>();

        if(task.getParentId() == task.getId()){
            for (Task taskFind : taskRepo.findAll()) {
                TaskDto taskDto = TaskConverter.Converter(taskFind);
                if (task.getId() == taskDto.getParentId()) {
                    taskList.add(taskDto);
                }
            }
        }

        if(task.getParentId() != task.getId()){
            for (Task taskFind : taskRepo.findAll()) {
                TaskDto taskDtoParent = TaskConverter.Converter(taskFind);
                if (task.getParentId() == taskDtoParent.getId()) {
                    taskList.add(taskDtoParent);
                }
            }
            TaskDto taskDto = TaskConverter.Converter(taskRepo.findById(id).get());
            taskList.add(taskDto);
        }

        return taskList;
    }

    @Override
    public Object updateTask(Long id, TaskDto taskDto) {
        Task taskFromDB = findById(id).get();
        String taskHistoryInfo = "Update Task: ";
        if ((taskFromDB.getUser().getId()) != taskDto.getUserId() && taskDto.getUserId() != null) {
            User user = userService.findUser(taskDto.getUserId()).get();
            taskHistoryInfo = taskHistoryInfo + "Assign: " + taskFromDB.getUser().getFirstName() + " " + taskFromDB.getUser().getLastName() + " to " + user.getFirstName() + " " + user.getLastName() + "; ";
            taskFromDB.setUser(user);
        }
        if (((taskFromDB.getDescription().equals(taskDto.getDescription())) == false) && taskDto.getDescription() != null) {
            taskHistoryInfo = taskHistoryInfo + "Description: " + taskFromDB.getDescription() + " to " + taskDto.getDescription() + "; ";
            taskFromDB.setDescription(taskDto.getDescription());
            if (taskFromDB.getParentId() == taskFromDB.getId()) {
                taskFromDB.setParentDescription(taskFromDB.getDescription());
            }
        }
        if (taskFromDB.getPoint() != taskDto.getPoint() && taskDto.getPoint() != null) {
            if (taskDto.getPoint() >= 0 && taskDto.getPoint() <= 5) {
                taskHistoryInfo = taskHistoryInfo + "Point: " + taskFromDB.getPoint() + " to " + taskDto.getPoint() + "; ";
                taskFromDB.setPoint(taskDto.getPoint());
            } else {
                return HttpStatus.BAD_REQUEST;
            }
        }
        if (((taskFromDB.getProgress().equals(taskDto.getProgress())) == false) && taskDto.getDescription() != null) {
            taskHistoryInfo = taskHistoryInfo + "Status: " + taskFromDB.getProgress() + " to " + taskDto.getProgress() + "; ";
            if (taskDto.getProgress().equals("IN_PROGRESS")) {
                taskFromDB.setStartDate(LocalDate.now());
                taskFromDB.setProgress("IN_PROGRESS");
            } else if (taskDto.getProgress().equals("DONE")) {
                taskFromDB.setEndDate(LocalDate.now());
                taskFromDB.setProgress("DONE");
            }
        }
        if (taskHistoryInfo != "Update Task: ") {
            save(taskFromDB);
            historyService.createHistory(taskFromDB.getId(), taskFromDB.getDescription(), taskHistoryInfo);
        }
        return TaskConverter.Converter(taskFromDB);
    }

    @Override
    public Object reAssignTask(Long id, Long newUserId) {
        User user = userService.findUser(newUserId).get();
        Task taskFromDB = findById(id).get();
        String taskHistoryInfo = "ReAssign Task: ";
        if (taskFromDB.getUser().getId() != newUserId) {
            taskHistoryInfo = taskHistoryInfo + "UserId: " + taskFromDB.getUser().getId() + " to " + newUserId + "; ";
            taskHistoryInfo = taskHistoryInfo + "Change " + "UserName: " + taskFromDB.getUser().getLastName() + " to " + user.getLastName() + "; ";
            taskFromDB.setUser(user);
            save(taskFromDB);
            historyService.createHistory(taskFromDB.getId(), taskFromDB.getDescription(), taskHistoryInfo);
        }
        return TaskConverter.Converter(taskFromDB);
    }

    @Override
    public Object createTask(TaskDto taskDto) {

        User user = userService.findUser(taskDto.getUserId()).get();

        if (taskDto.getParentId() != null) {
            Task taskParent = findById(taskDto.getParentId()).get();
            taskDto.setParentDescription(taskParent.getDescription());
        }

        Task task = TaskConverter.mapper(taskDto, user);
        save(task);

        String taskHistoryInfo = "Create Task: ";
        taskHistoryInfo = taskHistoryInfo + "Description: " + task.getDescription() + "; ";

        if (task.getParentId() != null) {
            taskHistoryInfo = taskHistoryInfo + "ParentId: " + task.getParentId() + "; ";
            taskHistoryInfo = taskHistoryInfo + "ParentDescription: " + task.getParentDescription() + "; ";
        } else {
            task.setParentId(task.getId());
            task.setParentDescription(task.getDescription());
            taskHistoryInfo = taskHistoryInfo + "ParentId: " + task.getParentId() + "; ";
            taskHistoryInfo = taskHistoryInfo + "ParentDescription: " + task.getParentDescription() + "; ";
        }
        taskHistoryInfo = taskHistoryInfo + "Point: " + task.getPoint() + "; ";
        taskHistoryInfo = taskHistoryInfo + "UserID: " + task.getUser().getId() + " ";
        taskHistoryInfo = taskHistoryInfo + "Assign: " + task.getUser().getFirstName() + " " + task.getUser().getLastName() + "; ";
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
