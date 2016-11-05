/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ccms.model;

import java.sql.*;
import java.util.*;

public class SearchCaseDAO {
    private static final String GET_ALL_CASE = "select c.case_id, c.reported_date, p.name, p.nric, p.contact_no, p.email, c.type, cc.status, c.description, cc.difficulty, cc.issue, cc.additional_info, cc.closing_remark from person p, cases c, complaint_case cc where p.nric = c.person_nric and c.case_id = cc.complaint_case_id";
    private static final String SEARCH_CASE = "select c.case_id, c.reported_date, p.name, p.nric, p.contact_no, p.email, c.type, cc.status, c.description, cc.difficulty, cc.issue, cc.additional_info, cc.closing_remark from person p, cases c, complaint_case cc where p.nric = c.person_nric and c.case_id = cc.complaint_case_id AND \n"+
"c.case_id=? AND p.nric=?";
    private static final String SHOW_CASE = "select c.case_id, c.reported_date, p.name, p.nric, p.contact_no, p.email, c.type, cc.status, c.description, cc.difficulty, cc.issue, cc.additional_info, cc.closing_remark from person p, cases c, complaint_case cc where p.nric = c.person_nric and c.case_id = cc.complaint_case_id AND \n"+
"c.case_id=? AND p.nric=?";
    private static final String ADD_INFO = "update ccms_db.complaint_case SET additional_info = ?, add_on_date = CAST(? AS DATETIME), status = (SELECT concat('Pending - ', e.position) FROM complaint_case_handling ch, employee e WHERE e.employee_id = ch.employee_id AND ch.complaint_case_id = ? HAVING MAX(received_date)) WHERE \n"+
            "complaint_case_id=?";
    private static final String ARCHIVE_CASE = "update ccms_db.complaint_case SET closing_remark=?, status='Closed' WHERE \n"+
            "complaint_case_id=?";
    private static final String INSERT_NEW_CASE_HANDLING = "INSERT INTO complaint_case_handling (complaint_case_id, employee_id, received_date, response_date, response, last_saved) VALUES (?, (SELECT employee_id FROM complaint_case_handling ch WHERE ch.complaint_case_id = ? HAVING MIN(received_date)), CAST(? AS DATETIME), NULL, NULL, NULL)";
    
    public SearchCaseDAO(){
    }
    
    public ArrayList<SearchCase> getAllCase() {
        ArrayList<SearchCase> caseList = new ArrayList<SearchCase>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_ALL_CASE);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                SearchCase case1 = new SearchCase(
                        rs.getInt("case_id"),
                        rs.getString("reported_date"),
                        rs.getString("name"),
                        rs.getString("nric"),
                        rs.getInt("contact_no"),
                        rs.getString("email"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getString("difficulty"),
                        rs.getString("issue"),
                        rs.getString("additional_info"),
                        rs.getString("closing_remark"));
                caseList.add(case1);
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
        return caseList;
    }
    
    public ArrayList<SearchCase> searchCase(int caseID, String nric) {
        ArrayList<SearchCase> caseList = new ArrayList<SearchCase>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(SEARCH_CASE);
            ps.setInt(1, caseID);
            ps.setString(2, nric);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                SearchCase case1 = new SearchCase(
                        rs.getInt("case_id"),
                        rs.getString("reported_date"),
                        rs.getString("name"),
                        rs.getString("nric"),
                        rs.getInt("contact_no"),
                        rs.getString("email"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getString("difficulty"),
                        rs.getString("issue"),
                        rs.getString("additional_info"),
                        rs.getString("closing_remark"));
                caseList.add(case1);
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
        return caseList;
    }
    
    public ArrayList<SearchCase> showCase(int caseID, String nric) {
        ArrayList<SearchCase> caseList = new ArrayList<SearchCase>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(SHOW_CASE);
            ps.setInt(1, caseID);
            ps.setString(2, nric);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                SearchCase case1 = new SearchCase(
                        rs.getInt("case_id"),
                        rs.getString("reported_date"),
                        rs.getString("name"),
                        rs.getString("nric"),
                        rs.getInt("contact_no"),
                        rs.getString("email"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getString("difficulty"),
                        rs.getString("issue"),
                        rs.getString("additional_info"),
                        rs.getString("closing_remark"));
                caseList.add(case1);
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
        return caseList;
    }
    
    public boolean addInfo(String addInfo, int caseID, String todayDate) {
        
        boolean success = false;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            conn = ConnectionManager.getConnection();
            
            pstmt = conn.prepareStatement(ADD_INFO);
            pstmt.setString(1, addInfo);
            pstmt.setString(2, todayDate);
            pstmt.setInt(3, caseID);
            pstmt.setInt(4, caseID);
            
            if (pstmt.executeUpdate() > 0) {
                success = true;
            }

        } catch (SQLException e) {
            success = false;

        }

        return success;
    }
    
     public boolean archiveCase(String closingRemark, int caseID) {
        
        boolean success = false;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {

            conn = ConnectionManager.getConnection();
            
            pstmt = conn.prepareStatement(ARCHIVE_CASE);
            pstmt.setString(1, closingRemark);
            pstmt.setInt(2, caseID);

            if (pstmt.executeUpdate() > 0) {
                success = true;
            }

        } catch (SQLException e) {
            success = false;

        }

        return success;
    }
     
    public boolean revertCaseToFirstOwner(int caseID, String received_date) {
        
        boolean success = false;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {

            conn = ConnectionManager.getConnection();
            
            pstmt = conn.prepareStatement(INSERT_NEW_CASE_HANDLING);
            pstmt.setInt(1, caseID);
            pstmt.setInt(2, caseID);
            pstmt.setString(3, received_date);

            if (pstmt.executeUpdate() > 0) {
                success = true;
            }

        } catch (SQLException e) {
            success = false;

        }

        return success;
    }
}
