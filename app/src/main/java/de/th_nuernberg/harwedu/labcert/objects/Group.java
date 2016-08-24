package de.th_nuernberg.harwedu.labcert.objects;

import java.util.ArrayList;

/**
 *
 */
public class Group {
    private ArrayList<Student> student;
    private long id;
    private String lab_id;
    private String lab_name;
    private String group;
    private String term;
    private String supervisor;

    public Group(long id, String lab_name, String group, String term, String supervisor,
                 ArrayList<Student> student) {
        this.id = id;
        this.lab_id = lab_id;
        this.lab_name = lab_name;
        this.group = group;
        this.term = term;
        this.supervisor = supervisor;
        this.student = student;
    }

    public Group(long id, String lab_name, String group, String term, String supervisor) {
        this.id = id;
        this.lab_id = lab_id;
        this.lab_name = lab_name;
        this.group = group;
        this.term = term;
        this.supervisor = supervisor;
    }

    public long getId() { return id; }

    public ArrayList<Student> getStudent() {
        return student;
    }

    public void setStudent(ArrayList<Student> student) {
        this.student = student;
    }

    public String getLab_id() { return lab_id; }

    public void setLab_id(String lab_id) { this.lab_id = lab_id; }

    public String getLab_name() { return lab_name; }

    public void setLab_name(String lab_name) { this.lab_name = lab_name; }

    public String getGroup() { return group; }

    public void setGroup(String group) { this.group = group; }

    public String getTerm() { return term; }

    public void setTerm(String term) { this.term = term; }

    public String getSupervisor() { return supervisor; }

    public void setSupervisor(String supervisor) { this.supervisor = supervisor; }
}