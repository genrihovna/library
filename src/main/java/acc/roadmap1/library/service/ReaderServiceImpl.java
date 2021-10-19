package acc.roadmap1.library.service;

import acc.roadmap1.library.model.Account;
import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.repository.AccountRepository;
import acc.roadmap1.library.repository.BookRepository;
import acc.roadmap1.library.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final AccountRepository accountRepository;
    private final BookRepository bookRepository;

    @Autowired
    public ReaderServiceImpl(ReaderRepository readerRepository,
                             AccountRepository accountRepository,
                             BookRepository bookRepository) {
        this.readerRepository = readerRepository;
        this.accountRepository = accountRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Reader> findAll() {
        return readerRepository.findAll();
    }

    @Override
    public Reader findByName(String username) {
        return accountRepository.findAccountByUsername(username)
                .map(Account::getReader)
                .orElseThrow(() -> new RuntimeException("Did not get reader - " + username));
    }

    @Override
    public void save(Reader reader) {
        readerRepository.save(reader);
    }

    @Override
    public Reader takeABook(ApplicationUserDetails userDetails, long bookId) {
        Reader currentReader = userDetails.getAccount().getReader();
        Set<Book> books = currentReader.getBooks();
        if (books == null)
            books = new HashSet<>();
        Book currentBook = bookRepository.findById(bookId).orElseThrow();
        books.add(currentBook);
        currentReader.setBooks(books);
        return readerRepository.save(currentReader);
    }

    @Override
    public void returnABook(ApplicationUserDetails userDetails, long bookId) {
        Reader currentReader = userDetails.getAccount().getReader();
        Set<Book> books = currentReader.getBooks();
        Book currentBook = bookRepository.findById(bookId).orElseThrow();
        books.remove(currentBook);
        currentReader.setBooks(books);
        readerRepository.save(currentReader);
    }
}
