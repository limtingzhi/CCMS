/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccms.model;

/**
 *
 * @author Wayne
 */
public class complimentCase {
    private String employee_name;
    private String department_name;

    public complimentCase(String employee_name, String department_name) {
        this.employee_name = employee_name;
        this.department_name = department_name;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }
}
