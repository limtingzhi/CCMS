package ccms.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author anita
 */
public class CaseDAO {
    private EmployeeDAO empDAO;
    private PersonDAO personDAO;
    private static final String GET_CASE_BY_EMP_ID = "SELECT cc.complaint_case_id, cc.expected_response_date, cc.issue, cc.difficulty, ch.received_date, ch.last_saved FROM complaint_case cc, complaint_case_handling ch WHERE cc.complaint_case_id = ch.complaint_case_id AND ch.employee_id = ?  AND ch.response_date IS NULL ORDER BY ch.received_date asc";
    private static final String GET_CASE_BY_CASE_ID = "SELECT p.nric, p.email, p.name, p.contact_no, c.description, cc.difficulty, cc.issue, cc.additional_info, ch.response FROM cases c, complaint_case cc, complaint_case_handling ch, person p WHERE ch.complaint_case_id = ? AND ch.response_date IS NULL AND c.case_id = cc.complaint_case_id AND cc.complaint_case_id = ch.complaint_case_id AND c.person_nric = p.nric";
    private static final String UPDATE_CASE_RESPONSE = "UPDATE complaint_case_handling SET response = ?, response_date = CAST(? AS DATETIME), last_saved = ? WHERE employee_id = ? AND complaint_case_id = ?"; 
    private static final String SAVE_CASE_RESPONSE = "UPDATE complaint_case_handling SET response = ?, last_saved = ? WHERE employee_id = ? AND complaint_case_id = ?"; 
    private static final String GET_CASE_RESPONSES = "SELECT e.position, e.employee_id, e.name, ch.response_date, ch.response FROM complaint_case_handling ch, employee e WHERE e.employee_id = ch.employee_id AND complaint_case_id = ? AND ch.response_date IS NOT NULL ORDER BY ch.response_date DESC";
    private static final String ROUTE_CASE = "INSERT INTO complaint_case_handling (complaint_case_id, employee_id, received_date, response_date, response) VALUES (?, ?, CAST(? AS DATETIME), NULL, NULL)";
    private static final String UPDATE_CASE_STATUS = "UPDATE complaint_case SET status = ? WHERE complaint_case_id = ?";
    
    public CaseDAO(EmployeeDAO empDAO, PersonDAO personDAO) {
        this.empDAO = empDAO;
        this.personDAO = personDAO;
    }
    
//    public void load() {
//        Scanner sc = null;
//        try {
//            File file = new File("./data/duke.csv");
//            sc = new Scanner(file);
//            sc.nextLine();
//            sc.useDelimiter(",|\r\n");
//            while(sc.hasNext()) {
//                String dukename = sc.next();
//                int power = sc.nextInt();
//                String region = sc.next();
//                int population = sc.nextInt();
//                Duke duke = new Duke(dukename, power, region, population);
//                if(!dukeList.containsKey(region)) {
//                    dukeList.put(region, new ArrayList<Duke>());
//                }
//                dukeList.get(region).add(duke);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            if(sc != null) {
//                sc.close();
//            }
//        }
//    }
    
