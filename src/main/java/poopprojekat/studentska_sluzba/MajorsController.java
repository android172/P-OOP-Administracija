package poopprojekat.studentska_sluzba;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

//INCLUDES:
//
// /get_all_majors -returns list of all majors todo da se proveri u database kako to ide
// /get_major?major_id= -returns major with given id
//
// /add_major?name= -adds new major with first empty id
// /update_major?id=new_id=name= -updates major with given id
// /delete_major?id= -deletes major with given id


@RestController
public class MajorsController {


    @GetMapping("/get_all_majors")
    public ArrayList<Major> get_all_majors(){

        ArrayList<Major> lista = Database.GetMajors(null);  //jos ne znam da li moze da se salje null, to treba u database da se namesti

        return lista;
    }

    @GetMapping("/get_major")
    public Major get_major(@RequestParam("major_id") int id){

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

    //TODO da se doda jos neka kontrola gresaka
    @GetMapping("/update_major")
    public String update_major(@RequestParam("id") int id,
                               @RequestParam("new_id") int new_id,
                               @RequestParam("name") String new_major_name){


        Major updated_major = new Major(new_id, new_major_name);

        Database.EditMajor(id, updated_major);

        return "Major successfuly updated";
    }

    @GetMapping("/delete_major")
    public String delete_major(@RequestParam("id") int id){

        Database.DeleteMajor(id);

        return "Major successfully deleted";
    }
}
