package poopprojekat.studentska_sluzba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class StudentskaSluzbaApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(StudentskaSluzbaApplication.class, args);

        Database db = new Database();

//        try
//        {
//            db.TestDummy();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }

//        Subject s = new Subject("M002");
//        s = Database.GetSubject(s);
//        System.out.println(s);
//
//        Professor p = new Professor(2);
//        p = Database.GetProfessor(p);
//        System.out.println(p);
//
//        Student stud = new Student("1/2020");
//        stud = Database.GetStudent(stud);
//        System.out.println(stud);

//        List<Student> lista;
//        lista = Database.GetFilteredStudents(null, null, "Smer1");
//
//        for (Student st:lista)
//        {
//            System.out.println(st);
//        }

//        ArrayList<Subject> subs = Database.GetSubjects(null, 0, "Professor1 Profesanovic1", null);
//
//        for(Subject s: subs)
//        {
//            System.out.println(s);
//        }

//        ArrayList<Major> majors = Database.GetMajors("Smer1");
//        for(Major m: majors)
//        {
//            System.out.println(m);
//        }

//        Major m = Database.GetMajor(3);
//        System.out.println(m);

//        Professor p = Database.GetProfessor(1);
//        p.GetSubjects();
//        System.out.println(p);

        Generate_random_data_point.get_random_professor();
    }

}
