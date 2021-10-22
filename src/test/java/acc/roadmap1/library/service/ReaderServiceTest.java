package acc.roadmap1.library.service;

import acc.roadmap1.library.model.Account;
import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.repository.AccountRepository;
import acc.roadmap1.library.repository.BookRepository;
import acc.roadmap1.library.repository.ReaderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
public class ReaderServiceTest {

    private final ReaderService readerService;

    @MockBean
    private ReaderRepository readerRepository;

    @MockBean
    private BookRepository bookRepository;

//    @MockBean
//    private AccountRepository accountRepository;

    @Autowired
    public ReaderServiceTest(ReaderService readerService) {
        this.readerService = readerService;
    }

    @DisplayName("to test readerService.returnABook() method and see that service can process this")
    @Test
    public void readerReturnsTheBookAndServiceIsAbleToFindThisBookTest(){
        //create a reader and mock his behavior
        Account account = Mockito.mock(Account.class);
        Reader reader = Mockito.mock(Reader.class);
        ApplicationUserDetails userDetails = Mockito.mock(ApplicationUserDetails.class);
        Mockito.when(userDetails.getAccount()).thenReturn(account);
        Mockito.when(account.getReader()).thenReturn(reader);

        //create a book and mock book's behavior
        Book bookToReturn = new Book("", 100, "");
        //Mockito.mock(Book.class);
        Mockito.when((bookRepository.findById(anyLong()))).thenReturn(Optional.of(bookToReturn));

        readerService.returnABook(userDetails, bookToReturn.getId());
        Mockito.verify(readerService, Mockito.times(1)).returnABook(userDetails, anyLong());
    }

    @DisplayName("test how reader returns a book and book disappears from list")
    @Test
    public void ReaderReturnsABookAndBookDisappearsFromListTest(){
        // Есть читатель с книгой, и он ее возвращает. Проверяем был ли поиск
        // книги по айди, и был ли вызван метод save() то есть у читателя книги
        // больше нет или ее не удалось сдать
    }



}
