package poopprojekat.studentska_sluzba;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

//INCLUDES:
// /dropdown_lecturers?token= -returns list of all lecturers !!!!Za razliku od Dejanove funkcije ova ti vraca Lecturer umesto String!!!!
                                                                //pa ti iz json-a mozes da uzmes ime i id
// /get_all_subjects?token= -returns all subjects
// /get_subjects?token=&name=&year=&lect_name=&major_name=  -returns filtered list of subjects (ArrayList<Subject>)
// /get_subjects_by_lecturer?token=&lect_id= -returns subjects of lecturer with given id
//
// /add_subject?token=&name=&id=&espb=&year=&lect_id=&major_id=
// /update_subject?token=&name=&id=&new_id=&espb=&year=&lect_id=&major_id=
// /delete_subject?token=&id=
//

@RestController
public class SubjectController {

    @GetMapping("/dropdown_lecturers")
    public ArrayList<Lecturer> get_subject_filters(@RequestParam("token") long token){

        return Database.GetLecturers(null, null);
    }

    @GetMapping("/get_all_subjects")
    public ArrayList<Subject> get_all_subjects(@RequestParam("token") long token){
        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

            return Database.GetSubjects(null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/get_subjects_by_lecturer")
    public ArrayList<Subject> get_subjects_by_lecturer(@RequestParam("token") long token,
                                           @RequestParam("lect_id") String id){

        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

            return Database.SubjectsOfLecturer(Database.GetLecturer(id));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/get_subjects")
    public ArrayList<Subject> get_subjects(@RequestParam("token") long token,
                                           @RequestParam("name") String name,
                                           @RequestParam("year") String year,
                                           @RequestParam("lect_name") String lect_name,
                                           @RequestParam("major_name") String major_name){

        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

            String[] names = null;
            if (name != "") {
                names = name.split("\\+");
            }
            int[] years = new int[15];
            if (year != ""){
                int i=0;
                for (String tmp : year.split("\\+")) {
                    years[i++] = Integer.parseInt(tmp);
                }
            }
            String[] lects = null;
            if (lect_name != ""){
                lects = lect_name.split("\\+");
            }
            String[] majors = null;
            if (major_name != ""){
                majors = major_name.split("\\+");
            }
            return Database.GetSubjects(names, years, lects, majors);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @GetMapping("/add_subject")
    public String add_subject(@RequestParam("token") long token,
                              @RequestParam("name") String name,
                              @RequestParam("id") String id,
                              @RequestParam("espb") int espb,
                              @RequestParam("year") int year,
                              @RequestParam("lect_id") String lect_id,
                              @RequestParam("major_id") String major_id){

        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

            Subject subject = new Subject(name, id, espb, year, lect_id, major_id);
            Database.AddSubject(subject);
            return "Subject successfully added";
        } catch (Exception e) {
            e.printStackTrace();
            return "Subject could not be added because of the following error: " + e.getMessage();
        }
    }

    @GetMapping("/update_subject")
    public String update_subject(@RequestParam("token") long token,
                                 @RequestParam("id") String old_id,
                                 @RequestParam("name") String name,
                                 @RequestParam("new_id") String id,
                                 @RequestParam("espb") int espb,
                                 @RequestParam("year") int year,
                                 @RequestParam("lect_id") String lect_id,
                                 @RequestParam("major_id") String major_id){

        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

            Subject subject = new Subject(name, id, espb, year, lect_id, major_id);
            Database.EditSubject(old_id, subject);
            return "Subject successfully updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Subject could not be updated because of the following error: " + e.getMessage();
        }
    }

    @GetMapping("/delete_subject")
    public String delete_subject(@RequestParam("token") long token,
                                 @RequestParam("id") String id){

        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

            Database.DeleteSubject(id);
            return "Subject successfully deleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "Subject could not be deleted because of the following error: " + e.getMessage();
        }
    }
}