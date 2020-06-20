package poopprojekat.studentska_sluzba;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Controller for student list related requests
// INCLUDES:
// 
//  Student filtering methods:
// /students                    - returns list of students (index, first_name, last_name)
// /students?date_of_birth=...&city=...&mayor=...&order_by=...  - returns filtered list of students (index, first_name,
//                                                                last_name, ?column by which we order data)
// /student?index=...                                           - returns student with given index
// /student?jmbg=...                                            - returns student with given jmbg
//@RestController
// /student?index=...           - returns student with given index
// /student?jmbg=...            - returns student with given jmbg
// 
// Student manipulation methods:
// // all of the following return conformation message or printed error
// /add_student?first_name=&last_name=&br_ind=&date_of_birth=&city=&jmbg=&major_id=                 - add new student to database
// /update_student?student=&first_name=&last_name=&br_ind=&date_of_birth=&city=&jmbg=&major_id=     - update existing student
// /delete_student?index_num=...    - delete requested student from database
@RestController
public class StudentController {

    // // public methods
    // per page load we send full list of students including only index numbers,
    // first and last names
    @GetMapping("/get_all_students")
    public String[][] get_students() {
        return filter_students_from_database(null, null, null, 0, true);
    }

    // returns filtered and ordered list of students
    @GetMapping("/get_students")
    public String[][] get_students(@RequestParam("date_of_birth") String date_of_birth,
            @RequestParam("city") String city, @RequestParam("major") String major,
            @RequestParam("order_by") String order_by) {

        // format picked dates
        Date dates[] = null;
        if (!date_of_birth.equals("all")) {
            String sd[] = date_of_birth.split("+");
            dates = new Date[sd.length];
            for (int i = 0; i < sd.length; i++)
                dates[i] = Date.valueOf(sd[i]);
        }
        // format picked cities
        String cities[] = null;
        if (!city.equals("all"))
            cities = city.split("+");
        // format picked majors
        String majors[] = null;
        if (!major.equals("all"))
            majors = major.split("+");
        // format order by
        int order_ctg;
        switch (order_by.split("-")[0]) {
            case "birthyear":
                order_ctg = 3;
                break;
            case "city":
                order_ctg = 4;
                break;
            case "mayor":
                order_ctg = 6;
                break;
            default:
                order_ctg = 0;
        }
        boolean asc = true;
        if (order_by.split("-")[1] == "des")
            asc = false;

        return filter_students_from_database(dates, cities, majors, order_ctg, asc);
    }

    // return selected student
    @GetMapping("/get_student")
    public Student get_student(@RequestParam("index") String index) {
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
    public Student get_student_wj(@RequestParam("jmbg") String jmbg) {
        return Database.GetStudent(jmbg);
    }

    // add student
    @GetMapping("/add_student")
    public String add_student(@RequestParam("first_name") String first_name,
            @RequestParam("last_name") String last_name, @RequestParam("index_num") String index_num,
            @RequestParam("date_of_birth") String date_of_birth, @RequestParam("city") String city,
            @RequestParam("jmbg") String jmbg, @RequestParam("major_id") int major_id) {
        try {
            Student new_student = new Student(first_name, last_name, new Index(index_num), Date.valueOf(date_of_birth),
                    city, jmbg, major_id);
            if (Database.AddStudent(new_student)) {
                return "Student was added";
            } else
                return "Database related error occurred; Student could not be added";
        } catch (Exception e) {
            e.printStackTrace();
            return "Student could not be added because of the following error: " + e.getMessage();
        }
    }

    // update student
    @GetMapping("/update_student")
    public String update_student(@RequestParam("student") String index_of_student_to_update,
            @RequestParam("first_name") String first_name, @RequestParam("last_name") String last_name,
            @RequestParam("index_num") String index_num, @RequestParam("date_of_birth") String date_of_birth,
            @RequestParam("city") String city, @RequestParam("jmbg") String jmbg,
            @RequestParam("major_id") String major_id) {
        try {
            Index req_index = new Index(index_of_student_to_update);
            Student student_to_update = Database.GetStudent(req_index);
            Student updated_student = new Student(req_index);

            if (first_name == "")
                updated_student.setFirstName(student_to_update.getFirstName());
            else updated_student.setFirstName(first_name);
            if (last_name == "")
                updated_student.setLastName(student_to_update.getLastName());
            else updated_student.setLastName(last_name);
            if (index_num != "")
                updated_student.setIndex(new Index(index_num));
            if (date_of_birth == "")
                updated_student.setDateOfBirth(student_to_update.getDateOfBirth());
            else updated_student.setDateOfBirth(Date.valueOf(date_of_birth));
            if (city == "")
                updated_student.setCity(student_to_update.getCity());
            else updated_student.setCity(city);
            if (jmbg == "")
                updated_student.setJmbg(student_to_update.getJmbg());
            else updated_student.setJmbg(jmbg);
            if (major_id == "")
                updated_student.setMajorId(student_to_update.getMajorId());
            else updated_student.setMajorId(Integer.parseInt(major_id));

            // Database.AddStudent(updated_student);
            return "Student was updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't update student because of the following error: " + e.getMessage();
        }
    }

    // delete student
    @GetMapping("/delete_student")
    public String delete_student(@RequestParam("index_num") String index) {
        return null;
    }

   //  // private methods
   private String[][] filter_students_from_database(Date date_of_birth[], String city[], String major[], int order_by, boolean ascending) {
       // get required data from database
       ArrayList<Student> requested_students = Database.GetStudents(date_of_birth, city, major, order_by , ascending);
       String[][] ret_s;

       // we will append data with bonus column only if we chose to sort by said column,
       // otherwise only first_name, last_name and index columns are used
       if (order_by != 0) {
           ret_s = new String[requested_students.size()][4];
           switch (order_by) {
               case 3:
                   for (int i = 0; i < ret_s.length; i++)
                       ret_s[i][3] = requested_students.get(i).getDateOfBirth().toString();
                   break;
               case 4:
                   for (int i = 0; i < ret_s.length; i++)
                       ret_s[i][3] = requested_students.get(i).getCity();
                   break;
               case 6:
                   for (int i = 0; i < ret_s.length; i++)
                       ret_s[i][3] = requested_students.get(i).getMajorname();
                   break;
           }
       }
       else ret_s = new String[requested_students.size()][3];

       for (int i = 0; i < ret_s.length; i++) {
           ret_s[i][0] = requested_students.get(i).getIndex().toString();
           ret_s[i][1] = requested_students.get(i).getFirstName();
           ret_s[i][2] = requested_students.get(i).getLastName();
       }

       return ret_s;
   }
}