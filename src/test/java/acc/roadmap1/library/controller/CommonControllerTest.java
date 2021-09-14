package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommonController.class)
class CommonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataSource securityDataSource;

    @MockBean
    private BookService bookService;

    @MockBean
    private ReaderService readerService;

    @Test
    void welcomePage() throws Exception {
        List<Book> books = Arrays.asList(new Book(100, "title", "author", (short)1990, null, false),
                new Book(101, "title1", "author", (short)2000, null, false),
                new Book(102, "title2", "author", (short)2010, null, false));
        when(bookService.findAll()).thenReturn(books);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
//                .andExpect(content()
//                        .json("[{id: 100,title:title,author:author,published:1990}," +
//                                "{id: 101,title:title,author:author,published:2000}," +
//                                "{id: 102,title:title,author:author,published:2010}]", false))
//                .andExpect(content().jsonPath("/html/body/div[3]/table/tbody/tr[1]/td[2]"))
                .andReturn();

    }

    @Test
    void login() {
    }

    @Test
    void error403() {
    }
}