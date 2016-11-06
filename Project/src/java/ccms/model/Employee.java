package ccms.model;

import java.util.Date;

/**
 *
 * @author limtingzhi
 */
public class Employee {
    private int employeeID;
    private String name;
    private String position;
    private String username;
    private String password;
    private String email;
    private Department dept;
    private Date onLeaveStart;
    private Date onLeaveEnd;
    private int inCharge_id;

    public Employee(int employeeID, String name, String position, String username, String password, Department dept, Date onLeaveStart, Date onLeaveEnd, int inCharge_id) {
        this.employeeID = employeeID;
        this.name = name;
        this.position = position;
        this.username = username;
        this.password = password;
        this.dept = dept;
        this.onLeaveStart = onLeaveStart;
        this.onLeaveEnd = onLeaveEnd;
        this.inCharge_id = inCharge_id;
    }
    public Employee(int employeeID, String name, String position, String username, String password, String email, Department dept, Date onLeaveStart, Date onLeaveEnd, int inCharge_id) {
        this.employeeID = employeeID;
        this.name = name;
        this.position = position;
        this.username = username;
        this.password = password;
        this.dept = dept;
        this.email = email;
        this.onLeaveStart = onLeaveStart;
        this.onLeaveEnd = onLeaveEnd;
        this.inCharge_id = inCharge_id;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public String getEmail() {
        return email;
    }

    public Department getDept() {
        return dept;
    }
    
    public int getInchargeID() {
        return inCharge_id;
    }
    
    public void setIncharge(int inCharge_id) {
        this.inCharge_id = inCharge_id;
    }
    
    public Date getOnLeaveStart() {
        return onLeaveStart;
    }

    public void setOnLeaveStart(Date onLeaveStart) {
        this.onLeaveStart = onLeaveStart;
    }

    public Date getOnLeaveEnd() {
        return onLeaveEnd;
    }

    public void setOnLeaveEnd(Date onLeaveEnd) {
        this.onLeaveEnd = onLeaveEnd;
    }

    public int getInCharge_id() {
        return inCharge_id;
    }

    public void setInCharge_id(int inCharge_id) {
        this.inCharge_id = inCharge_id;
    }
}