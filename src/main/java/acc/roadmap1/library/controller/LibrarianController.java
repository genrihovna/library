package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    private final BookService bookService;
    private final ReaderService readerService;

    @Autowired
    public LibrarianController(BookService bookService, ReaderService readerService) {
        this.bookService = bookService;
        this.readerService = readerService;
    }

    @GetMapping
    public String librarianPage(Model model, @AuthenticationPrincipal ApplicationUserDetails userDetails) {
        var books = bookService.findBooksWithStatusForCurrentUser(userDetails);
        model.addAttribute("books", books);
        return "librarian/index";
    }

    @GetMapping("/readers")
    public String takeReaderList(Model theModel) {
        List<Reader> readerList = readerService.findAll();
        theModel.addAttribute("readers", readerList);
        return "librarian/reader-list";
    }
}
