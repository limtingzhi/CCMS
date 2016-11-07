/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccms.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static java.time.LocalDate.now;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author asus user
 */
public class TestingMain {

    //private DepartmentDAO dDAO;
    /**
     * @param args the command line arguments
     */
    private static final String GET_CASE_BY_EMP_ID = "SELECT c.person_nric, c.reported_date, cc.complaint_case_id, cc.issue, cc.difficulty, cc.add_on_date, ch.received_date, ch.last_saved FROM cases c, complaint_case cc, complaint_case_handling ch WHERE c.case_id = cc.complaint_case_id AND cc.complaint_case_id = ch.complaint_case_id AND ch.employee_id = ?  AND cc.status NOT IN ('Replied', 'Closed') AND ch.response_date IS NULL ORDER BY ch.received_date asc";

//    public static String calculateExpectedResponseDate(int daysToAdd, String dateStr) throws ParseException {
//        //Assumption: received date is date recorded when the case is created in CCMS, not when they received the email.
//        String expectedDate = "";
//        LinkedHashMap<Integer, String> holidays = new CaseDAO().getAllHolidays();
//        //Set the recorded date to calendar
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//        Calendar cal = Calendar.getInstance();
//        java.util.Date date = sdf1.parse(dateStr);
//        dateStr = sdf2.format(date);
//        cal.setTime(sdf2.parse(dateStr));
//
//        // iterate over the dates from now and check if each day is a business day
//        int businessDayCounter = 0;
//        while (businessDayCounter < daysToAdd) {
//            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
//            //If the dayOfWeek hits Saturday, Sunday or Holiday, it will not add to businessDayCounter but it add one more day on the calendar
//            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && !holidays.containsKey(cal.get(Calendar.DAY_OF_YEAR))) {
//                businessDayCounter++;
//            }
//            cal.add(Calendar.DAY_OF_YEAR, 1);
//        }
//
//        sdf2 = new SimpleDateFormat("dd/MM/yyyy");
//        expectedDate = sdf2.format(cal.getTime());
//        return expectedDate;
//    }
//    public static String calculateExpectedResponseDate(String difficulty) throws ParseException {
//        //Assumption: received date is date recorded when the case is created in CCMS, not when they received the email.
//        String expectedDate =  "";
//        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//        Calendar cal = Calendar.getInstance();
//                
//        Scanner sc = null;
//        LinkedHashMap<Integer, String> holidays = new LinkedHashMap<Integer, String>();
//        try {    
//            File file = new File("./data/Holidays2016.txt");
//            sc = new Scanner(file);
//            sc.nextLine();
//            sc.useDelimiter(",|\r\n");
//            Calendar calG = new GregorianCalendar();
//
//            while(sc.hasNext()) {
//                String date = sc.next();
////                Date dateParse = (Date) dateFormat.parse(date);
//                calG.setTime(dateFormat.parse(date));
//                int dayOfYear = calG.get(Calendar.DAY_OF_YEAR);
//                holidays.put(dayOfYear, "");
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            if(sc != null) {
//                sc.close();
//            }
//        }
//
//        int daysToAdd = 3; //Default to easy case
//        if(difficulty.equalsIgnoreCase("hard")) {
//            daysToAdd = 5;
//        } else if (difficulty.equalsIgnoreCase("super complex")) {
//            daysToAdd = 7;
//        }
//        
//        // iterate over the dates from now and check if each day is a business day
//        int businessDayCounter = 0;
//        while (businessDayCounter < daysToAdd) {
////            cal.setTime(new Date());
//            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
//            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && !holidays.containsKey(cal.get(Calendar.DAY_OF_YEAR))) {
//                businessDayCounter++;
//            }
//            cal.add(Calendar.DAY_OF_YEAR, 1);
//        }
//
//        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        expectedDate = dateFormat.format(cal.getTime());
//        return expectedDate;
//    }
    public static int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        return workDays;
    }

