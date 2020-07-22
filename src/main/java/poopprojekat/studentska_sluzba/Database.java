package poopprojekat.studentska_sluzba;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import poopprojekat.studentska_sluzba.Generators.Fill_db_randomly;

// Database class Includes:
// ConnectToDatabase(String name) - Connects to database if exists otherwise creates new database
// Close() - Closes connection to database
// DropDatabase(String name) - Delete database

// AddStudent(Student s) - Adds student
// AddSubject(Subject s) - Adds subject
// AddLecturer(Lecturer p) - Adds lecturer
// AddMajor(Major m) - Adds Major
// AddUser(User user, String id) - Adds user, id is unique to lectuer/student, id for admin is -x
// Add functions return true if sucessfull, false if not
// GetStudents(Date dateOfBirth[], String city[], String MajorName[], int orderBy, boolean ascending) - accepts DoB/City/MajorName,
//                                      orderby: 3 - sorts po DoB 4 - sorts po City, 6 - sorts po MajorId,
//                                      ascending - true/false.. doesn't matter if orderby != 3/4/6
//                                      function returns ArrayList<Student>
//                                      if all params are null, returns all Students from table
// GetStudent(String jmbg) - return Student with forwarded jmbg
// GetStudent(Index index) - return Student with forwarded index number
// GetSubjets(String subjectName[], int year[], String profName[], String majorName[]) - prima 1 ili vise parametra (ostali null)
//                                                                               i pretrazuje ih, Vraca ArrayList<Subject>
//                                                                               Ukoliko su svi parametri null, f-ja vraca sve predmete iz tabele Subjects
// GetSubject(String SubjectId) - prima id predmeta, vraca Subject
// GetLecturer(int LectId) - prima id profesora, vraca Lecturer
// GetMajors(String majorName) - prima ime smera, vraca ArrayList<Major>
// GetMajor(String majorId) - prima id smera, vraca Major
// SubjectsOfLecturer(Lecturer p) - prima profesora i vraca sve predmete na kojima predaje. vraca ArrayList<Subjects>
// GetHighestIndex(int year) - Vraca najveci br indeksa za zadatu godinu
// GetEmptyId(tableName) - prima tabelu "Lecturers"/"Majors"/"Subjects" i vraca 1. slobodan Id. Izbacuje gresku ako prosledjena tabela nije jedna od navedenih
// GetUser(String username, String password) - pretrazuje korisnika u bazi, ako postoji vraca korisnika, u suprotnom vraca null
// GetLecturers(String subjects[], String majors[]) - pretrazuje po imenima predmeta i/ili smera i vraca listu Lecturers

// EditStudent(Index index, Student updated) - prima index studenta kog treba izmeniti i promenjivu Student sa izmenjenim podacima
// EditLecturer(int lectId, Lecturer updated) - prima id profesora kog treba izmeniti i Lecturer sa izmenjenim podacima
// EditSubject(String subjectId, Subject updated) - prima id predmeta koji treba izmeniti i Subject sa izmenjenim podacima
// EditMajor(int majorId, Major updated) - prima id smera koji treba izmeniti i Major sa izmenjenim podacima
// EditUser(String username, User updated) - prima username i User-a kome treba izmeniti username/password/role

// DeleteStudent(Index index) - prima indeks studenta kog treba obrisati iz baze
// DeleteLecturer(int lectId) - prima id profesora kog treba obrisati iz baze
// DeleteMajor(int majorId) - prima id smera koji treba obrisati iz baze
// DeleteSubject(int subjectId) - prima id predmeta koji treba obrisati iz baze
// DeleteUser(String id) - prima brind ili id profesora (kao String) i brise user-a iz tabele Users

public class Database
{
    private static Connection conn = null;
    private static Statement stat = null;
    private static String sql = null;

    public Database(String name)       // First time run
    {
        ConnectToDatabase(name);
    }

