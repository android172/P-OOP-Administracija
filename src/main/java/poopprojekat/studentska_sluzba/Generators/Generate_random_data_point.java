package poopprojekat.studentska_sluzba.Generators;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

import poopprojekat.studentska_sluzba.Database;
import poopprojekat.studentska_sluzba.Index;
import poopprojekat.studentska_sluzba.Lecturer;
import poopprojekat.studentska_sluzba.Student;

// Class for generating random data to be pushed to database
// INCLUDES:
// get_random_student   - method for generating random student object
// get_random_lecturer - method for generating random professor object
public class Generate_random_data_point {

    public static Student get_random_student() {
        LocalDate rng_d = Birthday_gen.get_random_birth_date();
        String jmbg = Birthday_gen.get_random_jmbg(rng_d);
        try {
            return new Student(Name_gen.get_random_first_name(), Name_gen.get_random_last_name(),
                    new Index(rng_d.getYear() + 19), Date.valueOf(rng_d), Name_gen.get_random_city(), jmbg, 1);
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
        return new Lecturer(Name_gen.get_random_first_name(), Name_gen.get_random_last_name(), title, Database.GetEmptyId("Lecturers"));
    }
}