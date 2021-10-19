package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Account;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class LibrarianControllerTest {

    private final MockMvc mockMvc;

    private final WebClient webClient;

    @MockBean
    private BookService bookService;

    @MockBean
    private ReaderService readerService;

    @Autowired
    public LibrarianControllerTest(MockMvc mockMvc){
        this.mockMvc = mockMvc;
        this.webClient = MockMvcWebClientBuilder
                .mockMvcSetup(mockMvc)
                .alwaysUseMockMvc().build();
    }

    @Test
    public void checkListOfReaders() throws IOException {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        //create the page
        HtmlPage readersListPage = webClient.getPage("http://localhost:8080/librarian/readers");
        //initialize the table
        HtmlTable readersList = readersListPage.getHtmlElementById("readers-list");

        assertEquals("Readers", readersList.getCaptionText());
        assertEquals(1, readersList.getRowCount());

        Account testAccount = new Account();
        Reader testReader = new Reader("name", testAccount);
        when(readerService.findAll()).thenReturn(List.of(testReader));
        readerService.findAll();
    }

}