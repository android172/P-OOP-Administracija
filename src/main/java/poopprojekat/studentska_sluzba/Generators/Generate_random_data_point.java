package poopprojekat.studentska_sluzba.Generators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import poopprojekat.studentska_sluzba.Database;
import poopprojekat.studentska_sluzba.Index;
import poopprojekat.studentska_sluzba.Lecturer;
import poopprojekat.studentska_sluzba.Major;
import poopprojekat.studentska_sluzba.Student;
import poopprojekat.studentska_sluzba.Subject;

// Class for generating random data to be pushed to database
// INCLUDES:
// get_random_student   - method for generating random student object
// get_random_lecturer - method for generating random professor object
public class Generate_random_data_point {

    // Class cannot be initialized
    private Generate_random_data_point() {
    }

    public static Student get_random_student() {
        LocalDate rng_d = Birthday_gen.get_random_birth_date();
        String jmbg = Birthday_gen.get_random_jmbg(rng_d);
        try {
            return new Student(Name_gen.get_random_first_name(), Name_gen.get_random_last_name(),
                    new Index(rng_d.getYear() + 19), rng_d, Name_gen.get_random_city(), jmbg,
                    random_major_id());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Lecturer get_random_lecturer() {
        String title;
        switch (new Random().nextInt(4)) {
            case 0:
                title = "full-time professor";
                break;
            case 1:
                title = "part-time professor";
            case 2:
                title = "docent";
            default:
                title = "assistant";
        }
        try {
            return new Lecturer(Name_gen.get_random_first_name(), Name_gen.get_random_last_name(), title, Database.GetEmptyId("Lecturers"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Major get_random_major() {
        try {
            return new Major(Database.GetEmptyId("Majors"), Major_gen.get_random_major());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Subject get_random_subject() {
        try {
            int req[] = {0, 26, 26, 26, 26, 36, 36};
            return new Subject(Subject_gen.get_random_subject(), Database.GetEmptyId("Subjects"),
                    new Random().nextInt(3) + 4, new Random().nextInt(4) + 1, random_lect_id(), random_major_id(), req[new Random().nextInt(7)]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String random_lect_id() {
        ArrayList<Lecturer> lecturers = Database.GetLecturers(null, null, 1);
        return lecturers.get(new Random().nextInt(lecturers.size())).getLectId();
    }

    private static String random_major_id() {
        ArrayList<Major> majors = Database.GetMajors(null);
        return majors.get(new Random().nextInt(majors.size())).id;
    }
}