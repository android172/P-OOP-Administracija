package poopprojekat.studentska_sluzba.Generators;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerTest
{
    @GetMapping("/")
    public String index(){
        return "admin";
    }

    @GetMapping("/students")
    public String students(){
        return "students";
    }

    @GetMapping("/staff")
    public String staff(){
        return "staff";
    }

    @GetMapping("/courses")
    public String courses(){
        return "/courses";
    }

    @GetMapping("/majors")
    public String majors(){
        return "majors";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    // Mora da bude @Controller, ne radi @RestController
}
