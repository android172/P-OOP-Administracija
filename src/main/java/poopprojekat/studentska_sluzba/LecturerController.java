package poopprojekat.studentska_sluzba;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LecturerController {

    // return requested lecturer
    @GetMapping("/lecturer")
    public Lecturer getLecturers(@RequestParam("lect_id") int lect_id) {
        return Database.GetLecturer(lect_id);
    }

    // add lecturer
    @GetMapping("/add_lecturer")
    public String add_lecturer(@RequestParam("first_name") String first_name,
            @RequestParam("last_name") String last_name, @RequestParam("title") String title,
            @RequestParam("lect_id") int lect_id) {
        Lecturer new_lecturer = new Lecturer(first_name, last_name, title, lect_id);
        try {
            if (Database.AddLecturer(new_lecturer))
                return "Lecturer was added";
            return "An database level error occurred; Lecturer could not be added";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lecturer could not be added because of the following error: " + e.getMessage();
        }
    }
    // update lecturer
    @GetMapping("/update_lecturer")
    public String update_lecturer(@RequestParam("lecturer") int id_of_lec_to_up, @RequestParam("first_name") String first_name,
            @RequestParam("last_name") String last_name, @RequestParam("title") String title,
            @RequestParam("lect_id") int lect_id) {
        Lecturer lect_to_update = Database.GetLecturer(id_of_lec_to_up);
        if (lect_to_update == null)
            return "Given lecturer was not found";
        
        return null;
    }
}