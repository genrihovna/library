package acc.roadmap1.library.controller;

import acc.roadmap1.library.dao.BookRepo;
import acc.roadmap1.library.service.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

@WebMvcTest(CommonController.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommonControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private DataSource securityDataSource;
//
//    @InjectMocks
//    private BookServiceImpl bookService;
//
//    @Mock
//    private BookRepo repository;

    @Test
    void welcomePage() {

    }

    @Test
    void login() {
    }

    @Test
    void error403() {
    }
}