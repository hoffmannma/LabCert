package de.th_nuernberg.harwedu.labcert.objects;

/**
 * Created by Edu on 21.08.2016.
 */
public class Group {
    private Student[] Student;
    private String group_id;
    private String lab_id;
    private String lab;


    private String supervisor;

    public Group(de.th_nuernberg.harwedu.labcert.objects.Student[] student, String group_id, String lab_id) {
        Student = student;
        this.group_id = group_id;
        this.lab_id = lab_id;
    }

    public Group(String group_id, String lab_id, String lab, String supervisor) {
        this.group_id = group_id;
        this.lab_id = lab_id;
        this.lab = lab;
        this.supervisor = supervisor;
    }

    public de.th_nuernberg.harwedu.labcert.objects.Student[] getStudent() {
        return Student;
    }

    public void setStudent(de.th_nuernberg.harwedu.labcert.objects.Student[] student) {
        Student = student;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getLab_id() {
        return lab_id;
    }

    public void setLab_id(String lab_id) {
        this.lab_id = lab_id;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
}
