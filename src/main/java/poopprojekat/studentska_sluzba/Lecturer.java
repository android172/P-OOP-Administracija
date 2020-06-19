package poopprojekat.studentska_sluzba;

import java.util.ArrayList;

public class Lecturer
{
    // Obavezne za dodavanje
    private String firstName;
    private String lastName;
    private String title;
    private int lectId;
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

    // getters
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getLectId() {
        return lectId;
    }
    public String getTitle() {
        return title;
    }
    public ArrayList<Subject> getSubjects() {
        return teach;
    }

    // setters
    public void setSubjects() {
        teach = Database.SubjectsOfLecturer(this);
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setLectId(int lectId) {
        this.lectId = lectId;
    }
    public void setTitle(String title) {
        this.title = title;
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
