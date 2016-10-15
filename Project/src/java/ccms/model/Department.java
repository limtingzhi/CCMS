package ccms.model;

/**
 *
 * @author limtingzhi
 */
public class Department {
    private int deptID;
    private String name;

    public Department(int deptID, String name) {
        this.deptID = deptID;
        this.name = name;
    }

    public int getDeptID() {
        return deptID;
    }

    public String getName() {
        return name;
    }
}