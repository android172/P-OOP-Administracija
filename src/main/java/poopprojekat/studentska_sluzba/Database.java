package poopprojekat.studentska_sluzba;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

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
// AddExamDeadline(String name, LocalDate startdate, LocalDate enddate, LocalDate startapp, LocalDate endapp) Adds exam deadlines (example: ExamName = "Januarski")
// ApplyForExam(Index index, String examId, int price)
// AddExam(Exam exam)
// ApplyForSubject(Index index, String subjectId[]) - Applies 1 student to multiple subjects
// ApllyForSubject(Index index[], String subjectId) - Applies multiple students to 1 subject
// AddStatus(Index index, int schoolYear, int year, boolean budget) - (schoolYear = (1,2,3 or 4))

// GetStudents(Date dateOfBirth[], String city[], String MajorName[], int orderBy) - accepts DoB/City/MajorName,
//                                      orderby: 1 - Index; 2 - FirstName; 3 - LastName; 4 - sorts po DoB; 5 - sorts po City; 6 - sorts po MajorId,
//                                      function returns ArrayList<Student> in ascending order
//                                      if all params are null, returns all Students from table
// GetStudent(String jmbg) - return Student with forwarded jmbg
// GetStudent(Index index) - return Student with forwarded index number
// GetSubjects(int year[], String profName[], String majorName[], int orderdy) - returns ArrayList<Subject> in ascending order
//                                                                               orderby: 1 - SubjectName; 2 - SubjectId; 3 - Year; 4 - ESPB; 5 - MajorName; 6 - LectId,
//                                                                               If all params are null, returns all subjects
// GetSubject(String SubjectId) - returns subject
// GetLecturer(int LectId) - returns Lecturer
// GetMajors(String majorName) - returns ArrayList<Major>
// GetMajor(String majorId) - returns Major
// SubjectsOfLecturer(Lecturer p) - returns ArrayList<Subjects> of subjects that lecturer teaches
// GetHighestIndex(int year) - returns highest index number for forwarded year
// GetEmptyId(tableName) - returns first empty id for tables: Lecturers, Majors, Subjects, Exams
// GetUser(String username, String password) - returns String array that contains Role and UniqueId
// GetLecturers(String subjects[], String majors[], int orderby) - returns ArrayList<Lecturer> in ascending order
//                                                                 orderby: 1 - LectId; 2 - FirstName; 3 - LastName; 4 - Title
// GetUser(String id) returns username (String)
// GetExamDeadlines(LocalDate targetdate) returns ArrayList<String> that contains exam names
// GetAvailableExams(Index index, String examName) returns ArrayList<Exam> of available exams that student can apply for
// GetAttempts(Index index, String examId) reutnrs number of attempts for subject
// IfBudget(Index index, int currectYear) returns true if student is on budget
// GetAllStudentsFromSubject(String subjectId, int year) returns ArrayList<Index> of all students that are/were applied for subject in 'year'
                                                        // if year = 0, returns all students from subject over time
// GetAttemtsOfStudent(Index index, int year) - returns ArraList<Attempts>
// GetSubjectsByMajor(String majorId) - returns ArrayList<Subject>, returns all subjects from given majorId
// GetSubjectsByStudent(Index index, int year) - returns ArrayList<Subject>, returns subjects that student applied to listen for given year
                                                // if year = 0, returns all subjects that student was applied to (subjects can repeat if student failed that subject).
// GetStudentsBySubject(String subjectId, int year) - returns ArrayList<Student>, returns all students that applied to given subject in given year.
                                                    // if year = 0, returns all students that were applied on given subject
// GetExamOfLect(String lectId) - returns ArrayList<Exam>, returns all exam where given lecturer is examiner
// GetStudentsByExam(String examId) - returns ArrayList<Attempts>, returns all students that applied for given examId

// EditStudent(Index index, Student updated)
// EditLecturer(int lectId, Lecturer updated)
// EditSubject(String subjectId, Subject updated)
// EditMajor(int majorId, Major updated)
// EditUser(String username, User updated)
// Grading(Index index[], int year, String examId[], mark[], points[]) - Grades students, all arrays must be equal lengths

// DeleteStudent(Index index) - Removes student from table Students
// DeleteLecturer(int lectId) - Removes lecturer from table Lecturers. Automatically removes that lecturer from table Users
// DeleteMajor(int majorId) - Removes major from table Majors
// DeleteSubject(int subjectId) - Removes subject from table Subjects
// DeleteUser(String id) - Removes user from table Users
// DeletExamDeadline(String name) - Removes exam deadline from table ExamDeadline
// DeleteExam(String examId) - Removes exam from table Exams

