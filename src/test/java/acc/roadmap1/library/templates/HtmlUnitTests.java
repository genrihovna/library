package acc.roadmap1.library.templates;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.service.BookService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class HtmlUnitTests {

    @MockBean
    private BookService bookService;

    private WebClient webClient;

    private final MockMvc mockMvc;

    @Autowired
    public HtmlUnitTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc).build();
    }

    @DisplayName("main table layout")
    @Test
    public void checkValidTableMapping() throws IOException {
        HtmlPage createMsgFormPage = webClient.getPage("http://localhost:8080");
        HtmlTable listOfBooksTable = createMsgFormPage.getHtmlElementById("list-books");

        Assertions.assertEquals("List of Books", listOfBooksTable.getCaptionText());
        Assertions.assertEquals(1, listOfBooksTable.getRowCount());
    }

    @DisplayName("main table with content")
    @Test
    public void checkValidLoadBooksInTable() throws IOException {

        Mockito.when(bookService.findAll()).thenReturn(List.of(
                new Book("author", 1991, "title")
        ));

        HtmlPage createMsgFormPage = webClient.getPage("http://localhost:8080");
        HtmlTable listOfBooksTable = createMsgFormPage.getHtmlElementById("list-books");

        Assertions.assertEquals("List of Books", listOfBooksTable.getCaptionText());
        Assertions.assertEquals(1, listOfBooksTable.getRowCount());
        Assertions.assertEquals("title", listOfBooksTable.getRow(0).getCell(0).getTextContent());
        Assertions.assertEquals("author", listOfBooksTable.getRow(0).getCell(1).getTextContent());
    }

    @DisplayName("book directory")
    @Test
    public void testAddForm() throws Exception {
        HtmlPage bookPage = webClient.getPage("http://localhost:8080/books/add");
        //get page form with fields and submit button
        HtmlForm form = bookPage.getForms().get(0);
        HtmlHiddenInput id = form.getInputByName("id");
        HtmlTextInput title = form.getInputByName("title");
        HtmlTextInput author = form.getInputByName("author");
        HtmlTextInput published = form.getInputByName("published");
        HtmlButton button = form.getButtonByName("button");

        //check all fields and button
        Assertions.assertTrue(button.isDisplayed());
        Assertions.assertTrue(author.isOptional());
        Assertions.assertFalse(title.isRequired());
        Assertions.assertTrue(id.isValid()); //what does it do?
        Assertions.assertFalse(published.getPlaceholder().isEmpty());

        //fill in fields and click button
        title.type("");
        author.type("");
        published.type(0);
        HtmlPage librarianPage = button.click();

        //check button is working
        Assertions.assertTrue(librarianPage.isHtmlPage());
    }

    @DisplayName("register page")
    @Test
    public void testRegisterPageLayout() throws IOException {
        HtmlPage registrationPage = webClient.getPage("http://localhost:8080/register");
        HtmlHeading3 heading = registrationPage.getHtmlElementById("heading3");
        HtmlForm form = registrationPage.getFormByName("registrationForm");
        HtmlButton button = form.getButtonByName("submitButton");
        HtmlInput input = registrationPage.getElementByName("username");
        HtmlTextInput textInput = registrationPage.getHtmlElementById("name");

        Assertions.assertEquals("Register page", heading.getTextContent());
        Assertions.assertEquals("Put your username", input.getPlaceholder());
        Assertions.assertEquals("username", input.getId());
        Assertions.assertEquals("Put your name", textInput.getPlaceholder());
        Assertions.assertEquals("Library Login Page", registrationPage.getTitleText());
        Assertions.assertEquals("Register", button.getVisibleText());
    }

    @DisplayName("register form")
    @Test
    public void testRegisterForm() throws IOException {
        webClient.getOptions().setCssEnabled(false);
        HtmlPage registrationPage = webClient.getPage("http://localhost:8080/register");
        //fill in all fields, press button, get new page
        HtmlForm form = registrationPage.getFormByName("registrationForm");
        HtmlButton button = form.getButtonByName("submitButton");
        HtmlTextInput username = form.getInputByName("username");
        HtmlTextInput name = form.getInputByName("name");
        HtmlPasswordInput pwd = form.getInputByName("password");

        username.type("username");
        name.type("name");
        pwd.type("pwd");

        HtmlPage loginPage = button.click();

        Assertions.assertTrue(button.isDisplayed());
        Assertions.assertTrue(loginPage.isHtmlPage());
        Assertions.assertEquals("http://localhost:8080/login", loginPage.getBaseURL().toString());
    }

    @DisplayName("readers list")
    @Test
    public void tableHeadersTest() throws Exception {
        mockMvc.perform(get("/librarian/readers")).andDo(print())
                .andExpect(content().string(stringContainsInOrder("Name", "Account", "Books")));
    }

    @DisplayName("links of the header")
    @Test
    public void headerLinksTest() throws IOException {
        HtmlPage welcomePage = webClient.getPage("http://localhost:8080/register");
        HtmlAnchor signIn = welcomePage.getAnchorByHref("/login");
        HtmlAnchor register = welcomePage.getAnchorByHref("/register");

        Assertions.assertTrue(signIn.click().isHtmlPage());
        Assertions.assertEquals("http://localhost:8080/login", signIn.click().getUrl().toString());
        Assertions.assertTrue(register.click().isHtmlPage());
        Assertions.assertEquals("http://localhost:8080/register", register.click().getUrl().toString());
    }
}
