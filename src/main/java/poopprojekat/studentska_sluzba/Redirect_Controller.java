package poopprojekat.studentska_sluzba;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String index(HttpServletResponse response, @RequestParam("token") long token){
        if ((Log_in_Controller.contains_user(token)[0]).equals("Admin")) return "admin";

        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/students")
    public String students(HttpServletResponse response, @RequestParam("token") long token){
        if ((Log_in_Controller.contains_user(token)[0]).equals("Admin")) return "students";
        
        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/staff")
    public String staff(HttpServletResponse response, @RequestParam("token") long token){
        if ((Log_in_Controller.contains_user(token)[0]).equals("Admin")) return "staff";
        
        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/courses")
    public String courses(HttpServletResponse response, @RequestParam("token") long token){
        if ((Log_in_Controller.contains_user(token)[0]).equals("Admin")) return "courses";
        
        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/majors")
    public String majors(HttpServletResponse response, @RequestParam("token") long token){
        if ((Log_in_Controller.contains_user(token)[0]).equals("Admin")) return "majors";
        
        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
