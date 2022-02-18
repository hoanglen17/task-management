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
    private EntityManager entityManager;

    @Override
    public List<TaskDto> findAll() {
        List<TaskDto> listTaskDto = new ArrayList<>();
        for (Task task : taskRepo.findAll()) {
            listTaskDto.add(TaskConverter.Converter(task));
        }
        return listTaskDto;
    }

    protected void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<TaskDto> forTest(SearchTaskDto searchTaskDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);

        Root<Task> task = criteriaQuery.from(Task.class);
        List<Predicate> predicates = new ArrayList<>();

        if (null != searchTaskDto.getId()) {
            predicates.add(criteriaBuilder.equal(task.get("id"), searchTaskDto.getId()));
        }

        // entityManager.createQuery(criteriaQuery).getResultList();
        return null;
    }

    @Override
    public List<TaskDto> searchTask(SearchTaskDto searchTaskDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);

        Root<Task> rootTask = criteriaQuery.from(Task.class);
        List<Predicate> predicates = new ArrayList<>();

        if (searchTaskDto.getId() != null) {
            predicates.add(criteriaBuilder.equal(rootTask.get("id"), searchTaskDto.getId()));
        }
        if (searchTaskDto.getDescription() != null) {
            predicates.add(criteriaBuilder.like(rootTask.get("description"), "%" + searchTaskDto.getDescription() + "%"));
        }
        if (searchTaskDto.getPoint() != null) {
            predicates.add(criteriaBuilder.equal(rootTask.get("point"), searchTaskDto.getPoint()));
        }
        if (searchTaskDto.getPointMin() != null) {
            predicates.add(criteriaBuilder.between(rootTask.get("point"), searchTaskDto.getPointMin(), searchTaskDto.getPointMax()));
        }
        if (searchTaskDto.getPointMax() != null) {
            predicates.add(criteriaBuilder.between(rootTask.get("point"), searchTaskDto.getPointMin(), searchTaskDto.getPointMax()));
        }
        if (searchTaskDto.getProgress() != null) {
            predicates.add(criteriaBuilder.like(rootTask.get("progress"), "%" + searchTaskDto.getProgress() + "%"));
        }
        if (searchTaskDto.getUserId() != null) {
            predicates.add(criteriaBuilder.equal(rootTask.get("user").get("id"), searchTaskDto.getUserId()));
        }
        if (searchTaskDto.getUserFirstName() != null) {
            predicates.add(criteriaBuilder.like(rootTask.get("user").get("firstName"), "%" + searchTaskDto.getUserFirstName() + "%"));
        }
        if (searchTaskDto.getUserLastName() != null) {
            predicates.add(criteriaBuilder.like(rootTask.get("user").get("lastName"), "%" + searchTaskDto.getUserLastName() + "%"));
        }
        if (searchTaskDto.getSortingLastName() != null) {
            if (searchTaskDto.getSortingLastName().equals("ASC")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(rootTask.get("user").get("lastName")));
            } else if (searchTaskDto.getSortingLastName().equals("DESC")) {
                criteriaQuery.orderBy(criteriaBuilder.desc(rootTask.get("user").get("lastName")));
            }
        }
        if (searchTaskDto.getSortingPoint() != null) {
            if (searchTaskDto.getSortingPoint().equals("ASC")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(rootTask.get("point")));
            } else if (searchTaskDto.getSortingPoint().equals("DESC")) {
                criteriaQuery.orderBy(criteriaBuilder.desc(rootTask.get("point")));
            }
        }
        if (searchTaskDto.getSortingProgress() != null) {
            Expression<Object> caseExpression = criteriaBuilder.selectCase()
                    .when(criteriaBuilder.equal(rootTask.get("progress"), criteriaBuilder.literal("TODO")), 1)
                    .when(criteriaBuilder.equal(rootTask.get("progress"), criteriaBuilder.literal("IN_PROGRESS")), 2)
                    .when(criteriaBuilder.equal(rootTask.get("progress"), criteriaBuilder.literal("DONE")), 3)
                    .otherwise(0);
            if (searchTaskDto.getSortingProgress().equals("ASC")) {
                Order temp2 = criteriaBuilder.asc(caseExpression);
                criteriaQuery = criteriaQuery.orderBy(temp2);
            } else if (searchTaskDto.getSortingProgress().equals("DESC")) {
                Order temp2 = criteriaBuilder.desc(caseExpression);
                criteriaQuery = criteriaQuery.orderBy(temp2);
            }
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        List<TaskDto> taskList = new ArrayList<>();
        if (searchTaskDto != null) {
            for (Task taskFind : entityManager.createQuery(criteriaQuery).getResultList()) {
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

        if (task.getParentId() == task.getId()) {
            for (Task taskFind : taskRepo.findAll()) {
                TaskDto taskDto = TaskConverter.Converter(taskFind);
                if (task.getId() == taskDto.getParentId()) {
                    taskList.add(taskDto);
                }
            }
        }

        if (task.getParentId() != task.getId()) {
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
    public TaskDto reAssignTask(Long id, Long newUserId) {
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
