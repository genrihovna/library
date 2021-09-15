package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller("/")
public class RootController {

    private final BookService bookService;

    @Autowired
    public RootController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String getMainPage(Model model) {
        List<Book> theBooks = bookService.findAll();
        model.addAttribute("books", theBooks);
        return "index";
    }
}
