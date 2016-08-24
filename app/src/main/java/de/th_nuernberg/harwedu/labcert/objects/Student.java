package de.th_nuernberg.harwedu.labcert.objects;


/**
 *
 */
public class Student {
    private long id;
    private String labName;
    private String group;
    private String term;

    private String title;
    private String surname;
    private String firstname;

    private String matr;
    private String email;
    private String bib;
    private String comment;

    private float progress;

    public Student(long id, String labName, String group, String term, String title, String surname,
                   String firstname, String matr, String email, String bib, String comment) {

        this.id = id;
        this.labName = labName;
        this.group = group;
        this.term = term;

        this.title = title;
        this.surname = surname;
        this.firstname = firstname;

        this.matr = matr;
        this.email = email;
        this.bib = bib;
        this.comment = comment;

        //TODO Progress Berechnung
        progress = 0;
    }

    public long getId() {
        return id;
    }

    public String getLabName() { return labName; }

    public void setLabName(String labName) { this.labName = labName; }

    public String getGroup() { return group; }

    public void setGroup(String group) { this.group = group; }

    public String getTerm() { return term; }

    public void setTerm(String term) { this.term = term; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {this.surname = surname; }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMatr() {
        return matr;
    }

    public void setMatr(String matr) {
        this.matr = matr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBib() {
        return bib;
    }

    public void setBib(String bib) {
        this.bib = bib;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    //TODO löschen?
    /*public String getStudentData() {
        attd_count = 5;
        String output = surname + ", " + firstname + "\n" +
                "\nGruppe: " + group +
                "\nTeam: " + team +
                "\nAnwesenheit: " + intToString(attd, attd_count)// +
                //"\nAufgabenstatus: " + intToString(tasks);
                ;
        return output;
    }

    private String intToString(int[] intArray, int count) {
        String delimiter = " | ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (sb.length() > 0) {
                sb.append(delimiter);
            }
            sb.append(intArray[i]);
        }
        return sb.toString();
    }*/

    /*
    @Override
    public String toString() {
        String output = surname + ", " + firstname + "\t\t\t\t" + "Team " + team
                + "\t\t\t\t" + comment;

        return output;
    }
    */

    /*
    public void addAttd()
    {
        attd_count = 5;
        for (int i=0; i<attd_count; i++) {
            if (attd[i]==0) {
                attd[i]++;
                return;
            }
        }
    }
    */
}