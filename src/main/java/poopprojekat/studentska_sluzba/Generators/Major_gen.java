package poopprojekat.studentska_sluzba.Generators;

import java.util.Random;

public class Major_gen {

    public static String get_random_major() {
        return majors[r_m.nextInt()];
    }
    
    private static final String majors[] = {
        "M281",
        "M221",
        "M2912",
        "M9182",
        "M9928",
        "M1123",
        "M907"
    };
    private static final Random r_m = new Random(majors.length);

}