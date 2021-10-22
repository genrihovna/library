package acc.roadmap1.library.service;

import acc.roadmap1.library.controller.dto.CreateBookItem;
import acc.roadmap1.library.model.*;
import acc.roadmap1.library.repository.BookRepository;
import acc.roadmap1.library.repository.ReaderRepository;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
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

    @Test
    public void checkValidCalculateStatusForBook() {

        Account account = Mockito.mock(Account.class);

        Reader reader = Mockito.mock(Reader.class);

        Mockito.when(account.getReader()).thenReturn(reader);

        Book book = new Book("author", 1991, "title");
        book.setReader(reader);

        reader.handOver(book);

        Mockito.when(bookRepository.findAll()).thenReturn(List.of(book));

        ApplicationUserDetails userDetails = Mockito.mock(ApplicationUserDetails.class);

        Mockito.when(userDetails.getAccount()).thenReturn(account);

        var result1 = bookService.findBooksWithStatusForCurrentUser(userDetails);
        Assertions.assertTrue(result1.size() > 0);
        Assertions.assertEquals(BookStatus.CAN_RETURN, result1.get(0).getStatus());
    }

    @Test
    public void testHandoverBook() {

        Account account = Mockito.mock(Account.class);
        Reader reader = Mockito.mock(Reader.class);
        Book book = new Book("author", 1991, "title");
        ApplicationUserDetails userDetails = Mockito.mock(ApplicationUserDetails.class);

        Mockito.when(userDetails.getAccount()).thenReturn(account);
        Mockito.when(readerRepository.findById(anyLong())).thenReturn(Optional.of(reader));
        Mockito.when(account.getReader()).thenReturn(reader);

        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        book.setReader(reader);

        Mockito.when(bookRepository.save(any())).thenReturn(book);

        var result = bookService.handOverBook(1, 1);

        Mockito.when(bookRepository.findAll()).thenReturn(List.of(result));

        result = bookService.findBooksWithStatusForCurrentUser(userDetails).get(0);

        Assertions.assertTrue(result.getReader().isPresent());
        Assertions.assertEquals(BookStatus.CAN_RETURN, result.getStatus());
    }

    @Test
    public void librarianAddsNewBookTest(){
        Book newBook = new Book("new author", 2000, "new book");
        Mockito.when(bookService.create(new CreateBookItem())).thenReturn(newBook);

        Book justCreatedBook = bookService.create(new CreateBookItem());
        Assertions.assertEquals("new author", justCreatedBook.getAuthor());
        Assertions.assertEquals(2000, justCreatedBook.getPublished());
        Assertions.assertEquals("new book", justCreatedBook.getTitle());
    }

    @Test
    public void deleteSomeBook(){
        Book newBook = new Book("new author", 2000, "new book");
        Mockito.when(bookRepository.findById(anyLong())).thenReturn(Optional.of(newBook));

        Book justFoundBook = bookService.findById(0);

        bookService.deleteById(justFoundBook.getId());
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(anyLong());
    }
}