// GetAllCities() - returns ArrayList<String> of all distinct cities in table Students
// GetAllSubjects() - returns ArrayList<String> (subjectname-SubjectId )
// GetAllMajors() - returns ArrayList<String> (MajorName-MajorId)
// GetAllExams() - returns ArrayList<Exam> of all currently active exams (for current school year)
// GetAllUsers() - returns ArrayList<User> (Username, role, id)

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

        AddUser(new User("admin", "123456789", "Admin", "A001"));

        Fill_db_randomly.with_majors(3);
        Fill_db_randomly.with_students(120);
        Fill_db_randomly.with_lecturers(15);
        Fill_db_randomly.with_Subjects(10);
        Fill_db_randomly.with_exams(6);
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
            CreateExamDeadline();

            CreateTableDeletedStudents();
            CreateTableDeletedSubjects();
            CreateTableDeletedLecturers();
            CreateTableDeletedMajors();
            CreateTableDeletedExams();

            CreateCheckStatusInsertTrigger();
            CreateCheckAppliedInsertTrigger();
            CreateArchiveStudentsTrigger();
            CreateArchiveSubjectsTrigger();
            CreateArchiveExamsTrigger();
            CreateArchiveMajorsTrigger();
            CreateArchiveLecturersTrigger();
            CreateCheckAppliedUpdateIndexTrigger();
            CreateCheckAppliedUpdateIdTrigger();
            CreateCheckStatusUpdateIndexTrigger();
            CreateArchiveExamsFromLecturersTrigger();
            CreateArchiveExamsFromSubjectsTrigger();
            CreateArchiveStudentsFromMajorTrigger();
            CreateArchiveSubjectsFromLecturersTrigger();
            CreateArchiveSubjectsFromMajorTrigger();
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
                "Role VARCHAR(20) not NULL, " +          // Student, Profesor, Admin
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
                "SubjectName NVARCHAR(50) not NULL, " +
                "SubjectId VARCHAR(10) not NULL UNIQUE , " +
                "Year INTEGER not NULL, " +
                "ESPB INTEGER not NULL, " +
                "MajorId VARCHAR(10) not NULL, " +
                "LectId VARCHAR(10) not NULL, " +
                "PointsRequired INTEGER not NULL, " +
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
                "Attempts INTEGER DEFAULT '0', " +
                "Mark INTEGER, " +
                "Points INTEGER DEFAULT '0', " +
                "PRIMARY KEY (id, IndexNum, SubjectId) ) ENGINE=InnoDB ";

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
                "ExamId VARCHAR(10) not NULL UNIQUE, " +
                "ExamDate DATE not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "LectId VARCHAR(10) not NULL," +
                "PRIMARY KEY (id, ExamId)," +
                "FOREIGN KEY (SubjectId) REFERENCES Subjects(SubjectId) ON UPDATE CASCADE ON DELETE CASCADE , " +
                "FOREIGN KEY (LectId) REFERENCES Lecturers(LectId) ON UPDATE CASCADE ON DELETE CASCADE ) ENGINE=InnoDB ";

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
                "IndexNum VARCHAR(10) not NULL UNIQUE ," +
                "ExamId VARCHAR(10) not NULL UNIQUE ," +
                "Price INTEGER not NULL," +
                "PRIMARY KEY (id, IndexNum, ExamId), " +
                "FOREIGN KEY (IndexNum) references Students(IndexNum) ON UPDATE CASCADE ON DELETE CASCADE , " +
                "FOREIGN KEY (ExamId) references Exams(ExamId) ON UPDATE CASCADE ON DELETE CASCADE ) ENGINE=InnoDB ";

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
                "SchoolYear INTEGER not NULL, " +
                "Year INTEGER not NULL, " +
                "Status VARCHAR(20) not NULL, " +
                "PRIMARY KEY (id, IndexNum) ) ENGINE=InnoDB ";

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

    private void CreateExamDeadline()
    {
        sql = "CREATE TABLE IF NOT EXISTS ExamDeadline " +
                "( id INTEGER not NULL AUTO_INCREMENT, " +
                "ExamName VARCHAR(20) not NULL UNIQUE, " +
                "StartDate DATE not NULL, " +
                "EndDate DATE not NULL," +
                "StartApplicationDate DATE not NULL," +
                "EndApplicationDate DATE not NULL," +
                "PRIMARY KEY (id, ExamName) ) ENGINE=InnoDB ";

        System.out.println("Creating table 'ExamDeadline'");
        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'ExamDeadline' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'ExamDeadline' failed");
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

        System.out.println("Creating table 'Lecturers'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'Lecturers' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'Lecturers' failed");
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

    private void CreateTableDeletedStudents()
    {
        sql = "CREATE TABLE IF NOT EXISTS DeletedStudents " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "IndexNum VARCHAR(10) not NULL UNIQUE," +
                "FirstName NVARCHAR(30) not NULL," +
                "LastName NVARCHAR(30) not NULL," +
                "JMBG VARCHAR(15) not NULL UNIQUE," +
                "DateOfBirth DATE not NULL," +
                "City NVARCHAR(30) not NULL," +
                "MajorId VARCHAR(10) not NULL, " +
                "PRIMARY KEY (id, JMBG, IndexNum) ) ENGINE=InnoDB ";

        System.out.println("Creating table 'DeletedStudents'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'DeletedStudents' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'DeletedStudents' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableDeletedSubjects()
    {
        sql = "CREATE TABLE IF NOT EXISTS DeletedSubjects " +
                "( id INTEGER not NULL AUTO_INCREMENT, " +
                "SubjectName NVARCHAR(50) not NULL, " +
                "SubjectId VARCHAR(10) not NULL UNIQUE , " +
                "Year INTEGER not NULL, " +
                "ESPB INTEGER not NULL, " +
                "MajorId VARCHAR(10) not NULL, " +
                "LectId VARCHAR(10) not NULL, " +
                "PointsRequired INTEGER not NULL, " +
                "PRIMARY KEY (id, SubjectId) ) ENGINE=InnoDB ";

        System.out.println("Creating table 'DeletedSubjects'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'DeletedSubjects' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'DeletedSubjects' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableDeletedLecturers()
    {
        sql = "CREATE TABLE IF NOT EXISTS DeletedLecturers " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "LectId VARCHAR(10) not NULL UNIQUE ," +
                "FirstName NVARCHAR(15) not NULL," +
                "LastName NVARCHAR(15) not NULL, " +
                "Title VARCHAR(30) not NULL, " +
                "PRIMARY KEY (id, LectId) ) ENGINE=InnoDB ";

        System.out.println("Creating table 'DeletedLecturer'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'DeletedLecturers' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'DeletedLecturers' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableDeletedMajors()
    {
        sql = "CREATE TABLE IF NOT EXISTS DeletedMajors " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "MajorId VARCHAR(10) not NULL UNIQUE ," +
                "MajorName NVARCHAR(30) not NULL," +
                "PRIMARY KEY(id, MajorId) ) ENGINE=InnoDB";

        System.out.println("Creating table 'DeletedMajors'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'DeletedMajors' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'DeletedMajors' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateTableDeletedExams()
    {
        sql = "CREATE TABLE IF NOT EXISTS DeletedExams " +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "ExamId VARCHAR(10) not NULL UNIQUE, " +
                "ExamDate DATE not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "LectId VARCHAR(10) not NULL," +
                "PRIMARY KEY (id, ExamId) ) ENGINE=InnoDB ";

        System.out.println("Creating table 'DeletedExams'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("Table 'DeletedExams' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating table 'DeletedExams' failed");
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

    // Triggers

    private void CreateCheckStatusInsertTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS CheckStatusInsert BEFORE INSERT ON StudentStatus " +
                "FOR EACH ROW " +
                "IF new.IndexNum NOT IN (SELECT s.IndexNum FROM Students as s) " +
                "THEN  SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Student does not exist'; " +
                "END IF; ";

        System.out.println("Creating trigger 'CheckStatusInsert'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'CheckStatusInsert' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'CheckStatusInsert' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateCheckStatusUpdateIndexTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS CheckStatusUpdateIndex AFTER UPDATE ON Students " +
                "FOR EACH ROW " +
                "UPDATE StudentStatus " +
                "SET IndexNum = new.IndexNum " +
                "WHERE IndexNum = old.IndexNum ";

        System.out.println("Creating trigger 'CheckStatusUpdateIndex'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'CheckStatusUpdateIndex' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'CheckStatusUpdateIndex' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateCheckAppliedInsertTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS CheckAppliedInsert BEFORE INSERT ON AppliedToListen " +
                "FOR EACH ROW " +
                "IF new.IndexNum NOT IN (SELECT s.IndexNum FROM Students as s)" +
                "THEN  SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Student does not exist'; " +
                "ELSEIF new.SubjectId NOT IN (SELECT s.SubjectId FROM Subjects as s) " +
                "THEN SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Subject does not exist';" +
                "END IF; ";

        System.out.println("Creating trigger 'CheckAppliedInsert'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'CheckAppliedInsert' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'CheckAppliedInsert' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateCheckAppliedUpdateIndexTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS CheckAppliedUpdateIndex AFTER UPDATE ON Students " +
                "FOR EACH ROW " +
                "UPDATE AppliedToListen " +
                "SET IndexNum = new.IndexNum " +
                "WHERE IndexNum = old.IndexNum ";

        System.out.println("Creating trigger 'CheckAppliedUpdateIndex'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'CheckAppliedUpdateIndex' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'CheckAppliedUpdateIndex' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateCheckAppliedUpdateIdTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS CheckAppliedUpdateId AFTER UPDATE ON Subjects " +
                "FOR EACH ROW " +
                "UPDATE AppliedToListen " +
                "SET SubjectId = new.SubjectId " +
                "WHERE SubjectId = old.SubjectId ";

        System.out.println("Creating trigger 'CheckAppliedUpdateId'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'CheckAppliedUpdateId' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'CheckAppliedUpdateId' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateArchiveStudentsTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS ArchiveStudents AFTER DELETE ON Students FOR EACH ROW " +
                "INSERT INTO DeletedStudents (IndexNum, FirstName, LastName, JMBG, DateOfBirth, City, MajorId) " +
                "VALUES (old.IndexNum, old.FirstName, old.LastName, old.JMBG, old.DateOfBirth, old.City, old.MajorId) ";

        System.out.println("Creating trigger 'ArchiveStudents'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'ArchiveStudents' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'ArchiveStudents' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateArchiveStudentsFromMajorTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS ArchiveStudentsFromMajor AFTER DELETE ON Majors FOR EACH ROW " +
                "INSERT INTO DeletedStudents (IndexNum, FirstName, LastName, JMBG, DateOfBirth, City, MajorId) " +
                "SELECT IndexNum, FirstName, LastName, JMBG, DateOfBirth, City, MajorId FROM Students " +
                "WHERE MajorId = old.MajorId ";

        System.out.println("Creating trigger 'ArchiveStudentsFromMajor'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'ArchiveStudentsFromMajor' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'ArchiveStudentsFromMajor' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateArchiveLecturersTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS ArchiveLecturers AFTER DELETE ON Lecturers FOR EACH ROW " +
                "INSERT INTO DeletedLecturers (LectId, FirstName, LastName, Title) " +
                "VALUES (old.LectId, old.FirstName, old.LastName, old.Title) ";

        System.out.println("Creating trigger 'ArchiveLecturers'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'ArchiveLecturers' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'ArchiveLecturers' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateArchiveSubjectsTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS ArchiveSubjects AFTER DELETE ON Subjects FOR EACH ROW " +
                "INSERT INTO DeletedSubjects (SubjectName, SubjectId, Year, ESPB, MajorId, LectId, PointsRequired) " +
                "VALUES (old.SubjectName, old.SubjectId, old.Year, old.ESPB, old.MajorId, old.LectId, old.POintsRequired) ";

        System.out.println("Creating trigger 'ArchiveSubjects'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'ArchiveSubjects' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'ArchiveSubjects' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateArchiveSubjectsFromMajorTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS ArchiveSubjectsFromMajor AFTER DELETE ON Majors FOR EACH ROW " +
                "INSERT INTO DeletedSubjects (SubjectName, SubjectId, Year, ESPB, MajorId, LectId, PointsRequired) " +
                "SELECT SubjectName, SubjectId, Year, ESPB, MajorId, LectId, PointsRequired FROM Subjects " +
                "WHERE MajorId = old.MajorId ";

        System.out.println("Creating trigger 'ArchiveSubjectsFromMajor'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'ArchiveSubjectsFromMajor' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'ArchiveSubjectsFromMajor' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateArchiveSubjectsFromLecturersTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS ArchiveSubjectsFromLecturers AFTER DELETE ON Lecturers FOR EACH ROW " +
                "INSERT INTO DeletedSubjects (SubjectName, SubjectId, Year, ESPB, MajorId, LectId, PointsRequired) " +
                "SELECT SubjectName, SubjectId, Year, ESPB, MajorId, LectId, PointsRequired FROM Subjects " +
                "WHERE LectId = old.LectId ";

        System.out.println("Creating trigger 'ArchiveSubjectsFromLecturers'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'ArchiveSubjectsFromLecturers' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'ArchiveSubjectsFromLecturers' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateArchiveMajorsTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS ArchiveMajors AFTER DELETE ON Majors FOR EACH ROW " +
                "INSERT INTO DeletedMajors (MajorId, MajorName) " +
                "VALUES (old.MajorId, old.MajorName) ";

        System.out.println("Creating trigger 'ArchiveMajors'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'ArchiveMajors' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'ArchiveMajors' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateArchiveExamsTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS ArchiveExams AFTER DELETE ON Exams FOR EACH ROW " +
                "INSERT INTO DeletedExams (ExamId, ExamDate, SubjectId, LectId) " +
                "VALUES (old.ExamId, old.ExamDate, old.SubjectId, old.LectId) ";

        System.out.println("Creating trigger 'ArchiveExams'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'ArchiveExams' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'ArchiveExams' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateArchiveExamsFromSubjectsTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS ArchiveExamsFromSubjects AFTER DELETE ON Subjects FOR EACH ROW " +
                "INSERT INTO DeletedExams (ExamId, ExamDate, SubjectId, LectId) " +
                "SELECT ExamId, ExamDate, SubjectId, LectId FROM Exams " +
                "WHERE SubjectId = old.SubjectId ";

        System.out.println("Creating trigger 'ArchiveExamsFromSubjects'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'ArchiveExamsFromSubjects' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'ArchiveExamsFromSubjects' failed");
            throwables.printStackTrace();
        }
    }

    private void CreateArchiveExamsFromLecturersTrigger()
    {
        sql = "CREATE TRIGGER IF NOT EXISTS ArchiveExamsFromLecturers AFTER DELETE ON Lecturers FOR EACH ROW " +
                "INSERT INTO DeletedExams (ExamId, ExamDate, SubjectId, LectId) " +
                "SELECT ExamId, ExamDate, SubjectId, LectId FROM Exams " +
                "WHERE LectId = old.LectId ";

        System.out.println("Creating trigger 'ArchiveExamsFromLecturers'");

        try
        {
            stat.executeQuery(sql);
            System.out.println("trigger 'ArchiveExamsFromLecturers' has been created");
        }
        catch (SQLException throwables)
        {
            System.out.println("Creating trigger 'ArchiveExamsFromLecturers' failed");
            throwables.printStackTrace();
        }
    }

    // ADD f-je -----------------------------------------------------

    // Za AddX f-je sve obavezne promenjive moraju biti popunjene

    public static void AddStudent(Student s) throws Exception       // JMBG and indexNum is unique
    {
        sql = "INSERT INTO Students (FirstName, LastName, IndexNum, JMBG, DateOfBirth, City, MajorId) " +
                "VALUES ( '" + s.getFirstName() + "', '" + s.getLastName() + "', '" + s.getIndex() + "', '" + s.getJmbg() + "', '" + s.getDateOfBirth() + "', '" + s.getCity() + "', '" + s.getMajorId() + "' )";
        try
        {
            stat.executeUpdate(sql);
            System.out.println("Student added");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("Index number or JMBG is already in table Students");
            throwables.printStackTrace();
        }
    }

    public static void AddSubject(Subject s) throws Exception       // SubjectId is unique
    {
        sql = "INSERT INTO Subjects (SubjectName, SubjectId, Year, ESPB, MajorId, LectId, PointsRequired) " +
                "VALUES ( '" + s.subjectName + "', '" + s.subjectId + "', '" + s.year + "', '" + s.espb +"', '" + s.majorid +"', '" + s.lectid +"', '" + s.points_required +"' ) ";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Subject added");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("SubjectId is already in table Subjects");
            throwables.printStackTrace();
        }
    }

    public static void AddLecturer(Lecturer p) throws Exception       // LectId is unique
    {
        sql = "INSERT INTO Lecturers (LectId, FirstName, LastName, Title) " +
                "VALUES ( '" + p.getLectId() + "', '" + p.getFirstName() +"', '" + p.getLastName() +"', '" + p.getTitle() + "' )";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Lecturer added");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("LectId is already in table Lecturers");
            throwables.printStackTrace();
        }
    }

    public static void AddMajor(Major m) throws Exception        // MajorId is unique
    {
        sql = "INSERT INTO Majors (MajorId, MajorName) " +
                "VALUES ( '" + m.id + "', '" + m.name +"' ) ";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Major added");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("MajorId is already in table Majors");
            throwables.printStackTrace();
        }
    }

    public static void AddUser(User user) throws Exception
    {
        sql = "INSERT INTO Users (Username, Password, Role, UniqueId) " +
                "VALUES ( '" + user.username + "', '" + user.password + "', '" + user.role + "', '" + user.id + "' ) ";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("User added");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("Username is already in table Users");
            throwables.printStackTrace();
        }
    }

    public static void AddExamDeadline(String name, LocalDate startdate, LocalDate enddate, LocalDate startapp, LocalDate endapp) throws Exception
    {
        sql = "INSERT INTO ExamDeadline (ExamName, StartDate, EndDate, StartApplicationDate, EndApplicationDate) " +
                "VALUES ( '" + name + "', '" + startdate + "', '" + enddate + "', '" + startapp + "', '" + endapp +"' ) ";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("ExamDeadline added");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("ExamName is already in table ExamDeadline");
            throwables.printStackTrace();
        }
    }

    public static void ApplyForExam(Index index, String examId, int price) throws Exception
    {
        sql = "INSERT INTO ExamApplication (IndexNum, ExamId, Price) " +
                "SELECT al.IndexNum, e.ExamId, '" + price + "' " +
                "FROM AppliedToListen as al join Exams as e on e.SubjectId = al.SubjectId join Subjects as s on e.SubjectId = s.Subjectid " +
                "WHERE al.IndexNum = '" + index + "' AND e.ExamId = '" + examId + "' AND al.DatePassed is null AND al.Points >= s.PointsRequired ";

        try
        {
            if(stat.executeUpdate(sql) != 0)
            {
                IncAttempts(index);
                System.out.println("Exam applied");
            }
            else
                System.out.println("Student doesn't have enough points");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("Student is already applied for this exam");
            throwables.printStackTrace();
        }
    }

    public static void AddExam(Exam exam) throws Exception
    {
        sql = "INSERT INTO Exams (ExamId, ExamDate, SubjectId, LectId) " +
                "VALUES ( '" + exam.getId() + "', '" + exam.getDate() + "', '" + exam.getSubject_id() + "', '" + exam.getLect_id() + "' )";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Exam added");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("ExamId is already in table Users");
            throwables.printStackTrace();
        }
    }

    public static void ApplyForSubject(Index index, String subjectId[]) throws Exception
    {
        if(index != null && subjectId != null)
            sql = "INSERT INTO AppliedToListen (IndexNum, SubjectId, Year) " +
                    "VALUES ";
        else
        {
            throw new Exception("index or subjectId[] is null");
        }

        int currentyear = LocalDate.now().getYear();

        for(int i=0;i<subjectId.length;i++)
        {
            sql += "( '" + index + "', '" + subjectId[i] + "', '" + currentyear + "' ) ";

            if(i != subjectId.length - 1)
                sql += ", ";
        }

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Subjects applied");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("Student is already applied to this subject");
            throwables.printStackTrace();
        }
    }

    public static void ApplyForSubject(Index index[], String subjectId) throws Exception
    {
        if(index != null && subjectId != null)
            sql = "INSERT INTO AppliedToListen (IndexNum, SubjectId, Year) " +
                    "VALUES ";
        else
        {
            throw new Exception("index[] or subjectId is null");
        }

        int currentyear = LocalDate.now().getYear();

        for(int i=0;i<index.length;i++)
        {
            sql += "( '" + index[i] + "', '" + subjectId + "', '" + currentyear + "' ) ";

            if(i != index.length - 1)
                sql += ", ";
        }

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Subject applied");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("Student is already applied to this subject");
            throwables.printStackTrace();
        }
    }

    public static void AddStatus(Index index, int schoolYear, int year, boolean budget) throws Exception
    {
        String temp;

        if(budget)
            temp = "Budzet";
        else
            temp = "Samofinansirajuci";

        sql = "INSERT INTO StudentStatus (IndexNum, SchoolYear, Year, Status) " +
                "VALUES ('" + index + "', '" + schoolYear +"', '" + year + "', '" + temp + "') ";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Student status added");
        }
        catch (SQLException throwables)
        {
            if(throwables.getErrorCode() == 1062)
                throw new Exception("Student status is already in table StudentStatus");
            throwables.printStackTrace();
        }
    }

    // GET f-je --------------------------------------

    public static ArrayList<Student> GetStudents(LocalDate dateOfBirth[], String city[], String majorId[], int orderBy) {
        ArrayList<Student> lista = new ArrayList<>();
        boolean uslov = false;
        ResultSet res;
        String sqlt;

        if(orderBy == 6)
            sqlt = "SELECT * FROM Students as s join Majors as m on s.majorId = m.majorId " +
                    "WHERE ";
        else
            sqlt = "SELECT * FROM Students as s " +
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
                sqlt += "ORDER BY s.Firstname ASC ";//
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
                        s = new Student(res.getString("FirstName"), res.getString("LastName"), new Index(res.getString("IndexNum")), res.getDate("DateOfBirth").toLocalDate());
                        lista.add(s);
                    }while (res.next());
                    break;
                case 5:
                    do
                    {
                        s = new Student(res.getString("FirstName"), res.getString("LastName"), res.getString("City"), new Index(res.getString("IndexNum")));
                        lista.add(s);
                    }while (res.next());
                    break;
                case 6:
                    do
                    {
                        s = new Student(res.getString("FirstName"), res.getString("LastName"), new Index(res.getString("IndexNum")), res.getString("s.MajorId"));
                        lista.add(s);
                    }while (res.next());
                    break;
                default:
                    do
                    {
                        s = new Student(res.getString("FirstName"), res.getString("LastName"), new Index(res.getString("IndexNum")));
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

            s = new Student(res.getString("FirstName"), res.getString("LastName"), new Index(res.getString("IndexNum")), res.getDate("DateOfBirth").toLocalDate(), res.getString("City"), res.getString("JMBG"), res.getString("MajorId"));

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

            s = new Student(res.getString(  "FirstName"), res.getString("LastName"), new Index(res.getString("IndexNum")) , res.getDate("DateOfBirth").toLocalDate(), res.getString("City"), res.getString("JMBG"), res.getString("MajorId"));

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

    public static ArrayList<Subject> GetSubjects(int year[], String profName[], String majorId[], int orderBy)
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

        if(year != null)
        {
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
                tempsubject = new Subject(res.getString("SubjectName"), res.getString("SubjectId"), res.getInt("ESPB"), res.getInt("Year"), res.getString("LectId"), res.getString("MajorId"), res.getInt("PointsRequired"));
                subjects.add(tempsubject);
            }while(res.next());

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }


        //test
        for (Subject subject : subjects) {
            System.out.println(subject);
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

            s = new Subject(res.getString("SubjectName"), res.getString("SubjectId"), res.getInt("ESPB"), res.getInt("Year"), res.getString("LectId"), res.getString("MajorId"), res.getInt("PointsRequired"));

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return s;
    }

    public static String GetLecturerFromSubjectId(String subjectId)
    {
        sql = "SELECT LectId FROM Subjects " +
                "WHERE SubjectId = '" + subjectId + "' ";

        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            return res.getString("LectId");

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return null;
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
        String sqlt;

        if(subjects != null || majorId != null)
            sqlt = "SELECT * FROM Lecturers as l join Subjects as s on l.LectId = s.LectId " +
                    "WHERE ";
        else
            sqlt = "SELECT * FROM Lecturers as l " +
                    "WHERE ";

        if(subjects != null)
        {
            sqlt += "( ";

             for(int i=0; i<subjects.length; i++)
            {
                sqlt += " s.SubjectId = '" + subjects[i] + "' OR ";
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
        System.out.println(sqlt);
        try
        {
            res = stat.executeQuery(sqlt);
            if(!res.first())
                return null;

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
        else if(tableName == "Exams")
        {
            sql = "SELECT ExamId FROM Exams";
            prefix = "E";
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

    public static ArrayList<ExamDeadline> GetExamDeadlines(LocalDate targetdate)
    {
        if(targetdate == null)
            sql = "SELECT * FROM ExamDeadline ";

        else
            sql = "SELECT * FROM ExamDeadline " +
                    "WHERE StartDate <= '" + targetdate + "' AND EndDate >= '" + targetdate + "' ";

        ResultSet res = null;
        ArrayList<ExamDeadline> deadlines = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                deadlines.add(new ExamDeadline(res.getString("ExamName"), res.getDate("StartDate").toLocalDate(), res.getDate("EndDate").toLocalDate(), res.getDate("StartApplicationDate").toLocalDate(), res.getDate("EndApplicationDate").toLocalDate()));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return deadlines;
    }

    public static ArrayList<Exam> GetAvailableExams(Index index, String examName) throws Exception
    {
        if(index != null && examName != null)
                sql = "SELECT * FROM Exams as e join ExamDeadline as ed on (ed.ExamName = '" + examName + "' AND ed.StartDate <= e.ExamDate AND ed.EndDate >= e.ExamDate) " +
                    "join AppliedToListen as al on e.SubjectId = al.SubjectId join Subjects as s on e.SubjectId = s.SubjectId " +
                    "WHERE al.IndexNum = '" + index + "' AND al.DatePassed is null AND al.Points >= s.PointsRequired ";
        else
            throw new Exception("Index or examName is null");

        ResultSet res = null;
        ArrayList<Exam> available = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                available.add(new Exam(res.getString("e.ExamId"), res.getString("e.SubjectId"), res.getString("e.LectId"), res.getDate("e.ExamDate").toLocalDate()));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return available;
    }

    public static int GetAttempts(Index index, String examId) throws Exception
    {
        if(index != null && examId != null)
            sql = "SELECT * FROM AppliedToListen as al join Exams as e on al.SubjectId = e.ExamId " +
                    "WHERE al.IndexNum = '" + index + "' AND e.ExamId = '" + examId + "' ";
        else
            throw new Exception("Index or ExamId is null");

        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                throw new Exception("No results");

            return res.getInt("al.Attempts");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return -1;
    }

    public static boolean IfBudget(Index index, int currentYear) throws Exception
    {
        if(index != null && currentYear != 0)
            sql = "SELECT * FROM StudentStatus " +
                    "WHERE IndexNum = '" + index + "' AND Year = " + currentYear + " ";
        else
            throw new Exception("Index or currentYear is null");

        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                throw new Exception("No results");

            if(res.getString("Status") == "Budzet")
                return true;
            else if(res.getString("Status") == "Samofinansirajuci")
                return false;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return false;
    }

    public static ArrayList<Index> GetAllStudentsFromSubject(String subjectId, int year) throws Exception
    {
        if(subjectId != null)
        {
            sql = "SELECT IndexNum FROM AppliedToListen " +
                    "WHERE SubjectId = '" + subjectId + "' ";

            if(year != 0)
            {
                sql += "AND Year = " + year + " ";
            }
        }
        else
            throw new Exception("SubjectId is null");

        ResultSet res = null;
        ArrayList<Index> applied = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                applied.add(new Index(res.getString("IndexNum")));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return applied;
    }

    public static ArrayList<Attempts> GetAttemptsOfStudent(Index index, int year) throws Exception
    {
        if(index != null)
        {
            sql = "SELECT * FROM AppliedToListen as al join Subjects as s on al.SubjectId = s.SubjectId " +
                    "WHERE al.IndexNum = '" + index + "' ";

            if(year != 0)
                sql += "AND Year = '" + year + "' ";
        }
        else
            throw new Exception("Index is null");

        ResultSet res = null;
        ArrayList<Attempts> attempts = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                if(res.getDate("DatePassed") == null)
                    attempts.add(new Attempts(new Index(res.getString("al.IndexNum")), res.getString("al.SubjectId"), res.getInt("al.Year"), null, res.getInt("al.Attempts"), res.getInt("al.Mark"), res.getInt("al.Points")));
                else
                    attempts.add(new Attempts(new Index(res.getString("al.IndexNum")), res.getString("al.SubjectId"), res.getInt("al.Year"), res.getDate("al.DatePassed").toLocalDate(), res.getInt("al.Attempts"), res.getInt("al.Mark"), res.getInt("al.Points")));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return attempts;
    }

    public static ArrayList<Subject> GetSubjectsByMajor(String majorId) throws Exception
    {
        sql = "SELECT * FROM Subjects " +
                "WHERE MajorId = '" + majorId + "' ";

        ResultSet res = null;
        ArrayList<Subject> subjects = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                subjects.add(new Subject(res.getString("SubjectName"), res.getString("SubjectId"), res.getInt("ESPB"), res.getInt("Year"), res.getString("LectId"), res.getString("MajorId"), res.getInt("PointsRequired")));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return subjects;
    }

    public static ArrayList<Subject> GetSubjectsByStudent(Index index, int year) throws Exception
    {
        if(index != null)
        {
            sql = "SELECT * FROM AppliedToListen as al join Subjects as s on al.SubjectId = s.SubjectId" +
                    "WHERE al.IndexNum = '" + index + "' AND al.DatePassed is null ";

            if(year != 0)
                sql += "AND al.Year = '" + year + "' ";
        }
        else
            throw new Exception("Index is null");

        ResultSet res = null;
        ArrayList<Subject> subjects = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                subjects.add(new Subject(res.getString("s.SubjectName"), res.getString("s.SubjectId"), res.getInt("s.ESPB"), res.getInt("s.Year"), res.getString("ss.LectId"), res.getString("s.MajorId"), res.getInt("s.PointsRequired")));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return subjects;
    }

    public static ArrayList<Student> GetStudentsBySubject(String subjectId, int year) throws Exception
    {
        if(subjectId != null)
        {
            sql = "SELECT * FROM AppliedToListen as al join Students as s on al.IndexNum = s.IndexNum " +
                    "WHERE al.SubjectId = '" + subjectId + "' AND DatePassed is null ";

            if(year != 0)
                sql += "AND al.Year = '" + year + "'";
        }
        else
            throw new Exception("SubjectId is null");

        ResultSet res = null;
        ArrayList<Student> listen = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                listen.add(new Student(res.getString("s.FirstName"), res.getString("s.LastName"), new Index(res.getString("s.IndexNum"))));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return listen;
    }

    public static ArrayList<Attempts> GetStudentsBySubject(String subjectId) throws Exception
    {
        if(subjectId != null)
        {
            sql = "SELECT * FROM AppliedToListen " +
                    "WHERE SubjectId = '" + subjectId + "' AND YEAR = '" + LocalDate.now().getYear() + "' ";
        }
        else
            throw new Exception("SubjectId is null");

        ResultSet res = null;
        ArrayList<Attempts> applied = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                if(res.getDate("DatePassed") == null)
                    applied.add(new Attempts(new Index(res.getString("IndexNum")), res.getString("SubjectId"), res.getInt("Year"), null, res.getInt("Attempts"), res.getInt("Mark"), res.getInt("Points")));
                else
                    applied.add(new Attempts(new Index(res.getString("IndexNum")), res.getString("SubjectId"), res.getInt("Year"), res.getDate("DatePassed").toLocalDate(), res.getInt("Attempts"), res.getInt("Mark"), res.getInt("Points")));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return applied;
    }

    public static ArrayList<Exam> GetExamOfLect(String lectId) throws Exception
    {
        if(lectId != null)
        {
            sql = "SELECT * FROM Exams " +
                    "WHERE LectId = '" + lectId + "' ";
        }
        else
            throw new Exception("LectId is null");

        ResultSet res = null;
        ArrayList<Exam> exams = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                exams.add(new Exam(res.getString("ExamId"), res.getString("SubjectId"), res.getString("LectId"), res.getDate("ExamDate").toLocalDate()));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return exams;
    }

    public static ArrayList<Attempts> GetStudentsByExam(String examId) throws Exception
    {
        if(examId != null)
        {
            sql = "SELECT * FROM AppliedToListen as al " +
                    "WHERE al.IndexNum IN (SELECT ea.IndexNum FROM ExamApplication as ea " +
                                        "WHERE ea.ExamId = '" + examId + "' )";
        }
        else
            throw new Exception("ExamId is null");

        ResultSet res = null;
        ArrayList<Attempts> applied = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                applied.add(new Attempts(new Index(res.getString("al.IndexNum")), res.getString("al.SubjectId"), res.getInt("al.Year"), res.getDate("DatePassed").toLocalDate(), res.getInt("al.Attempts"), res.getInt("Mark"), res.getInt("al.Points")));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return applied;
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

    public static void DeleteExamDeadline(String name) throws Exception
    {
        sql = "DELETE FROM ExamDeadline " +
                "WHERE ExamName = '" + name + "' ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("ExamName doesn't exist");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public static void DeleteExam(String examid) throws Exception
    {
        sql = "DELETE FROM Exams " +
                "WHERE ExamId = '" + examid + "' ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("ExamId doesn't exist");
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

    public static void Grading(Index index[], int year, String examId[], int mark[], int points[]) throws Exception
    {
        sql = "INSERT INTO AppliedToListen (IndexNum, SubjectId, DatePassed, Year, Mark, Points) " +
                "VALUES ";

        for(int i = 0; i<index.length;i++)
        {
            sql += "( '" + index[i] + "' , (SELECT SubjectId FROM Exams WHERE ExamId = " + examId[i] + "'), (SELECT ExamDate FROM Exams WHERE ExamId = '" + examId[i] + "') '" + year + "', '" + mark[i] + "', '" + points[i] + "' ) ";

            if(i < index.length - 1)
                sql += ", ";
        }

        sql += "ON DUPLICATE KEY UPDATE " +
                "Mark = VALUES(Mark), Points = VALUES(Points), DatePassed = VALUES(DatePassed) ";

        try
        {
            if(stat.executeUpdate(sql) == 0)
                throw new Exception("Couldn't update");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    private static void IncAttempts(Index index)
    {
        sql = "UPDATE AppliedToListen " +
                "SET Attempts = Attempts + 1 " +
                "WHERE IndexNum = new.IndexNum ";

        try
        {
            stat.executeUpdate(sql);
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
                subs.add(res.getString("SubjectName") + "|" + res.getString("SubjectId"));
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
                majors.add(res.getString("MajorName") + "|" + res.getString("MajorId"));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return majors;
    }

    public static ArrayList<Exam> GetAllExams()
    {
        sql = "SELECT * FROM Exams ";
        ResultSet res = null;
        ArrayList<Exam> exams = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                exams.add(new Exam(res.getString("ExamId"), res.getString("SubjectId"), res.getString("LectId"), res.getDate("ExamDate").toLocalDate()));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return exams;
    }

    public static ArrayList<User> GetAllUsers()
    {
        sql = "SELECT * FROM Users ";

        ResultSet res = null;
        ArrayList<User> users = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                users.add(new User(res.getString("Username"), res.getString("Role"), res.getString("UniqueId")));
            }while(res.next());

            return users;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return users;
    }

    public static ArrayList<String> GetAllLecturers()
    {
        sql = "SELECT * FROM Lecturers ";

        ResultSet res = null;
        ArrayList<String> lects = new ArrayList<>();

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            do
            {
                lects.add(res.getString("FirstName") + " " + res.getString("LastName") + "|" + res.getString("LectId"));
            }while(res.next());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return lects;
    }

}