    public LinkedHashMap<Integer, ArrayList<String>> getAllCasebyEmpID(int employee_id) throws ParseException {        
        LinkedHashMap<Integer, ArrayList<String>> cases = new LinkedHashMap<Integer, ArrayList<String>>();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_CASE_BY_EMP_ID);
            ps.setInt(1, employee_id);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                   ArrayList<String> details = new ArrayList<String>();
                   int caseID = rs.getInt("complaint_case_id");
                   String dateReceived = df.format(rs.getDate("received_date"));                   
                   String difficulty = rs.getString("difficulty");
                   String expectedResponseDate = calculateExpectedResponseDate(difficulty);
                   String issue = rs.getString("issue");
                   String lastSaved = rs.getString("last_saved");
                   if(lastSaved == null) {
                       lastSaved = "-";
                   }
                   details.add(dateReceived);
                   details.add(expectedResponseDate);
                   details.add(difficulty);
                   details.add(issue);
                   details.add(lastSaved);
                   cases.put(caseID, details);
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
        return cases;
    }
    
    public ArrayList<String> getCaseDetails(int case_id) {
        ArrayList<String> details = new ArrayList<String>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_CASE_BY_CASE_ID);
            ps.setInt(1, case_id);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                   String nric = rs.getString("nric");
                   String email = rs.getString("email");
                   String cName = rs.getString("name");
                   String contact_no = String.valueOf(rs.getInt("contact_no"));
                   String description = rs.getString("description");
                   String additional_info = rs.getString("additional_info");
                   String difficulty = rs.getString("difficulty");
                   String issue = rs.getString("issue");
                   String responseMsg = rs.getString("response");
                   details.add(String.valueOf(case_id));
                   details.add(nric);
                   details.add(email);
                   details.add(cName);
                   details.add(contact_no);
                   details.add(description);
                   details.add(additional_info);
                   details.add(difficulty);
                   details.add(issue);
                   details.add(responseMsg);
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
        return details;
    }
    
    public int updateCaseResponse(String response, String datetime, int employeeID, int caseID) {
        Connection con = null;
        PreparedStatement ps = null;
        int rowUpdate = 0;
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(UPDATE_CASE_RESPONSE);
            ps.setString(1, response);
            ps.setString(2, datetime);            
            ps.setString(3, datetime);
            ps.setInt(4, employeeID);
            ps.setInt(5, caseID);
            rowUpdate = ps.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
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
        return rowUpdate;
    }
        
    public int saveCaseResponse(String response, String datetime, int employeeID, int caseID) {
        Connection con = null;
        PreparedStatement ps = null;
        int rowUpdate = 0;
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(SAVE_CASE_RESPONSE);
            ps.setString(1, response);            
            ps.setString(2, datetime);
            ps.setInt(3, employeeID);
            ps.setInt(4, caseID);
            rowUpdate = ps.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
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
        return rowUpdate;
    }
    
    public int routeCaseToIncharge(int caseID, int employeeID, String datetime) {
        Connection con = null;
        PreparedStatement ps = null;
        int rowUpdate = 0;
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(ROUTE_CASE);
            ps.setInt(1, caseID);
            ps.setInt(2, employeeID);
            ps.setString(3, datetime);
            rowUpdate = ps.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
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
        return rowUpdate;
    }
    
    public int updateCaseStatus(String status, int caseID) {
        Connection con = null;
        PreparedStatement ps = null;
        int rowUpdate = 0;
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(UPDATE_CASE_STATUS);
            ps.setString(1, status);
            ps.setInt(2, caseID);
            rowUpdate = ps.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
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
        return rowUpdate;
    }
    
    public LinkedHashMap<Integer, ArrayList<String>> getCaseResponses (int caseID) {
        LinkedHashMap<Integer, ArrayList<String>> map = new LinkedHashMap<Integer, ArrayList<String>>();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_CASE_RESPONSES);
            ps.setInt(1, caseID);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                   ArrayList<String> responses = new ArrayList<String>();
                   String responseDate = df.format(rs.getDate("response_date"));
                   String response = rs.getString("response");
                   String name = rs.getString("name") + ", " + rs.getString("position");
                   int employeeID = rs.getInt("employee_id");
                   responses.add(name);
                   responses.add(responseDate);
                   responses.add(response);
                   if(!map.containsKey(employeeID)) {
                       map.put(employeeID, responses);
                   } 
                   else {
                       map.get(employeeID).add(responseDate);
                       map.get(employeeID).add(response);
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
        return map;
    }
    
    public String calculateExpectedResponseDate(String difficulty) throws ParseException {
        //Assumption: received date is date recorded when the case is created in CCMS, not when they received the email.
        String expectedDate =  "";
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
                
        Scanner sc = null;
        LinkedHashMap<Integer, String> holidays = new LinkedHashMap<Integer, String>();
        try {    
            File file = new File("./data/Holidays2016.txt");
            sc = new Scanner(file);
            sc.nextLine();
            sc.useDelimiter(",|\r\n");
            Calendar calG = new GregorianCalendar();

            while(sc.hasNext()) {
                String date = sc.next();
//                Date dateParse = (Date) dateFormat.parse(date);
                calG.setTime(dateFormat.parse(date));
                int dayOfYear = calG.get(Calendar.DAY_OF_YEAR);
                holidays.put(dayOfYear, "");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(sc != null) {
                sc.close();
            }
        }

        int daysToAdd = 3; //Default to easy case
        if(difficulty.equalsIgnoreCase("hard")) {
            daysToAdd = 5;
        } else if (difficulty.equalsIgnoreCase("super complex")) {
            daysToAdd = 7;
        }
        
        // iterate over the dates from now and check if each day is a business day
        int businessDayCounter = 0;
        while (businessDayCounter < daysToAdd) {
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && !holidays.containsKey(cal.get(Calendar.DAY_OF_YEAR))) {
                businessDayCounter++;
            }
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        expectedDate = dateFormat.format(cal.getTime());
        return expectedDate;
    }
}