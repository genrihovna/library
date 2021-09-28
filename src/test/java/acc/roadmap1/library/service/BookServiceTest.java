package acc.roadmap1.library.service;

import acc.roadmap1.library.model.Account;
import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.BookStatus;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class BookServiceTest {

    private final BookService bookService;
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    BookServiceTest(BookService bookService) {
        this.bookService = bookService;
    }

    @Test
    public void checkValidCalculateStatusForBook() {

        Account account = Mockito.mock(Account.class);

        Reader reader = Mockito.mock(Reader.class);

        Mockito.when(account.getReader()).thenReturn(reader);

        Book book = new Book("author", 1991, "title");
        book.setReader(reader);

        reader.handOver(book);

        Mockito.when(bookRepository.findAll()).thenReturn(List.of(
                book
        ));

        ApplicationUserDetails userDetails = Mockito.mock(ApplicationUserDetails.class);

        Mockito.when(userDetails.getAccount()).thenReturn(account);

//        var result = bookService.findBooksWithStatusForCurrentUser(userDetails);
//
//        Assertions.assertTrue(result.size() > 0);
//        Assertions.assertEquals(BookStatus.CAN_RETURN, result.get(0).getStatus());

//        book.setReader(null);
        // Изменили book - у book есть reader
//        reader.handOver(book);
//
//        Mockito.when(bookRepository.findAll()).thenReturn(List.of(
//                book
//        ));

        var result1 = bookService.findBooksWithStatusForCurrentUser(userDetails);
        Assertions.assertTrue(result1.size() > 0);
        Assertions.assertEquals(BookStatus.CAN_TAKE, result1.get(0).getStatus());
    }
}