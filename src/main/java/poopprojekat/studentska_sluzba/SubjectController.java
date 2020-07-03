package poopprojekat.studentska_sluzba;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class SubjectController {

    @GetMapping("/get_all_subjects")
    public ArrayList<Subject> get_all_subjects(){
        return Database.GetSubjects(null, null, null, null);
    }

    @GetMapping("/add_subject")
    public String add_subject(@RequestParam("name") String name,
                              @RequestParam("id") String id,
                              @RequestParam("espb") int espb,
                              @RequestParam("year") int year,
                              @RequestParam("lect_id") int lect_id,
                              @RequestParam("major_id") int major_id){
        return null;
    }
}
