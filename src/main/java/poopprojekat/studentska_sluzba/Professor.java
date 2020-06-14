package poopprojekat.studentska_sluzba;

import java.util.ArrayList;
import java.util.List;

public class Professor
{

    public String firstName;
    public String lastName;
    public int profId;
    List<Subject> teach = new ArrayList<>();

    public Professor(int id)        // za pretragu baze
    {
        profId = id;
    }

    public Professor(String fname, String lname, int id)        // za unos u bazu
    {
        firstName = fname;
        lastName = lname;
        profId = id;
    }

    public void GetSubjects()
    {
        teach = Database.SubjectsOfProfessor(this);
    }

    @Override
    public String toString()
    {
        String s = firstName + " " + lastName + " " + profId;


        for (int i = 0; i< teach.size(); i++)
        {
            s += "\n";
            s += teach.get(i);
        }

        return s;
    }
}
