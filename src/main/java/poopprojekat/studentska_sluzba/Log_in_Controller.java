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
        if (ri == null || contains_id(ri[1]))
            return "access denied";
        Cashed_user cu = new Cashed_user(new Random().nextLong(), ri[0], ri[1]);
        cashed_users.add(cu);
        return (cu.role + ":" + cu.token + ":" + cu.id);
    }
    
    public static String[] contains_user(long token) {
        for (Cashed_user cs : cashed_users)
            if (cs.equals(token)) {
                String ret[] = {cs.role, cs.id};
                return ret;
            }
        return null;
    }

    private boolean contains_id(String id) {
        for (Cashed_user cs : cashed_users)
            if (cs.id.equals(id)) return true;
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