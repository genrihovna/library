package acc.roadmap1.library.service;

import acc.roadmap1.library.repository.AccountRepository;
import acc.roadmap1.library.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SecurityServiceTest {
    private static final Faker FAKER = new Faker();

    private final MockMvc mockMvc;

    private final WebClient webClient;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    public SecurityServiceTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc)
                .build();
    }

    @DisplayName("test registers the user and redirects him to the login page")
    @Test
    public void registerUserTest() throws Exception {
        MockHttpServletRequestBuilder builder = getMockHttpServletRequestBuilder();
        mockMvc.perform(builder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andReturn();
    }

    @DisplayName("test try to register the user with already existed credentials")
    @Test
    public void alreadyRegisteredUserTest() throws Exception {
        Mockito.when(accountRepository.save(any())).thenThrow(RuntimeException.class);
        MockHttpServletRequestBuilder builder = getMockHttpServletRequestBuilder();
        mockMvc.perform(builder)
                .andExpect(status().is5xxServerError())
                .andReturn();
    }

    @DisplayName("test tries to register user with wrong role")
    @Test
    public void breakRoleTest() throws Exception {
        Mockito.when(roleRepository.findRoleByName(anyString())).thenThrow(RuntimeException.class);
        MockHttpServletRequestBuilder builder = getMockHttpServletRequestBuilder();
        mockMvc.perform(builder)
                .andExpect(status().is5xxServerError())
                .andReturn();
    }

    private MockHttpServletRequestBuilder getMockHttpServletRequestBuilder() {
        return post("/register")
                .param("name", FAKER.name().name())
                .param("username", FAKER.zelda().character())
                .param("password", FAKER.yoda().quote())
                .with(csrf());
    }

}
