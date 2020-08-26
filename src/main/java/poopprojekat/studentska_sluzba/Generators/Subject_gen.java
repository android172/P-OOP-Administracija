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
        "Informatika",
        "Osnovi programiranja",
        "Strukture podataka i algoritmi",
        "Objektno-Orijentisano programiranje",
        "Softverski alati",
        "Računarski sistemi",
        "Uvod u geometriju",
        "Engleski jezik",
        "Diskretna matematika",
        "Linearna algebra",
        "Analitička geometrija",
        "Analiza 1",
        "Analiza 2",
        "Analiza 3",
        "Analiza 4",
        "Funkcionalna analiza",
        "Verovatnoća i statistika",
        "Geometrija",
        "Algebarske strukture",
        "Računarske mreže",
        "Paralelno programiranje",
        "Operativni sistemi",
        "Veb programiranje",
        "Računarske simulacije",
        "Nauka o podacima",
        "Distributivni sistemi",
        "Optimizacione metode u računarstvu",
        "Robotika"
    };
}