package poopprojekat.studentska_sluzba.Generators;

import java.util.Random;

public class Major_gen {

    public static String get_random_major() throws Exception {
        if (length < 1) throw new Exception("No more unique majors can be generated.");
        int id = new Random().nextInt(length);
        String generated = majors[id];
        majors[id] = majors[length-- - 1];
        return generated;
    }
    
    private static final String majors[] = {
        "Racunarske nauke",
        "Softversko inzinjerstvo",
        "Telekomunikacije",
        "Matematika"
    };

    private static int length = majors.length;
}