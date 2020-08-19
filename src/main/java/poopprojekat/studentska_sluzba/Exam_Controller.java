package poopprojekat.studentska_sluzba;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Exam_Controller {

    @PostMapping("/get_exam_dropdown")
    public ArrayList<String> get_exam_dropdown(@RequestParam("token") long token) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        return Database.GetAllLecturers();
    }

    @PostMapping("/get_exam_deadlines")
    public ArrayList<ExamDeadline> get_exam_deadlines(@RequestParam("token") long token) {
        if (Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return Database.GetExamDeadlines(null);
        return Database.GetExamDeadlines(LocalDate.now());
    }

    @PostMapping("/add_exam_deadline")
    public String add_exam_deadline(@RequestParam("token") long token, @RequestParam("name") String name,
            @RequestParam("start_date") String start_date, @RequestParam("end_date") String end_date,
            @RequestParam("start_app_date") String start_app_date, @RequestParam("end_app_date") String end_app_date) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        try {
            Database.AddExamDeadline(name, LocalDate.parse(start_date), LocalDate.parse(end_date),
                    LocalDate.parse(start_app_date), LocalDate.parse(end_app_date));
            return "Exam deadline was successfully added";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exam deadline was not added due to database related issue: " + e.getMessage();
        }
    }

    @PostMapping("/delete_exam_deadline")
    public String delete_exam_deadline(@RequestParam("token") long token, @RequestParam("name") String name) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } })) return null;
        try {
            Database.DeleteExamDeadline(name);
            return "Exam deadline was successfully removed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exam deadline was not removed due to database related issue: " + e.getMessage();
        }
    }

    @PostMapping("/get_all_exams")
    public ArrayList<Exam> get_all_exams(@RequestParam("token") long token) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        return Database.GetAllExams();
    }

    @PostMapping("/get_exams")
    public ArrayList<Exam> get_exams(@RequestParam("token") long token, @RequestParam("student") String index,
            @RequestParam("exam_deadline") String exam_deadline) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Student", index } }))
            return null;
        try {
            return Database.GetAvailableExams(new Index(index), exam_deadline);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/get_exams_for")
    public ArrayList<Exam> get_exams(@RequestParam("token") long token, @RequestParam("student") String index) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Student", index } }))
            return null;
        try {
            return Database.GetAvailableExams(new Index(index), null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("get_exams_for_lecturer")
    public ArrayList<Exam> get_exams_for_lecturer(@RequestParam("token") long token,
            @RequestParam("lecturer_id") String lect_id) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Lecturer", lect_id } }))
            return null;
        try {
            return Database.GetExamOfLect(lect_id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(value = "/add_exam")
    public String add_exam(@RequestParam("token") long token, @RequestParam("subject_id") String subject_id,
            @RequestParam("lect_id") String lect_id, @RequestParam("Date") String date) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;

        try {
            Database.AddExam(new Exam(Database.GetEmptyId("Exams"), subject_id, lect_id, LocalDate.parse(date)));
            return "Exam was successfully added";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exam could not be added because of the following error: " + e.getMessage();
        }
    }

    @PostMapping(value = "/delete_exam")
    public String delete_exam(@RequestParam("token") long token, @RequestParam("exam_id") String id) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        try {
            Database.DeleteExam(id);
            return "Exam was successfully deleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exam could not be deleted because of the following error: " + e.getMessage();
        }
    }

    @PostMapping("/apply_for_exam")
    public String apply_for_exam(@RequestParam("token") long token, @RequestParam("student") String index,
            @RequestParam("exam") String exam_id, @RequestParam("payed") int payed) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Student", index } }))
            return null;

        try {
            if (Database.IfBudget(new Index(index), LocalDate.now().getYear()) == false
                    || Database.GetAttempts(new Index(index), exam_id) > 1) {
                if (payed != 2000)
                    return "Payed amount is incorrect";
            } else if (payed != 0)
                return "Payed amount is incorrect";

            Database.ApplyForExam(new Index(index), exam_id, payed);
            return "The application has been completed successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Application was not completed because of the following exception: " + e.getMessage();
        }
    }

    @GetMapping("/budget")
    public boolean is_budget(@RequestParam("token") long token, @RequestParam("index") String index) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Student", index }, { "Admin", "any" } })) {
            System.out.println("access denied :: Exam_Controller.java :: line:~147");
            return false;
        }
        try {
            return Database.IfBudget(new Index(index), LocalDate.now().getYear());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/grade_student")
    public String grade_student(@RequestParam("token") long token, @RequestParam("index") String index,
            @RequestParam("exam_id") String exam_id, @RequestParam("points") String points,
            @RequestParam("grade") String grade) {
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
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Lecturer", lect_id } }))
            return null;

        String index_as[] = index.split("\\+");
        String exam_id_array[] = exam_id.split("\\+");
        String grade_as[] = grade.split("\\+");
        String points_as[] = points.split("\\+");
        int length = index_as.length;

        Index index_array[] = new Index[length];
        int grade_array[] = new int[length];
        int points_array[] = new int[length];

        try {
            for (int i = 0; i < index_array.length; i++) {
                index_array[i] = new Index(index_as[i]);
                grade_array[i] = Integer.parseInt(grade_as[i]);
                points_array[i] = Integer.parseInt(points_as[i]);
            }
            Database.Grading(index_array, LocalDate.now().getYear(), exam_id_array, grade_array, points_array);
            return "Student grading successful";
        } catch (Exception e1) {
            e1.printStackTrace();
            return "Grading failed due to the following error: " + e1.getMessage();
        }
    }

    // Subject Controller
    // TO DO: Daj sve predmete na datom smeru
    // TO DO: Daj sve predmete koje slusa dati ucenik (Svi upisani predmeti)
}