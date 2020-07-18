package poopprojekat.studentska_sluzba.Generators;

import java.util.Random;

public class Subject_gen {
    
    public static String get_random_subject() {
        return Subjects[new Random().nextInt(Subjects.length)];
    }
    
    private static final String Subjects[] = {
        "Matematika 1",
        "Matematika 2",
        "Matematika 3",
        "Informatika"
    };
}