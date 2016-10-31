/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccms.model;
import java.util.*;
/**
 *
 * @author asus user
 */
public class Case {
    private int caseID;
    private String description;
    private Date reportedDate;
    private String type;
    private Employee employee;
    private Person person;

    public Case(int caseID, String description, Date reportedDate, String type, Employee employee, Person person) {
        this.caseID = caseID;
        this.description = description;
        this.reportedDate = reportedDate;
        this.type = type;
        this.employee = employee;
        this.person = person;
    }

    public int getCaseID() {
        return caseID;
    }

    public String getDescription() {
        return description;
    }

    public Date getReportedDate() {
        return reportedDate;
    }

    public String getType() {
        return type;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Person getPerson() {
        return person;
    }
}
