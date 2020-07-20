package poopprojekat.studentska_sluzba;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class Log_in_Controller {

    private static ArrayList<Cashed_user> cashed_users = new ArrayList<>();

    @GetMapping(value = "/login_req")
    public String log_in_request(@RequestParam("username") String username,
            @RequestParam("password") String password) {
        String ri[] = Database.GetUser(username, password);
        if (ri == null)
            return "access denied";

        for (Cashed_user cu : cashed_users)
            if (cu.id.equals(ri[1])) log_out_request(cu.token);
            
        Cashed_user cu = new Cashed_user(new Random().nextLong(), ri[0], ri[1]);
        cashed_users.add(cu);
        return (cu.role + ":" + cu.token + ":" + cu.id);
    }

    @GetMapping("/logout_req")
    public static String log_out_request(@RequestParam("token") long token) {
        int i = 0;
        for (Cashed_user cu : cashed_users) {
            if (cu.equals(token)) {
                cashed_users.remove(i);
                return "log-out was successful";
            }
            i++;
        }
        return "user doesn't exist";
    }

    public static boolean access_allowed(long token, String roles[][]) {
        for (Cashed_user cu : cashed_users)
            if (cu.equals(token)) {
                for (String r[] : roles) 
                    if (r[0].equals(cu.role) && (r[1].equals("any") || r[1].equals(cu.id)))
                        return true;
                return false;
            }
        return false;
    }

    private class Cashed_user {

        private long token;
        private String role;
        private String id;
        private Timer timer;
    
        Cashed_user(long token, String role, String id) {
            this.token = token;
            this.role = role;
            this.id = id;
            timer = new Timer();
            timer.schedule(new TimerTask(){
            
                @Override
                public void run() {
                    cashed_users.remove(0);
                }
            }, 6*3600*1000);
        }
    
        boolean equals(long token) {
            return token == this.token;
        }
    }
}