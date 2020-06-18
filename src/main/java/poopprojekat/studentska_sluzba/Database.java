package poopprojekat.studentska_sluzba;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Database class Includes:
// DropDatabase(String name) - Brise bazu
// AddStudent(Student s) - prima studenta i ubacuje u bazu. Pogledati obavezne promenjive u Student
// AddSubject(Subject s) - prima predmet i ubacuje u bazu. Pogledati obavezne promenjive u Subject
// AddLecturer(Lecturer p) - prima Lecturer i ubacuje u bazu. Pogledati obavezne promenjive u Lecturer
// AddMajor(Major m) - prima Major i ubacuje u bazu. Pogledati obavezne promenjive u Major
// sve Add f-je vracaju true ako je uspesno dodalo, false ako nije
// GetStudents(Date dateOfBirth, String city, String MajorName, int orderBy, boolean ascending) - prima DoB/City/MajorName,
//                                      orderby: 3 - sortira po DoB 4 - sortira po City, 6 - sortira po MajorId,
//                                      ascending - da li sortira od manjeg ka vecem ili obrnuto (vrednost nije bitna ako orderby nije 3,4 ili 6)
//                                      f-ja vraca ArrayList<Student>
// GetStudent(String jmbg) - prima jmbg studenta, vraca Student
// GetStudent(Index index) - prima index studenta, vraca Student
// GetSubjets(String subjectName, int year, String profName, String majorName) - prima 1 ili vise parametra (ostali 0 ili null)
//                                                                               i pretrazuje ih, Vraca ArrayList<Subject>
// GetSubject(String SubjectId) - prima id predmeta, vraca Subject
// GetLecturer(int profId) - prima id profesora, vraca Lecturer
// GetMajors(String majorName) - prima ime smera, vraca ArrayList<Major>
// GetMajor(int majorId) - prima id smera, vraca Major
// SubjectsOfLecturer(Lecturer p) - prima profesora i vraca sve predmete na kojima predaje. vraca ArrayList<Subjects>
// GetHighestIndex(int year) - Vraca najveci br indeksa za zadatu godinu
// GetEmptyId(tableName) - prima tabelu "Lecturers" ili "Majors" i vraca 1. slobodan Id

public class Database
{
    private static Connection conn = null;
    private static Statement stat = null;
    private static String sql = null;

    public Database()       // First time run
    {
        ConnectToDatabase("");

        CreateDatabase("Testing");

    }

    public void TestDummy() throws Exception
    {
        CreateDatabase("Testing");

        Student s = new Student("Student1", "Jedanovic", new Index("1/2020"), Date.valueOf("2000-1-1"), "Prvogradic", "0123456789123", 1);
        AddStudent(s);
        s = new Student("Student2", "Drugovanovic", new Index("2/2020"), Date.valueOf("2000-2-1"), "Drugogradic", "1234567890123", 1);
        AddStudent(s);
        s = new Student("Student3", "Trecanovic", new Index("3/2020"), Date.valueOf("2000-3-1"), "Prvogradic", "2223334448882", 2);
        AddStudent(s);
        s = new Student("Student3", "Cetvrtanovic", new Index("4/2020"), Date.valueOf("2000-4-1"), "Prvogradic", "2223434498882", 1);
        AddStudent(s);
        s = new Student("Student3", "Petic", new Index("5/2020"), Date.valueOf("2000-5-1"), "Trecegradic", "2223374446882", 2);
        AddStudent(s);
        s = new Student("Student3", "Sestic", new Index("6/2020"), Date.valueOf("2000-6-1"), "Drugogradic", "2223934448822", 2);
        AddStudent(s);

        Lecturer p = new Lecturer("Lecturer1", "Profesanovic1", 1);
        AddLecturer(p);
        p = new Lecturer("Lecturer2", "Profesanovic2", 2);
        AddLecturer(p);
        p = new Lecturer("Lecturer3", "Profesanovic3", 3);
        AddLecturer(p);

        Subject sub = new Subject("Predmet1", "M001", 7, 1, 2, 1);
        AddSubject(sub);
        sub = new Subject("Predmet2", "M002", 6, 3, 1, 2);
        AddSubject(sub);
        sub = new Subject("Predmet3", "M003", 4, 1, 3, 1);
        AddSubject(sub);

        Major m = new Major(1, "Smer1");
        AddMajor(m);
        m = new Major(2, "Smer2");
        AddMajor(m);
        m = new Major(3, "Smer2");
        AddMajor(m);
    }

