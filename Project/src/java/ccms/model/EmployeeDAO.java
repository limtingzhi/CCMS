package ccms.model;

import java.sql.*;
import java.util.*;

/**
 *
 * @author limtingzhi
 */
public class EmployeeDAO {

    private DepartmentDAO departmentDAO;
    private static final String GET_ALL_EMPLOYEE = "SELECT * FROM employee";

    public EmployeeDAO(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    public ArrayList<Employee> getAllEmployee() {
        ArrayList<Employee> employeeList = new ArrayList<Employee>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_ALL_EMPLOYEE);
            rs = ps.executeQuery();

            while (rs.next()) {
                Department dept = departmentDAO.getDeptByDeptID(rs.getInt("dept_id"));

                Employee employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("on_Leave"),
                        dept);

                employeeList.add(employee);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return employeeList;
    }
}
