package de.th_nuernberg.harwedu.labcert.database;


/** TODO:
- Constructors ergänzen
- attd / tasks -> array
 */


public class Student {

    private String surname;
    private String firstname;
    private String comment_student;
    private String group;
    private String team;
    private String matr;
    private String bib;

    private String[] comment_attd;
    private String[] comment_task;

    private long id;
    private int[] attd;
    private int[] tasks;
    private int attd_count;
    private int task_count;


    public Student(){

    }

    public Student(String surname, String firstname, int[] attd, int[] tasks){
        this.surname = surname;
        this.firstname = firstname;
        this.attd = attd;
        this.tasks = tasks;
    }

    public Student(String surname, String firstname, String comment,
                   String group, String team, String matr,
                   String bib, int[] attd, int[] tasks, long id) {
        this.surname = surname;
        this.firstname = firstname;
        this.comment_student = comment;
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

    public String getCommentStudent() {
        return comment_student;
    }

    public void setCommentStudent(String comment) {
        this.comment_student = comment;
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

    public int[] getAttd() {
        return attd;
    }

    public void setAttd(int[] attd) {
        this.attd = attd;
    }

    public int[] getTasks() {
        return tasks;
    }

    public void setTasks(int[] tasks) {
        this.tasks = tasks;
    }

    public String[] getComment_attd() {
        return comment_attd;
    }

    public void setComment_attd(String[] comment_attd) {
        this.comment_attd = comment_attd;
    }

    public String getStudentData(){
        attd_count = 5;
        String output = surname + ", " + firstname + "\n" +
                "\nGruppe: " + group +
                "\nTeam: " + team +
                "\nAnwesenheit: " + intToString(attd, attd_count)/* +
                "\nAufgabenstatus: " + intToString(tasks)*/;
                return output;
    }

    public String getAttdString(){
        attd_count = 5;
         return intToString(attd, attd_count);
    }

    public String getTaskString(){
        task_count = 5;
        return intToString(tasks, task_count);
    }

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

    private String intToString(int[] intArray, int count) {
        String delimiter = " | ";
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<count; i++) {
            if (sb.length() > 0) {
                sb.append(delimiter);
            }
            sb.append(intArray[i]);
        }
        return sb.toString();
    }

}
