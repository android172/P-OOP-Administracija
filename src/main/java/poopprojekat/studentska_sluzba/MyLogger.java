package poopprojekat.studentska_sluzba;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyLogger extends Thread{

    private File file = new File("log.txt");
    private FileWriter writer = null;

    LocalDateTime myDateObj = null;
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("[dd-MM-yyyy HH:mm:ss] ");

    public MyLogger() {

        myDateObj = LocalDateTime.now();

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                StartMsg(false);
            }
            else {
                System.out.println("File already exists.");
                StartMsg(true);
            }

        } catch (IOException e) {
            System.out.println("Error creating file");
            e.printStackTrace();
        }
    }

    private void StartMsg(boolean ind){

        try {
            writer = new FileWriter("log.txt", true);

            if (!ind) {
                writer.write("App started: " + LocalDateTime.now().format(myFormatObj) + "\n\n");
            }
            else {
                writer.write(LocalDateTime.now().format(myFormatObj) + "App stated again.\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        ConsoleOutputCapturer capturer = new ConsoleOutputCapturer();

        try {

            while (true) {
                writer = new FileWriter("log.txt", true);
                capturer.start();
                sleep(1000 * 60 * 3 );
                String tmp = capturer.stop();
                if (tmp != null || tmp != "") {
                    writer.write(tmp);
                    System.out.println("Updating log file");
                }
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}