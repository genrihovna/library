package acc.roadmap1.library.service;

import acc.roadmap1.library.controller.dto.CreateBookItem;
import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book create(CreateBookItem book);

    Book findById(long booksId);

    void deleteById(long booksId);

    List<Book> findBooksWithStatusForCurrentUser(ApplicationUserDetails userDetails);

    Book handOverBook(long bookId, long readerId);
}
