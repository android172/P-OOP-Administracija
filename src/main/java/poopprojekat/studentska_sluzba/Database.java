package poopprojekat.studentska_sluzba;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public void TestDummy()
    {
        CreateDatabase("Testing");

        Student s = new Student("Student1", "Jedanovic", "1/2020", Date.valueOf("2000-1-1"), "Prvogradic", "0123456789123", 1);
        AddStudent(s);
        s = new Student("Student2", "Drugovanovic", "2/2020", Date.valueOf("2000-2-1"), "Drugogradic", "1234567890123", 1);
        AddStudent(s);
        s = new Student("Student3", "Trecanovic", "3/2020", Date.valueOf("2000-3-1"), "Prvogradic", "2223334448882", 2);
        AddStudent(s);
        s = new Student("Student3", "Cetvrtanovic", "4/2020", Date.valueOf("2000-4-1"), "Prvogradic", "2223434498882", 1);
        AddStudent(s);
        s = new Student("Student3", "Petic", "5/2020", Date.valueOf("2000-5-1"), "Trecegradic", "2223374446882", 2);
        AddStudent(s);
        s = new Student("Student3", "Sestic", "6/2020", Date.valueOf("2000-6-1"), "Drugogradic", "2223934448822", 2);
        AddStudent(s);

        Professor p = new Professor("Professor1", "Profesanovic1", 1);
        AddProfessor(p);
        p = new Professor("Professor2", "Profesanovic2", 2);
        AddProfessor(p);
        p = new Professor("Professor3", "Profesanovic3", 3);
        AddProfessor(p);

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
            CreateTableProfessors();
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
                "FOREIGN KEY (ProfId) REFERENCES Professors (ProfId) )";

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
                "ProfessorId INTEGER not NULL," +
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

    private void CreateTableProfessors()
    {
        sql = "CREATE TABLE IF NOT EXISTS Professors " +
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
                "SELECT '" + s.firstName +"', '" + s.lastName + "', '" + s.indexNum + "', '" + s.jmbg +"', '" + s.dateOfBirth +"', '" + s.city +"', '"+ s.majorId +"' FROM DUAL " +
                "WHERE NOT EXISTS (SELECT JMBG FROM Students " +
                "WHERE JMBG = '" + s.jmbg + "' AND IndexNum = '" + s.indexNum + "' LIMIT 1)";

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
                "SELECT '" + s.subjectName + "', '" + s.subjectId + "', '" + s.year + "', '" + s.espb +"', '" + s.majorid +"', '" + s.profid +"' FROM DUAL " +
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

    public static boolean AddProfessor(Professor p)       // ProfId is unique
    {
        sql = "INSERT INTO Professors (ProfId, FirstName, LastName)" +
                "SELECT '" + p.profId + "', '" + p.firstName +"', '" + p.lastName +"' FROM DUAL " +
                "WHERE NOT EXISTS (SELECT ProfId FROM Subjects " +
                "WHERE ProfId = '" + p.profId + "' LIMIT 1)";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Professor added");
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

    // GET f-je

    public static List<Student> GetStudents(Date dateOfBirth, String city, String majorName)
    {
        List<Student> lista = new ArrayList<>();
        boolean uslov = false;
        ResultSet res;
        String sqlt = "SELECT * FROM Students " +
                "WHERE ";

        if(dateOfBirth != null)
        {
            sqlt += "DateofBirth = '" + dateOfBirth + "' ";
            uslov = true;
        }
        if(city != null)
        {
            if(uslov)
                sqlt += "AND ";

            sqlt += "city = '" + city + "' ";
            uslov = true;
        }
        if(majorName != null)
        {
            if(uslov)
                sqlt += "AND ";
            List<Major> temp = GetMajor(majorName, 0);

            sqlt += "MajorId = " + (temp.get(0)).id + " ";
            uslov = true;
        }
        if(!uslov)
            sqlt += "1 ";

        try
        {
            res = stat.executeQuery(sqlt);

            if(!res.first())
                return lista;

            Student s;

            s = new Student(res.getString("FirstName"), res.getString("LastName"), res.getNString("IndexNum"));
            lista.add(s);

            while(res.next())
            {
                s = new Student(res.getString("FirstName"), res.getString("LastName"), res.getNString("IndexNum"));
                lista.add(s);
            }

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
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

            s = new Student(res.getString("FirstName"), res.getString("LastName"), res.getString("IndexNum"), res.getDate("DateOfBirth"), res.getString("City"), res.getString("JMBG"), res.getInt("MajorId"));

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return s;
    }

    public static Student GetStudent(String indexNum)
    {
        sql = "SELECT * FROM Students " +
                "WHERE IndexNum = '" + indexNum + "'";

        Student s = null;
        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            s = new Student(res.getString("FirstName"), res.getString("LastName"), res.getString("IndexNum"), res.getDate("DateOfBirth"), res.getString("City"), res.getString("JMBG"), res.getInt("MajorId"));

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return s;
    }

    public static Subject GetSubject(Subject s)
    {
        if(s.subjectName == null)
        {
            sql = "SELECT SubjectName, SubjectId, ESPB, Year, p.FirstName, p.LastName, m.MajorName " +
                    "FROM Subjects as sa join Professors as p on " +
                    "sa.profId = p.profId join Majors as m on sa.MajorId = m.MajorId " +
                    "WHERE SubjectId = '" + s.subjectId + "'";
        }
        else
        {
            sql = "SELECT SubjectName, SubjectId, ESPB, Year, p.FirstName, p.LastName, m.MajorName " +
                    "FROM Subjects as sa join Professors as p on " +
                    "sa.profId = p.profId join Majors as m on sa.MajorId = m.MajorId " +
                    "WHERE SubjectId = '" + s.subjectId + "' OR SubjectName = '" + s.subjectName + "'";
        }


        ResultSet res = null;
        String t1, t2;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            s.subjectName = res.getString("SubjectName");
            s.subjectId = res.getString("SubjectId");
            s.espb = res.getInt("ESPB");
            s.year = res.getInt("Year");
            s.major = res.getString("m.MajorName");

            t1 = res.getString("p.FirstName");
            t2 = res.getString("p.LastName");
            s.prof = t1 + " " + t2;

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return s;
    }

    public static Professor GetProfessor(Professor p)
    {
        if(p.firstName == null && p.lastName == null)
        {
            sql = "SELECT * FROM Professors " +
                    "WHERE ProfId = " + p.profId;
        }
        else
        {
            sql = "SELECT * FROM Professors " +
                    "WHERE ProfId = " + p.profId + " OR (FirstName = '" + p.firstName + "'" +
                    "AND LastName = '" + p.lastName + "')";
        }


        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            p.firstName = res.getString("FirstName");
            p.lastName = res.getString("LastName");
            p.profId = res.getInt("ProfId");

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return p;
    }

    public static List GetMajors(String majorName)
    {
        List<Major> lista = new ArrayList<>();
        boolean uslov = false;
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
                "WHERE MajorId = '" + majorId + "' ";

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

    public static List<Subject> SubjectsOfProfessor(Professor p)
    {
        sql = "SELECT * FROM SubjectsALL" +
                "WHERE ProfId = '" + p.profId + "'";

        ResultSet res = null;
        Subject temp = null;
        List<Subject> list = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            while(res.next())
            {
                temp.subjectId = res.getString("SubjectId");
                temp = GetSubject(temp);

                list.add(temp);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return list;


    }

}
