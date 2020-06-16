package poopprojekat.studentska_sluzba;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Student
{
    //Obavezne za dodavanje
    public String firstName;
    public String lastName;
    public String indexNum;         //  br/god
    public Date dateOfBirth;        // Godina rodjenja - 1900 (zbog tabele)
    public String city;
    public String jmbg;
    public String majorname;
    //
    public int majorId;
    public List<Subject> listen = new ArrayList<>();

    public Student(String brind)
    {
        indexNum = brind;
    }

    public Student(String fname, String lname, String brind)
    {
        firstName = fname;
        lastName = lname;
        indexNum = brind;
    }

    public Student(String fname, String lname, String brind, Date dob, String city, String jmbg, int majorId)
    {
        this(fname, lname, brind);
        dateOfBirth = dob;
        this.city = city;
        this.jmbg = jmbg;
        this.majorId = majorId;
    }
    public void ApplyToListen(Subject s)
    {
        listen.add(s);
    }

    public void GetAppliedSubjects()
    {

    }

    @Override
    public String toString()
    {
        return firstName + " " + lastName + " " + indexNum + " " + dateOfBirth + " From:" + city + " JMBG: " + jmbg + " Smer: " + majorname;
    }
}
