package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    //CRUD book operations
    //CRUD reader operations

    private BookService bookService;
    private ReaderService readerService;

    @Autowired
    public LibrarianController(BookService bookService, ReaderService readerService) {
        this.bookService = bookService;
        this.readerService = readerService;
    }

    @GetMapping
    public String librarianPage(Model theModel){
        List<Book> bookList = bookService.findAll();
        theModel.addAttribute("books", bookList);
        return "librarian";
    }

    @GetMapping("/showFormForAdd")
    public String addABook(Model model){
        Book theBook = new Book();
        model.addAttribute("book", theBook);
        return "book-form";
    }

    @PostMapping("/books/save")
    public String saveABook(@ModelAttribute("book")
                            Book theBook){
        bookService.save(theBook);
        return "book-form";
    }

    @GetMapping("/deleteABook")
    public String deleteABook(@RequestParam("bookId") int theId){
        bookService.deleteById(theId);
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("deleted", Boolean.TRUE);
        return "book-form";
    }

    @GetMapping("/showFormForUpdate")
    public String updateABook(@RequestParam("bookId") int theId,
                              Model theModel){
        Book theBook = bookService.findById(theId);
        theModel.addAttribute("book", theBook);
        return "book-form";
    }



}
