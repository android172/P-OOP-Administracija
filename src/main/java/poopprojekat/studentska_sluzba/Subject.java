package poopprojekat.studentska_sluzba;

public class Subject
{
    // Obavezne za dodavanje
    public String subjectName;
    public String subjectId;        // npr: M333
    public int espb;
    public int year;
    public String lectid;
    public String majorid;
    //
    public String lectName;
    public String majorName;

    public Subject(String id)
    {
        subjectId = id;
    }

    public Subject(String subjectName, String id, int espb, int year, String profId, String majorId)
    {
        this.subjectName = subjectName;
        subjectId = id;
        this.espb = espb;
        this.year = year;
        this.lectid = profId;
        this.majorid = majorId;
        GetLectName();
        GetMajorName();
    }

    private void GetLectName()
    {
       Lecturer temp = Database.GetLecturer(lectid);
       lectName = temp.getFirstName() + " " + temp.getLastName();
    }

    private void GetMajorName()
    {
        majorName = Database.GetMajor(majorid).name;
    }

    @Override
    public String toString()
    {
        return subjectName + " " + subjectId + " ESPB: " + espb + " Year: " + year + " " + lectName + " " + majorName;
    }
}
