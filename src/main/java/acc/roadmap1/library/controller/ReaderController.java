package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class ReaderController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ReaderService readerService;

    @PostMapping("/takeABook")
    public String takeABook(@RequestParam int theBook,
                            Principal principal,
                            Model attribute) {
        Book tempBook = bookService.findById(theBook);
        String readerName = principal.getName();
        Reader reader = readerService.findByName(readerName);
        if (reader.getBooks().contains(tempBook)) {
            throw new RuntimeException("This book is already in your list.");
        } else {
            reader.add(tempBook);
            readerService.save(reader);
            attribute.addAttribute("reader", reader);
            attribute.addAttribute("readersBooks", reader.getBooks());
        }
        return "redirect:/";
    }

    @PostMapping("/returnABook")
    public String returnABook(@RequestParam Integer theBook, Principal principal, Model model) {
        Book tempBook = bookService.findById(theBook);
        String readerName = principal.getName();
        Reader reader = readerService.findByName(readerName);
        reader.handOver(tempBook);
        readerService.save(reader);
        model.addAttribute("readersBooks", reader.getBooks());
        return "redirect:/";
    }

    @GetMapping()
    public String showReaderProfile(Principal principal, Model theModel) {
        String readerName = principal.getName();
        Reader theReader = readerService.findByName(readerName);
        List<Book> readersBooks = theReader.getBooks();
        theModel.addAttribute("reader", theReader);
        theModel.addAttribute("readerId", readerName);
        theModel.addAttribute("readersBooks", readersBooks);
        return "yourAccount";
    }

    @GetMapping("/update")
    public String updateProfile(Principal principal, Model theModel) {
        String readerName = principal.getName();
        Reader tempReader = readerService.findByName(readerName);
        theModel.addAttribute("reader", tempReader);
        return "users/index";
    }

}
