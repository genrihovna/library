package acc.roadmap1.library.templates;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReadersListLayout {

    private final MockMvc mockMvc;
    private final WebClient webClient;
    private HtmlPage readersListPage;
    private final String urlTemplate = "/librarian/readers";
    private final String urlBase = "http://localhost:8080";

    @Autowired
    public ReadersListLayout(MockMvc mockMvc) throws IOException {
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc).build();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        this.readersListPage = webClient.getPage(urlBase + urlTemplate);
    }

    @Test
    public void testRegistrationLinks() throws Exception {
        //link as texts, link functionality
        mockMvc.perform(get(urlTemplate)).andDo(print()).andExpect(
                        status().isOk())
                .andExpect(content().string(containsString("Register new reader")));

        HtmlAnchor registrationLink = (HtmlAnchor) readersListPage.getByXPath(
                "//*[@id=\"register-link\"]").get(0);
        HtmlPage loginPage = registrationLink.click();
        Assertions.assertTrue(loginPage.isHtmlPage());
        Assertions.assertEquals(loginPage.getBaseURL().toString(), urlBase + "/register");
    }

    @Test
    public void testHomeLink() throws Exception {
        //link as texts, link functionality
        mockMvc.perform(get(urlTemplate)).andDo(print()).andExpect(
                        status().isOk())
                .andExpect(content().string(containsString("Return to Welcome page")));

        HtmlAnchor homeLink = (HtmlAnchor) readersListPage.getByXPath(
                "//*[@id=\"welcome-page-link\"]").get(0);
        HtmlPage homePage = homeLink.click();
        Assertions.assertTrue(homePage.isHtmlPage());
        Assertions.assertEquals(homePage.getBaseURL().toString(), urlBase + "/");
    }

    @Test
    public void testLibrarianLink() throws Exception {
        //link as texts, link functionality
        mockMvc.perform(get(urlTemplate)).andDo(print()).andExpect(
                        status().isOk())
                .andExpect(content().string(containsString("Return to librarian page")));

        HtmlAnchor librarianLink = (HtmlAnchor) readersListPage.getByXPath(
                "//*[@id=\"librarian-page-link\"]").get(0);
        HtmlPage librarianPage = librarianLink.click();
        Assertions.assertTrue(librarianPage.isHtmlPage());
    }

    @Test
    public void testTableHeads() throws Exception {
        mockMvc.perform(get(urlTemplate)).andDo(print())
                .andExpect(content().string(stringContainsInOrder("Name", "Account", "Books")));
    }

    @Test
    public void testHeading() {
        //heading text, number of headings
        List<HtmlHeading1> h1 = readersListPage.getByXPath("/html/body/div[3]/h1");
        Assertions.assertEquals(h1.size(), 1);
        Assertions.assertEquals("Readers List", h1.get(0).getTextContent());
    }
}
