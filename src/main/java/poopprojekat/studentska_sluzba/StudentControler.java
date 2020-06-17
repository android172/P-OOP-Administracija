//package poopprojekat.studentska_sluzba;
//
//import java.sql.Date;
//import java.util.ArrayList;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//
//// Controller for student list related requests
//// INCLUDES:
//// /students                                                    - returns list of students (index, first_name, last_name)
//// /students?date_of_birth=...&city=...&mayor=...&order_by=...  - returns filtered list of students (index, first_name,
////                                                                last_name, ?column by which we order data)
//// /student?index=...                                           - returns student with given index
//// /student?jmbg=...                                            - returns student with given jmbg
//@RestController
//public class StudentControler {
//
//    //  // public methods
//    // per page load we send full list of students including only index numbers,
//    // first and last names
//    @GetMapping("/students")
//    public String[][] get_students() {
//        return filter_students_from_database(null, null, null, 0, true);
//    }
//
//    // returns filtered and ordered list of students
//    @GetMapping("/students")
//    public String[][] get_students(@RequestParam("date_of_birth") String date_of_birth,
//            @RequestParam("city") String city, @RequestParam("major") String major,
//            @RequestParam("orderby") String order_by) {
//
//        // format picked dates
//        Date dates[] = null;
//        if (!date_of_birth.equals("all")) {
//            String sd[] = date_of_birth.split("+");
//            dates = new Date[sd.length];
//            for (int i = 0; i < sd.length; i++)
//                dates[i] = Date.valueOf(sd[i]);
//        }
//        // format picked cities
//        String cities[] = null;
//        if (!city.equals("all"))
//            cities = city.split("+");
//        // format picked majors
//        String majors[] = null;
//        if (!major.equals("all"))
//            majors = major.split("+");
//        // format order by
//        int order_ctg;
//        switch (order_by.split("-")[0]) {
//            case "birthyear":
//                order_ctg = 3;
//                break;
//            case "city":
//                order_ctg = 4;
//                break;
//            case "mayor":
//                order_ctg = 6;
//                break;
//            default:
//                order_ctg = 0;
//        }
//        boolean asc = true;
//        if (order_by.split("-")[1] == "des")
//            asc = false;
//
//        return filter_students_from_database(dates, cities, majors, order_ctg, asc);
//    }
//
//    // return selected student
//    @GetMapping("/student")
//    public Student get_student(@RequestParam("index") String index) {
//        Index i;
//        try {
//            i = new Index(index);
//            return Database.GetStudent(i);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    @GetMapping("/student")
//    public Student get_student_wj(@RequestParam("jmbg") String jmbg) {
//        return Database.GetStudent(jmbg);
//    }
//
//    //  // private methods
//    private String[][] filter_students_from_database(Date date_of_birth[], String city[], String major[], int order_by, boolean ascending) {
//        // get required data from database
//        ArrayList<Student> requested_students = Database.GetStudents(date_of_birth[0], city[0], major[0] /*, orderby , asc*/);
//        String[][] ret_s;
//
//        // we will append data with bonus column only if we chose to sort by said column,
//        // otherwise only first_name, last_name and index columns are used
//        if (order_by != 0) {
//            ret_s = new String[requested_students.size()][4];
//            switch (order_by) {
//                case 3:
//                    for (int i = 0; i < ret_s.length; i++)
//                        ret_s[i][3] = requested_students.get(i).dateOfBirth.toString();
//                    break;
//                case 4:
//                    for (int i = 0; i < ret_s.length; i++)
//                        ret_s[i][3] = requested_students.get(i).city;
//                    break;
//                case 6:
//                    for (int i = 0; i < ret_s.length; i++)
//                        ret_s[i][3] = requested_students.get(i).majorname;
//                    break;
//            }
//        }
//        else ret_s = new String[requested_students.size()][3];
//
//        for (int i = 0; i < ret_s.length; i++) {
//            ret_s[i][0] = requested_students.get(i).index.toString();
//            ret_s[i][1] = requested_students.get(i).firstName;
//            ret_s[i][2] = requested_students.get(i).lastName;
//        }
//
//        return ret_s;
//    }
//}