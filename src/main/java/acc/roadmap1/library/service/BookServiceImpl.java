package acc.roadmap1.library.service;

import acc.roadmap1.library.controller.dto.CreateBookItem;
import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.BookStatus;
import acc.roadmap1.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
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
        Book theBook = null;
        if (result.isPresent()) {
            theBook = result.get();
        } else {
            throw new RuntimeException("Did not get book - " + theId);
        }
        return theBook;
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
}
