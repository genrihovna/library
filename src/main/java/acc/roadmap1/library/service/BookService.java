package acc.roadmap1.library.service;

import acc.roadmap1.library.model.Book;

import java.util.List;

public interface BookService {

    public List<Book> findAll();
    public Book findById(int booksId);
    public void save (Book theBook);
    public void deleteById(int booksId);
}
