package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final List<Book> books = Arrays.asList(new Book(100, "title", "author", (short)1990, null, false),
            new Book(101, "title1", "author", (short)2000, null, false),
            new Book(102, "title2", "author", (short)2010, null, false));

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private ReaderService readerService;

    @MockBean
    private DataSource securityDataSource;

    @Test
    void getAllBooksTest() throws Exception {
        when(bookService.findAll()).thenReturn(books);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/books")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books)))
                .andReturn();
    }

    @Test
    void getBookByIdTest() throws Exception {
        when(bookService.findById(0)).thenReturn(books.get(0));
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/book/0")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result= mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books.get(0))))
                .andReturn();
    }

    @Test
    void addBookTest() throws Exception {
        int bookId = 0;
        when(bookService.save(new Book())).thenReturn(books.get(bookId));
        Book theBook = bookService.save(new Book());
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/books", theBook)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                ;
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books.get(bookId))))
                .andReturn();
        //verify(bookService, times(1)).save(new Book());
    }
}