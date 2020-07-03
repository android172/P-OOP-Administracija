package poopprojekat.studentska_sluzba;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

//INCLUDES:
//
// /get_all_majors -returns list of all majors
// /get_major?id= -returns major with given id
//
// /add_major?name= -adds new major with first empty id
// /update_major?id=new_id=name= -updates major with given id
// /delete_major?id= -deletes major with given id


@RestController
public class MajorsController {


    @GetMapping("/get_all_majors")
    public ArrayList<Major> get_all_majors(){

        ArrayList<Major> lista = Database.GetMajors(null);

        return lista;
    }

    @GetMapping("/get_major")
    public Major get_major(@RequestParam("id") int id){

        return Database.GetMajor(id);
    }


    @GetMapping("/add_major")
    public String add_major(@RequestParam("name") String name){

        try {
            Major new_major = new Major(Database.GetEmptyId("Majors"), name);
            if (Database.AddMajor(new_major))
                return "Major was added";
            else {
                return "Database related error occurred; Major could not be added";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Student could not be added because of the following error: " + e.getMessage();
        }
    }


    @GetMapping("/update_major")
    public String update_major(@RequestParam("id") int id,
                               @RequestParam("new_id") int new_id,
                               @RequestParam("name") String new_major_name){


        Major updated_major = new Major(new_id, new_major_name);

        try {
            Database.EditMajor(id, updated_major);

        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't update major because of the following error: " + e.getMessage();
        }
        return "Major successfuly updated";
    }

    @GetMapping("/delete_major")
    public String delete_major(@RequestParam("id") int id){

        try {
            Database.DeleteMajor(id);

        }catch (Exception e){
            e.printStackTrace();
            return "Couldn't delete major because of the following error: " + e.getMessage();
        }
        return "Major successfully deleted";
    }
}
