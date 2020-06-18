package poopprojekat.studentska_sluzba;

import java.util.ArrayList;

public class Lecturer
{
    // Obavezne za dodavanje
    public String firstName;
    public String lastName;
    public String title;
    public int lectId;
    //
    ArrayList<Subject> teach = new ArrayList<>();

    public Lecturer(int id) // za pretragu baze
    {
        lectId = id;
    }

    public Lecturer(String fname, String lname, String title, int id)        // za unos u bazu
    {
        firstName = fname;
        lastName = lname;
        lectId = id;
        this.title = title;
    }

    public void GetSubjects()
    {
        teach = Database.SubjectsOfLecturer(this);
    }

    @Override
    public String toString()
    {
        String s = firstName + " " + lastName + " ID:" + lectId + "\nTeaches:";

        for (int i = 0; i < teach.size(); i++)
        {
            s += "\n--";
            s += teach.get(i);
        }

        return s;
    }
}
