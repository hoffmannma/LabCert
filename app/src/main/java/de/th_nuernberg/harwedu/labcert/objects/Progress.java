package de.th_nuernberg.harwedu.labcert.objects;


public class Progress {

    private long id;
    private String lab_name;
    private String group;
    private String term;
    private String type;
    private String matr;
    private String score;
    private String def;
    private String comment;
    private String ts;

    public Progress(long id, String lab_name, String group, String term, String type, String matr, String score, String def, String comment, String ts) {
        this.id = id;
        this.lab_name = lab_name;
        this.group = group;
        this.term = term;
        this.type = type;
        this.matr = matr;
        this.score = score;
        this.def = def;
        this.comment = comment;
        this.ts = ts;
    }

    public Progress(long id, String lab_name, String group, String term, String type, String matr, String score, String comment, String ts) {
        this.id = id;
        this.lab_name = lab_name;
        this.group = group;
        this.term = term;
        this.type = type;
        this.matr = matr;
        this.score = score;
        this.def = def;
        this.comment = comment;
        this.ts = ts;
    }
    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getLab_name() {return lab_name;}

    public void setLab_name(String lab_name) {this.lab_name = lab_name;}

    public String getGroup() {return group;}

    public void setGroup(String group) {this.group = group;}

    public String getTerm() {return term;}

    public void setTerm(String term) {this.term = term;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public String getMatr() {return matr;}

    public void setMatr(String matr) {this.matr = matr;}

    public String getScore() {return score;}

    public void setScore(String score) {this.score = score;}

    public String getDef() {return def;}

    public void setDef(String def) {this.def = def;}

    public String getComment() {return comment;}

    public void setComment(String comment) {this.comment = comment;}

    public String getTs() {return ts;}

    public void setTs(String ts) {this.ts = ts;}



}
