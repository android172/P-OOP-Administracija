package poopprojekat.studentska_sluzba.Generators;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerTest
{
    @GetMapping("/")
    public String login(){
        return "login";
    }

    // Mora da bude @Controller, ne radi @RestController
}
