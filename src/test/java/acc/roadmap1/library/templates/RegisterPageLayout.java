package acc.roadmap1.library.templates;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.w3c.dom.html.HTMLDivElement;

import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterPageLayout {

    private MockMvc mockMvc;
    private WebClient webClient;
    private HtmlPage registrationPage = null;

    @Autowired
    public RegisterPageLayout(MockMvc mockMvc) throws IOException {
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc).build();
        webClient.getOptions().setCssEnabled(false);
        this.registrationPage = webClient.getPage("http://localhost:8080/register");
    }

    @Test
    public void testRegisterPageLayout(){
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

    @Test
    public void testRegisterForm() throws IOException {
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
        Assertions.assertEquals(loginPage.getBaseURL().toString(), "http://localhost:8080/login");
    }
}
