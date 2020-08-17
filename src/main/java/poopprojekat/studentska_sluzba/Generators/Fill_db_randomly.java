package poopprojekat.studentska_sluzba.Generators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import poopprojekat.studentska_sluzba.Database;
import poopprojekat.studentska_sluzba.Exam;
import poopprojekat.studentska_sluzba.Index;
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
                // 
                int index_year = gen.getIndex().getYear();
                int years = LocalDate.now().getYear() - index_year + 1;
                int school_year = 1;
                boolean budget = true;
                for (int j = 0; j < years; j++) {
                    if (school_year > 4) break;
                    Database.AddStatus(gen.getIndex(), school_year, index_year + j, budget);
                    if (new Random().nextInt(100) < 65) {
                        school_year++;
                        if (new Random().nextInt(100) < 50) budget = !budget;
                    }
                    else budget = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void with_lecturers(int number) {
        try {
            for (int i = 0; i < number; i++) {
                Lecturer gen = Generate_random_data_point.get_random_lecturer();
                String username = gen.getLastName();
                username = username.replace("ć", "c");
                username = username.replace("č", "c");
                username = username.replace("ž", "z");
                username = username.replace("š", "s");
                username = username.replace("đ", "d");
                username = username.replace("Ć", "c");
                username = username.replace("Č", "c");
                username = username.replace("Ž", "z");
                username = username.replace("Š", "s");
                username = username.replace("Đ", "d");
                Database.AddLecturer(gen);
                Database.AddUser(new User(username + gen.getLectId(), gen.getLectId() + gen.getLectId(), "Lecturer",
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
            ArrayList<Student> students = Database.GetStudents(null, null, null, 1);
            for (int i = 0; i < number; i++) {
                Subject gen = Generate_random_data_point.get_random_subject();
                Database.AddSubject(gen);

                int subject_year = LocalDate.now().getYear() - gen.year + 1;
                ArrayList<Index> applied = new ArrayList<>();
                
                for (Student s : students) {
                    int index_year = s.getIndex().getYear();
                    if (subject_year >= index_year) {
                        int chance = 100;
                        for (int j = 0; j < subject_year - index_year; j++) chance *= 0.6;
                        if (new Random().nextInt(100) < chance) applied.add(s.getIndex());
                    }
                }
                Database.ApplyForSubject(applied.toArray(new Index[applied.size()]), gen.subjectId);
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
                                        new Exam(Database.GetEmptyId("Exams"), subject.subjectId, Generate_random_data_point.random_lect_id(),
                                                starting_date.plusDays(new Random().nextInt(days_between))));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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