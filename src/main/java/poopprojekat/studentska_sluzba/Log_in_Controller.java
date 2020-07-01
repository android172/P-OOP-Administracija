package poopprojekat.studentska_sluzba;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class Log_in_Controller {
    
    private static ArrayList<cashed_user> cashed_users = new ArrayList<>();

    @PostMapping(value="/login_req")
    public long getMethodName(@RequestParam("username") String username, @RequestParam("password") String password) {
        String role = Database.GetUser(username, password);
        if (role == null)
            return 0;
        long token = new Random().nextLong();
        cashed_users.add(new cashed_user(token, role));
        return token;
    }
    
    public static String contains_user(long token) {
        for (cashed_user cs : cashed_users)
            if (cs.equals(token)) return cs.getRole();
        return null;
    }

    private class cashed_user {

        private long token;
        private String role;
    
        cashed_user(long token, String role) {
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