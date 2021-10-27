package acc.roadmap1.library.service;

import acc.roadmap1.library.controller.dto.CreateBookItem;
import acc.roadmap1.library.model.*;
import acc.roadmap1.library.repository.BookRepository;
import acc.roadmap1.library.repository.ReaderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
class BookServiceTest {

    private final BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private ReaderRepository readerRepository;

    @Autowired
    BookServiceTest(BookService bookService) {
        this.bookService = bookService;
    }

    @DisplayName("to test bookService.findBooksWithStatusForCurrentUser() method, case CAN_RETURN")
    @Test
    public void calculateValidStatusForBookTest() {
        Account account = Mockito.mock(Account.class);
        Reader reader = Mockito.mock(Reader.class);
        ApplicationUserDetails userDetails = Mockito.mock(ApplicationUserDetails.class);

        Book bookToCheckStatus = new Book("author", 1991, "title");
        bookToCheckStatus.setReader(reader);
//        reader.handOver(bookToCheckStatus);

        Mockito.when(account.getReader()).thenReturn(reader);
        Mockito.when(bookRepository.findAll()).thenReturn(List.of(bookToCheckStatus));
        Mockito.when(userDetails.getAccount()).thenReturn(account);

        var result = bookService.findBooksWithStatusForCurrentUser(userDetails);
        Assertions.assertTrue(result.size() > 0);
        Assertions.assertEquals(BookStatus.CAN_RETURN, result.get(0).getStatus());
    }

    @DisplayName("to test bookService.handOverBook() method, case - reader takes a book")
    @Test
    public void handoverBookTest() {
        Account account = Mockito.mock(Account.class);
        Reader reader = Mockito.mock(Reader.class);
        Book bookToHandover = new Book("author", 1991, "title");
        ApplicationUserDetails userDetails = Mockito.mock(ApplicationUserDetails.class);

        Mockito.when(userDetails.getAccount()).thenReturn(account);
        Mockito.when(readerRepository.findById(anyLong())).thenReturn(Optional.of(reader));
        Mockito.when(account.getReader()).thenReturn(reader);
        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookToHandover));

        bookToHandover.setReader(reader);

        Mockito.when(bookRepository.save(any())).thenReturn(bookToHandover);

        var result = bookService.handOverBook(1, 1);

        Mockito.when(bookRepository.findAll()).thenReturn(List.of(result));

        result = bookService.findBooksWithStatusForCurrentUser(userDetails).get(0);

        Assertions.assertTrue(result.getReader().isPresent());
        Assertions.assertEquals(BookStatus.CAN_RETURN, result.getStatus());
    }

    @DisplayName("to test bookService.handOverBook() method, case - reader can't take a book")
    @Test()
    public void handoverBookTest2(){
        //create a reader a a book
        Account currentAccount = Mockito.mock(Account.class);
        Reader currentReader = Mockito.mock(Reader.class);
        Book bookForTest = new Book("author", 1991, "title");
        ApplicationUserDetails currentUserDetails = Mockito.mock(ApplicationUserDetails.class);

        //create some other reader
        Reader someOtherReader = Mockito.mock(Reader.class);
        Account someOtherAccount = Mockito.mock(Account.class);
        ApplicationUserDetails someOtherUserDetails = Mockito.mock(ApplicationUserDetails.class);

        //simulate readers behavior
        Mockito.when(currentAccount.getReader()).thenReturn(currentReader);
        Mockito.when(currentUserDetails.getAccount()).thenReturn(currentAccount);
        Mockito.when(someOtherAccount.getReader()).thenReturn(someOtherReader);
        Mockito.when(someOtherUserDetails.getAccount()).thenReturn(someOtherAccount);

        Mockito.when(readerRepository.findById(1L)).thenReturn(Optional.of(currentReader));
        Mockito.when(readerRepository.findById(2L)).thenReturn(Optional.of(someOtherReader));

        //simulate book's behavior
        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.of(bookForTest));
        Mockito.when(bookRepository.save(any())).thenReturn(bookForTest);

        //connect book and current reader
        bookForTest.setReader(currentReader);
        var result = bookService.handOverBook(1, 1L);

        //simulate books list behavior
        Mockito.when(bookRepository.findAll()).thenReturn(List.of(result));

        //actual testing
        result = bookService.findBooksWithStatusForCurrentUser(someOtherUserDetails).get(0);

        Assertions.assertTrue(result.getReader().isPresent());
        Assertions.assertEquals(BookStatus.ALREADY_TAKEN, result.getStatus());
    }

    @DisplayName("to test bookService.create() method")
    @Test
    public void librarianAddsNewBookTest(){
        Book newBook = new Book("new author", 2000, "new book");
        Mockito.when(bookService.create(new CreateBookItem())).thenReturn(newBook);

        Book justCreatedBook = bookService.create(new CreateBookItem());
        Assertions.assertEquals("new author", justCreatedBook.getAuthor());
        Assertions.assertEquals(2000, justCreatedBook.getPublished());
        Assertions.assertEquals("new book", justCreatedBook.getTitle());
    }

    @DisplayName("to test bookService.deleteById() method")
    @Test
    public void deleteSomeBookTest(){
        bookService.deleteById(0);
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(anyLong());
    }
}
