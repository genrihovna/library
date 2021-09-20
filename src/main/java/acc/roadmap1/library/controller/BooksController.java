package acc.roadmap1.library.controller;

import acc.roadmap1.library.controller.dto.CreateBookItem;
import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Secured("MANAGE_BOOKS")
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

    @PostMapping("/take")
    public String takeABook(@RequestParam(name = "id") Integer bookId,
                            Principal principal,
                            Model attribute) {
        Book book = bookService.findById(bookId);
        String readerName = principal.getName();
        Reader reader = readerService.findByName(readerName);
        if (reader.getBooks().contains(book)) {
            throw new RuntimeException("This book is already in your list.");
        } else {
            reader.add(book);
            readerService.save(reader);
            attribute.addAttribute("reader", reader);
            attribute.addAttribute("readersBooks", reader.getBooks());
        }
        return "redirect:/";
    }

    @PostMapping("/return")
    public String returnABook(@RequestParam(name = "id") Integer bookId,
                              @AuthenticationPrincipal ApplicationUserDetails userDetails, Model model) {
        Book book = bookService.findById(bookId);
        Reader reader = userDetails.getAccount().getReader();
        reader.handOver(book);
        readerService.save(reader);
        model.addAttribute("readersBooks", reader.getBooks());
        return "redirect:/";
    }
}
