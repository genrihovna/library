package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/api/books", produces = "application/json")
    public List<Book> getAllBooks() {
        return Arrays.asList(new Book(100, "title", "author", (short) 1990, null, false),
                new Book(101, "title1", "author", (short) 2000, null, false),
                new Book(102, "title2", "author", (short) 2010, null, false));
    }
}
