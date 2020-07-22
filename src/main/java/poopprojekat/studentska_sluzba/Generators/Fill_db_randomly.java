package poopprojekat.studentska_sluzba.Generators;

import poopprojekat.studentska_sluzba.Database;
import poopprojekat.studentska_sluzba.Lecturer;
import poopprojekat.studentska_sluzba.Student;
import poopprojekat.studentska_sluzba.User;

public class Fill_db_randomly {
    
    public static void with_students(int number) {
        try {
            for (int i = 0; i < number; i++) {
                Student gen = Generate_random_data_point.get_random_student();
                Database.AddStudent(gen);
                Database.AddUser(new User(gen.getIndex().toString(), gen.getJmbg(), "Student"), gen.getIndex().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void with_lecturers(int number) {
        try {
            for (int i = 0; i < number; i++) {
                Lecturer gen = Generate_random_data_point.get_random_lecturer();
                Database.AddLecturer(gen);
                Database.AddUser(new User(gen.getLastName() + gen.getLectId(), gen.getLectId(), "Lecturer"), gen.getLectId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void with_majors(int number) {
        try {
            for (int i = 0; i < number; i++) {
                Database.AddMajor(Generate_random_data_point.get_random_major());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void with_Subjects(int number) {
        try {
            for (int i = 0; i < number; i++) {
                Database.AddSubject(Generate_random_data_point.get_random_subject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
