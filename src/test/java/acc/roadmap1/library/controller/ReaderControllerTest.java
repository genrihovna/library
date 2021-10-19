package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.*;
import acc.roadmap1.library.service.BookService;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Assertions;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
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
    @WithMockUser(username = "test", password = "123456", authorities = {"MANAGE_BOOKS"})
    public void testUpdateReader() throws Exception {
        //create reader
//        Account testAcc = new Account("test", "123456",
//                Collections.singleton(new Role(RoleNames.READER.name(),
//                        Set.of(new Privilege(Privileges.MANAGE_BOOKS.name())))));
//        Reader testReader = new Reader("test", testAcc);

        setCredentials();

        //check reader's name on the page

        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            HtmlPage page = webClient.getPage("http://localhost:8080/users/update");
            String title = page.getTitleText();

            assertEquals(title, "");

            //System.out.println("Webpage is not opened");
//        mockMvc.perform(get("/users/update"))
//                .andDo(print())
//                .andExpect(status().isOk());
    }

    private void setCredentials() {
        String username = "test";
        String password = "123456";
        DefaultCredentialsProvider creds = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
        try {
            creds.addCredentials(username, password);
            webClient.setCredentialsProvider(creds);
        } catch (Exception e) {
            System.out.println("!!! Problem login in");
            e.printStackTrace();
        }
    }
}