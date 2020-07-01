package poopprojekat.studentska_sluzba;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class Log_in_Controller {

    private static ArrayList<Cashed_user> cashed_users = new ArrayList<>();

    @GetMapping(value = "/login_req")
    public Cashed_user getMethodName(@RequestParam("username") String username,
            @RequestParam("password") String password) {
        String role = Database.GetUser(username, password);
        if (role == null)
            return null;
        Cashed_user cu = new Cashed_user(new Random().nextLong(), role);
        cashed_users.add(cu);
        return cu;
    }
    
    public static String contains_user(long token) {
        for (Cashed_user cs : cashed_users)
            if (cs.equals(token)) return cs.getRole();
        return null;
    }

    private class Cashed_user {

        private long token;
        private String role;
    
        Cashed_user(long token, String role) {
            this.token = token;
            this.role = role;
        }
    
        String getRole() {
            return role;
        }
    
        boolean equals(long token) {
            return token == this.token;
        }
    }
}