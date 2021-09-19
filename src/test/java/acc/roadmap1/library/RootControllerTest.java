package acc.roadmap1.library;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RootControllerTest extends BaseMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void welcomePageTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.APPLICATION_JSON)
                ;
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//div[contains(@class, 'is reader')]").exists())
                .andExpect(xpath("//div[3]/h1").string("Welcome to Library Page"))
                .andReturn();
    }

    @Test
    void getLoginPageTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/login")
                .accept(MediaType.APPLICATION_JSON)
                ;
        mockMvc.perform(request)
                .andExpect(xpath("//div[3]/h3").string("Library Login Page"));
    }

    @Test
    void getRegisterPageTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/register")
                .accept(MediaType.APPLICATION_JSON)
                ;
        mockMvc.perform(request)
                .andExpect(xpath("//div[2]/h3").string("Library Login Page"));
    }

    @Test
    void getAdminRegisterPageTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/register/admin")
                .accept(MediaType.APPLICATION_JSON)
                ;
        mockMvc.perform(request)
                .andExpect(xpath("//div[2]/h3").string("Library Login Page"));
    }

    @Test
    void registerNewUserTest() throws Exception {
        RequestBuilder registerUser = MockMvcRequestBuilders
                .post("/register")
//                .param(username, "Username")
//                .param(name, "Name")
//                .param(password, "Password")
                ;
        mockMvc.perform(registerUser)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void registerAdminTest() {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/register/admin")
                .accept(MediaType.APPLICATION_JSON)
                ;
    }
}
