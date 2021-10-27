package acc.roadmap1.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorMapping {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(Throwable throwable, Model model) {
        String errorMessage =
                (throwable != null && throwable.getMessage() != null && !throwable.getMessage().isBlank() ?
                        throwable.getMessage() :
                        "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorCode", 500);

        throwable.printStackTrace();

        return "error";
    }
}
