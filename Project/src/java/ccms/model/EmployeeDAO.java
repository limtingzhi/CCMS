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
    private static final String GET_EMP_BY_ID = "SELECT * FROM employee WHERE employee_id = ?";
    private static final String GET_EMP_BY_NAME = "SELECT email FROM employee WHERE name = '"+"?"+"'";
    private static final String GET_EMP_BY_POSITION = "SELECT email FROM employee WHERE position = 'Director'";
    private static final String GET_AVAIL_MANAGER = "SELECT employee_id FROM employee WHERE dept_id IN (SELECT dept_id FROM employee WHERE employee_id = ?) AND position = 'manager' AND on_leave = 0";
    private static final String GETEMPEMAIL = "Select email from employee where name = ?";

    public EmployeeDAO(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    public EmployeeDAO() {
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
                        dept,
                        rs.getDate("on_leave_start"),
                        rs.getDate("on_leave_end"),
                        rs.getInt("inCharge_id"));

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

    public int getAvailManager(int employeeID) {
        int manager = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_AVAIL_MANAGER);
            ps.setInt(1, employeeID);
            rs = ps.executeQuery();

            while (rs.next()) {
                manager = rs.getInt("employee_id");
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
        return manager;
    }

    public Employee getEmployeeByID(int employeeID) {
        Employee employee = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_EMP_BY_ID);
            ps.setInt(1, employeeID);
            rs = ps.executeQuery();

            while (rs.next()) {
                Department dept = departmentDAO.getDeptByDeptID(rs.getInt("dept_id"));
                employee = new Employee(
                        employeeID,
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        dept,
                        rs.getDate("on_leave_start"),
                        rs.getDate("on_leave_end"),
                        rs.getInt("incharge_id"));
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
        return employee;
    }

    public String getEmployeeByName(String name) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Statement stmt = null;
        //String email = "wayne.zhong91@gmail.com";
        String email = "";
        
        try {
            con = ConnectionManager.getConnection();
            /*ps = con.prepareStatement(GET_EMP_BY_NAME);
            ps.setString(1, name);
            rs = ps.executeQuery();*/
           
            ps = con.prepareStatement(GETEMPEMAIL);
            ps.setString(1, name.trim());
            rs = ps.executeQuery();
 
            rs.next();
            email = rs.getString("email");
   
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
        return email;
    }

    public String getEmployeeByPosition() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Statement stmt = null;
        String email = "";
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_EMP_BY_POSITION);
            //ps.setString(1, name);
            rs = ps.executeQuery();

            //rs = stmt.executeQuery(GET_EMP_BY_POSITION);
            rs.next();
            email = (String) rs.getString(1);

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
        return email;
    }
}
