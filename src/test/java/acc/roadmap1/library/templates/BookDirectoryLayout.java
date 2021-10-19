package acc.roadmap1.library.templates;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class BookDirectoryLayout {

    private MockMvc mockMvc;
    private WebClient webClient;
    private HtmlPage bookPage = null;

    @Autowired
    public BookDirectoryLayout(MockMvc mockMvc) throws IOException {
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc).build();
        this.bookPage = webClient.getPage("http://localhost:8080/books/add");
    }

    @Test
    public void testHeadings(){
        List<HtmlHeading3> h3 = bookPage.getByXPath("/html/body/div/h3");
        Assertions.assertEquals("Book Directory", h3.get(0).getTextContent());
        HtmlElement p = bookPage.getDocumentElement().getFirstByXPath("/html/body/div/p");
        Assertions.assertEquals("Save Book", p.getTextContent());
    }

    @Disabled
    @Test
    public void testAddForm() throws IOException {
        //failed -> librarian page redirect to login??
        HtmlForm form = bookPage.getForms().get(0);
        HtmlHiddenInput id = form.getInputByName("id");
        HtmlTextInput title = form.getInputByName("title");
        HtmlTextInput author = form.getInputByName("author");
        HtmlTextInput published = form.getInputByName("published");
        HtmlButton button = form.getButtonByName("button");

        Assertions.assertTrue(button.isDisplayed());
        Assertions.assertTrue(author.isOptional());
        Assertions.assertFalse(title.isRequired());
        Assertions.assertTrue(id.isValid()); //what does it do?
        Assertions.assertFalse(published.getPlaceholder().isEmpty());

        title.type("");
        author.type("");
        published.type(0);
        HtmlPage librarianPage = button.click();
        Assertions.assertTrue(librarianPage.isHtmlPage());
        Assertions.assertEquals("http://localhost:8080/librarian", librarianPage.getBaseURL());
    }
}
