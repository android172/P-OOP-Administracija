
package poopprojekat.studentska_sluzba;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Controller for lecturer list related requests
// INCLUDES:
// 
// Control:
// /get_lecturer_filters?=token         - return all subjects and majors found in database
// 
// Lecturer filtering methods:
// /get_all_lecturers?token=            - returns list of all lecturers (first_name, last_name, title)
// /get_lecturers?token=subject=&major=&order_by=   - returns filtered and sorted list of lecturers (first_name, last_name, title)
// /get_lecturer?token=index=           - returns lecturer with a given id
// 
// Lecturer manipulation methods:
// // all of the following return conformation message or printed error
// /add_lecturer?token=first_name=&last_name=&title=&lect_id                - add new lecturer to database
// /update_lecturer?token=lecturer=&first_name=&last_name=&title=&lect_id   - update existing lecturer
// /delete_lecturer?token=index_num=    - delete requested lecturer from database
@RestController
public class LecturerController {

    // public methods
    // get all lecturer filters
    @GetMapping("/get_lecturer_filters")
    public ArrayList<ArrayList<String>> get_lecturer_filters(@RequestParam("token") long token) {
        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;
        ArrayList<ArrayList<String>> ret = new ArrayList<>();
        ret.add(Database.GetAllSubjects());
        ret.add(Database.GetAllMajors());
        return ret;
    }

    // return all lecturers
    @GetMapping("/get_all_lecturers")
    public ArrayList<Lecturer> getLecturers(@RequestParam("token") long token) {
        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;
        return Database.GetLecturers(null, null, 1);
    }
    
    // returns filtered and ordered list of lecturers
    @GetMapping("/get_lecturers")
    public ArrayList<Lecturer> getLecturers(@RequestParam("token") long token, @RequestParam("subject") String subject, @RequestParam("major") String major, @RequestParam("order_by") String order_by) {
        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;
        // format picked subjects
        String subjects[] = null;
        if (!subject.equals("all"))
            subjects = subject.split("\\+");
        // format picked majors
        String majors[] = null;
        if (!major.equals("all"))
            majors = major.split("\\+");
        int order_ctg;
        switch (order_by) {
            case "lect_id":
                order_ctg = 1;
                break;
            case "first_name":
                order_ctg = 2;
                break;
            case "last_name":
                order_ctg = 3;
                break;
            case "title":
                order_ctg = 4;
                break;
            default:
                order_ctg = 0;
        }

        return Database.GetLecturers(subjects, majors, order_ctg);
    }

    // return requested lecturer
    @GetMapping("/get_lecturer")
    public Lecturer getLecturers(@RequestParam("token") long token, @RequestParam("lect_id") String lect_id) {
        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}, {"Lecturer", lect_id}})) return null;
        return Database.GetLecturer(lect_id);
    }

    // add lecturer
    @GetMapping("/add_lecturer")
    public String add_lecturer(@RequestParam("token") long token, @RequestParam("first_name") String first_name,
            @RequestParam("last_name") String last_name, @RequestParam("title") String title,
            @RequestParam("lect_id") String lect_id) {
        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;
        Lecturer new_lecturer = new Lecturer(first_name, last_name, title, lect_id);
        try {
            Database.AddLecturer(new_lecturer);
            Database.AddUser(new User(last_name + lect_id, lect_id, "Lecturer", lect_id));
            return "Lecturer was added";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lecturer could not be added because of the following error: " + e.getMessage();
        }
    }
    // update lecturer
    @GetMapping("/update_lecturer")
    public String update_lecturer(@RequestParam("token") long token, @RequestParam("lecturer") String id_of_lec_to_up, @RequestParam("first_name") String first_name,
            @RequestParam("last_name") String last_name, @RequestParam("title") String title, @RequestParam("lect_id") String lect_id) {
        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;
        try {
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
                updated_lecturer.setLectId(lect_id);

            Database.EditLecturer(lect_id, updated_lecturer);
            return "Lecturer was updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't update lecturer because of the following error: " + e.getMessage();
        }
    }

    // delete lecturer
    @GetMapping("/delete_lecturer")
    public String delete_lecturer(@RequestParam("token") long token, @RequestParam("lecturer") String lect_id) {
        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;
        try {
            Database.DeleteLecturer(lect_id);
            Database.DeleteUser(String.valueOf(lect_id));
            return "Lecturer was deleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't delete lecturer because of the following error: " + e.getMessage();
        }
    }
}