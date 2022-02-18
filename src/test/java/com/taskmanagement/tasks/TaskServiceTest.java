package com.taskmanagement.tasks;

import com.taskmanagement.history.HistoryService;
import com.taskmanagement.users.User;
import com.taskmanagement.users.UserService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    EntityManager entityManager;
    @Mock
    ITaskRepo taskRepo;
    @Mock
    TaskService taskService;
    @Mock
    HistoryService historyService;
    @Mock
    UserService userService;

    @BeforeEach
    void init() {
        taskService = new TaskService(taskRepo, historyService, userService);
        taskService.setEntityManager(entityManager);
    }

    @Test
    public void test() {
        SearchTaskDto searchTaskDto = new SearchTaskDto();
        searchTaskDto.setId(1l);

        CriteriaBuilder mockCriteriaBuilder = Mockito.mock(CriteriaBuilder.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(mockCriteriaBuilder);
        CriteriaQuery<Task> mockCriteriaQuery = Mockito.mock(CriteriaQuery.class);
        Mockito.when(mockCriteriaBuilder.createQuery(Task.class)).thenReturn(mockCriteriaQuery);
        Root<Task> mockRoot = Mockito.mock(Root.class);
        Mockito.when(mockCriteriaQuery.from(Task.class)).thenReturn(mockRoot);

        Path<Object> mockPathId = Mockito.mock(Path.class);
        Mockito.when(mockRoot.get("id")).thenReturn(mockPathId);

        taskService.forTest(searchTaskDto);

        Mockito.verify(mockCriteriaBuilder).equal(mockPathId, searchTaskDto.getId());

    }

    @Test
    public void test_findAllTask_repoFindAll_listHaveTask() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        user.setFirstName("Tran Hoang");
        user.setLastName("Len");
        Task task = new Task();
        task.setId(1L);
        task.setDescription("description1");
        task.setUser(user);
        task.setProgress("TODO");
        task.setParentId(1L);
        task.setPoint(4);
        Mockito.when(taskRepo.findAll()).thenReturn(List.of(task));
        // WHEN
        List<Task> results = taskRepo.findAll();
        // THEN
        Assert.assertEquals(1, results.size());
    }

    @Test
    public void test_findAllTask_repoFindAll_listNullTask() {
        // GIVEN
        Mockito.when(taskRepo.findAll()).thenReturn(List.of());
        // WHEN
        List<Task> results = taskRepo.findAll();
        // THEN
        Assert.assertEquals(0, results.size());
    }

    @Test
    public void test_findTaskById_inputIdIs1_repoFindById() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        user.setFirstName("Tran Hoang");
        user.setLastName("Len");
        Task task = new Task();
        task.setId(1L);
        task.setDescription("description1");
        task.setUser(user);
        task.setProgress("TODO");
        task.setParentId(1L);
        task.setPoint(4);
        // WHEN
        Assert.assertThrows(IllegalArgumentException.class, () -> taskService.findById(null));
        // THEN
    }

    @Test
    public void test_reAssignTask_inputIdTaskIs1_inputIdNewUserIs2_repoFindById() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        user.setFirstName("Tran Hoang");
        user.setLastName("Len");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Nguyen Thi Hoang");
        user2.setLastName("Huyen");

        Task task = new Task();
        task.setId(1L);
        task.setDescription("description1");
        task.setUser(user);
        task.setProgress("TODO");
        task.setParentId(1L);
        task.setPoint(4);

        Mockito.when(userService.findUser(2L)).thenReturn(Optional.of(user2));
        Mockito.when(taskRepo.findById(1L)).thenReturn(Optional.of(task));
        // WHEN
        TaskDto result = taskService.reAssignTask(1L, 2L);

        // THEN
        Assert.assertEquals(user2.getId(), result.getUserId());
        Mockito.verify(taskRepo).save(Mockito.any());
        Mockito.verify(historyService).createHistory(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void test_upDateTask_inputIdTaskIs1_inputTaskDto() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        user.setFirstName("Tran Hoang");
        user.setLastName("Len");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Nguyen Thi Hoang");
        user2.setLastName("Huyen");

        Task task = new Task();
        task.setId(1L);
        task.setDescription("description1");
        task.setUser(user);
        task.setProgress("TODO");
        task.setParentId(1L);
        task.setPoint(4);

        TaskDto taskDto = new TaskDto();
        taskDto.setDescription("description2");
        taskDto.setUserId(2L);
        taskDto.setProgress("IN_PROGRESS");
        taskDto.setParentId(2L);
        taskDto.setPoint(3);

        Mockito.when(taskService.findById(1L)).thenReturn(Optional.of(task));
        Mockito.when(userService.findUser(2L)).thenReturn(Optional.of(user2));
        // WHEN
        Object result = taskService.updateTask(1L, taskDto);

        // THEN
        Mockito.verify(taskRepo).save(Mockito.any());
        Mockito.verify(historyService).createHistory(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void test_upDateTask_inputIdTaskIs1_inputTaskDto_errorPoint() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        user.setFirstName("Tran Hoang");
        user.setLastName("Len");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Nguyen Thi Hoang");
        user2.setLastName("Huyen");

        Task task = new Task();
        task.setId(1L);
        task.setDescription("description1");
        task.setUser(user);
        task.setProgress("TODO");
        task.setParentId(1L);
        task.setPoint(4);

        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(2L);
        taskDto.setDescription("description2");
        taskDto.setProgress("IN_PROGRESS");
        taskDto.setParentId(2L);
        taskDto.setPoint(6);

        Mockito.when(taskService.findById(1L)).thenReturn(Optional.of(task));
        Mockito.when(userService.findUser(2L)).thenReturn(Optional.of(user2));
        // WHEN
        Object result = taskService.updateTask(1L, taskDto);

        // THEN
        Assert.assertEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    public void test_createTask_inputTaskDto_taskHaveParent() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        user.setFirstName("Tran Hoang");
        user.setLastName("Len");

        Task task = new Task();
        task.setId(1L);
        task.setDescription("description1");
        task.setUser(user);
        task.setProgress("TODO");
        task.setParentId(1L);
        task.setPoint(4);

        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(1L);
        taskDto.setDescription("description1");
        taskDto.setParentId(1L);
        taskDto.setPoint(4);


        Mockito.when(userService.findUser(1L)).thenReturn(Optional.of(user));
        Mockito.when(taskService.findById(1L)).thenReturn(Optional.of(task));

        // WHEN
        Object result = taskService.createTask(taskDto);

        // THEN
        Mockito.verify(taskRepo, new Times(2)).save(Mockito.any());
        Mockito.verify(historyService).createHistory(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void test_createTask_inputTaskDto_taskDontHaveParent() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        user.setFirstName("Tran Hoang");
        user.setLastName("Len");

        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(1L);
        taskDto.setDescription("description1");
        taskDto.setPoint(4);

        Mockito.when(userService.findUser(1L)).thenReturn(Optional.of(user));

        // WHEN
        Object result = taskService.createTask(taskDto);

        // THEN
        Mockito.verify(taskRepo, new Times(2)).save(Mockito.any());
        Mockito.verify(historyService).createHistory(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void test_createTask_inputTaskDto_errorPoint() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        user.setFirstName("Tran Hoang");
        user.setLastName("Len");

        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(1L);
        taskDto.setDescription("description1");
        taskDto.setParentId(1L);
        taskDto.setPoint(7);

        Task task = new Task();

        Mockito.when(userService.findUser(1L)).thenReturn(Optional.of(user));
        Mockito.when(taskService.findById(1L)).thenReturn(Optional.of(task));

        // WHEN
        Object result = taskService.createTask(taskDto);

        // THEN
        Assert.assertEquals(HttpStatus.BAD_REQUEST, result);
    }
//    @Test
//    public void test_searchTask_inputTaskDto() {
//        SearchTaskDto searchTaskDto = new SearchTaskDto();
//        searchTaskDto.setId(1l);
//
//        CriteriaBuilder mockCriteriaBuilder = Mockito.mock(CriteriaBuilder.class);
//
//        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(mockCriteriaBuilder);
//        CriteriaQuery<Task> mockCriteriaQuery = Mockito.mock(CriteriaQuery.class);
//        Mockito.when(mockCriteriaBuilder.createQuery(Task.class)).thenReturn(mockCriteriaQuery);
//        Root<Task> mockRoot = Mockito.mock(Root.class);
//        Mockito.when(mockCriteriaQuery.from(Task.class)).thenReturn(mockRoot);
//
//        Path<Object> mockPathId = Mockito.mock(Path.class);
//        Mockito.when(mockRoot.get("id")).thenReturn(mockPathId);
//
//        List<Task> taskList = new ArrayList<>();
//        Mockito.when(mockCriteriaQuery.from(Task.class)).thenReturn(Task);
//
//        taskService.searchTask(searchTaskDto);
//
//        Mockito.verify(mockCriteriaBuilder).equal(mockPathId, searchTaskDto.getId());
//
//    }
}

