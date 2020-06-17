package poopprojekat.studentska_sluzba.Generators;

import java.time.LocalDate;
import java.util.Random;

public class Birthday_gen {
    public static LocalDate get_random_birth_date() {
        int minDay = (int) LocalDate.of(1990, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2002, 1, 1).toEpochDay();
        long randomDay = minDay + new Random().nextInt(maxDay - minDay);
        return LocalDate.ofEpochDay(randomDay);
    }
    public static String get_random_jmbg(LocalDate birth_date) {
        int year = birth_date.getYear();
        int month = birth_date.getMonthValue();
        int day = birth_date.getDayOfMonth();

        String jmbg = "";
        if (day < 10) jmbg += "0";
        jmbg += day;
        if (month < 10) jmbg += "0";
        jmbg += month;
        if (year < 2000) jmbg += (year - 1000);
        else jmbg += "000" + (year - 2000);
        jmbg += new Random().nextInt(10);
        jmbg += new Random().nextInt(10);
        jmbg += new Random().nextInt(10);
        jmbg += new Random().nextInt(10);
        return jmbg;
    }
}