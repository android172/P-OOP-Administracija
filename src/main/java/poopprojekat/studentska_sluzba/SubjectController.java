package poopprojekat.studentska_sluzba;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;

//INCLUDES:
// /dropdown_lecturers?token= -returns list of names of all lecturers
// /get_all_subjects?token= -returns all subjects
// /get_subjects?token=&name=&year=&lect_name=&major_name=&order_by  -returns filtered ordered list of subjects (ArrayList<Subject>)
// /get_subjects_by_lecturer?token=&lect_id= -returns subjects of lecturer with given id
// /get_subjects_by_major?token=&major_id= -returns subjects from given major
// /get_subjects_by_student?token=&index= -returns subjects that student is applied
//
// /add_subject?token=&name=&id=&espb=&year=&lect_id=&major_id=
// /update_subject?token=&name=&id=&new_id=&espb=&year=&lect_id=&major_id=
// /delete_subject?token=&id=
//

@RestController
public class SubjectController {

    @PostMapping("/get_subject_filters")
    public ArrayList<ArrayList<String>> get_subject_filters(@RequestParam("token") long token){

        if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

        ArrayList<ArrayList<String>> ret = new ArrayList<>();

        ret.add(Database.GetAllLecturers());
        ret.add(Database.GetAllMajors());

        return ret;
    }

    @PostMapping("/get_all_subjects")
    public ArrayList<Subject> get_all_subjects(@RequestParam("token") long token){
        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

            return Database.GetSubjects(null, null, null, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("/get_subject")
    public Subject get_subject(@RequestParam("token") long token,
                               @RequestParam("subjectId") String id){

        try {
            if (!Log_in_Controller.access_allowed(token, new String[][]{{"Admin", "any"}})) return null;
            return Database.GetSubject(id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("get_subject request error");
            return null;
        }
    }

    @PostMapping("/get_subjects_by_lecturer")
    public ArrayList<Subject> get_subjects_by_lecturer(@RequestParam("token") long token,
                                           @RequestParam("lect_id") String id){

        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] { {"Admin", "any"}, {"Lecturer", id} })) return null;

            return Database.SubjectsOfLecturer(id);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/get_subjects")
    public ArrayList<Subject> get_subjects(@RequestParam("token") long token,
                                           @RequestParam("year") String year,
                                           @RequestParam("lect_name") String lect_name,
                                           @RequestParam("major_name") String major_name,
                                           @RequestParam("order_by") String ord){

        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;
            if (year.equals("") || lect_name.equals("") || major_name.equals("")) return null;
            // String[] names = null;
            int[] years = null;
            String[] pom = year.split("\\+");

            if (!year.equals("all")){
                years = new int[pom.length];
                int i=0;
                for (String tmp : pom) {
                    years[i++] = Integer.parseInt(tmp);
                }
            }
            String[] lects = null;
            if (!lect_name.equals("all")){
                lects = lect_name.split("\\+");
            }
            String[] majors = null;
            if (!major_name.equals("all")){
                majors = major_name.split("\\+");
            }

            int order_ctg;
            switch (ord) {
                case "subj_name":
                    order_ctg = 1;
                    break;
                case "subj_id":
                    order_ctg = 2;
                    break;
                case "year":
                    order_ctg = 3;
                    break;
                case "espb":
                    order_ctg = 4;
                    break;
                case "major_name":
                    order_ctg = 5;
                    break;
                case "lect_id":
                    order_ctg = 6;
                    break;
                default:
                    order_ctg = 0;
            }

            return Database.GetSubjects(years, lects, majors, order_ctg);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/get_subjects_by_major")
    public ArrayList<Subject> get_subjects_by_major(@RequestParam("token") long token,
                                                    @RequestParam("major_id") String id){

        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}, {"Student", "any"}})) return null;
            if (id.equals("")) return Database.GetSubjectsByMajor(null);
            return Database.GetSubjectsByMajor(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/get_subjects_by_student")
    public ArrayList<Subject> get_subjects_by_student(@RequestParam("token") long token,
                                                      @RequestParam("index") String index){
        
        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}, {"Student", index}})) return null;
            return Database.GetSubjectsByStudent(new Index(index), LocalDate.now().getYear());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/add_subject")
    public String add_subject(@RequestParam("token") long token,
                              @RequestParam("name") String name,
                              @RequestParam("espb") int espb,
                              @RequestParam("year") int year,
                              @RequestParam("lect_id") String lect_id,
                              @RequestParam("major_id") String major_id,
                              @RequestParam("points_req") int points_req){

        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

            Subject subject = new Subject(name, Database.GetEmptyId("Subjects"), espb, year, lect_id, major_id, points_req);
            Database.AddSubject(subject);
            return "Subject successfully added";
        } catch (Exception e) {
            e.printStackTrace();
            return "Subject could not be added because of the following error: " + e.getMessage();
        }
    }

    @PostMapping("/update_subject")
    public String update_subject(@RequestParam("token") long token,
                                 @RequestParam("subjectId") String old_id,
                                 @RequestParam("subjectName") String name,
                                 @RequestParam("new-subjectId") String id,
                                 @RequestParam("espb") int espb,
                                 @RequestParam("year") int year,
                                 @RequestParam("lectid") String lect_id,
                                 @RequestParam("majorid") String major_id,
                                 @RequestParam("points_required") int points_req){

        try {
            if (!Log_in_Controller.access_allowed(token, new String[][] {{"Admin", "any"}})) return null;

            Subject subject = new Subject(name, id, espb, year, lect_id, major_id, points_req);
            Database.EditSubject(old_id, subject);
            return "Subject successfully updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Subject could not be updated because of the following error: " + e.getMessage();
        }
    }

    @PostMapping("/delete_subject")
    public String delete_subject(@RequestParam("token") long token,
                                 @RequestParam("subjectId") String id){

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