package acc.roadmap1.library.controller;

import acc.roadmap1.library.LibraryException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ErrorMapping {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMapping.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(Throwable throwable, Model model) {

        LOGGER.error(ExceptionUtils.getStackTrace(throwable));

        String errorMessage =
                (throwable != null && throwable.getMessage() != null && !throwable.getMessage().isBlank() ?
                        throwable.getMessage() :
                        "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorCode", 500);
        return "error";
    }

    @ExceptionHandler(LibraryException.class)
    public String handleProjectException(LibraryException e, RedirectAttributes redirectAttrs) {

        redirectAttrs.addFlashAttribute("errorMessage", e.getMessage());
        redirectAttrs.addFlashAttribute("errorCode", e.getErrorCode());

        return "redirect:" + e.getRedirectPage();
    }
}
