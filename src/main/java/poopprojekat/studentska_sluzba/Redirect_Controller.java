package poopprojekat.studentska_sluzba;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String index(@RequestParam("token") long token){
        return redirect_to(token, "admin");
    }

    @GetMapping("/students")
    public String students(@RequestParam("token") long token){
        return redirect_to(token, "students");
    }

    @GetMapping("/staff")
    public String staff(@RequestParam("token") long token){
        return redirect_to(token, "staff");
    }

    @GetMapping("/courses")
    public String courses(@RequestParam("token") long token){
        return redirect_to(token, "courses");
    }

    @GetMapping("/majors")
    public String majors(@RequestParam("token") long token){
        return redirect_to(token, "majors");
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    private String redirect_to(long token, String requested) {
        String role = Log_in_Controller.contains_user(token);
        if (role == null) return "login";
        if (role == "Admin") return requested;
        if (role == "Lecturer") {
            return null;
        }
        return null;
    }
}
