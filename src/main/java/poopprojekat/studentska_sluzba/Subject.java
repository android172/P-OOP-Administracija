package poopprojekat.studentska_sluzba;

public class Subject
{
    // Obavezne za dodavanje
    public String subjectName;
    public String subjectId;        // npr: M333
    public int espb;
    public int year;
    public int lectid;
    public int majorid;
    //
    public String prof;
    public String major;

    public Subject(String id)
    {
        subjectId = id;
    }

    public Subject(String subjectName, String id, int espb, int year, int profId, int majorId)
    {
        this.subjectName = subjectName;
        subjectId = id;
        this.espb = espb;
        this.year = year;
        this.lectid = profId;
        this.majorid = majorId;
    }

    @Override
    public String toString()
    {
        return subjectName + " " + subjectId + " ESPB: " + espb + " Year: " + year + " " + prof + " " + major;
    }
}
