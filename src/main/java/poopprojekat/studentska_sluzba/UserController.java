package poopprojekat.studentska_sluzba;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//INCLUDES:
// /get_user_role -returns role of user with given username todo and password da se doda da moze samo preko username-a da se pretrazuje
// /add_user?username=&password=&year= -adds new user with given username, password todo and year nije svaki user student pa da ima indeks i godinu upisa
// /update_user?username=&new_username=&new_password=&new_role= -updates user with given username, sets new given values
// /delete_user?unique_id= -deletes ueser wiht given id

@RestController
public class UserController {

    @GetMapping("/get_user_role")
    public String get_user_role(@RequestParam("username") String username,
                                @RequestParam("password") String password){

        /*String tmp = Database.GetUser(username, password);

        if (tmp == null) return "User don't exist";
        return tmp;*/
        return null;
    }

    @GetMapping("/add_user")
    public String add_user(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("role") String role,
                           @RequestParam("year") int year){

        User user = new User(username, password, role);

        String index = Database.GetHighestIndex(year) + "/" + year;
        try {
            Database.AddUser(user, index);
            return "User successfully added";
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't add user because of the following error: " + e.getMessage();
        }
    }

    @GetMapping("/update_user")
    public String update_user(@RequestParam("username") String username,
                              @RequestParam("new_username") String new_username,
                              @RequestParam("new_password") String new_password,
                              @RequestParam("new_role") String new_role){

        User updated_user = new User(new_username, new_password, new_role);
        try{
            Database.EditUser(username, updated_user);
            return "User successfully updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't update user because of the following error: " + e.getMessage();
        }
    }

    @GetMapping("/delete_user")
    public String delete_user(@RequestParam("unique_id") String id){
        try{
            Database.DeleteUser(id);
            return "User successfully deleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't delete user because of the following error: " + e.getMessage();
        }
    }

}
