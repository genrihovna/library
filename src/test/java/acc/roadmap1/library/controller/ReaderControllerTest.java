package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.*;
import acc.roadmap1.library.service.BookService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReaderControllerTest {

    private final MockMvc mockMvc;
    private final WebClient webClient;

    @MockBean
    private BookService bookService;

    @Autowired
    public ReaderControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc).alwaysUseMockMvc().build();
    }

    @Test
    @WithMockUser(username = "test", password = "123456", authorities = {"MANAGE_BOOKS"})
    public void testReaderHasBooks(){

        //create reader
        Account testAcc = new Account("test", "123456",
                Collections.singleton(new Role(RoleNames.READER.name(),
                        Set.of(new Privilege(Privileges.MANAGE_BOOKS.name())))));
        Reader testReader = new Reader("test", testAcc);
        GrantedAuthority grantedAuthority = Mockito.mock(GrantedAuthority.class);
        ApplicationUserDetails userDetails = new ApplicationUserDetails(
                "test", "123456", List.of(grantedAuthority), testAcc
        );

        //create book and set to the reader
        Book testBook = Mockito.mock(Book.class);
        testReader.setBooks(Set.of(testBook));
        when(testBook.getTitle()).thenReturn("test");
        when(testBook.getAuthor()).thenReturn("test");
        when(testBook.getPublished()).thenReturn(1500);
        when(testBook.getReader()).thenReturn(Optional.of(testReader));
        when(bookService.findBooksWithStatusForCurrentUser(userDetails)).thenReturn(List.of(testBook));

        //check reader's books
        assertEquals(testBook.getReader(), Optional.of(testReader));
        assertEquals(testReader.getBooks(), Set.of(testBook));
    }

    @Test
    @WithMockUser(username = "uname", password = "123456", authorities = {"MANAGE_BOOKS"})
    public void testUpdateReader() throws Exception {
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);

        //create reader
        Account testAcc = new Account("uname", "123456",
                Collections.singleton(new Role(RoleNames.READER.name(),
                        Set.of(new Privilege(Privileges.MANAGE_BOOKS.name())))));
        Reader testReader = new Reader("test", testAcc);

        //sign in
        mockMvc.perform(post("/register")
                .param("name", "test")
                .param("username", "uname")
                .param("password", "123456")
                .with(csrf()))
                        .andExpect(status().is3xxRedirection())
                                .andReturn();

        //check reader's name on the page
        HtmlPage page = webClient.getPage("http://localhost:8080/users");
        HtmlHeading1 h1 = page.getHtmlElementById("h1");
        String welcome=h1.getTextContent();
        mockMvc.perform(get("/users/update"))
                .andDo(print())
                .andExpect(status().isOk());

        assertTrue(h1.hasChildNodes());
        assertEquals("Welcome, uname", welcome);
    }
}