package ccms.model;

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
    private boolean onLeave;
    private Department dept;
    private int inCharge_id;

    public Employee(int employeeID, String name, String position, String username, String password, boolean onLeave, Department dept, int inCharge_id) {
        this.employeeID = employeeID;
        this.name = name;
        this.position = position;
        this.username = username;
        this.password = password;
        this.onLeave = onLeave;
        this.dept = dept;
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

    public boolean isOnLeave() {
        return onLeave;
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
}