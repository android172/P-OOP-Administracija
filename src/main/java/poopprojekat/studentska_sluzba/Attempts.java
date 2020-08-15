package poopprojekat.studentska_sluzba;

import java.time.LocalDate;

public class Attempts {
    private Index index;
    private String id_subject;
    private int year;
    private LocalDate date;
    private int number_of_attempts;
    private int grade;
    private int points;
    private String examiner;

    public Attempts(Index index, String id_subject, int year, LocalDate date, int number_of_attempts, int grade, int points) {
        this.index = index;
        this.id_subject = id_subject;
        this.year = year;
        this.date = date;
        this.number_of_attempts = number_of_attempts;
        this.grade = grade;
        this.points = points;
    }

    public LocalDate getDate() {
        return date;
    }
    public String getExaminer() {
        return examiner;
    }
    public int getGrade() {
        return grade;
    }
    public String getId_subject() {
        return id_subject;
    }
    public Index getIndex() {
        return index;
    }
    public int getNumber_of_attempts() {
        return number_of_attempts;
    }
    public int getPoints() {
        return points;
    }
    public int getYear() {
        return year;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setExaminer(String examiner) {
        this.examiner = examiner;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }
    public void setId_subject(String id_subject) {
        this.id_subject = id_subject;
    }
    public void setIndex(Index index) {
        this.index = index;
    }
    public void setNumber_of_attempts(int number_of_attempts) {
        this.number_of_attempts = number_of_attempts;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public void setYear(int year) {
        this.year = year;
    }
}