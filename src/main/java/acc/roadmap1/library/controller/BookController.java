package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.service.BookService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/api/books", produces = "application/json")
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping(value = "/api/book/{id}", produces = "application/json")
    public Book getBookById(@PathVariable int id){
        return bookService.findById(id);
    }

    @PostMapping(value="/api/books",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public  Book addBook(@RequestBody Book book){
        Book newBook = bookService.save(book);
        return newBook;
    }

    @PutMapping(value = "/api/books/{id}")
    public Book updateBook(Book book){
        return null;
    }

    @DeleteMapping(value = "api/books/{id}")
    public void deleteBook(int bookId){
        bookService.deleteById(bookId);
    }
}
