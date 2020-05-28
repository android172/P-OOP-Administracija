package poopprojekat.studentska_sluzba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.crypto.Data;

@SpringBootApplication
public class StudentskaSluzbaApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(StudentskaSluzbaApplication.class, args);

        Database db = new Database();
    }

}
