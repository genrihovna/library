package acc.roadmap1.library.service;

import acc.roadmap1.library.controller.dto.RegisterAccount;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.w3c.dom.html.HTMLDivElement;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityServiceTest {

    private final MockMvc mockMvc;

    private final WebClient webClient;

    @MockBean
    private SecurityService securityService;

    @Autowired
    public SecurityServiceTest(MockMvc mockMvc){
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc)
                .build();
    }

    @Test
    public void testRegisterReader() throws Exception {
        RegisterAccount testAccount = new RegisterAccount();
        securityService.createReaderAccount(
                testAccount.getUsername(), testAccount.getPassword(), testAccount.getName());


        mockMvc.perform(post("/register")
                        //.with(user("reader").roles(RoleNames.READER.name()))
                )
                //.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/login"))
                .andReturn();

    }

    @Test
    public void testRegisterAdmin(){

    }

}
