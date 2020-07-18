package poopprojekat.studentska_sluzba.Generators;

import java.util.Random;

public class Major_gen {

    public static String get_random_major() {
        return majors[new Random().nextInt(majors.length)];
    }
    
    private static final String majors[] = {
        "Racunarske nauke",
        "Softversko inzinjerstvo",
        "Telekomunikacije"
    };

}