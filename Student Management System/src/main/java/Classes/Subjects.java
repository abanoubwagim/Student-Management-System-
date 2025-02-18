package Classes;

import java.util.ArrayList;


public class Subjects {

    private String subject;
    private String day;
    private Student student;
    private ArrayList<Subjects> subjectsArrayList = new ArrayList<>();


    public Subjects(String subject, String day,Student student) {
        this.subject = subject;
        this.day = day;
        this.student = student;
    }


    public void addSubject(String subject , String day,Student student) {
        Subjects subjects = new Subjects(subject,day,student);
        this.subjectsArrayList.add(subjects);
    }


}

