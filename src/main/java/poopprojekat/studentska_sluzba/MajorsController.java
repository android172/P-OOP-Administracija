package poopprojekat.studentska_sluzba;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

//INCLUDES:
//
// /get_all_majors? -returns list of all majors
// /get_major?token=&id= -returns major with given id
//
// /add_major?token=&name= -adds new major with first empty id
// /update_major?token=&id=&new_id=&name= -updates major with given id
// /delete_major?token=&id= -deletes major with given id


@RestController
public class MajorsController {


    @GetMapping("/get_all_majors")
    public ArrayList<Major> get_all_majors(@RequestParam("token") long token){

        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

        ArrayList<Major> lista = Database.GetMajors(null);

        return lista;
    }

    @GetMapping("/get_major")
    public Major get_major(@RequestParam("token") long token,
                           @RequestParam("id") String id){

        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;
        return Database.GetMajor(id);
    }


    @GetMapping("/add_major")
    public String add_major(@RequestParam("token") long token,
                            @RequestParam("name") String name){
        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

        try {
            Major new_major = new Major(Database.GetEmptyId("Majors"), name);
            Database.AddMajor(new_major);
            return "Major successfully added";
        } catch (Exception e) {
            e.printStackTrace();
            return "Student could not be added because of the following error: " + e.getMessage();
        }
    }


    @GetMapping("/update_major")
    public String update_major(@RequestParam("token") long token,
                               @RequestParam("id") String id,
                               @RequestParam("new_id") String new_id,
                               @RequestParam("name") String new_major_name){

        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

        try {
            Major updated_major = new Major(new_id, new_major_name);
            Database.EditMajor(id, updated_major);

        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't update major because of the following error: " + e.getMessage();
        }
        return "Major successfully updated";
    }

    @GetMapping("/delete_major")
    public String delete_major(@RequestParam("token") long token,
                               @RequestParam("id") String id){

        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

        try {
            Database.DeleteMajor(id);

        }catch (Exception e){
            e.printStackTrace();
            return "Couldn't delete major because of the following error: " + e.getMessage();
        }
        return "Major successfully deleted";
    }
}
