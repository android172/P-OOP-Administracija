package poopprojekat.studentska_sluzba;

import java.time.LocalDate;

public class Exam {
    private String id;
    private String subject_id;
    private String lect_id;
    private LocalDate date;

    public Exam(String id, String subject_id, String lect_id, LocalDate date) {
        this.id = id;
        this.subject_id = subject_id;
        this.lect_id = lect_id;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
    public String getId() {
        return id;
    }
    public String getLect_id() {
        return lect_id;
    }
    public String getSubject_id() {
        return subject_id;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setLect_id(String lect_id) {
        this.lect_id = lect_id;
    }
    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }
}