package acc.roadmap1.library.controller;

import acc.roadmap1.library.controller.dto.RegisterAccount;
import acc.roadmap1.library.model.ApplicationUserDetails;
import acc.roadmap1.library.model.Book;
import acc.roadmap1.library.service.BookService;
import acc.roadmap1.library.service.SecurityService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller("/")
public class RootController {

    private final BookService bookService;

    private final SecurityService securityService;

    @Autowired
    public RootController(BookService bookService, SecurityService securityService) {
        this.bookService = bookService;
        this.securityService = securityService;
    }

    @GetMapping("/")
    public String getMainPage(Model model,
                              @AuthenticationPrincipal ApplicationUserDetails userDetails) {
        List<Book> books = bookService.findBooksWithStatusForCurrentUser(userDetails);
        model.addAttribute("books", books);

        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new RegisterAccount());
        return "register";
    }

    @GetMapping("/register/admin")
    public String getAdminRegisterPage(Model model) {
        model.addAttribute("user", new RegisterAccount());
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView registerNewUser(@ModelAttribute("user") RegisterAccount registerAccount, ModelMap model) {
        securityService.createReaderAccount(registerAccount.getUsername(), registerAccount.getPassword(),
                registerAccount.getName());
        return new ModelAndView("redirect:/login");
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/register/admin")
    public String registerAdmin(@ModelAttribute("user") RegisterAccount registerAccount) {
        securityService.createLibrarianAccount(registerAccount.getUsername(), registerAccount.getPassword(),
                registerAccount.getName());
        return "redirect:/login";
    }
}
