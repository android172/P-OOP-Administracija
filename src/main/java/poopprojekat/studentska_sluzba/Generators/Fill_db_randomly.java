package poopprojekat.studentska_sluzba.Generators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import poopprojekat.studentska_sluzba.Database;
import poopprojekat.studentska_sluzba.Exam;
import poopprojekat.studentska_sluzba.Lecturer;
import poopprojekat.studentska_sluzba.Student;
import poopprojekat.studentska_sluzba.Subject;
import poopprojekat.studentska_sluzba.User;

public class Fill_db_randomly {

    public static void with_students(int number) {
        try {
            for (int i = 0; i < number; i++) {
                Student gen = Generate_random_data_point.get_random_student();
                Database.AddStudent(gen);
                Database.AddUser(
                        new User(gen.getIndex().toString(), gen.getJmbg(), "Student", gen.getIndex().toString()));
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
                Database.AddUser(new User(gen.getLastName() + gen.getLectId(), gen.getLectId(), "Lecturer",
                        gen.getLectId().toString()));
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

    public static void with_exams(int number) {
        String []deadline_possibilities = { "Januarski", "Februarski", "Martovski", "Aprilski", "Majski",
                "Junski", "Julski", "Avgustovski", "Septembarski", "Oktobarski", "Novembarski", "Decembarski" };
        boolean used[] = new boolean[12];
        Arrays.fill(used, false);
        int j;
        for (int i = 0; i < number; i++) {
            j = new Random().nextInt(12);
            if (!used[j]) used[j] = true;
            else i--;
        }

        LocalDate now = LocalDate.now();
        int this_year = now.getYear();
        LocalDate starting_date = LocalDate.of(this_year, 1, 1);
        int days_between;
        LocalDate ending_date;

        ArrayList<Subject> subjects = Database.GetSubjects(null, null, null, 0);
        int active_chance = 90;

        int exam_id = 1;

        for (int i = 0; i < deadline_possibilities.length; i++) {
            if (used[i]) {
                String deadline = deadline_possibilities[i];
                starting_date = starting_date.plusDays(new Random().nextInt(26));
                days_between = new Random().nextInt(8) + 14;
                ending_date = starting_date.plusDays(days_between);
                try {
                    Database.AddExamDeadline(deadline, starting_date, ending_date, starting_date.minusDays(6),
                            starting_date.minusDays(3));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (now.isAfter(starting_date.minusDays(10)) && now.isBefore(ending_date)) {
                    for (Subject subject : subjects) {
                        if (new Random().nextInt(100) < active_chance) {
                            try {
                                Database.AddExam(
                                        new Exam("E" + exam_id, subject.subjectId, Generate_random_data_point.random_lect_id(),
                                                starting_date.plusDays(new Random().nextInt(days_between))));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            exam_id++;
                        }
                    }
                }

                if (ending_date.isAfter(LocalDate.of(this_year, (i+2) % 13, 1))) starting_date = ending_date;
                else starting_date = LocalDate.of(this_year, (i+2) % 13, 1);
            }
            else starting_date = LocalDate.of(this_year, (i+2) % 13, 1);
        }
    }
}