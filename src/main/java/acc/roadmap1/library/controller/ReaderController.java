package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.User;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.UserService;
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
    private UserService readerService;

    @PostMapping("/takeABook")
    public String takeABook(@RequestParam int theBook,
                            Principal principal,
                            Model attribute) {
        Book tempBook = bookService.findById(theBook);
        String readerName = principal.getName();
        User user = readerService.findByName(readerName);
        if (user.getBooks().contains(tempBook)) {
            throw new RuntimeException("This book is already in your list.");
        } else {
            user.add(tempBook);
            readerService.save(user);
            attribute.addAttribute("reader", user);
            attribute.addAttribute("readersBooks", user.getBooks());
        }
        return "redirect:/";
    }

    @PostMapping("/returnABook")
    public String returnABook(@RequestParam Integer theBook, Principal principal, Model model) {
        Book tempBook = bookService.findById(theBook);
        String readerName = principal.getName();
        User user = readerService.findByName(readerName);
        user.handOver(tempBook);
        readerService.save(user);
        model.addAttribute("readersBooks", user.getBooks());
        return "redirect:/";
    }

    @GetMapping()
    public String showReaderProfile(Principal principal, Model theModel) {
        String readerName = principal.getName();
        User theReader = readerService.findByName(readerName);
        List<Book> readersBooks = theReader.getBooks();
        theModel.addAttribute("reader", theReader);
        theModel.addAttribute("readerId", readerName);
        theModel.addAttribute("readersBooks", readersBooks);
        return "yourAccount";
    }

    @GetMapping("/update")
    public String updateProfile(Principal principal, Model theModel) {
        String readerName = principal.getName();
        User tempReader = readerService.findByName(readerName);
        theModel.addAttribute("reader", tempReader);
        return "users/index";
    }

}
