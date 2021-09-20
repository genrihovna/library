package acc.roadmap1.library.mvc;

import acc.roadmap1.library.model.Account;
import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.BookStatus;
import acc.roadmap1.library.model.Privilege;
import acc.roadmap1.library.model.Privileges;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.model.Role;
import acc.roadmap1.library.model.RoleNames;
import acc.roadmap1.library.service.BookService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class RootControllerTest {

    private final MockMvc mockMvc;

    private final WebClient webClient;

    @MockBean
    private BookService bookService;

    @Autowired
    public RootControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc)
                .alwaysUseMockMvc()
                .build();
    }

    @Test
    public void checkValidTableMapping() throws IOException {
        HtmlPage createMsgFormPage = webClient.getPage("http://localhost:8080");
        HtmlTable listOfBooksTable = createMsgFormPage.getHtmlElementById("list-of-books");

        Assertions.assertEquals("List of books", listOfBooksTable.getCaptionText());
        Assertions.assertEquals(1, listOfBooksTable.getRowCount());
    }

    @Test
    public void checkValidLoadBooksInTable() throws IOException {

        Mockito.when(bookService.findAll()).thenReturn(List.of(
                new Book("author", 1991, "title")
        ));

        HtmlPage createMsgFormPage = webClient.getPage("http://localhost:8080");
        HtmlTable listOfBooksTable = createMsgFormPage.getHtmlElementById("list-of-books");

        Assertions.assertEquals("List of books", listOfBooksTable.getCaptionText());
        Assertions.assertEquals(2, listOfBooksTable.getRowCount());
        Assertions.assertEquals("0", listOfBooksTable.getRow(1).getCell(0).getTextContent());
        Assertions.assertEquals("title", listOfBooksTable.getRow(1).getCell(1).getTextContent());
    }

    @Test
    @WithMockUser(username = "test", password = "123456", authorities = {"MANAGE_BOOKS"})
    public void checkValidAuthLoadBooksInTable() throws IOException {

        Book book = Mockito.mock(Book.class);

        Mockito.when(book.getReader())
                .thenReturn(
                        new Reader(
                                "test",
                                new Account(
                                        "test",
                                        "123456",
                                        Collections.singleton(
                                                new Role(RoleNames.READER.name(),
                                                        Set.of(
                                                                new Privilege(Privileges.MANAGE_BOOKS.name())
                                                        )
                                                )
                                        )
                                )
                        )
                );

        Mockito.when(book.getAuthor()).thenReturn("author");
        Mockito.when(book.getStatus()).thenReturn(BookStatus.CAN_TAKE);
        Mockito.when(book.getId()).thenReturn(Long.valueOf(1));
        Mockito.when(book.getPublished()).thenReturn(1991);
        Mockito.when(book.getTitle()).thenReturn("title");

        Mockito.when(bookService.findAll()).thenReturn(List.of(
                book
        ));

        HtmlPage createMsgFormPage = webClient.getPage("http://localhost:8080");
        HtmlTable listOfBooksTable = createMsgFormPage.getHtmlElementById("list-of-books");

        Assertions.assertEquals("List of books", listOfBooksTable.getCaptionText());
        Assertions.assertEquals(2, listOfBooksTable.getRowCount());
        Assertions.assertEquals("1", listOfBooksTable.getRow(1).getCell(0).getTextContent());
        Assertions.assertEquals("title", listOfBooksTable.getRow(1).getCell(1).getTextContent());
        Assertions.assertNotNull(createMsgFormPage.getForms().get(0).getInputByValue("Take"));
    }
}
