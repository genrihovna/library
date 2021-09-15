package acc.roadmap1.library.service;

import acc.roadmap1.library.dao.BookRepo;
import acc.roadmap1.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{
    private BookRepo bookRepo;

    @Autowired
    public BookServiceImpl(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    @Override
    public Book findById(int theId) {
        Optional<Book> result = bookRepo.findById(theId);
        Book theBook = null;
        if(result.isPresent()){
            theBook= result.get();
        }else{
            throw new RuntimeException("Did not get book - " + theId);
        }
        return theBook;
    }

    @Override
    public Book save(Book theBook) {
        return bookRepo.save(theBook);
    }

    @Override
    public void deleteById(int theId) {
        bookRepo.deleteById(theId);
    }
}
