package com.taskmanagement.users;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    IUserRepo userRepo;
    UserService userService;

    @BeforeEach
    void init() {
        userService = new UserService(userRepo);
    }

    @Test
    public void test_findUser_inputIdIs1_repoFindById() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        user.setFirstName("Len");
        user.setLastName("Tran");

        // WHEN
        Assert.assertThrows(IllegalArgumentException.class, () -> userService.findUser(null));

        // THEN
    }

    @Test
    public void test_findAll_repoFindAll_haveUser() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        user.setFirstName("Len");
        user.setLastName("Tran");

        Mockito.when(userRepo.findAll()).thenReturn(List.of(user));

        // WHEN
        List<UserDto> results = userService.findAll();

        // THEN
        Assert.assertEquals(1, results.size());
    }

    @Test
    public void test_findAll_repoFindAll_listNull() {
        // GIVEN
        Mockito.when(userRepo.findAll()).thenReturn(List.of());

        // WHEN
        List<UserDto> results = userService.findAll();

        // THEN
        Assert.assertEquals(0, results.size());
    }
}