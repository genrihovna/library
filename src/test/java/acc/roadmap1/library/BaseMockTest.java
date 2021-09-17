package acc.roadmap1.library;

import acc.roadmap1.library.repository.AccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;

public class BaseMockTest {

    @MockBean
    private AccountRepository accountRepository;
}
