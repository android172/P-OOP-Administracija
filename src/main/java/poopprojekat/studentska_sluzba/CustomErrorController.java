package poopprojekat.studentska_sluzba;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "redirect";
    }

    @Override
    public java.lang.String getErrorPath() {
        return PATH;
    }
}