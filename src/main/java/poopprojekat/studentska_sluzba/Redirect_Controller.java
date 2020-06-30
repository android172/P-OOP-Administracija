package poopprojekat.studentska_sluzba;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Redirect_Controller
{
    @PostMapping("/")
    public String index(@RequestParam("token") long token){
        return redirect_to(token, "admin");
    }

    @PostMapping("/students")
    public String students(@RequestParam("token") long token){
        return redirect_to(token, "students");
    }

    @PostMapping("/staff")
    public String staff(@RequestParam("token") long token){
        return redirect_to(token, "staff");
    }

    @PostMapping("/courses")
    public String courses(@RequestParam("token") long token){
        return redirect_to(token, "courses");
    }

    @PostMapping("/majors")
    public String majors(@RequestParam("token") long token){
        return redirect_to(token, "majors");
    }

    @PostMapping("/login")
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
