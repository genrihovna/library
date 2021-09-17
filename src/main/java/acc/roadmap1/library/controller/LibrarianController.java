package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    private final BookService bookService;

    @Autowired
    public LibrarianController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String librarianPage(Model theModel) {
        List<Book> bookList = bookService.findAll();
        theModel.addAttribute("books", bookList);
        return "librarian/index";
    }
}
