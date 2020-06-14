package poopprojekat.studentska_sluzba;

public class Subject
{
    public String subjectName;
    public String subjectId;        // npr: M333
    public int espb;
    public int year;
    public String prof;
    public String major;
    public int profid;
    public int majorid;

    public Subject(String id)
    {
        subjectId = id;
    }

    public Subject(String name, String id, int espb, int year, int p, int m)
    {
        subjectName = name;
        subjectId = id;
        this.espb = espb;
        this.year = year;
        profid = p;
        majorid = m;
    }

    @Override
    public String toString()
    {
        return subjectName + " " + subjectId + " ESPB: " + espb + " Year: " + year + " " + prof + " " + major;
    }
}
