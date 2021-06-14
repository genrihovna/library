package acc.roadmap1.library.controller;

import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.model.Reader;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/reader")
public class ReaderController {

    //read-update reader's info
    @Autowired
    private BookService bookService;

    @Autowired
    private ReaderService readerService;

    @GetMapping("/takeABook")
    public String takeABook(@RequestParam("theBook") int theBook,
                            Reader theReader,
                            Model theModel){
        Book tempBook = bookService.findById(theBook);
        if(theReader.getBooks().contains(tempBook)){
            throw new RuntimeException("This book is already in your list.");
        } else {
            theReader.add(tempBook);
            tempBook.setReader(theReader);
            theModel.addAttribute("reader", theReader);
        }
        return "index";
    }

    @GetMapping("/returnABook")
    public String returnABook(@RequestParam("theBook") int theBook,
                              Reader theReader,
                              Model theModel){
        Book tempBook = bookService.findById(theBook);
        if(theReader.getBooks().contains(tempBook)){
            theReader.handOver(tempBook);
            tempBook.setReader(null);
            theModel.addAttribute("reader", theReader);
        } else
            throw new RuntimeException("You do not have this book in your list.");

        return "index";
    }

    @GetMapping("/yourProfile")
    @ResponseBody
    public String showReaderProfile(Principal principal,
                                    Model theModel
                                    ){
        //(@RequestParam("readerId") String readerName, Model theModel){

        String readerName = principal.getName();
        Reader theReader = readerService.findByName(readerName);
        List<Book> readersBooks = theReader.getBooks();
        theModel.addAttribute("reader", theReader);

        theModel.addAttribute("readerId", readerName);
        return "yourAccount";
    }

    @GetMapping("/yourProfile/update")
    public String updateProfile(@RequestParam("readerId") String readerName,
                                Model theModel){
        Reader tempReader = readerService.findByName(readerName);
        theModel.addAttribute("reader", tempReader);
        return "yourAccount";
    }

}
