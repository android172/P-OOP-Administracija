package poopprojekat.studentska_sluzba;

import java.sql.*;

public class Database
{
    private Connection conn = null;
    private Statement stat = null;
    private String sql = null;

    public Database()       // First time run
    {
        ConnectToDatabase("");

        CreateDatabase("Testing");

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

            CreateTableUsers();
            CreateTableSubjects();
            CreateTableExams();
            CreateTableStudents();
            CreateTableExamApplications();
            CreateTableAppliedToListen();
            CreateTableArchive();

            ConnectToDatabase(name);
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
        sql = "CREATE TABLE IF NOT EXISTS Users" +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "Username VARCHAR(30) not NULL," +
                "Password VARCHAR(30) not NULL," +
                "Role VARCHAR(10) not NULL," +
                "PRIMARY KEY (id) )";

        System.out.println("Creating table 'Users'");

        try
        {
            stat.executeQuery(sql);
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
        sql = "CREATE TABLE IF NOT EXISTS Subjects" +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "SubjectName VARCHAR(30) not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "Year INTEGER not NULL," +
                "ESPB INTEGER not NULL," +
                "PRIMARY KEY (id) )";

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
        sql = "CREATE TABLE IF NOT EXISTS Students" +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "IndexNum VARCHAR(10) not NULL," +
                "FirstName VARCHAR(30) not NULL," +
                "LastName VARCHAR(30) not NULL," +
                "JMBG INTEGER not NULL," +
                "DateOfBirth DATE not NULL," +
                "From VARCHAR(15) not NULL," +
                "PRIMARY KEY (id) )";

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
        sql = "CREATE TABLE IF NOT EXISTS AppliedToListen" +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "IndexNum VARCHAR(10) not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "Year INTEGER not NULL," +      // Na kojoj godini je predmet
                "PRIMARY KEY (id) )";

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
        sql = "CREATE TABLE IF NOT EXISTS Exams" +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "ExamDate DATE not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "ProfessorId INTEGER not NULL" +
                "PRIMARY KEY (id) )";

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
        sql = "CREATE TABLE IF NOT EXISTS ExamApplication" +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "IndexNum VARCHAR(10) not NULL," +
                "ExamDate DATE not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "PRIMARY KEY (id) )";

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
        sql = "CREATE TABLE IF NOT EXISTS ExamsArchive" +
                "( id INTEGER not NULL AUTO_INCREMENT," +
                "IndexNum VARCHAR(10) not NULL," +
                "ExamDate DATE not NULL," +
                "SubjectId VARCHAR(10) not NULL," +
                "Mark INTEGER not NULL," +
                "Show VARCHAR(5) not NULL," +
                "Pass/Fail VARCHAR(8) not NULL," +
                "PRIMARY KEY (id) )";

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

    public void AddStudent(Student s)       // JMBG is unique
    {
        sql = "INSERT INTO Students (FirstName, LastName, IndexNum, JMBG, DateOfBirth, From)" +
                "SELECT '" + s.jmbg + "' FROM DUAL" +
                "WHERE NOT EXISTS(SELECT JMBG FROM Students" +
                "WHERE JMBG = '" + s.jmbg + "' LIMIT 1)";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Student added");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public Student GetStudent(Student s)        // s must have jmbg (rest is optional)
    {
        sql = "SELECT * FROM Studends" +
                "WHERE JMBG = '" + s.jmbg + "'";

        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            s.firstName = res.getString("FirstName");
            s.lastName = res.getString("LastName");
            s.indexNum = res.getString("IndexNum");
            s.jmbg = res.getInt("JMBG");
            s.dateOfBirth = res.getDate("DateOfBirth");
            s.city = res.getString("From");

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return s;
    }

    public void AddSubject(Subject s)       // SubjectId is unique
    {
        sql = "INSERT INTO SubjectsAll (SubjectName, SubjectId, Year, ESPB)" +
                "SELECT '" + s.subjectId + "' FROM DUAL" +
                "WHERE NOT EXISTS(SELECT SubjectId FROM SubjectsAll" +
                "WHERE SubjectId = '" + s.subjectId + "' LIMIT 1)";

        try
        {
            stat.executeUpdate(sql);
            System.out.println("Subject added");
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public Subject GetSubject(Subject s)       // s must have subjectId (rest is optional)
    {
        sql = "SELECT * FROM SubjectsAll" +
                "WHERE SubjectId = '" + s.subjectId + "'";

        ResultSet res = null;

        try
        {
            res = stat.executeQuery(sql);

            if(!res.first())
                return null;

            s.subjectName = res.getString("SubjectName");
            s.subjectId = res.getString("SubjectId");
            s.espb = res.getInt("ESPB");
            s.year = res.getInt("Year");

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return s;
    }
}
