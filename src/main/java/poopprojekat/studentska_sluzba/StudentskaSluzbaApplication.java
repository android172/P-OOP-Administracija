package poopprojekat.studentska_sluzba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.crypto.Data;
import java.util.List;

@SpringBootApplication
public class StudentskaSluzbaApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(StudentskaSluzbaApplication.class, args);

        Database db = new Database();

        db.TestDummy();

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

        List<Student> lista;
        lista = Database.GetFilteredStudents(null, null, "Smer1");

        for (Student st:lista)
        {
            System.out.println(st);
        }
    }

}
