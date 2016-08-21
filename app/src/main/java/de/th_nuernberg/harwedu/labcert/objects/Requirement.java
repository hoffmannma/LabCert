package de.th_nuernberg.harwedu.labcert.objects;

/**
 * Created by Edu on 21.08.2016.
 */
public class Requirement {

    private String reqType;
    private String reqName;
    private String group;
    private String lab_id;
    private String term;
    private String count;

    public Requirement(String reqType, String reqName, String count, String group,
                       String lab_id, String term) {
        this.reqType = reqType;
        this.reqName = reqName;
        this.group = group;
        this.lab_id = lab_id;
        this.term = term;
        this.count = count;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLab_id() {
        return lab_id;
    }

    public void setLab_id(String lab_id) {
        this.lab_id = lab_id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
