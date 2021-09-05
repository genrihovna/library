package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CommonController {

    private BookService bookService;
    private ReaderService readerService;

    @Autowired
    public CommonController(BookService bookService, ReaderService readerService) {
        this.bookService = bookService;
        this.readerService = readerService;
    }

    @GetMapping
    public String welcomePage(Model model){
        List<Book> theBooks = bookService.findAll();
        model.addAttribute("books", theBooks);
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/403")
    public String error403(){
        return "/error/403";
    }

}
