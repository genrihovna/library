package acc.roadmap1.library.service;

import acc.roadmap1.library.controller.dto.RegisterAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

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

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    public SecurityServiceTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc)
                .build();
    }

    @Test
    public void testRegisterReader() throws Exception {
        mockMvc.perform(post("/register")
                        .param("name", FAKER.name().name())
                        .param("username", FAKER.zelda().character())
                        .param("password", FAKER.yoda().quote())
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andReturn();
    }

}
