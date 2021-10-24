package acc.roadmap1.library.service;

import acc.roadmap1.library.model.Account;
import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.repository.BookRepository;
import acc.roadmap1.library.repository.ReaderRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
public class ReaderServiceTest {

    private final ReaderService readerService;

    @MockBean
    private ReaderRepository readerRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    public ReaderServiceTest(ReaderService readerService) {
        this.readerService = readerService;
    }

    @DisplayName("to test readerService.returnABook() method and readerService.save() method")
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
        Mockito.when(bookRepository.findAll()).thenReturn(List.of(bookToReturn));

        readerService.returnABook(userDetails, bookToReturn.getId());
        Mockito.verify(readerService, Mockito.times(1)).returnABook(userDetails, anyLong());
        Mockito.verify(readerService, Mockito.times(1)).save(reader);
    }

    @DisplayName("to test readerService.takeABook() method")
    @Test
    public void readerTakesABookTest(){
        //create a reader and mock his behavior
        Account account = Mockito.mock(Account.class);
        Reader reader = Mockito.mock(Reader.class);
        ApplicationUserDetails userDetails = Mockito.mock(ApplicationUserDetails.class);
        Mockito.when(userDetails.getAccount()).thenReturn(account);
        Mockito.when(account.getReader()).thenReturn(reader);

        //create a book and mock book's behavior
        Book bookToReturn = Mockito.mock(Book.class);

        Mockito.when(readerRepository.findReaderByName(anyString())).thenReturn(Optional.of(reader));
        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookToReturn));

        readerService.takeABook(userDetails, bookToReturn.getId());

        Mockito.verify(readerRepository, Mockito.times(1)).save(any());
    }
}