    public static void main(String[] args) throws ParseException {
        // TODO code application logic here
        DepartmentDAO dDAO = new DepartmentDAO();
        EmployeeDAO eDAO = new EmployeeDAO(dDAO);
        PersonDAO pDAO = new PersonDAO();
        CaseDAO cDAO = new CaseDAO(eDAO, pDAO);

//        String description = "a";
//        String date = "2016-11-02 00:00:00";
//        String type = "type";
//        int employeeID = 4;
//        String nric = "G0987935L";
//        int insertedRow = cDAO.updateCaseStatus(description, date, type, employeeID, nric);
//        System.out.println(insertedRow);
        
//        LinkedHashMap<Integer, ArrayList<String>> cList = cDAO.getAllCasebyEmpID(2);
//        LinkedHashMap<Integer, ArrayList<String>> cList = cDAO.getOutstandingCases();

//        LinkedHashMap<Integer, ArrayList<String>> cList = cDAO.getCaseCountByDifficulty();
//        System.out.println(cList);

//        ArrayList<String> details = cDAO.getCaseDetails(5);
//        int eId = Integer.parseInt(details.get(10));
//        String currentEmployeeName = eDAO.getEmployeeByID(eId).getName();
//        details.add(11, currentEmployeeName);
//        
//        System.out.println(details.size());
//        for(String s: details) { 
//            System.out.println(s);
//        }

        LinkedHashMap<Integer, ArrayList<String>> responses = cDAO.getCaseResponses(1);
        System.out.println(responses);
        
//        String casecount = "easy_1";
//        int lastIndexOf_ = casecount.lastIndexOf("_");
//        String casedifficulty = casecount.substring(0, lastIndexOf_);
//        System.out.println(casedifficulty + " " + casecount.substring(lastIndexOf_ + 1));
//        Date dateNow = new Date();
//        System.out.println(dateNow);
//
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
////        String dateStr = "22-12-2016 00:00:00";
////        System.out.println(sdf2.format(sdf1.parse(dateStr)));
//        Set<Integer> caseIDs = cList.keySet();
//        for (int i : caseIDs) {
//            ArrayList<String> arr = cList.get(i);
//            String dateReceivedStr = arr.get(0);
//            Date dateReceived = sdf1.parse(dateReceivedStr);
//            System.out.println("date received: " + sdf2.format(dateReceived));
//            String expectedResponseDateStr = arr.get(1);
//            
//            Date today = sdf1.parse("2016-11-07 01:00:00");
////            Date today = new Date();
//            Date expectedResponseDate = sdf1.parse(expectedResponseDateStr);
//            
//            int dayDiff = getWorkingDaysBetweenTwoDates(today, dateReceived);
//            System.out.println("expected response: " + sdf2.format(expectedResponseDate));
//            String lastsavedStr = arr.get(arr.size() - 1);
////            int dayDiff = 0;
//            Date lastsaved = null;
//            if (!lastsavedStr.equals("-")) {
//                lastsaved = sdf1.parse(lastsavedStr);
//                dayDiff = getWorkingDaysBetweenTwoDates(today, lastsaved);
//                arr.set(arr.size()-1, sdf2.format(lastsaved));
//                System.out.println("last saved: " + sdf2.format(lastsaved));
//            }
//            
//            String onLeaveStart = arr.get(4);
//            String onLeaveEnd = arr.get(5);
//            
//            if(onLeaveStart != null && onLeaveEnd != null) {
//                Date onLeaveStartD = sdf1.parse(onLeaveStart);
//                Date onLeaveEndD = sdf1.parse(onLeaveEnd);
//                
//                System.out.println("start leave: " + sdf2.format(onLeaveStartD));
//                System.out.println("end leave: " + sdf2.format(onLeaveEndD));
//                boolean checkOverlap = expectedResponseDate.after(onLeaveStartD) && expectedResponseDate.before(onLeaveEndD);
//                System.out.println("Overlap: " + checkOverlap);
//            } 
//            
//            //Check if case already overdue
//            boolean isOverdue = today.after(expectedResponseDate);
//            String remarks = "";
//
//            System.out.println("is overdue? " + today.after(expectedResponseDate));
////            long diff = today.getTime() - lastWorkingDate.getTime();
////            int dayDiff = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
////            int dayDiff = getWorkingDaysBetweenTwoDates(today, lastWorkingDate);
//            if(isOverdue) {
//                remarks = "Case overdue for " + dayDiff + " days";
//            } else if (!isOverdue && dayDiff == 0) {
//                remarks = "Just received case";                
//            } else if (!isOverdue && dayDiff > 0) {
//                remarks = "Case idle for " + dayDiff + " days";
//            }
//            arr.set(0, sdf2.format(dateReceived));
//            arr.set(1, sdf2.format(expectedResponseDate));
////            arr.set(arr.size()-1, sdf2.format(lastsaved));
////            remarks = remarks + dayDiff;
//            arr.add(remarks);
//            System.out.println(remarks);
//        }
//        System.out.println(cList);
//        ArrayList<String> caseDetails = cDAO.getCaseDetails(1);
//        System.out.println(caseDetails.size());
//        
//        LinkedHashMap<Integer, ArrayList<String>> responses = cDAO.getCaseResponses(1);
//        System.out.println(responses);
//        System.out.println();
//        ArrayList<String> details = cDAO.getCaseDetails(2);
//        for(String s: details) {
//            System.out.println(s);
//        }
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
//        System.out.println(dateFormat.format(date).getClass().getSimpleName());
//        int update = cDAO.updateCaseResponse("a", dateFormat.format(date), 1, 1);
//        System.out.println(update);
//        LinkedHashMap<Integer, ArrayList<String>> r = cDAO.getCaseResponses(1);
//        System.out.println(r);

//        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//        Calendar cal = Calendar.getInstance();        
//        Calendar calG = new GregorianCalendar();
//                
//        Scanner sc = null;
//        LinkedHashMap<Integer, String> holidays = new LinkedHashMap<Integer, String>();
//        try {    
//            File file = new File("C:\\Users\\asus user\\Documents\\SMU Year 4\\Term 1\\IS432 - Public Sector Systems (G1)\\Group Assignments\\Assignment 2\\Holidays2.txt");
//            sc = new Scanner(file);
//            sc.nextLine();
//            sc.useDelimiter(",|\r\n");
//
//            while(sc.hasNext()) {
//                String date = sc.next();
//                Date dateParse = (Date) dateFormat.parse(date);
//                calG.setTime(dateParse);
//                int dayOfYear = calG.get(Calendar.DAY_OF_YEAR);
//                holidays.put(dayOfYear, "");
////                int holiday = sc.nextInt();
////                holidays.put(holiday, "");
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            if(sc != null) {
//                sc.close();
//            }
//        }
//        
//        // get the current date without the hours, minutes, seconds and millis
////        cal.set(Calendar.HOUR_OF_DAY, 0);
////        cal.set(Calendar.MINUTE, 0);
////        cal.set(Calendar.SECOND, 0);
////        cal.set(Calendar.MILLISECOND, 0);
//
//        // iterate over the dates from now and check if each day is a business day
//        int businessDayCounter = 0;
////        dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//        while (businessDayCounter < 7) {
////            String d = dateFormat.format(new Date());
////            cal.setTime((Date) dateFormat.parse(d));
////            System.out.println((Date) dateFormat.parse(d));
//            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
////            System.out.println(dayOfWeek);
//            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && !holidays.containsKey(cal.get(Calendar.DAY_OF_YEAR))) {
//                businessDayCounter++;
//            }
//            cal.add(Calendar.DAY_OF_YEAR, 1);
//        }
//        Date threeBusinessDaysFromNow = cal.getTime();
//        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        System.out.println(dateFormat.format(threeBusinessDaysFromNow));
//        String a = calculateExpectedResponseDate("hard");
//        System.out.println(a);
//        ArrayList<String> caseDetails = cDAO.getCaseDetails(1);
////        String dateReceived = caseDetails.get(1);
//        String difficulty = caseDetails.get(7);
//        String expectedResponseDate = calculateExpectedResponseDate(difficulty);
//        System.out.println(difficulty + " " + expectedResponseDate);        
//        String additional_info = caseDetails.get(6);
//        if(additional_info == null) { 
//            caseDetails.set(6,"N/A");
//        }
//        System.out.println(caseDetails.size());
//        caseDetails.add(2, expectedResponseDate);
//        System.out.println(caseDetails.size());
//        for(String s: caseDetails) {
//            System.out.println(s);
//        }
//        LinkedHashMap<Integer, ArrayList<String>> cList = cDAO.getAllCasebyEmpID(2);
//        System.out.println(cList);
//            
//        String d = cDAO.calculateExpectedResponseDate("easy", "29-10-2016 12:00:00");
//        System.out.println(d);
//        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");        
//        Calendar cal = Calendar.getInstance();
//        String dateStr = "29-10-2016 12:00:00";
//        System.out.println(dateFormat.format(dateStr));
//        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        SimpleDateFormat format2 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//        Date date = format1.parse("2016-10-29 12:00:00");
//        System.out.println(format2.format(date));
//        LinkedHashMap<Integer, ArrayList<String>> cases = new LinkedHashMap<Integer, ArrayList<String>>();
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//        Connection con = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        try {
//            con = ConnectionManager.getConnection();
//            ps = con.prepareStatement(GET_CASE_BY_EMP_ID);
//            ps.setInt(1, 2);
//            rs = ps.executeQuery();
//
//            while (rs.next()) {
//                ArrayList<String> details = new ArrayList<String>();
//                int caseID = rs.getInt("complaint_case_id");
//                String personNRIC = rs.getString("person_nric");
//                String dateReceived = df.format(rs.getDate("received_date"));
//                String difficulty = rs.getString("difficulty");
//                int daysToAdd = 3;
//                if (difficulty.equalsIgnoreCase("hard")) {
//                    daysToAdd = 5;
//                } else if (difficulty.equalsIgnoreCase("super complex")) {
//                    daysToAdd = 7;
//                }
//                String reportedDate = rs.getString("reported_date");
//                String addOnDate = rs.getString("add_on_date");
//                String expectedResponseDate = null;
//                if (addOnDate != null) {
//                    System.out.println("Line 251");
//                    expectedResponseDate = calculateExpectedResponseDate(14, addOnDate);
//                } else {
//                    System.out.println("Line 254");
//                    expectedResponseDate = calculateExpectedResponseDate(daysToAdd, reportedDate);
//                }
//                System.out.println("Reported: "+ reportedDate);
//                System.out.println("Add On Date: " + addOnDate);
//                System.out.println("Expected: " + expectedResponseDate);
////                   String expectedResponseDate = calculateExpectedResponseDate(difficulty, reportedDate);
//                String issue = rs.getString("issue");
//                String lastSaved = rs.getString("last_saved");
//                if (lastSaved == null) {
//                    lastSaved = "-";
//                }
//                details.add(personNRIC);
//                details.add(dateReceived);
////                   details.add(reportedDate);
//                details.add(expectedResponseDate);
//                details.add(difficulty);
//                details.add(issue);
//                details.add(lastSaved);
//                cases.put(caseID, details);
//            }
//        } catch (SQLException se) {
//            se.printStackTrace();
//        } finally {
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (con != null) {
//                try {
//                    con.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

}
