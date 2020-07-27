package poopprojekat.studentska_sluzba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.crypto.Data;
import java.util.ArrayList;


@SpringBootApplication
public class StudentskaSluzbaApplication
{
    public static void main(String[] args) throws Exception {
        SpringApplication.run(StudentskaSluzbaApplication.class, args);
        Database db = new Database("testing");


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

//        ArrayList<Student> lista;
//        lista = Database.GetStudents(null, null, new String[]{"M001", "M002"}, 6);
//
//        for (Student st:lista)
//        {
//            System.out.println(st);
//        }

//        ArrayList<Subject> subs = Database.GetSubjects(null, null, null, null);
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

//        System.out.println(Database.GetHighestIndex(2020));

//        System.out.println("Resenje: " + Database.GetEmptyId("Majors"));

//        System.out.println(Database.GetUser("1/2020", "0123456789123"));
//
//        Database.DeleteStudent(new Index("1/2020"));
//
//        System.out.println(Database.GetUser("1/2020", "0123456789123"));

//        ArrayList<Lecturer> lect = Database.GetLecturers(new String[]{"Predmet1"}, new String[]{"Smer1"});
//        for(Lecturer l: lect)
//        {
//            System.out.println(l);
//        }

//        ArrayList<String> temp = Database.GetAllCities();
//        for (String s:temp)
//        {
//            System.out.println(s);
//        }
//        System.out.println("--------------");
//        ArrayList<String> temp1 = Database.GetAllMajors();
//        for (String s:temp1)
//        {
//            System.out.println(s);
//        }
//        System.out.println("--------------");
//        ArrayList<String> temp2 = Database.GetAllSubjects();
//        for (String s:temp2)
//        {
//            System.out.println(s);
//        }

         //Database.DropDatabase("testing");

        //db.Close();
    }

}
