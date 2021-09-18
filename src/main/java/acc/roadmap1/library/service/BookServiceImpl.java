package acc.roadmap1.library.service;

import acc.roadmap1.library.repository.BookRepository;
import acc.roadmap1.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Book save(Book theBook) {
        return bookRepository.save(theBook);
    }

    @Override
    public void deleteById(long theId) {
        bookRepository.deleteById(theId);
    }
}
