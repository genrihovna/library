package acc.roadmap1.library.templates;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlFooter;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.contains;

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
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
    }

    @DisplayName("test links on Welcome page for registered reader")
    @Test
    @WithMockUser(username = "reader", password = "123456", authorities = {"MANAGE_BOOKS"})
    public void mainPageLinksForReader() throws Exception {

        HtmlHeading2 h2 = (HtmlHeading2) welcomePage.getByXPath("/html/body/div[3]/div/h2[2]").get(0);
        HtmlHeading2 h2l = (HtmlHeading2) welcomePage.getByXPath("/html/body/div[3]/div/h2[1]").get(0);
        HtmlPage readersPage = h2.click();
        HtmlPage lPage = h2l.click();

        Assertions.assertTrue(h2.isDisplayed());
        Assertions.assertFalse(h2l.isDisplayed()); //doesnt work!!
        Assertions.assertTrue(readersPage.isHtmlPage());
        Assertions.assertFalse(lPage.isHtmlPage()); //doesnt work!!
}

    @DisplayName("test how Welcome page looks for librarian")
    @Test
    @WithMockUser(username = "librarian", password = "123456", authorities = {"MANAGE_ACCOUNTS"})
    public void mainPageForLibrarian() throws IOException {
        HtmlHeading2 readerLink = (HtmlHeading2) welcomePage.getByXPath("/html/body/div[3]/div/h2[1]").get(0);
        HtmlPage readersPage = readerLink.click();
        HtmlHeading2 librarianLink = (HtmlHeading2) welcomePage.getByXPath("/html/body/div[3]/div/h2[2]").get(0);
        HtmlPage librarianPage = librarianLink.click();

        HtmlFooter footer = welcomePage.getElementByName("footer");

        Assertions.assertTrue(readerLink.isDisplayed());
        Assertions.assertTrue(librarianLink.isDisplayed());
        Assertions.assertTrue(footer.isDisplayed());
        Assertions.assertTrue(readersPage.isHtmlPage());
        Assertions.assertTrue(librarianPage.isHtmlPage());
        Assertions.assertEquals("http://localhost:8080/users", readersPage.getUrl().toString()); //doesnt work!!
        Assertions.assertEquals("http://localhost:8080/librarian", librarianPage.getUrl().toString()); //doesnt work!!
    }

    @DisplayName("test how Welcome page looks for anonymous user")
    @Test
    public void mainPageForAnonymousUserTest(){
        HtmlFooter footer = welcomePage.getElementByName("footer");
        HtmlHeading2 readerLink = (HtmlHeading2) welcomePage.getByXPath("/html/body/div[3]/div/h2[1]").get(0);
        HtmlHeading2 librarianLink = (HtmlHeading2) welcomePage.getByXPath("/html/body/div[3]/div/h2[2]").get(0);
        Assertions.assertEquals(readerLink.getTextContent(), contains("Visit")); //doesnt work!!
        Assertions.assertTrue(footer.isDisplayed());
        Assertions.assertTrue(readerLink.isHidden()); //doesnt work!!
        Assertions.assertFalse(librarianLink.isDisplayed()); //doesnt work!!
    }

    @DisplayName("test links of the header")
    @Test
    public void headerLinksTest() throws IOException {
        HtmlAnchor signIn = welcomePage.getAnchorByHref("/login");
        HtmlAnchor register = welcomePage.getAnchorByHref("/register");

        Assertions.assertTrue(signIn.click().isHtmlPage());
        Assertions.assertEquals("http://localhost:8080/login", signIn.click().getUrl().toString());
        Assertions.assertTrue(register.click().isHtmlPage());
        Assertions.assertEquals("http://localhost:8080/register", register.click().getUrl().toString());
    }
}
