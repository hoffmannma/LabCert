package de.th_nuernberg.harwedu.labcert.objects;

/**
 * Created by Edu on 21.08.2016.
 */
public class Requirement {

    private long id;
    private String lab_name;
    private String group;
    private String term;
    private String type;
    private String count;

    public Requirement(long id, String lab_name, String group, String term, String type, String count) {
        this.id = id;
        this.lab_name = lab_name;
        this.group = group;
        this.term = term;
        this.type = type;
        this.count = count;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getLab_name() { return lab_name; }

    public void setLab_name(String lab_name) { this.lab_name = lab_name; }

    public String getGroup() { return group; }

    public void setGroup(String group) { this.group = group; }

    public String getTerm() { return term; }

    public void setTerm(String term) { this.term = term; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getCount() { return count; }

    public void setCount(String count) { this.count = count; }
}
