package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.BookStatus;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Book> bookList = bookService.findAll().stream().peek(book -> {
            if (book.getReader().equals(userDetails.getAccount().getReader())) {
                book.setStatus(BookStatus.CAN_RETURN);
            } else if (!book.getReader().equals(userDetails.getAccount().getReader())) {
                book.setStatus(BookStatus.ALREADY_TAKE);
            } else if (book.getReader() == null) {
                book.setStatus(BookStatus.CAN_TAKE);
            }
        }).collect(Collectors.toList());
        model.addAttribute("books", bookList);
        return "librarian/index";
    }

    @GetMapping("/readers")
    public String takeReaderList(Model theModel){
        List<Reader> readerList = readerService.findAll();
        theModel.addAttribute("readers", readerList);
        return "librarian/reader-list";
    }
}
