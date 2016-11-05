/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccms.model;

import java.sql.Date;

/**
 *
 * @author Wayne
 */
public class complaintCase {

    private String description;
    private Date reported_date;
    private String type;
    private int recorded_employee_id;
    private String person_nric;
    private String difficulty;
    private String issues;

    public complaintCase(String description, Date reported_date, String type, int recorded_employee_id, String person_nric, String difficulty, String issues) {
        this.description = description;
        this.reported_date = reported_date;
        this.type = type;
        this.recorded_employee_id = recorded_employee_id;
        this.person_nric = person_nric;
        this.difficulty = difficulty;
        this.issues = issues;
    }

    public complaintCase(String difficulty, String issues) {
        this.difficulty = difficulty;
        this.issues = issues;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getIssues() {
        return issues;
    }

    public complaintCase() {
    }

    public String getDescription() {
        return description;
    }

    public Date getReported_date() {
        return reported_date;
    }

    public String getType() {
        return type;
    }

    public int getRecorded_employee_id() {
        return recorded_employee_id;
    }

    public String getPerson_nric() {
        return person_nric;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReported_date(Date reported_date) {
        this.reported_date = reported_date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRecorded_employee_id(int recorded_employee_id) {
        this.recorded_employee_id = recorded_employee_id;
    }

    public void setPerson_nric(String person_nric) {
        this.person_nric = person_nric;
    }

}
