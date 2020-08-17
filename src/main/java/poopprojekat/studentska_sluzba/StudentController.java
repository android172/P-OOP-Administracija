package poopprojekat.studentska_sluzba;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Controller for student list related requests
// INCLUDES:
// 
// Control:
// /get_student_filters             - return all cities and majors found in database
// 
// Student filtering methods:
// /get_all_students?token=         - returns list of students (index, first_name, last_name)
// /get_students?token=date_of_birth=&city=&major=&order_by=    - returns filtered list of students (index, first_name,
//                                                                last_name, ?(column by which we order data))
// /get_student?token=index=        - returns student with given index
// /get_student_by_jmbg?token=jmbg= - returns student with given jmbg
// 
// Student manipulation methods:
// // all of the following return conformation message or printed error
// /add_student?token=first_name=&last_name=&br_ind=&date_of_birth=&city=&jmbg=&major_id=               - add new student to database
// /update_student?token=student=&first_name=&last_name=&br_ind=&date_of_birth=&city=&jmbg=&major_id=   - update existing student
// /delete_student?token=index_num= - delete requested student from database
// 
//  
@RestController
public class StudentController {

    // // public methods
    // get all student filters
    @GetMapping("/get_student_filters")
    public ArrayList<ArrayList<String>> get_student_filters(@RequestParam("token") long token) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        ArrayList<ArrayList<String>> ret = new ArrayList<>();
        ret.add(Database.GetAllCities());
        ret.add(Database.GetAllMajors());
        return ret;
    }

    // per page load we send full list of students including only index numbers,
    // first and last names
    @GetMapping("/get_all_students")
    public String[][] get_students(@RequestParam("token") long token) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        return filter_students_from_database(null, null, null, 1);
    }

    // returns filtered and ordered list of students
    @GetMapping("/get_students")
    public String[][] get_students(@RequestParam("token") long token,
            @RequestParam("date_of_birth") String date_of_birth, @RequestParam("city") String city,
            @RequestParam("major") String major, @RequestParam("order_by") String order_by) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        // format picked dates
        LocalDate dates[] = null;
        if (!date_of_birth.equals("all")) {
            String sd[] = date_of_birth.split("\\+");
            dates = new LocalDate[sd.length];
            for (int i = 0; i < sd.length; i++)
                dates[i] = LocalDate.parse(sd[i]);
        }
        // format picked cities
        String cities[] = null;
        if (!city.equals("all"))
            cities = city.split("\\+");
        // format picked majors
        String majors[] = null;
        if (!major.equals("all"))
            majors = major.split("\\+");
        // format order by
        int order_ctg;
        switch (order_by) {
            case "index":
                order_ctg = 1;
                break;
            case "first_name":
                order_ctg = 2;
                break;
            case "last_name":
                order_ctg = 3;
                break;
            case "birthyear":
                order_ctg = 4;
                break;
            case "city":
                order_ctg = 5;
                break;
            case "major":
                order_ctg = 6;
                break;
            default:
                order_ctg = 0;
        }

        return filter_students_from_database(dates, cities, majors, order_ctg);
    }

    @GetMapping("get_students_by_exam")
    public ArrayList<Attempts> get_students_by_exam(@RequestParam("token") long token,
            @RequestParam("exam_id") String exam_id) {
        String lect_id = null;
        for (Exam e : Database.GetAllExams())
            if (e.getId() == exam_id) {
                lect_id = e.getLect_id();
                break;
            }
        if (lect_id == null) {
            System.out.println("Exam id : " + exam_id + "doesn't exist");
            return null;
        }
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" }, { "Lecturer", lect_id } }))
            return null;
        try {
            return Database.GetStudentsByExam(exam_id);
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    // return selected student
    @GetMapping("/get_student")
    public Student get_student(@RequestParam("token") long token, @RequestParam("index") String index) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" }, { "Student", index } }))
            return null;
        Index i;
        try {
            i = new Index(index);
            return Database.GetStudent(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/get_student_by_jmbg")
    public Student get_student_wj(@RequestParam("token") long token, @RequestParam("jmbg") String jmbg) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        return Database.GetStudent(jmbg);
    }

    // add student
    @GetMapping("/add_student")
    public String add_student(@RequestParam("token") long token, @RequestParam("first_name") String first_name,
            @RequestParam("last_name") String last_name, @RequestParam("year") int year,
            @RequestParam("date_of_birth") String date_of_birth, @RequestParam("city") String city,
            @RequestParam("jmbg") String jmbg, @RequestParam("major_id") String major_id) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        try {
            Index ind = new Index(Database.GetHighestIndex(year), year);
            Student new_student = new Student(first_name, last_name, ind,
                    LocalDate.parse(date_of_birth), city, jmbg, major_id);
            Database.AddStudent(new_student);
            Database.AddUser(new User(ind.toString(), jmbg, "Student", ind.toString()));
            return "Student was added";
        } catch (Exception e) {
            e.printStackTrace();
            return "Student could not be added because of the following error: " + e.getMessage();
        }
    }

    // update student
    @GetMapping("/update_student")
    public String update_student(@RequestParam("token") long token,
            @RequestParam("student") String index_of_student_to_update, @RequestParam("first_name") String first_name,
            @RequestParam("last_name") String last_name, @RequestParam("index_num") String index_num,
            @RequestParam("date_of_birth") String date_of_birth, @RequestParam("city") String city,
            @RequestParam("jmbg") String jmbg, @RequestParam("major_id") String major_id) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        try {
            Index req_index = new Index(index_of_student_to_update);
            Student updated_student = new Student(req_index);

            if (first_name == "")
                updated_student.setFirstName(null);
            else
                updated_student.setFirstName(first_name);
            if (last_name == "")
                updated_student.setLastName(null);
            else
                updated_student.setLastName(last_name);
            if (index_num != "")
                updated_student.setIndex(new Index(index_num));
            if (date_of_birth == "")
                updated_student.setDateOfBirth(null);
            else
                updated_student.setDateOfBirth(LocalDate.parse(date_of_birth));
            if (city == "")
                updated_student.setCity(null);
            else
                updated_student.setCity(city);
            if (jmbg == "")
                updated_student.setJmbg(null);
            else
                updated_student.setJmbg(jmbg);
            if (major_id == "")
                updated_student.setMajorId(null);
            else
                updated_student.setMajorId(major_id);

            Database.EditStudent(req_index, updated_student);
            return "Student was updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't update student because of the following error: " + e.getMessage();
        }
    }

    // delete student
    @GetMapping("/delete_student")
    public String delete_student(@RequestParam("token") long token, @RequestParam("index_num") String index) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        try {
            Index req_index = new Index(index);
            Database.DeleteStudent(req_index);
            Database.DeleteUser(index);
            return "Student was deleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't delete student because of the following error: " + e.getMessage();
        }
    }

    // apply student to given subjects
    @GetMapping("/apply_for_subject")
    public String apply_for_subject(@RequestParam("token") long token, @RequestParam("index_num") String index,
            @RequestParam("subjects") String subjects) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" }, { "Student", index } }))
            return null;

        String ids[] = subjects.split("\\+");
        try {
            Database.ApplyForSubject(new Index(index), ids);
            return "application concluded successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "application failed because of the following error: " + e.getMessage();
        }
    }

    // return all students for given year and subjects
    @GetMapping(value = "get_all_students_from_subject")
    public ArrayList<Index> get_all_students_from_subject(@RequestParam("token") long token,
            @RequestParam("subject") String subject_id) {
        Lecturer lect = Database.GetLecturer(subject_id);
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" }, { "Student", lect.getLectId() } }))
            return null;
        try {
            return Database.GetAllStudentsFromSubject(subject_id, LocalDate.now().getYear());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/get_attempts_info_for_index")
    public ArrayList<Attempts> get_attempts_info(@RequestParam("token") long token,
            @RequestParam("index") String index) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Student", index } }))
            return null;
        try {
            return Database.GetAttemptsOfStudent(new Index(index), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @GetMapping("/get_attempts_info_for_subject")
    public ArrayList<Attempts> get_attempts_info_s(@RequestParam("token") long token,
            @RequestParam("subject_id") String subject_id) {
        String lect_id = Database.GetSubject(subject_id).lectid;
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Lecturer", lect_id } }))
            return null;
        try {
            return Database.GetStudentsBySubject(subject_id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

   //  // private methods
   private String[][] filter_students_from_database(LocalDate date_of_birth[], String city[], String major[], int order_by) {
       // get required data from database
       ArrayList<Student> requested_students = Database.GetStudents(date_of_birth, city, major, order_by);
       String[][] ret_s;

       // we will append data with bonus column only if we chose to sort by said column,
       // otherwise only first_name, last_name and index columns are used
       if (order_by > 3) {
           ret_s = new String[requested_students.size()+1][4];
           switch (order_by) {
               case 4:
                   for (int i = 0; i < ret_s.length-1; i++)
                       ret_s[i+1][3] = requested_students.get(i).getDateOfBirth().toString();
                   ret_s[0][3] = "Datum rodjenja";
                   break;
               case 5:
                   for (int i = 0; i < ret_s.length-1; i++)
                       ret_s[i+1][3] = requested_students.get(i).getCity();
                   ret_s[0][3] = "Grad";
                   break;
               case 6:
                   for (int i = 0; i < ret_s.length-1; i++)
                       ret_s[i+1][3] = requested_students.get(i).getMajorname();
                   ret_s[0][3] = "Smer";
                   break;
           }
       }
       else ret_s = new String[requested_students.size()+1][3];

       ret_s[0][0] = "Indeks";
       ret_s[0][1] = "Ime";
       ret_s[0][2] = "Prezime";

       for (int i = 0; i < ret_s.length-1; i++) {
           ret_s[i+1][0] = requested_students.get(i).getIndex().toString();
           ret_s[i+1][1] = requested_students.get(i).getFirstName();
           ret_s[i+1][2] = requested_students.get(i).getLastName();
       }

       return ret_s;
   }
}