package poopprojekat.studentska_sluzba;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MajorsController {


//    @GetMapping("/get_all_majors")
//    public ArrayList<Major> get_all_majors(){
//
//        ArrayList<Major> lista = Database.GetMajors(null);
//
//        return lista;
//    }


    @GetMapping("/add_major")
    public String add_major(int id, String name){  //RequestParam treba da se doda

        try {

            Major new_major = new Major(id, name);
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

    //todo da se doda jos neka kontrola gresaka
    @GetMapping("/update_major")
    public String update_major(@RequestParam("major_id") int id,
                               @RequestParam("new_major_id") int new_id,
                               @RequestParam("major_name") String new_major_name){


        Major updated_major = new Major(new_id, new_major_name);

        Database.EditMajor(id, updated_major);

        return "Major successfuly updated";
    }

    @GetMapping("/delete_major")
    public String delete_major(@RequestParam("major_id") int id){

        Database.DeleteMajor(id);

        return "Major successfully deleted";
    }
}
