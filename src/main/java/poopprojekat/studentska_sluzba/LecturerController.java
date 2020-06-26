package poopprojekat.studentska_sluzba;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Controller for lecturer list related requests
// INCLUDES:
// 
//  Lecturer filtering methods:
// /get_all_lecturers           - returns list of all lecturers (first_name, last_name, title)
// /get_lecturers?subject=&major=   - returns filtered list of lecturers (first_name, last_name, title)
// /get_lecturer?index=         - returns lecturer with a given id
// 
// Lecturer manipulation methods:
// // all of the following return conformation message or printed error
// /add_lecturer?first_name=&last_name=&title=&lect_id              - add new lecturer to database
// /update_lecturer?lecturer=&first_name=&last_name=&title=&lect_id - update existing lecturer
// /delete_lecturer?index_num=  - delete requested lecturer from database
@RestController
public class LecturerController {

    // return all lecturers
    @GetMapping("/get_all_lecturers")
    public ArrayList<Lecturer> getLecturers() {
        return Database.GetLecturers(null, null);
    }
    
    // returns filtered and ordered list of lecturers
    @GetMapping("/get_lecturers")
    public ArrayList<Lecturer> getLecturers(@RequestParam("subject") String subject, @RequestParam("major") String major) {
        // format picked subjects
        String subjects[] = null;
        if (!subject.equals("all"))
            subjects = subject.split("+");
        // format picked majors
        String majors[] = null;
        if (!major.equals("all"))
            majors = major.split("+");

        return Database.GetLecturers(subjects, majors);
    }

    // return requested lecturer
    @GetMapping("/get_lecturer")
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
            @RequestParam("last_name") String last_name, @RequestParam("title") String title, @RequestParam("lect_id") String lect_id) {
        try {
            int id = Integer.parseInt(lect_id);
            Lecturer updated_lecturer = new Lecturer(id_of_lec_to_up);

            if (first_name == "")
                updated_lecturer.setFirstName(null);
            else updated_lecturer.setFirstName(first_name);
            if (last_name == "")
                updated_lecturer.setLastName(null);
            else updated_lecturer.setLastName(last_name);
            if (title == "")
                updated_lecturer.setTitle(null);
            else updated_lecturer.setTitle(title);
            if (lect_id == "")
                updated_lecturer.setLectId(id);

            Database.EditLecturer(id, updated_lecturer);
            return "Lecturer was updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't update lecturer because of the following error: " + e.getMessage();
        }
    }

    // delete lecturer
    @GetMapping("/delete_lecturer")
    public String delete_lecturer(@RequestParam("lecturer") int lect_id) {
        try {
            Database.DeleteLecturer(lect_id);
            return "Lecturer was deleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't delete lecturer because of the following error: " + e.getMessage();
        }
    }
}