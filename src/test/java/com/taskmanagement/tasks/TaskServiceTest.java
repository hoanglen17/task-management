package com.taskmanagement.tasks;

import com.taskmanagement.history.HistoryService;
import com.taskmanagement.users.User;
import com.taskmanagement.users.UserDto;
import com.taskmanagement.users.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {


    @Mock
    ITaskRepo taskRepo;
    TaskService taskService;
    HistoryService historyService;
    UserService userService;

    @BeforeEach
    void init() {
        taskService = new TaskService(taskRepo,historyService,userService);
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
}