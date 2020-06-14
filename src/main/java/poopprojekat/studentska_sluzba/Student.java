package poopprojekat.studentska_sluzba;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Student
{
    public String firstName;
    public String lastName;
    public String indexNum;         //  br/god
    public Date dateOfBirth;        // Godina rodjenja - 1900 (zbog tabele)
    public String city;
    public String jmbg;
    public List<Subject> listen = new ArrayList<>();

    public Student(String brind)
    {
        indexNum = brind;
    }

    public Student(String fname, String lname, String brind, Date dob, String city, String jmbg)
    {
        firstName = fname;
        lastName = lname;
        indexNum = brind;
        dateOfBirth = dob;
        this.city = city;
        this.jmbg = jmbg;
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
        return firstName + " " + lastName + " " + indexNum + " " + dateOfBirth + " From:" + city + " JMBG: " + jmbg;
    }
}
