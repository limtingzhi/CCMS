package ccms.model;

import java.sql.*;
import java.util.*;

/**
 *
 * @author limtingzhi
 */
public class DepartmentDAO {
    private static final String GET_ALL_DEPT = "SELECT * FROM department";
    private static final String GET_DEPT_BY_DEPT_ID = "SELECT * FROM department WHERE dept_id = ?";
 
    public ArrayList<Department> getAllDept() {
        ArrayList<Department> deptList = new ArrayList<Department>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_ALL_DEPT);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Department dept = new Department(
                        rs.getInt("dept_id"),
                        rs.getString("name"));
                deptList.add(dept);
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
        return deptList;
    }
    
    public Department getDeptByDeptID(int deptID) {
        Department dept = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_DEPT_BY_DEPT_ID);
            ps.setInt(1, deptID);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                dept = new Department(
                        rs.getInt("dept_id"),
                        rs.getString("name"));
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
        return dept;
    }
}