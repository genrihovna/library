package acc.roadmap1.library.service;

import acc.roadmap1.library.controller.dto.CreateBookItem;
import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.BookStatus;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.repository.BookRepository;
import acc.roadmap1.library.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final ReaderRepository readerRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book create(CreateBookItem book) {
        return bookRepository.save(new Book(book.getAuthor(), book.getPublished(), book.getTitle()));
    }

    @Override
    public Book findById(long theId) {
        Optional<Book> result = bookRepository.findById(theId);
        Book book;
        if (result.isPresent()) {
            book = result.get();
        } else {
            throw new RuntimeException("Did not get book - " + theId);
        }
        return book;
    }

    @Override
    public void deleteById(long theId) {
        bookRepository.deleteById(theId);
    }

    @Override
    public List<Book> findBooksWithStatusForCurrentUser(ApplicationUserDetails userDetails) {
        return bookRepository.findAll().stream().peek(book -> {
            if (book.getReader().equals(userDetails.getAccount().getReader())) {
                book.setStatus(BookStatus.CAN_RETURN);
            } else if (!book.getReader().equals(userDetails.getAccount().getReader())) {
                book.setStatus(BookStatus.ALREADY_TAKE);
            } else if (book.getReader() == null) {
                book.setStatus(BookStatus.CAN_TAKE);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public Book handOverBook(long bookId, long readerId) {

        Reader reader = readerRepository.findById(readerId).orElseThrow();

        return bookRepository.findById(bookId).map(book -> {
            book.setReader(reader);

            return bookRepository.save(book);
        }).orElseThrow();
    }
}
