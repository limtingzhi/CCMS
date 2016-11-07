/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccms.model;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class SearchCaseDAO {

    private static final String GET_ALL_CASE = "SELECT c.case_id, c.reported_date, p.name, p.nric, p.contact_no, p.email, c.type, cc.status, c.description, cc.difficulty, cc.issue, cc.add_on_date, cc.additional_info, cc.closing_remark\n" +
"FROM cases c\n" +
"LEFT JOIN person p ON p.nric = c.person_nric\n" +
"LEFT JOIN complaint_case cc ON c.case_id = cc.complaint_case_id\n" +
"ORDER BY c.case_id DESC";
    private static final String SEARCH_CASE_1 = "SELECT c.case_id, c.reported_date, p.name, p.nric, p.contact_no, p.email, c.type, cc.status, c.description, cc.difficulty, cc.issue, cc.add_on_date, cc.additional_info, cc.closing_remark\n" +
"FROM cases c\n" +
"LEFT JOIN person p ON p.nric = c.person_nric\n" +
"LEFT JOIN complaint_case cc ON c.case_id = cc.complaint_case_id WHERE \n"
            + "c.case_id=? AND p.nric=? ORDER BY c.case_id DESC";
    private static final String SEARCH_CASE_2 = "SELECT c.case_id, c.reported_date, p.name, p.nric, p.contact_no, p.email, c.type, cc.status, c.description, cc.difficulty, cc.issue, cc.add_on_date, cc.additional_info, cc.closing_remark\n" +
"FROM cases c\n" +
"LEFT JOIN person p ON p.nric = c.person_nric\n" +
"LEFT JOIN complaint_case cc ON c.case_id = cc.complaint_case_id WHERE \n"
            + "c.case_id=? ORDER BY c.case_id DESC";
    private static final String SEARCH_CASE_3 = "SELECT c.case_id, c.reported_date, p.name, p.nric, p.contact_no, p.email, c.type, cc.status, c.description, cc.difficulty, cc.issue, cc.add_on_date, cc.additional_info, cc.closing_remark\n" +
"FROM cases c\n" +
"LEFT JOIN person p ON p.nric = c.person_nric\n" +
"LEFT JOIN complaint_case cc ON c.case_id = cc.complaint_case_id WHERE \n"
            + "p.nric=? ORDER BY c.case_id DESC";
    private static final String SHOW_COMPLAINT_CASE = "select c.case_id, c.reported_date, p.name, p.nric, p.contact_no, p.email, c.type, cc.status, c.description, cc.difficulty, cc.issue, cc.add_on_date, cc.additional_info, cc.closing_remark from person p, cases c, complaint_case cc where p.nric = c.person_nric and c.case_id = cc.complaint_case_id AND \n"
            + "c.case_id=? AND p.nric=?";
    private static final String SHOW_COMPLIMENT_CASE_1 = "select c.case_id, c.reported_date, p.name, p.nric, p.contact_no, p.email, c.description, d.name as dname, e.name from person p, cases c, compliment_case cc, department d, employee_compliment_case ecc, employee e where p.nric = c.person_nric and c.case_id = cc.compliment_case_id and d.dept_id=cc.compliment_dept and ecc.compliment_case_id=c.case_id and ecc.employee_id=e.employee_id AND \n"
            + "c.case_id=? AND p.nric=?";
    private static final String SHOW_COMPLIMENT_CASE_2 = "select c.case_id, c.reported_date, p.name, p.nric, p.contact_no, p.email, c.description, e.name from person p, cases c, compliment_case cc, employee_compliment_case ecc, employee e where p.nric = c.person_nric and c.case_id = cc.compliment_case_id and ecc.compliment_case_id=c.case_id and ecc.employee_id=e.employee_id AND \n"
            + "c.case_id=? AND p.nric=?";
    private static final String CHECK_COMPLIMENT_DEPT = "select compliment_dept FROM compliment_case WHERE compliment_case_id=?";
    private static final String ADD_INFO = "update ccms_db.complaint_case SET additional_info = ?, add_on_date = CAST(? AS DATETIME), status = (SELECT concat('Pending - ', e.position) FROM complaint_case_handling ch, employee e WHERE e.employee_id = ch.employee_id AND ch.complaint_case_id = ? HAVING MAX(received_date)) WHERE \n"
            + "complaint_case_id=?";
    private static final String ARCHIVE_CASE = "update ccms_db.complaint_case SET closing_remark=?, status='Closed' WHERE \n"
            + "complaint_case_id=?";
    private static final String INSERT_NEW_CASE_HANDLING = "INSERT INTO complaint_case_handling (complaint_case_id, employee_id, received_date, response_date, response, last_saved) VALUES (?, (SELECT employee_id FROM complaint_case_handling ch WHERE ch.complaint_case_id = ? HAVING MIN(received_date)), CAST(? AS DATETIME), NULL, NULL, NULL)";

    public SearchCaseDAO() {
    }

    public ArrayList<SearchCase> getAllCase() {
        ArrayList<SearchCase> caseList = new ArrayList<SearchCase>();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_ALL_CASE);
            rs = ps.executeQuery();

            while (rs.next()) {
                String addOnDate = "-";
                if (rs.getTimestamp("add_on_date") != null) {
                    addOnDate = df.format(rs.getTimestamp("add_on_date"));
                }

                String additionalInfo = "-";
                if (rs.getString("additional_info") != null) {
                    additionalInfo = rs.getString("additional_info");
                }

                String remark = "-";
                if (rs.getString("closing_remark") != null) {
                    additionalInfo = rs.getString("closing_remark");
                }

                SearchCase case1 = new SearchCase(
                        rs.getInt("case_id"),
                        df.format(rs.getTimestamp("reported_date")),
                        rs.getString("name"),
                        rs.getString("nric"),
                        rs.getInt("contact_no"),
                        rs.getString("email"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getString("difficulty"),
                        rs.getString("issue"),
                        addOnDate,
                        additionalInfo,
                        remark);
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
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String SEARCH_CASE = SEARCH_CASE_1;
        
        try {
            con = ConnectionManager.getConnection();
            
            if (caseID != 0 && !nric.contains("empty")) {
                SEARCH_CASE = SEARCH_CASE_1;
                ps = con.prepareStatement(SEARCH_CASE);
                ps.setInt(1, caseID);
                ps.setString(2, nric);
                rs = ps.executeQuery();
            } else if (caseID > 0 && nric.contains("empty")) {
                SEARCH_CASE = SEARCH_CASE_2;
                ps = con.prepareStatement(SEARCH_CASE);
                ps.setInt(1, caseID);
                rs = ps.executeQuery();
            } else {
                SEARCH_CASE = SEARCH_CASE_3;
                ps = con.prepareStatement(SEARCH_CASE);
                ps.setString(1, nric);
                rs = ps.executeQuery();
            }

            while (rs.next()) {
                String addOnDate = "-";
                if (rs.getTimestamp("add_on_date") != null) {
                    addOnDate = df.format(rs.getTimestamp("add_on_date"));
                }

                String additionalInfo = "-";
                if (rs.getString("additional_info") != null) {
                    additionalInfo = rs.getString("additional_info");
                }

                String remark = "-";
                if (rs.getString("closing_remark") != null) {
                    additionalInfo = rs.getString("closing_remark");
                }

                SearchCase case1 = new SearchCase(
                        rs.getInt("case_id"),
                        df.format(rs.getTimestamp("reported_date")),
                        rs.getString("name"),
                        rs.getString("nric"),
                        rs.getInt("contact_no"),
                        rs.getString("email"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getString("difficulty"),
                        rs.getString("issue"),
                        addOnDate,
                        additionalInfo,
                        remark);
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
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(SHOW_COMPLAINT_CASE);
            ps.setInt(1, caseID);
            ps.setString(2, nric);
            rs = ps.executeQuery();

            while (rs.next()) {
                String addOnDate = "-";
                if (rs.getTimestamp("add_on_date") != null) {
                    addOnDate = df.format(rs.getTimestamp("add_on_date"));
                }

                String additionalInfo = "-";
                if (rs.getString("additional_info") != null) {
                    additionalInfo = rs.getString("additional_info");
                }
                
                String remark = "-";
                if (rs.getString("closing_remark") != null) {
                    additionalInfo = rs.getString("closing_remark");
                }
                
                SearchCase case1 = new SearchCase(
                        rs.getInt("case_id"),
                        df.format(rs.getTimestamp("reported_date")),
                        rs.getString("name"),
                        rs.getString("nric"),
                        rs.getInt("contact_no"),
                        rs.getString("email"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getString("description"),
                        rs.getString("difficulty"),
                        rs.getString("issue"),
                        addOnDate,
                        additionalInfo,
                        remark);
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
    
    public String showComplimentCase(int caseID, String nric) {
       
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String result = "";
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(CHECK_COMPLIMENT_DEPT);
            ps.setInt(1, caseID);
            //ps.setString(2, nric);
            rs = ps.executeQuery();

            while (rs.next()) {
                boolean complimentDept = rs.getBoolean("compliment_dept");
                System.out.println(complimentDept);
                if (complimentDept == true) {
                    ps2 = con.prepareStatement(SHOW_COMPLIMENT_CASE_1);
                    ps2.setInt(1, caseID);
                    ps2.setString(2, nric);
                    rs2 = ps2.executeQuery();
                    
                    while(rs2.next()){
                        result = rs2.getInt("case_id")+","+rs2.getTimestamp("reported_date")+","+rs2.getString("name")+","+rs2.getInt("contact_no")+","+rs2.getString("email")+","+rs2.getString("description")+","+rs2.getString("dname")+",-";
                    }
                }else{
                    ps2 = con.prepareStatement(SHOW_COMPLIMENT_CASE_2);
                    ps2.setInt(1, caseID);
                    ps2.setString(2, nric);
                    rs2 = ps2.executeQuery();
                    
                    while(rs2.next()){
                        result = rs2.getInt("case_id")+","+rs2.getTimestamp("reported_date")+","+rs2.getString("name")+","+rs2.getInt("contact_no")+","+rs2.getString("email")+","+rs2.getString("description")+",-,"+rs2.getString("e.name");
                    }
                }
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
        return result;
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
