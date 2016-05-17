package de.th_nuernberg.harwedu.labcert.database;

/**
 * Created by Edu on 17.05.2016.
 */
public class Student {

    private String surname;
    private String firstname;
    private String comment;
    private String group;
    private String team;
    private String matr;
    private String bib;
    private long id;
    private int attd;
    private int tasks;


    public Student(String surname, String firstname, String comment,
                   String group, String team, String matr,
                   String bib, int attd, int tasks, long id) {
        this.surname = surname;
        this.firstname = firstname;
        this.comment = comment;
        this.group = group;
        this.team = team;
        this.matr = matr;
        this.bib = bib;
        this.attd = attd;
        this.tasks = tasks;
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getMatr() {
        return matr;
    }

    public void setMatr(String matr) {
        this.matr = matr;
    }

    public String getBib() {
        return bib;
    }

    public void setBib(String bib) {
        this.bib = bib;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAttd() {
        return attd;
    }

    public void setAttd(int attd) {
        this.attd = attd;
    }

    public int getTasks() {
        return tasks;
    }

    public void setTasks(int tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        String output = surname + ", " + firstname + "\t\t\t\t" + "Team " + team
                + "\t\t\t\t" + comment;

        return output;
    }
}
