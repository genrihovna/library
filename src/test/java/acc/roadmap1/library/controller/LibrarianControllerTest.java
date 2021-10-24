package acc.roadmap1.library.controller;

import acc.roadmap1.library.controller.dto.CreateBookItem;
import acc.roadmap1.library.model.Account;
import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LibrarianControllerTest {

    private static final Faker FAKER = new Faker();
    private final MockMvc mockMvc;
    private final WebClient webClient;

    @MockBean
    private ReaderService readerService;
    @MockBean
    private BookService bookService;

    @Autowired
    public LibrarianControllerTest(MockMvc mockMvc){
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc)
                .alwaysUseMockMvc().build();
    }

    @DisplayName("test how list of readers is presented")
    @Test
    public void listOfReadersTest() throws IOException {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        //create the page
        HtmlPage readersListPage = webClient.getPage("http://localhost:8080/librarian/readers");
        //initialize the table
        HtmlTable readersList = readersListPage.getHtmlElementById("readers-list");

        assertEquals("Readers", readersList.getCaptionText());
        assertEquals(1, readersList.getRowCount());
    }

    @DisplayName("test new book adding")
    @Test
    public void addNewBookTest() throws Exception {
        MockHttpServletRequestBuilder builder = post("/books/add")
                .param("title", FAKER.shakespeare().hamletQuote())
                .param("author", FAKER.name().lastName())
                .param("published", FAKER.number().digit())
                .with(csrf());
        mockMvc.perform(builder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/librarian"))
                .andReturn();
        HtmlPage page = webClient.getPage("http://localhost:8080/librarian");
        HtmlTable table = page.getHtmlElementById("table"); //fails here
        assertEquals(2, table.getRowCount());
    }

    @DisplayName("test new book adding with sick data")
    @Test
    public void addNewBookSickDataTest() throws Exception {
        MockHttpServletRequestBuilder builder = post("/books/add")
                .param("title", FAKER.shakespeare().hamletQuote())
                .param("author", FAKER.name().lastName())
                .param("published", FAKER.name().bloodGroup())
                .with(csrf());
        mockMvc.perform(builder)
                .andExpect(status().is5xxServerError())
                .andReturn();
    }

    @DisplayName("test librarian delete the book")
    @WithMockUser(authorities = {"MANAGE_ACCOUNTS"})
    @Test
    public void deleteBookTest() throws Exception {
        //create a book to delete and mock its behavior
        Book bookToDelete = Mockito.mock(Book.class);

        //mock get request
        mockMvc.perform(get("/books/delete?id=" + bookToDelete.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/librarian"))
                .andReturn();
    }

    @WithMockUser(authorities = {"MANAGE_ACCOUNTS"})
    @DisplayName("test librarian update the book")
    @Test
    public void updateBookTest() throws Exception {

        Book bookToUpdate = Mockito.mock(Book.class);

        MockHttpServletRequestBuilder builder = get("/books/update?id=" + bookToUpdate.getId())
                .param("title", bookToUpdate.getTitle())
                .param("author", bookToUpdate.getAuthor())
                .param("published", FAKER.number().digit())
                .with(csrf());
        mockMvc.perform(builder)
                .andExpect(status().is2xxSuccessful())
                .andExpect(forwardedUrl("/books/add"))
                .andReturn();
    }

    @WithMockUser(username = "user", password = "pwd", authorities = {"MANAGE_BOOKS"})
    @DisplayName("test reader takes the book")
    @Test
    public void takeABookTest() throws Exception {

        Account account = Mockito.mock(Account.class);
        Reader reader = Mockito.mock(Reader.class);
        ApplicationUserDetails userDetails = Mockito.mock(ApplicationUserDetails.class);
        Book bookToTake = Mockito.mock(Book.class);

        Mockito.when(userDetails.getAccount()).thenReturn(account);
        Mockito.when(account.getReader()).thenReturn(reader);
        Mockito.when(userDetails.getAccount().getReader().getId()).thenReturn(reader.getId());

        Mockito.when(bookService.findById(anyLong())).thenReturn(bookToTake);


        mockMvc.perform(post("/books/take?id=" + bookToTake.getId())
                        .with(user(userDetails))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("test reader returns the book")
    @Test
    public void returnABookTest() throws Exception {
        Book bookToReturn = Mockito.mock(Book.class);
        Account account = Mockito.mock(Account.class);
        Reader reader = Mockito.mock(Reader.class);
        ApplicationUserDetails userDetails = Mockito.mock(ApplicationUserDetails.class);

        Mockito.when(userDetails.getAccount()).thenReturn(account);
        Mockito.when(account.getReader()).thenReturn(reader);
        Mockito.when(bookService.findById(anyLong())).thenReturn(bookToReturn);

        mockMvc.perform(post("/books/return&bookId=" + bookToReturn.getId())
                        .with(user(userDetails))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andReturn();
    }
}