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
import org.springframework.web.bind.annotation.*;

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

    //@Secured("MANAGE_BOOKS")
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
    public String takeABook(@RequestParam(name = "id") long bookId,
                            @AuthenticationPrincipal ApplicationUserDetails userDetails,
                            Model attribute) {
        Book book = bookService.findById(bookId);
        System.out.println("userdetails //" + userDetails);
        System.out.println(userDetails.getUsername());
        String readerName = userDetails.getUsername();
        Reader reader = readerService.findByName(readerName);
        if (reader.getBooks().contains(book)) {
            throw new RuntimeException("This book is already in your list.");
        } else {
            bookService.handOverBook(bookId, reader.getId());
            //reader = readerService.takeABook(userDetails, bookId);
            attribute.addAttribute("reader", reader);
            attribute.addAttribute("readersBooks", reader.getBooks());
        }
        return "redirect:/";
    }

    @PostMapping("/return")
    public String returnABook(@RequestParam(name = "bookId") long bookId,
                              @AuthenticationPrincipal ApplicationUserDetails userDetails, Model model) {
        readerService.returnABook(userDetails, bookId);
        model.addAttribute("theBook", bookService.findById(bookId));
        model.addAttribute("readersBooks", userDetails.getAccount().getReader().getBooks());
        return "redirect:/";
    }
}
