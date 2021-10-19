package acc.roadmap1.library.templates;

import acc.roadmap1.library.model.*;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class WelcomePageLayout {

    private WebClient webClient;
    private final MockMvc mockMvc;
    private HtmlPage welcomePage;

    @Autowired
    public WelcomePageLayout(MockMvc mockMvc) throws IOException {
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc).build();
        this.welcomePage = webClient.getPage("http://localhost:8080/");
    }

    @Disabled
    @Test
    @WithMockUser(username = "test", password = "123456", authorities = {"MANAGE_BOOKS"})
    public void testMainLinks() throws IOException {
//        Account testAcc = new Account("test", "123456",
//                Collections.singleton(new Role(RoleNames.READER.name(),
//                        Set.of(new Privilege(Privileges.MANAGE_BOOKS.name())))));
//        Reader testReader = new Reader("test", testAcc);
       // Mockito.when(welcomePage.getAnchorByHref("/user")).thenReturn("");
        //HtmlAnchor readersLink = welcomePage.getAnchorByHref("/user");
        //HtmlAnchor librarianLink = welcomePage.getAnchorByHref("/librarian");
        //HtmlLink readersLink = welcomePage.getFirstByXPath("<a href=\"/users\">your Profile page</a>");
        //Mockito.when(welcomePage.isDisplayed()).thenReturn();

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

        HtmlLink readersLink = welcomePage.getHtmlElementById("profile");
        //HtmlLink librarianLink = welcomePage.getFirstByXPath("/html/body/div[3]/div/h2[2]/div/a");
        HtmlPage profilePage = webClient.getPage("http://localhost:8080/users");
        //HtmlPage librarianPage = webClient.getPage("http://localhost:8080/librarian");

        Mockito.when(readersLink.click()).thenReturn(profilePage);
//        Mockito.when(librarianLink.click()).thenReturn(librarianPage);
//
        Assertions.assertTrue(profilePage.isHtmlPage());
        Assertions.assertTrue(profilePage.getTitleText().contains("profile"));
        Assertions.assertTrue(profilePage.isHtmlPage());
        Assertions.assertTrue(profilePage.getTitleText().contains("profile"));
    }

    @Test
    public void testHeaderLinks() throws IOException {
        HtmlAnchor signIn = welcomePage.getAnchorByHref("/login");
        HtmlAnchor register = welcomePage.getAnchorByHref("/register");

        Assertions.assertTrue(signIn.click().isHtmlPage());
        Assertions.assertEquals(signIn.click().getUrl().toString(), "http://localhost:8080/login");
        Assertions.assertTrue(register.click().isHtmlPage());
        Assertions.assertEquals(register.click().getUrl().toString(), "http://localhost:8080/register");
    }
}