    public void ConnectToDatabase(String name)
    {
        System.out.println("Connecting to database");

        try
        {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost/" + name, "root", "");
            stat = conn.createStatement();

            System.out.println("Connection successful");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1049)       // Database doesn't exsist
            {
                System.out.println("Database doesn't exist");
            }
            else        // other errors
            {
                System.out.println("Connecting failed");
                throwables.printStackTrace();
            }

        }

    }

    public void CreateDatabase(String name)     //  Creates full database for the project
    {
        sql = "CREATE DATABASE " + name;

        System.out.println("Creating database");

        try
        {
            stat.executeUpdate(sql);

            System.out.println("Database " + name + " has been created");
            ConnectToDatabase(name);

            CreateTableStudents();
            CreateTableLecturers();
            CreateTableMajors();
            CreateTableUsers();
            CreateTableSubjects();
            CreateTableExams();
            CreateTableExamApplications();
            CreateTableAppliedToListen();
            CreateTableArchive();
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1007)       // baza postoji (izbrisati "IF NOT EXISTS")
            {
                System.out.println("Database already exists");
                ConnectToDatabase(name);
            }


            else        // Other errors
            {
                System.out.println("Creating database failed");
                throwables.printStackTrace();
            }

        }
    }

    private void CreateTableUsers()
    {
        sql = "CREATE TABLE IF NOT EXISTS Users " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "Username VARCHAR(30) not NULL," +      // ind/god ili ind-god
                "Password VARCHAR(30) not NULL," +
                "Role VARCHAR(10) not NULL," +          // Student, Profesor, Admin
                "PRIMARY KEY (id, Username)," +
                "FOREIGN KEY (Username) REFERENCES Students (IndexNum) )";

        System.out.println("Creating table 'Users'");

        try
        {
            stat.execute(sql);
            System.out.println("Table 'Users' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'Users' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableSubjects()
    {
        sql = "CREATE TABLE IF NOT EXISTS Subjects " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "SubjectName VARCHAR(30) not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "Year INTEGER not NULL," +
                "ESPB INTEGER not NULL," +
                "MajorId INTEGER not NULL," +
                "ProfId INTEGER not NULL," +
                "PRIMARY KEY (id, SubjectId)," +
                "FOREIGN KEY (MajorId) REFERENCES Majors (MajorId)," +
                "FOREIGN KEY (ProfId) REFERENCES Lecturers (ProfId) )";

        System.out.println("Creating table 'Subjects'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'Subjects' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'Subjects' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableStudents()
    {
        sql = "CREATE TABLE IF NOT EXISTS Students " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "IndexNum VARCHAR(10) not NULL," +
                "FirstName VARCHAR(30) not NULL," +
                "LastName VARCHAR(30) not NULL," +
                "JMBG VARCHAR(15) not NULL," +
                "DateOfBirth DATE not NULL," +
                "City VARCHAR(15) not NULL," +
                "MajorId INTEGER not NULL, " +
                "PRIMARY KEY (id, JMBG, IndexNum), " +
                "FOREIGN KEY (MajorId) REFERENCES Majors(MajorId))";

        System.out.println("Creating table 'Students'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'Students' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'Students' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableAppliedToListen()
    {
        sql = "CREATE TABLE IF NOT EXISTS AppliedToListen " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "IndexNum VARCHAR(10) not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "Year INTEGER not NULL," +      // Na kojoj godini je predmet
                "PRIMARY KEY (id, IndexNum, SubjectId) )";

        System.out.println("Creating table 'AppliedToListen'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'AppliedToListen' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'AppliedToListen' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableExams()
    {
        sql = "CREATE TABLE IF NOT EXISTS Exams " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "ExamDate DATE not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "LecturerId INTEGER not NULL," +
                "PRIMARY KEY (id, ExamDate, SubjectId) )";

        System.out.println("Creating table 'Exams'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'Exams' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'Exams' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableExamApplications()
    {
        sql = "CREATE TABLE IF NOT EXISTS ExamApplication " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "IndexNum VARCHAR(10) not NULL," +
                "ExamDate DATE not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "PRIMARY KEY (id, IndexNum, ExamDate, SubjectId) )";

        System.out.println("Creating table 'ExamApplication'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'ExamApplication' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'ExamApplication' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableArchive()
    {
        sql = "CREATE TABLE IF NOT EXISTS ExamsArchive " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "IndexNum VARCHAR(10) not NULL," +
                "ExamDate DATE not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "Mark INTEGER not NULL," +
                "Show VARCHAR(5) not NULL," +       // Pojavio se ili ne
                "Status VARCHAR(8) not NULL," +     // Passed/Failed
                "PRIMARY KEY (id, IndexNum, ExamDate, SubjectId) )";

        System.out.println("Creating table 'ExamsArchive'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'ExamsArchive' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'ExamsArchive' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableLecturers()
    {
        sql = "CREATE TABLE IF NOT EXISTS Lecturers " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "ProfId INTEGER not NULL," +
                "FirstName VARCHAR(15) not NULL," +
                "LastName VARCHAR(15) not NULL," +
                "PRIMARY KEY (id, ProfId) )";

        System.out.println("Creating table 'ExamsArchive'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'ExamsArchive' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'ExamsArchive' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableMajors()
    {
        sql = "CREATE TABLE IF NOT EXISTS Majors " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "MajorId INTEGER not NULL," +
                "MajorName VARCHAR(30) not NULL," +
                "PRIMARY KEY(id, MajorId) )";

        System.out.println("Creating table 'Majors'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'Majors' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'Majors' failed");
            throwables.printStackTrace();
        }
    }

    // End of Create -----------------------------------------------

    public static void DropDatabase(String name)
    {
        sql = "DROP DATABASE " + name;

        System.out.println("Deleting Database " + name);

        try
        {
            stat.executeQuery(sql);
            System.out.println("Database deleted");
        }
        catch (SQLException throwables)
        {
            System.out.println("Failed to delete database");
            throwables.printStackTrace();
        }
    }

    // boolean true u AddX -> uspesno dodalo
    // null u GetX -> ne postoji
    // Za AddX f-je sve obavezne promenjive moraju biti popunjene

    // ADD f-je -----------------------------------------------------

    public static boolean AddStudent(Student s)       // JMBG and indexNum is unique
    {
        sql = "INSERT INTO Students (FirstName, LastName, IndexNum, JMBG, DateOfBirth, City, MajorId)" +
                "SELECT '" + s.firstName +"', '" + s.lastName + "', '" + s.index + "', '" + s.jmbg +"', '" + s.dateOfBirth +"', '" + s.city +"', '"+ s.majorId +"' FROM DUAL " +
                "WHERE NOT EXISTS (SELECT JMBG FROM Students " +
                "WHERE JMBG = '" + s.jmbg + "' AND IndexNum = '" + s.index + "' LIMIT 1)";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Student added");
            return true;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return false;
    }

    public static boolean AddSubject(Subject s)       // SubjectId is unique
    {
        sql = "INSERT INTO Subjects (SubjectName, SubjectId, Year, ESPB, MajorId, ProfId)" +
                "SELECT '" + s.subjectName + "', '" + s.subjectId + "', '" + s.year + "', '" + s.espb +"', '" + s.majorid +"', '" + s.lectid +"' FROM DUAL " +
                "WHERE NOT EXISTS (SELECT SubjectId FROM Subjects " +
                "WHERE SubjectId = '" + s.subjectId + "' LIMIT 1)";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Subject added");
            return true;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return false;
    }

    public static boolean AddLecturer(Lecturer p)       // ProfId is unique
    {
        sql = "INSERT INTO Lecturers (ProfId, FirstName, LastName)" +
                "SELECT '" + p.lectId + "', '" + p.firstName +"', '" + p.lastName +"' FROM DUAL " +
                "WHERE NOT EXISTS (SELECT ProfId FROM Subjects " +
                "WHERE ProfId = '" + p.lectId + "' LIMIT 1)";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Lecturer added");
            return true;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return false;
    }

    public static boolean AddMajor(Major m)        // MajorId is unique
    {
        sql = "INSERT INTO Majors (MajorId, MajorName)" +
                "SELECT '" + m.id + "', '" + m.name +"' FROM DUAL " +
                "WHERE NOT EXISTS (SELECT * FROM Majors " +
                "WHERE MajorId = " + m.id + " LIMIT 1)";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Major added");
            return true;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return false;
    }

    // GET f-je --------------------------------------

    public static ArrayList<Student> GetStudents(Date dateOfBirth[], String city[], String majorName[], int orderBy, boolean ascending)
    {
        ArrayList<Student> lista = new ArrayList<>();
        boolean uslov = false;
        ResultSet res;
        String sqlt = "SELECT * FROM Students " +
                "WHERE ";

        if(dateOfBirth != null)
        {
            sqlt += "( ";
            for(int i = 0; i < dateOfBirth.length; i++)
            {
                sqlt += "DateofBirth = '" + dateOfBirth[i] + "' OR ";
            }
            sqlt += "0 ) ";

            uslov = true;
        }
        if(city != null)
        {
            if(uslov)
                sqlt += "AND ";

            sqlt += "( ";
            for(int i = 0; i < city.length; i++)
            {
                sqlt += "city = '" + city[i] + "' OR ";
            }
            sqlt += "0 ) ";

            uslov = true;
        }
        if(majorName != null)
        {
            if(uslov)
                sqlt += "AND ";

            sqlt += "( ";
            for(int i=0; i<majorName.length; i++)
            {
                List<Major> majors = GetMajors(majorName[i]);

                if(majors != null)
                {
                    if(majors.size() > 1)
                    {
                        sqlt += "( ";
                        for (Major m: majors)
                        {
                            sqlt += "MajorId = " + m.id + " OR ";
                        }
                        sqlt += "0 ) OR ";
                    }
                    else
                    {
                        sqlt += "MajorId = " + majors.get(0).id + " OR ";
                    }
                }
            }
            sqlt += "0 ) ";

            uslov = true;
        }
        if(!uslov)
            sqlt += "1 ";

        if(orderBy > 2 && orderBy < 7)
        {
            switch(orderBy)
            {
                case 3:
                    sqlt += "ORDER BY DateofBirth ";
                    break;
                case 4:
                    sqlt += "ORDER BY City ";
                    break;
                case 6:
                    sqlt += "ORDER BY MajorId ";
                    break;
            }

            if(ascending)
                sqlt += "ASC ";
            else
                sqlt += "DESC ";
        }


        System.out.println(sqlt);

        try
        {
            res = stat.executeQuery(sqlt);

            if(!res.first())
                return lista;

            Student s;

            s = new Student(res.getString("FirstName"), res.getString("LastName"), new Index(res.getNString("IndexNum")));
            lista.add(s);

            while(res.next())
            {
                s = new Student(res.getString("FirstName"), res.getString("LastName"), new Index(res.getNString("IndexNum")));
                lista.add(s);
            }

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return lista;

    }

    public static Student GetStudent(String jmbg)
    {
        sql = "SELECT * FROM Students " +
                "WHERE JMBG = '" + jmbg + "'";

        Student s = null;
        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            s = new Student(res.getString("FirstName"), res.getString("LastName"), new Index(res.getString("IndexNum")), res.getDate("DateOfBirth"), res.getString("City"), res.getString("JMBG"), res.getInt("MajorId"));

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return s;
    }

    public static Student GetStudent(Index indexNum)
    {
        sql = "SELECT * FROM Students " +
                "WHERE IndexNum = '" + indexNum.getIndex() + "'";

        Student s = null;
        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            s = new Student(res.getString("FirstName"), res.getString("LastName"), new Index(res.getString("IndexNum")) , res.getDate("DateOfBirth"), res.getString("City"), res.getString("JMBG"), res.getInt("MajorId"));

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return s;
    }

    public static ArrayList<Subject> GetSubjects(String subjectName, int year, String profName, String majorName)
    {
        String sqlt = "SELECT * FROM Subjects " +
                "WHERE ";

        ResultSet res = null;
        ArrayList<Subject> subjects = new ArrayList<>();
        Subject tempsubject;
        boolean uslov = false;

        if(subjectName != null)
        {
            sqlt += "SubjectName = '" + subjectName + "' ";
            uslov = true;
        }
        if(year != 0)
        {
            if(uslov)
                sqlt += "AND ";
            sqlt += "Year = " + year + " ";
            uslov = true;
        }
        if(profName != null)
        {
            if(uslov)
                sqlt += "AND ";

            String temp[] = profName.split(" ");

            ArrayList<Integer> profIds = GetLecturers(temp[0], temp[1]);

            if(profIds != null)
            {
                if(profIds.size() > 1)
                {
                    sqlt += "( ";
                    for(int id : profIds)
                    {
                        sqlt += "ProfId = " + id + " OR ";
                    }
                    sqlt += "0 ) ";
                }
                else
                {
                    sqlt += "ProfId = " + profIds.get(0) + " ";
                }
            }
            uslov = true;
        }
        if(majorName != null)
        {
            if(uslov)
                sqlt += "AND ";

            ArrayList<Major> majorIds = GetMajors(majorName);

            if(majorIds != null)
            {
                if(majorIds.size() > 1)
                {
                    sqlt += "( ";
                    for(Major m : majorIds)
                    {
                        sqlt += "MajorId = " + m.id + " OR ";
                    }
                    sqlt += "0 ) ";
                }
                else
                {
                    sqlt += "ProfId = " + majorIds.get(0) + " ";
                }
            }

            uslov = true;
        }
        if(!uslov)
            sqlt += "1 ";

        try
        {
            res = stat.executeQuery(sqlt);

            if(!res.first())
                return null;

            tempsubject = new Subject(res.getString("SubjectName"), res.getString("SubjectId"), res.getInt("ESPB"), res.getInt("Year"), res.getInt("ProfId"), res.getInt("MajorId"));
            subjects.add(tempsubject);

            while(res.next())
            {
                tempsubject = new Subject(res.getString("SubjectName"), res.getString("SubjectId"), res.getInt("ESPB"), res.getInt("Year"), res.getInt("ProfId"), res.getInt("MajorId"));
                subjects.add(tempsubject);
            }

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return subjects;
    }

    public static Subject GetSubject(String subjectId)
    {
        sql = "SELECT * FROM Subjects " +
                "WHERE SubjectId = '" + subjectId + "' ";

        ResultSet res = null;
        Subject s = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            s = new Subject(res.getString("SubjectName"), res.getString("SubjectId"), res.getInt("ESPB"), res.getInt("Year"), res.getInt("ProfId"), res.getInt("MajorId"));

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return s;
    }

    public static Lecturer GetLecturer(int profId)
    {
        sql = "SELECT * FROM Lecturers " +
                "WHERE ProfId = " + profId;

        ResultSet res = null;
        Lecturer p = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            p = new Lecturer(res.getString("FirstName"), res.getString("LastName"), res.getInt("ProfId"));

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return p;
    }

    private static ArrayList<Integer> GetLecturers(String fname, String lname)
    {
        ArrayList<Integer> profIds = new ArrayList<>();
        sql = "SELECT * FROM Lecturers " +
                "WHERE FirstName = '" + fname + "' AND LastName = '" + lname + "' ";

        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            profIds.add(res.getInt("ProfId"));

            while(res.next())
                profIds.add(res.getInt("ProfId"));
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return profIds;
    }

    public static ArrayList<Major> GetMajors(String majorName)
    {
        ArrayList<Major> lista = new ArrayList<>();
        sql = "SELECT * FROM Majors " +
                "WHERE MajorName = '" + majorName + "' ";

        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            Major temp;

            temp = new Major(res.getInt("MajorId"), res.getString("MajorName"));
            lista.add(temp);

            while(res.next())
            {
                temp = new Major(res.getInt("MajorId"), res.getString("MajorName"));
                lista.add(temp);
            }

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return lista;
    }

    public static Major GetMajor(int majorId)
    {
        Major m = null;
        ResultSet res;

        sql = "SELECT * FROM Majors " +
                "WHERE MajorId = " + majorId + " ";

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            m = new Major(res.getInt("MajorId"), res.getString("MajorName"));
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return m;
    }

    public static ArrayList<Subject> SubjectsOfLecturer(Lecturer p)
    {
        sql = "SELECT * FROM Subjects " +
                "WHERE ProfId = " + p.lectId + " ";

        ResultSet res = null;
        Subject temp = null;
        ArrayList<Subject> list = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            temp = new Subject(res.getString("SubjectId"));
            temp = GetSubject(temp.subjectId);
            list.add(temp);

            while(res.next())
            {
                temp = new Subject(res.getString("SubjectId"));
                temp = GetSubject(temp.subjectId);
                list.add(temp);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return list;


    }

    public static int GetHighestIndex(int year)
    {
        sql = "SELECT MAX(SUBSTR(IndexNum, 1, INSTR(IndexNum, '/')-1)) as ind, SUBSTR(IndexNum, INSTR(IndexNum, '/')+1) as god FROM Students " +
                "WHERE SUBSTR(IndexNum, INSTR(IndexNum, '/')+1) = " + year + " ";

        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return 0;

            return res.getInt("ind");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return 0;
    }

    public static int GetEmptyId(String tableName)
    {
        if(tableName == "Lecturers")
            sql = "SELECT ProfId FROM Lecturers ";

        else if(tableName == "Majors")
            sql = "SELECT MajorId FROM Majors";

        ResultSet res = null;
        int pret = 0;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return 0;

            do
            {
                if(res.getInt(1) != pret + 1)
                    return pret + 1;

                pret = res.getInt(1);
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return pret + 1;
    }
}
