package poopprojekat.studentska_sluzba;

public class Lecturer
{
    // Obavezne za dodavanje
    private String firstName;
    private String lastName;
    private String title;
    private String lectId;

    public Lecturer(String id) // za pretragu baze
    {
        lectId = id;
    }

    public Lecturer(String fname, String lname, String title, String id)        // za unos u bazu
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
    public String getLectId() {
        return lectId;
    }
    public String getTitle() {
        return title;
    }

    // setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setLectId(String lectId) {
        this.lectId = lectId;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return firstName + " " + lastName + " ID:" + lectId + "\nTeaches:";
    }
}
