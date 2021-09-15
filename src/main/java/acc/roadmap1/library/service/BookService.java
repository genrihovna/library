package acc.roadmap1.library.service;

import acc.roadmap1.library.model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book findById(int booksId);

    Book save(Book theBook);

    void deleteById(int booksId);
}
