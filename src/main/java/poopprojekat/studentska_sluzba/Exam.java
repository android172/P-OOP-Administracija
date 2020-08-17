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

class ExamDeadline {

    private String name;
    private LocalDate deadline_begin;
    private LocalDate deadline_end;
    private LocalDate application_begin;
    private LocalDate application_end;

    public ExamDeadline(String name, LocalDate deadline_begin, LocalDate deadline_end, LocalDate application_begin, LocalDate application_end) {
        this.name = name;
        this.deadline_begin = deadline_begin;
        this.deadline_end = deadline_end;
        this.application_begin = application_begin;
        this.application_end = application_end;
    }

    public LocalDate getApplication_begin() {
        return application_begin;
    }
    public LocalDate getApplication_end() {
        return application_end;
    }
    public LocalDate getDeadline_begin() {
        return deadline_begin;
    }
    public LocalDate getDeadline_end() {
        return deadline_end;
    }
    public String getName() {
        return name;
    }

    public void setApplication_begin(LocalDate application_begin) {
        this.application_begin = application_begin;
    }
    public void setApplication_end(LocalDate application_end) {
        this.application_end = application_end;
    }
    public void setDeadline_begin(LocalDate deadline_begin) {
        this.deadline_begin = deadline_begin;
    }
    public void setDeadline_end(LocalDate deadline_end) {
        this.deadline_end = deadline_end;
    }
    public void setName(String name) {
        this.name = name;
    }
}