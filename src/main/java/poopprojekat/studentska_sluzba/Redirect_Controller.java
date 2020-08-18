package poopprojekat.studentska_sluzba;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Redirect_Controller {
    @GetMapping("/")
    public void redirect_to_login(HttpServletResponse response) {
        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/admin")
    public String index(HttpServletResponse response){
        return "admin";
    }

    @GetMapping("/student")
    public String student(HttpServletResponse response){
        return "student";
    }

    @GetMapping("/students")
    public String students(HttpServletResponse response){
        return "students";
    }

    @GetMapping("/staff")
    public String staff(HttpServletResponse response){
        return "staff";
    }

    @GetMapping("/courses")
    public String courses(HttpServletResponse response){
        return "courses";
    }

    @GetMapping("/majors")
    public String majors(HttpServletResponse response){
        return "majors";
    }

    @GetMapping("/users")
    public String users(HttpServletResponse response){
        return "users";
    }

    @GetMapping("/exams")
    public String exams(HttpServletResponse response){
        return "exams";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