    public void Close()
    {
        try
        {
            conn.close();
            stat.close();
            System.out.println("Connection closed");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public void TestDummy() throws Exception
    {
        CreateDatabase("Testing");

        Fill_db_randomly.with_majors(1);
        Fill_db_randomly.with_students(20);
        Fill_db_randomly.with_lecturers(5);
        Fill_db_randomly.with_Subjects(6);
    }

    public void ConnectToDatabase(String name)
    {
        if(name != "")
            System.out.println("Connecting to database");

        try
        {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost/" + name, "root", "");
            stat = conn.createStatement();

            if(name != "")
                System.out.println("Connection successful");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1049)       // Database doesn't exsist
            {
                System.out.println("Database doesn't exist");
                ConnectToDatabase("");
                CreateDatabase(name);
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

            CreateTableMajors();
            CreateTableStudents();
            CreateTableLecturers();
            CreateTableUsers();
            CreateTableSubjects();
            CreateTableExams();
            CreateTableExamApplications();
            CreateTableAppliedToListen();
            CreateTableStudentStatus();
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
                "Username NVARCHAR(20) not NULL Unique, " +      // ind/god ili ind-god
                "Password NVARCHAR(20) not NULL, " +
                "Role VARCHAR(10) not NULL, " +          // Student, Profesor, Admin
                "UniqueId VARCHAR(10) not NULL UNIQUE , " +
                "PRIMARY KEY (id, Username) ) ENGINE=InnoDB";

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
                "( id INTEGER not NULL AUTO_INCREMENT, " +
                "SubjectName NVARCHAR(30) not NULL, " +
                "SubjectId VARCHAR(10) not NULL UNIQUE , " +
                "Year INTEGER not NULL, " +
                "ESPB INTEGER not NULL, " +
                "MajorId VARCHAR(10) not NULL, " +
                "LectId VARCHAR(10) not NULL, " +
                "PRIMARY KEY (id, SubjectId), " +
                "FOREIGN KEY (MajorId) REFERENCES Majors (MajorId) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (LectId) REFERENCES Lecturers (LectId) ON DELETE CASCADE ON UPDATE CASCADE ) ENGINE=InnoDB ";

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
                "IndexNum VARCHAR(10) not NULL UNIQUE," +
                "FirstName NVARCHAR(30) not NULL," +
                "LastName NVARCHAR(30) not NULL," +
                "JMBG VARCHAR(15) not NULL UNIQUE," +
                "DateOfBirth DATE not NULL," +
                "City NVARCHAR(30) not NULL," +
                "MajorId VARCHAR(10) not NULL, " +
                "PRIMARY KEY (id, JMBG, IndexNum), " +
                "FOREIGN KEY (MajorId) REFERENCES Majors(MajorId) ON DELETE CASCADE ON UPDATE CASCADE ) ENGINE=InnoDB ";

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
                "Year INTEGER not NULL," +
                "DatePassed Date, " +
                "Attempts INTEGER, " +
                "Mark INTEGER, " +
                "Points INTEGER, " +
                "LectId VARCHAR(10), " +
                "PRIMARY KEY (id, IndexNum, SubjectId)," +
                "FOREIGN KEY (IndexNum) REFERENCES Students(IndexNum) ON UPDATE CASCADE, " +
                "FOREIGN KEY (SubjectId) REFERENCES Subjects(SubjectId) ON UPDATE CASCADE, " +
                "FOREIGN KEY (LectId) references Lecturers(LectId) ) ENGINE=InnoDB ";

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
                "ExamId VARCHAR(10) not NULL UNIQUE , " +
                "ExamDate DATE not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "LectId VARCHAR(10) not NULL," +
                "PRIMARY KEY (id, ExamId)," +
                "FOREIGN KEY (SubjectId) references Subjects(SubjectId)," +
                "FOREIGN KEY (LectId) references Lecturers(LectId) ) ENGINE=InnoDB ";

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
                "ExamId VARCHAR(10) not NULL," +
                "Price INTEGER not NULL," +
                "PRIMARY KEY (id, IndexNum, ExamId), " +
                "FOREIGN KEY (IndexNum) references Students(IndexNum), " +
                "FOREIGN KEY (ExamId) references Exams(ExamId) ) ENGINE=InnoDB ";

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

    private void CreateTableStudentStatus()
    {
        sql = "CREATE TABLE IF NOT EXISTS StudentStatus " +
                "( id INTEGER not NULL AUTO_INCREMENT, " +
                "IndexNum VARCHAR(10) not NULL, " +
                "Year INTEGER not NULL, " +
                "CurrentYear INTEGER not NULL, " +
                "Status VARCHAR(15) not NULL, " +
                "PRIMARY KEY (id, IndexNum), " +
                "FOREIGN KEY (IndexNum) references Students(IndexNum) ) ENGINE=InnoDB ";

        System.out.println("Creating table 'StudentStatus'");
        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'StudentStatus' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'StudentStatus' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableLecturers()
    {
        sql = "CREATE TABLE IF NOT EXISTS Lecturers " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "LectId VARCHAR(10) not NULL UNIQUE ," +
                "FirstName NVARCHAR(15) not NULL," +
                "LastName NVARCHAR(15) not NULL, " +
                "Title VARCHAR(30) not NULL, " +
                "PRIMARY KEY (id, LectId) ) ENGINE=InnoDB ";

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
                "MajorId VARCHAR(10) not NULL UNIQUE ," +
                "MajorName NVARCHAR(30) not NULL," +
                "PRIMARY KEY(id, MajorId) ) ENGINE=InnoDB";

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

    public static boolean AddStudent(Student s) throws Exception       // JMBG and indexNum is unique
    {
        sql = "INSERT INTO Students (FirstName, LastName, IndexNum, JMBG, DateOfBirth, City, MajorId) " +
                "VALUES ( '" + s.getFirstName() + "', '" + s.getLastName() + "', '" + s.getIndex() + "', '" + s.getJmbg() + "', '" + s.getDateOfBirth() + "', '" + s.getCity() + "', '" + s.getMajorId() + "' )";
        try
        {
            stat.executeUpdate(sql);
            System.out.println("Student added");
            return true;
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("Index number or JMBG is already in table Students");
            throwables.printStackTrace();
        }

        return false;
    }

    public static boolean AddSubject(Subject s) throws Exception       // SubjectId is unique
    {
        sql = "INSERT INTO Subjects (SubjectName, SubjectId, Year, ESPB, MajorId, LectId) " +
                "VALUES ( '" + s.subjectName + "', '" + s.subjectId + "', '" + s.year + "', '" + s.espb +"', '" + s.majorid +"', '" + s.lectid +"' ) ";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Subject added");
            return true;
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("SubjectId is already in table Subjects");
            throwables.printStackTrace();
        }

        return false;
    }

    public static boolean AddLecturer(Lecturer p) throws Exception       // LectId is unique
    {
        sql = "INSERT INTO Lecturers (LectId, FirstName, LastName, Title) " +
                "VALUES ( '" + p.getLectId() + "', '" + p.getFirstName() +"', '" + p.getLastName() +"', '" + p.getTitle() + "' )";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Lecturer added");
            return true;
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("LectId is already in table Lecturers");
            throwables.printStackTrace();
        }

        return false;
    }

    public static boolean AddMajor(Major m) throws Exception        // MajorId is unique
    {
        sql = "INSERT INTO Majors (MajorId, MajorName) " +
                "VALUES ( '" + m.id + "', '" + m.name +"' ) ";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Major added");
            return true;
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("MajorId is already in table Majors");
            throwables.printStackTrace();
        }

        return false;
    }

    public static boolean AddUser(User user, String id) throws Exception
    {
        sql = "INSERT INTO Users (Username, Password, Role, UniqueId) " +
                "VALUES ( '" + user.username + "', '" + user.password + "', '" + user.role + "', '" + id + "' ) ";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("User added");
            return true;
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("Username is already in table Users");
            throwables.printStackTrace();
        }

        return false;
    }

    // GET f-je --------------------------------------

    public static ArrayList<Student> GetStudents(Date dateOfBirth[], String city[], String majorId[], int orderBy) {
        ArrayList<Student> lista = new ArrayList<>();
        boolean uslov = false;
        ResultSet res;
        String sqlt;

        if(orderBy == 6)
            sqlt = "SELECT * FROM Students as s join Majors as m on s.majorId = m.majorId " +
                    "WHERE ";
        else
            sqlt = "SELECT * FROM Students as s" +
                    "WHERE ";

        if (dateOfBirth != null) {
            sqlt += "( ";
            for (int i = 0; i < dateOfBirth.length; i++) {
                sqlt += "s.DateofBirth = '" + dateOfBirth[i] + "' OR ";
            }
            sqlt += "0 ) ";
            uslov = true;
        }
        if (city != null) {
            if (uslov)
                sqlt += "AND ";

            sqlt += "( ";
            for (int i = 0; i < city.length; i++) {
                sqlt += "s.City = '" + city[i] + "' OR ";
            }
            sqlt += "0 ) ";

            uslov = true;
        }
        if (majorId != null)
        {
            if (uslov)
                sqlt += "AND ";

            sqlt += "( ";
            for (int i = 0; i < majorId.length; i++)
            {
                sqlt += "s.MajorId = '" + majorId[i] + "' OR ";
            }

        sqlt += "0 ) ";

        uslov = true;
        }
        if(!uslov)
            sqlt += "1 ";

        switch(orderBy)
        {
            case 1:
                sqlt += "ORDER BY s.IndexNum ASC ";
                break;
            case 2:
                sqlt += "ORDER BY s.Firstname ASC ";
                break;
            case 3:
                sqlt += "ORDER BY s.Lastname ASC ";
                break;
            case 4:
                sqlt += "ORDER BY s.DateOfBirth ASC ";
                break;
            case 5:
                sqlt += "ORDER BY s.City ASC ";
                break;
            case 6:
                sqlt += "ORDER BY m.MajorName ASC ";
                break;
        }

        try
        {
            res = stat.executeQuery(sqlt);

            if(!res.first())
                return lista;

            Student s;

            switch(orderBy)
            {
                case 4:
                    do
                    {
                        s = new Student(res.getString("FirstName"), res.getString("LastName"), new Index(res.getNString("IndexNum")), res.getDate("DateOfBirth"));
                        lista.add(s);
                    }while (res.next());
                    break;
                case 5:
                    do
                    {
                        s = new Student(res.getString("FirstName"), res.getString("LastName"), res.getString("City"), new Index(res.getNString("IndexNum")));
                        lista.add(s);
                    }while (res.next());
                    break;
                case 6:
                    do
                    {
                        s = new Student(res.getString("FirstName"), res.getString("LastName"), new Index(res.getNString("IndexNum")), res.getString("s.MajorId"));
                        lista.add(s);
                    }while (res.next());
                    break;
                default:
                    do
                    {
                        s = new Student(res.getString("FirstName"), res.getString("LastName"), new Index(res.getNString("IndexNum")));
                        lista.add(s);
                    }while (res.next());
                    break;

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

            s = new Student(res.getString("FirstName"), res.getString("LastName"), new Index(res.getString("IndexNum")), res.getDate("DateOfBirth"), res.getString("City"), res.getString("JMBG"), res.getString("MajorId"));

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

            s = new Student(res.getString(  "FirstName"), res.getString("LastName"), new Index(res.getString("IndexNum")) , res.getDate("DateOfBirth"), res.getString("City"), res.getString("JMBG"), res.getString("MajorId"));

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

    public static ArrayList<Subject> GetSubjects(String subjectName[], int year[], String profName[], String majorId[], int orderBy)
    {
        String sqlt;

        if(orderBy == 5)
            sqlt = "SELECT * FROM Subjects as s join Majors as m on s.MajorId = m.MajorId " +
                    "WHERE ";
        else
            sqlt = "SELECT * FROM Subjects as s " +
                    "WHERE ";

        ResultSet res = null;
        ArrayList<Subject> subjects = new ArrayList<>();
        Subject tempsubject;
        boolean uslov = false;

        if(subjectName != null)
        {
            sqlt += "( ";
            for(int i = 0;i<subjectName.length; i++)
            {
                sqlt += "s.SubjectName = '" + subjectName[i] + "' OR ";
            }
            sqlt += "0 ) ";
            uslov = true;
        }
        if(year != null)
        {
            if(uslov)
                sqlt += "AND ";

            sqlt += "( ";
            for(int i = 0;i<year.length;i++)
            {
                sqlt += "s.Year = " + year[i] + " OR ";
            }
            sqlt += "0 ) ";
            uslov = true;
        }
        if(profName != null)
        {
            if(uslov)
                sqlt += "AND ";
            sqlt += "( ";
            for(int i = 0;i<profName.length;i++)
            {
                String temp[] = profName[i].split(" ");

                ArrayList<String> LectIds = GetLecturers(temp[0], temp[1]);

                if(LectIds != null)
                {
                    for (String id : LectIds)
                    {
                        sqlt += "s.LectId = '" + id + "' OR ";
                    }
                }
            }

            sqlt += "0 ) ";
            uslov = true;
        }
        if(majorId != null)
        {
            if(uslov)
                sqlt += "AND ";
            sqlt += "( ";
            for(int i = 0;i<majorId.length;i++)
            {
                sqlt += "s.MajorId = '" + majorId[i] + "' OR ";
            }
            sqlt += "0 ) ";
            uslov = true;
        }
        if(!uslov)
            sqlt += "1 ";

        switch(orderBy)
        {
            case 1:
                sqlt += "ORDER BY s.SubjectName ASC ";
                break;
            case 2:
                sqlt += "ORDER BY s.SubjectId ASC ";
                break;
            case 3:
                sqlt += "ORDER BY s.Year ASC ";
                break;
            case 4:
                sqlt += "ORDER BY s.ESPB ASC ";
                break;
            case 5:
                sqlt += "ORDER BY m.MajorName ASC ";
                break;
            case 6:
                sqlt += "ORDER BY s.LectId ASC ";
                break;
        }

        try
        {
            res = stat.executeQuery(sqlt);

            if(!res.first())
                return null;

            do
            {
                tempsubject = new Subject(res.getString("SubjectName"), res.getString("SubjectId"), res.getInt("ESPB"), res.getInt("Year"), res.getString("LectId"), res.getString("MajorId"));
                subjects.add(tempsubject);
            }while(res.next());

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

            s = new Subject(res.getString("SubjectName"), res.getString("SubjectId"), res.getInt("ESPB"), res.getInt("Year"), res.getString("LectId"), res.getString("MajorId"));

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return s;
    }

    public static Lecturer GetLecturer(String LectId)
    {
        sql = "SELECT * FROM Lecturers " +
                "WHERE LectId = '" + LectId + "' ";

        ResultSet res = null;
        Lecturer p = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            p = new Lecturer(res.getString("FirstName"), res.getString("LastName"), res.getString("Title"), res.getString("LectId"));

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return p;
    }

    private static ArrayList<String> GetLecturers(String fname, String lname)
    {
        ArrayList<String> LectIds = new ArrayList<>();
        sql = "SELECT * FROM Lecturers " +
                "WHERE FirstName = '" + fname + "' AND LastName = '" + lname + "' ";

        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            LectIds.add(res.getString("LectId"));

            while(res.next())
                LectIds.add(res.getString("LectId"));
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return LectIds;
    }

    public static ArrayList<Lecturer> GetLecturers(String subjects[], String majorId[], int orderBy)
    {
        ArrayList<Lecturer> lista = new ArrayList<>();
        boolean uslov = false;
        ResultSet res;

        String sqlt = "SELECT * FROM Lecturers as l join Subjects as s on l.LectId = s.LectId " +
                "WHERE ";

        if(subjects != null)
        {
            sqlt += "( ";

            for(int i=0; i<subjects.length; i++)
            {
                sqlt += " s.SubjectName = '" + subjects[i] + "' OR ";
            }

            sqlt += " 0) ";

            uslov = true;
        }
        if(majorId != null)
        {
            if(uslov)
                sqlt += "AND ";

            sqlt += "( ";

            for(int i=0; i<majorId.length; i++)
            {
                sqlt += " s.MajorId = '" + majorId[i] + "' OR ";
            }

            sqlt += " 0) ";

            uslov = true;
        }

        if(uslov)
            sqlt += "AND ";

        sqlt += "1 ";

        switch(orderBy)
        {
            case 1:
                sqlt += "ORDER BY l.LectId ASC ";
                break;
            case 2:
                sqlt += "ORDER BY l.FirstName ASC ";
                break;
            case 3:
                sqlt += "ORDER BY l.LastName ASC ";
                break;
            case 4:
                sqlt += "ORDER BY l.Title ASC ";
                break;
        }

        try
        {
            res = stat.executeQuery(sqlt);

            if(!res.first())
                return lista;

            Lecturer l;

            do
            {
                l = new Lecturer(res.getString("l.FirstName"), res.getString("l.LastName"), res.getString("l.Title"), res.getString("l.LectId"));
                lista.add(l);
            }while(res.next());

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

    public static ArrayList<Major> GetMajors(String majorName)
    {
        ArrayList<Major> lista = new ArrayList<>();

        sql = "SELECT * FROM Majors ";
        if (majorName != null)
        {
            sql += "WHERE MajorName = '" + majorName + "' ";
        }

        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            Major temp;

            temp = new Major(res.getString("MajorId"), res.getString("MajorName"));
            lista.add(temp);

            while(res.next())
            {
                temp = new Major(res.getString("MajorId"), res.getString("MajorName"));
                lista.add(temp);
            }

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return lista;
    }

    public static Major GetMajor(String majorId)
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

            m = new Major(res.getString("MajorId"), res.getString("MajorName"));
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
                "WHERE LectId = '" + p.getLectId() + "' ";

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

    public static String GetEmptyId(String tableName) throws Exception
    {
        String prefix = null;

        if(tableName == "Lecturers")
        {
            sql = "SELECT LectId FROM Lecturers ";
            prefix = "P";
        }
        else if(tableName == "Majors")
        {
            sql = "SELECT MajorId FROM Majors";
            prefix = "M";
        }
        else if(tableName == "Subjects")
        {
            sql = "SELECT SubjectId FROM Subjects";
            prefix = "S";
        }
        else
            throw new Exception("Function 'GetEmptyId' doesn't work on this table");

        ResultSet res = null;
        int pret = 0;
        int tren = 1;
        String temp = "";

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return prefix + "001";

            do
            {
                tren = Integer.parseInt(res.getString(1).substring(1));
                if(tren != pret + 1)
                {
                    if(pret + 1 < 10)
                        temp = "00";
                    else if(pret + 1 < 100)
                        temp = "0";

                    return prefix + temp + Integer.toString(pret + 1);
                }

                pret = tren;
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        tren++;
        if(tren < 10)
            temp = "00";
        else if(tren < 100)
            temp = "0";

        return prefix + temp + Integer.toString(tren);
    }

    public static String[] GetUser(String username, String password)
    {
        sql = "SELECT * FROM Users " +
                "WHERE Username = '" + username + "' AND Password = '" + password + "' ";

        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            String ret[] = {res.getString("Role"), res.getString("UniqueId")};
            return ret;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return null;
    }

    public static String GetUser(String id)
    {
        sql = "SELECT * FROM Users " +
                "WHERE UniqueId = '" + id + "' ";

        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            return res.getString("Username");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return null;
    }

    // Remove f-je

    public static void DeleteStudent(Index index) throws Exception
    {
        sql = "DELETE FROM Students " +
                "WHERE IndexNum = '" + index + "' ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("Student doesn't exist");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public static void DeleteLecturer(String lectId) throws Exception
    {
        sql = "DELETE FROM Lecturers " +
                "WHERE LectId = '" + lectId + "' ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("Lecturer doesn't exist");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public static void DeleteMajor(String majorId) throws Exception
    {
        sql = "DELETE FROM Majors " +
                "WHERE MajorId = '" + majorId + "' ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("Major doesn't exist");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public static void DeleteSubject(String subjectId) throws Exception
    {
        sql = "DELETE FROM Subjects " +
                "WHERE SubjectId = '" + subjectId + "' ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("Subject doesn't exist");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public static void DeleteUser(String id) throws Exception
    {
        sql = "DELETE FROM Users " +
                "WHERE UniqueId = '" + id + "' ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("User doesn't exist");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    // Edit f-je

    public static void EditStudent(Index index, Student updated) throws Exception
    {
        sql = "UPDATE Students SET ";
        boolean uslov = false;

        if(updated.getFirstName() != null)
        {
            sql += "FirstName = '" + updated.getFirstName() + "' ";
            uslov = true;
        }
        if(updated.getLastName() != null)
        {
            if(uslov)
                sql += ", ";

            sql += "LastName = '" + updated.getLastName() + "' ";
            uslov = true;
        }
        if(updated.getIndex() != null)
        {
            if(uslov)
                sql += ", ";

            sql += "IndexNum = '" + updated.getIndex() + "' ";
            uslov = true;
        }
        if(updated.getJmbg() != null)
        {
            if(uslov)
                sql += ", ";

            sql += "JMBG = '" + updated.getJmbg() + "' ";
            uslov = true;
        }
        if(updated.getDateOfBirth() != null)
        {
            if(uslov)
                sql += ", ";

            sql += "DateOfBirth = '" + updated.getDateOfBirth() + "' ";
            uslov = true;
        }
        if(updated.getCity() != null)
        {
            if(uslov)
                sql += ", ";

            sql += "City = '" + updated.getCity() + "' ";
            uslov = true;
        }
        if(updated.getMajorId() != null)
        {
            if(uslov)
                sql += ", ";

            sql += "MajorId = '" + updated.getMajorId() + "' ";
            uslov = true;
        }

        sql += "WHERE IndexNum = '" + index + "' ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("Student doesn't exist");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

    }

    public static void EditLecturer(String lectId, Lecturer updated) throws Exception
    {
        sql = "UPDATE Lecturers SET ";
        boolean uslov = false;

        if(updated.getFirstName() != null)
        {
            sql += "FirstName = '" + updated.getFirstName() + "' ";
            uslov = true;
        }
        if(updated.getLastName() != null)
        {
            if(uslov)
                sql += ", ";

            sql += "LastName = '" + updated.getLastName() + "' ";
            uslov = true;
        }
        if(updated.getLectId() != null)
        {
            if(uslov)
                sql += ", ";

            sql += "LectId = '" + updated.getLectId() + "' ";
            uslov = true;
        }
        if(updated.getTitle() != null)
        {
            if(uslov)
                sql += ", ";

            sql += "Title = '" + updated.getTitle() + "' ";
            uslov = true;
        }

        sql += "WHERE LectId = '" + lectId + "' ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("Lecturer doesn't exist");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public static void EditSubject(String subjectId, Subject updated) throws Exception
    {
        sql = "UPDATE Subjects SET ";
        boolean uslov = false;

        if(updated.subjectName != null)
        {
            sql += "SubjectName = '" + updated.subjectName + "' ";
            uslov = true;
        }
        if(updated.subjectId != null)
        {
            if(uslov)
                sql += ", ";

            sql += "SubjectId = '" + updated.subjectId + "' ";
            uslov = true;
        }
        if(updated.espb != 0)
        {
            if(uslov)
                sql += ", ";

            sql += "ESPB = " + updated.espb + " ";
            uslov = true;
        }
        if(updated.year != 0)
        {
            if(uslov)
                sql += ", ";

            sql += "Year = " + updated.year + " ";
            uslov = true;
        }
        if(updated.lectid != null)
        {
            if(uslov)
                sql += ", ";

            sql += "LectId = '" + updated.lectid + "' ";
            uslov = true;
        }
        if(updated.majorid != null)
        {
            if(uslov)
                sql += ", ";

            sql += "MajorId = '" + updated.majorid + "' ";
            uslov = true;
        }

        sql += "WHERE SubjectId = '" + subjectId + "' ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("Subject doesn't exist");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public static void EditMajor(String majorId, Major updated) throws Exception
    {
        sql = "UPDATE Majors SET ";
        boolean uslov = false;

        if(updated.name != null)
        {
            sql += "MajorName = '" + updated.name + "' ";
            uslov = true;
        }
        if(updated.id != null)
        {
            if(uslov)
                sql += ", ";

            sql += "MajorId = '" + updated.id + "' ";
            uslov = true;
        }

        sql += "WHERE MajorId = '" + majorId + "' ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("Major doesn't exist");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public static void EditUser(String username, User updated) throws Exception
    {
        sql = "UPDATE Users SET ";
        boolean uslov = false;

        if(updated.username != null)
        {
            sql += "Username = '" + updated.username + "' ";
            uslov = true;
        }
        if(updated.password != null)
        {
            if(uslov)
                sql += ", ";

            sql += "Password = '" + updated.password + "' ";
            uslov = true;
        }
        if(updated.role != null)
        {
            if(uslov)
                sql += ", ";

            sql += "Role = '" + updated.role + "' ";
            uslov = true;
        }

        sql += "WHERE Username = '" + username + "' ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("User doesn't exist");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    // GetFilters f-je

    public static ArrayList<String> GetAllCities()
    {
        sql = "SELECT DISTINCT City FROM Students ";
        ResultSet res = null;
        ArrayList<String> cities = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                cities.add(res.getString("City"));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return cities;
    }

    public static ArrayList<String> GetAllSubjects()
    {
        sql = "SELECT SubjectName, SubjectId FROM Subjects ";
        ResultSet res = null;
        ArrayList<String> subs = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                subs.add(res.getString("SubjectName") + "-" + res.getString("SubjectId"));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return subs;
    }

    public static ArrayList<String> GetAllMajors()
    {
        sql = "SELECT MajorName, MajorId FROM Majors ";
        ResultSet res = null;
        ArrayList<String> majors = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                majors.add(res.getString("MajorName") + "-" + res.getString("MajorId"));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return majors;
    }
}
