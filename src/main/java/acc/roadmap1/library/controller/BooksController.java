package acc.roadmap1.library.controller;

import acc.roadmap1.library.controller.dto.CreateBookItem;
import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;

    private final ReaderService readerService;

    @Autowired
    public BooksController(BookService bookService, ReaderService readerService) {
        this.bookService = bookService;
        this.readerService = readerService;
    }

    @GetMapping("/add")
    public String addABook(Model model) {
        CreateBookItem book = new CreateBookItem();
        model.addAttribute("book", book);
        return "books/add";
    }

    @PostMapping("/add")
    public String saveABook(@ModelAttribute("book") CreateBookItem book, Model model) {
        bookService.create(book);
        model.addAttribute("book", book);
        return "redirect:/librarian";
    }

    @GetMapping("/delete")
    public String deleteABook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/librarian";
    }

    @GetMapping("/update")
    public String updateABook(@RequestParam("id") long id, Model theModel) {
        Book book = bookService.findById(id);
        return "books/add";
    }

    @PostMapping("/take")
    public String takeABook(@RequestParam(name = "id") long id,
                            Principal principal,
                            Model attribute) {
        Book book = bookService.findById(id);
        String readerName = principal.getName();
        Reader reader = readerService.findByName(readerName);
        if (reader.getBooks().contains(book)) {
            throw new RuntimeException("This book is already in your list.");
        } else {
            bookService.handOverBook(id, reader.getId());
            //reader = readerService.takeABook(userDetails, bookId);
            attribute.addAttribute("reader", reader);
            attribute.addAttribute("readersBooks", reader.getBooks());
        }
        return "redirect:/";
    }

    @PostMapping("/return")
    public String returnABook(@RequestParam(name = "id") long id,
                              Principal principal, Model model) {
        readerService.returnABook(principal, id);
        model.addAttribute("theBook", bookService.findById(id));
        model.addAttribute("readersBooks", readerService.findByName(principal.getName()).getBooks());
        return "redirect:/";
    }
}
