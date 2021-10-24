package acc.roadmap1.library.templates;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LibrarianPageLayout {

    private MockMvc mockMvc;
    private WebClient webClient;
    private HtmlPage librarianPage;
    private final String baseUrl = "http://localhost:8080";

    @Autowired
    public LibrarianPageLayout(MockMvc mockMvc) throws IOException {
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc).build();
        webClient.getOptions().setCssEnabled(false);
        this.librarianPage = webClient.getPage(baseUrl+"/librarian");
    }

    @DisplayName("test list of books on librarian page")
    @Test
    public void booksTableTest(){
        HtmlTable table = librarianPage.getHtmlElementById("table");
        assertTrue(table.getRowCount()==1);
    }

    @DisplayName("test how link to the readers list is working")
    @Test
    public void readerListLinkTest() throws Exception {
        //link as texts, link functionality
        mockMvc.perform(get("/librarian/readers")).andExpect(
                        status().isOk())
                .andExpect(content().string(containsString("Readers List")));

        HtmlAnchor readersLink = librarianPage.getFirstByXPath("/html/body/div[3]/div/a");
                //.getAnchorByHref("/librarian/readers");
        HtmlPage readersPage = readersLink.click();
        Assertions.assertTrue(readersPage.isHtmlPage());
        Assertions.assertEquals(readersPage.getBaseURL().toString(), baseUrl+"/register");
    }

    @DisplayName("test how 'add new book' link is working")
    @Test
    public void librarianLinkTest() throws Exception {
        //link as texts, link functionality
        mockMvc.perform(get("/books/add")).andExpect(
                        status().isOk())
                .andExpect(content().string(containsString("Book Directory")));

        HtmlAnchor addBookLink = (HtmlAnchor) librarianPage.getByXPath(
                "/html/body/div[3]/a").get(0);
        HtmlPage librarianPage = addBookLink.click();
        Assertions.assertTrue(librarianPage.isHtmlPage());
    }

    @DisplayName("test column headers in the table")
    @Test
    public void tableHeadersTest() throws Exception {
        mockMvc.perform(get("/librarian")).andDo(print())
                .andExpect(content().string(stringContainsInOrder("Id", "Title", "Author", "Published", "Status", "Action", "Reader name")));
    }

    @DisplayName("test heading")
    @Test
    public void headingTest() {
        //heading text, number of headings
        List<HtmlHeading1> h1 = librarianPage.getByXPath("/html/body/div[3]/h1");
        Assertions.assertEquals(1, h1.size());
        Assertions.assertEquals("Book Management", h1.get(0).getTextContent());
    }
}
