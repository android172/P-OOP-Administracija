package poopprojekat.studentska_sluzba;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Exam_Controller {

    @GetMapping("/get_exam_deadlines")
    public ArrayList<String> get_exam_deadlines(@RequestParam("token") long token) {
        if (Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return Database.GetExamDeadlines(null);
        return Database.GetExamDeadlines(LocalDate.now());
    }

    @GetMapping("/add_exam_deadline")
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

    @GetMapping("/delete_exam_deadline")
    public String delete_exam_deadline(@RequestParam("token") long token, @RequestParam("name") String name) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        try {
            Database.DeleteExamDeadline(name);
            return "Exam deadline was successfully removed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exam deadline was not removed due to database related issue: " + e.getMessage();
        }
    }

    @GetMapping("/get_all_exams")
    public ArrayList<Exam> get_all_exams(@RequestParam("token") long token) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } }))
            return null;
        return Database.GetAllExams();
    }

    @GetMapping("/get_exams")
    public ArrayList<Exam> get_exams(@RequestParam("token") long token, @RequestParam("student") String index,
            @RequestParam("exam_deadline") String exam_deadline) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" }, { "Student", index } }))
            return null;
        try {
            return Database.GetAvailableExams(new Index(index), exam_deadline);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value = "add_exam")
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

    @GetMapping(value = "delete_exam")
    public String delete_exam(@RequestParam("token") long token, @RequestParam("exam_id") String id) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Admin", "any" } })) return null;
        try {
            Database.DeleteExam(id);
            return "Exam was successfully deleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "Exam could not be deleted because of the following error: " + e.getMessage();
        }
    }

    @GetMapping("/apply_for_exam")
    public String apply_for_exam(@RequestParam("token") long token, @RequestParam("student") String index,
            @RequestParam("exam") String exam_id, @RequestParam("payed") int payed) {
        if (!Log_in_Controller.access_allowed(token, new String[][] { { "Student", index } })) return null;
        
        try {
            if (Database.IfBudget(new Index(index), LocalDate.now().getYear()) == false
                    || Database.GetAttempts(new Index(index), exam_id) > 1) {
                if (payed != 2000) return "Payed amount is incorrect";
            } else if (payed != 0) return "Payed amount is incorrect";

            Database.ApplyForExam(new Index(index), exam_id, payed);
            return "The application has been completed successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Application was not completed because of the following exception: " + e.getMessage();
        }
    }

    // Student Controller
    // TO DO: Daj sve studente sa datog predmeta
    // TO DO: Upisi datog studenta na dati niz predmeta

    // Subject Controller
    // TO DO: Daj sve predmete na datom smeru
    // TO DO: Daj sve predmete koje slusa dati ucenik (Svi upisani predmeti)
}