package poopprojekat.studentska_sluzba;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import poopprojekat.studentska_sluzba.Logger.MyLogger;

@SpringBootApplication
public class StudentskaSluzbaApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(StudentskaSluzbaApplication.class, args);
        MyLogger logger = new MyLogger();
        logger.start();
        Database db = new Database("testing");
        System.out.println(db);

    //    Database.DropDatabase("testing");

    //    try
    //    {
    //        db.TestDummy();
    //    }
    //    catch (Exception e)
    //    {
    //        e.printStackTrace();
    //    }
    }

}