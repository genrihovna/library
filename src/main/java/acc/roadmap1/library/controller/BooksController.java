package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;

    @Autowired
    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/add")
    public String addABook(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "books/add";
    }

    @PostMapping("/add")
    public String saveABook(@ModelAttribute("book") Book book, Model model) {
        bookService.save(book);
        model.addAttribute("book", book);
        return "redirect:/librarian";
    }

    @GetMapping("/delete")
    public String deleteABook(@RequestParam("id") int id) {
        bookService.deleteById(id);
        return "redirect:/librarian";
    }

    @GetMapping("/update")
    public String updateABook(@RequestParam("id") int id, Model theModel) {
        Book book = bookService.findById(id);
        theModel.addAttribute("book", book);
        return "books/add";
    }
}