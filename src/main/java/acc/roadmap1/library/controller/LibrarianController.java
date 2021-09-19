package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String librarianPage(Model theModel) {
        List<Book> bookList = bookService.findAll();
        theModel.addAttribute("books", bookList);
        return "librarian/index";
    }

    @GetMapping("/readers")
    public String takeReaderList(Model theModel){
        List<Reader> readerList = readerService.findAll();
        List<String> books = readerList.stream()
                        .map(reader -> reader.getAllBooks())
                                .collect(Collectors.toList());
        theModel.addAttribute("readers", readerList);
        return "librarian/reader-list";
    }
}
