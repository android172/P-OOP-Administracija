package poopprojekat.studentska_sluzba;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Student
{
    //Obavezne za dodavanje
    public String firstName;
    public String lastName;
    public Index index;
    public Date dateOfBirth;        // Godina rodjenja - 1900 (zbog tabele)
    public String city;
    public String jmbg;
    public String majorname;
    //
    public int majorId;
    public List<Subject> listen = new ArrayList<>();

    public Student(Index brind)
    {
        index = brind;
    }

    public Student(String fname, String lname, Index brind)        // za vracanje iz baze za spisak studenata
    {
        firstName = fname;
        lastName = lname;
        index = brind;
    }

    public Student(String fname, String lname, Index brind, Date dob, String city, String jmbg, int majorId)       // Za dodavanje studenta u bazu
    {
        this(fname, lname, brind);
        dateOfBirth = dob;
        this.city = city;
        this.jmbg = jmbg;
        this.majorId = majorId;
        GetMajorName();
    }
    public void ApplyToListen(Subject s)
    {
        listen.add(s);
    }

    public void GetAppliedSubjects()
    {

    }

    public void GetMajorName()
    {
        majorname = (Database.GetMajor(majorId)).name;
    }

    @Override
    public String toString()
    {
        return firstName + " " + lastName + " " + index + " " + dateOfBirth + " From:" + city + " JMBG: " + jmbg + " Smer: " + majorname;
    }
}
