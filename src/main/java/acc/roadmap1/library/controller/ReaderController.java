package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @GetMapping()
    public String showReaderProfile(Principal principal, Model theModel) {
        String readerName = principal.getName();
        Reader theReader = readerService.findByName(readerName);
        Set<Book> readersBooks = theReader.getBooks();
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
