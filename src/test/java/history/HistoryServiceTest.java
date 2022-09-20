package history;


import com.taskmanagement.history.*;
import com.taskmanagement.users.IUserRepo;
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

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceTest {

    @Mock
    IHistoryRepo historyRepo;
    HistoryService historyService;


    @BeforeEach
    void init() {
        historyService = new HistoryService(historyRepo);
    }


    @Test
    public void test_findAllHistory_repoFindAll_haveHistory() {
        // GIVEN
        History history = new History();
        history.setId(1L);
        history.setIdTask(1L);
        history.setDescriptionTask("description1");
        history.setInfo("Create: Description: description1 Point: 4 UserID: 1 Assign: Tran Hoang Len");
        //history.setDateTime(LocalDateTime.parse("2021-10-12"));

        Mockito.when(historyRepo.findAll()).thenReturn(List.of(history));

        // WHEN
        List<History> results = historyRepo.findAll();

        // THEN
        Assert.assertEquals(1, results.size());
    }

    @Test
    public void test_findAll_repoFindAll_listNull() {
        // GIVEN
        Mockito.when(historyRepo.findAll()).thenReturn(List.of());

        // WHEN
        List<History> results = historyRepo.findAll();

        // THEN
        Assert.assertEquals(0, results.size());
    }
}
